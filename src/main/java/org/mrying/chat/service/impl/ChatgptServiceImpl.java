package org.mrying.chat.service.impl;

import com.plexpt.chatgpt.ChatGPTStream;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.Message;
import com.plexpt.chatgpt.util.SseHelper;
import org.mrying.chat.constants.Constants;
import org.mrying.chat.event.GptEventSourceListener;
import org.mrying.chat.exception.NoTokenCountException;
import org.mrying.chat.mapper.ChatgptMessageMapper;
import org.mrying.chat.mapper.UserMapper;
import org.mrying.chat.model.ChatgptMessage;
import org.mrying.chat.model.User;
import org.mrying.chat.service.ChatgptService;
import org.mrying.chat.settings.ChatgptConfiguration;
import org.mrying.chat.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 邓和颖
 * @since 2023/10/14 19:38
 */
@Service("chatgptService")
public class ChatgptServiceImpl implements ChatgptService {

    private ChatgptConfiguration chatgptConfiguration;

    private UserMapper userMapper;

    private ChatgptMessageMapper chatgptMessageMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setChatgptMessageMapper(ChatgptMessageMapper chatgptMessageMapper) {
        this.chatgptMessageMapper = chatgptMessageMapper;
    }

    @Autowired
    public void setChatgptConfiguration(ChatgptConfiguration chatgptConfiguration) {
        this.chatgptConfiguration = chatgptConfiguration;
    }

    // 将 ChatgptMessage 转为 SDK(Software Development Kit) 所需的 Message 格式
    private List<Message> transform(List<ChatgptMessage> messageList) {
        List<Message> messages = new ArrayList<>();

        // 获取每条消息记录的内容，区分用户和 gpt 消息
        for (ChatgptMessage message : messageList) {
            // 加入上下文消息列表
            messages.add(message.getMessageDirection() == Constants.DIRECTION_QUESTION
                    // 用户发送的消息
                    ? Message.of((message.getContent()))

                    // gpt 发送的消息
                    : Message.ofAssistant(message.getContent())
            );
        }

        return messages;
    }

    // 保存当前对话内容
    private void saveCurrentMessage(String conversationId,String msg,Integer messageDirection) {
        ChatgptMessage message = new ChatgptMessage();
        message.setMessageId(UUIDUtils.getUUID());
        message.setMessageDirection(messageDirection);
        message.setContent(msg);
        message.setCreateTime(new Date());

        if(conversationId != null) {
            message.setConversationId(conversationId);
        }

        chatgptMessageMapper.insertCurrentMessage(message);
    }

    // 向 chatgpt 提问
    @Transactional
    @Override
    public synchronized SseEmitter sendMsgBySse(Boolean usePublicApi, String conversationId, String prompt) {

        // 查询用户
        User user = userMapper.selectUserById("34382773848d4c86acb1f960c4681530");

        // 1、设置 api-key
        String key;

        // 选择使用公共 api
        if(usePublicApi) {
            // 检查 api 使用次数
            if(user.getUserTokenCount() > 0) {
                key = chatgptConfiguration.getGptKey();
            }else {
                throw new NoTokenCountException("api 请求次数不足");
            }

        }else {
            // 使用个人 api
            key = user.getUserApiKey();
        }

        // 2、设置代理
        // 国内需要代理
        // Proxy proxy = Proxys.http("192.168.56.104", 7890);

        // 3、借助 SDK 工具，实例化 ChatGPTStream 工具类对象
        // 多 KEY 自动轮询 .apiKeyList()
        ChatGPTStream chatGPTStream = ChatGPTStream.builder()
                .timeout(50)
                .apiKey(key)
                .apiHost(chatgptConfiguration.getHost())
                .build()
                .init();

        // 4、实例化流式输出类，设置监听，使得在所有消息输出完成后回调
        // 设置一个超时时间为负无穷大（-1），永不超时，一直等待直到操作完成
        // 有限时间会导致长回答生成中断，服务器端回调前单方面中断，报错 ResponseBodyEmitter has already completed
        SseEmitter sseEmitter = new SseEmitter(-1L);
        GptEventSourceListener listener = new GptEventSourceListener(sseEmitter);

        // 5、加入历史消息记录，提供上下文信息
        List<Message> messages = conversationId != null ?
                transform(queryMessagesByConversationId(conversationId,Constants.MESSAGE_LIMIT_NUM)) : new ArrayList<>();

        // 6、加入本次提问问题
        Message message = Message.of(prompt);
        messages.add(message);

        ChatCompletion chatCompletion = ChatCompletion.builder()
                .messages(messages)
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
                .maxTokens(8192)
                .build();

        // 7、设置完成时的回调函数
        listener.setOnComplete(msg -> {
            // 保存历史信息
            saveCurrentMessage(conversationId,prompt, Constants.DIRECTION_QUESTION);
            saveCurrentMessage(conversationId,msg, Constants.DIRECTION_ANSWER);

            if(usePublicApi) {
                user.setUserTokenCount(user.getUserTokenCount()-1);
                if(userMapper.updateUserTokenCount(user) <= 0) {
                    throw new RuntimeException("扣除公用 api 使用次数失败");
                }
            }
            SseHelper.send(sseEmitter,"[DONE]");
            SseHelper.complete(sseEmitter);
            System.out.println(msg);
        });

        // 8、提问
        chatGPTStream.streamChatCompletion(chatCompletion,listener);

        return sseEmitter;
    }

    // 根据 conversationId 查询某个会话的若干条对话
    @Override
    public List<ChatgptMessage> queryMessagesByConversationId(String conversationId, Integer limit) {
        // 校验会话是否属于当前用户

        // 根据 conversationId 查询某个会话的若干条对话
        return chatgptMessageMapper.selectMessagesByConversationId(conversationId,limit);
    }
}
