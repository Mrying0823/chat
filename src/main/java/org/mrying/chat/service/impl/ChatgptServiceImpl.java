package org.mrying.chat.service.impl;

import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.ChatGPTStream;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.ChatCompletionResponse;
import com.plexpt.chatgpt.entity.chat.Message;
import com.plexpt.chatgpt.util.SseHelper;
import org.mrying.chat.constants.Constants;
import org.mrying.chat.constants.RedisKey;
import org.mrying.chat.event.GptEventSourceListener;
import org.mrying.chat.exception.NoTokenCountException;
import org.mrying.chat.mapper.ChatgptMessageMapper;
import org.mrying.chat.mapper.UserMapper;
import org.mrying.chat.model.ChatgptMessage;
import org.mrying.chat.model.User;
import org.mrying.chat.service.ChatgptService;
import org.mrying.chat.settings.ChatgptConfiguration;
import org.mrying.chat.utils.SecurityContextHolderUtils;
import org.mrying.chat.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 邓和颖
 * @since 2023/10/14 19:38
 */
@Service("chatgptService")
public class ChatgptServiceImpl implements ChatgptService {

    private StringRedisTemplate stringRedisTemplate;

    // 上一个消息的时间戳
    private long lastMessageTimestamp = 0;

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

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
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

        // 获取当前时间的秒数
        long currentTimestamp = System.currentTimeMillis() / 1000;
        if (currentTimestamp <= lastMessageTimestamp) {
            // 如果新消息的时间戳与上一条消息的时间戳相同或更小，添加一个小的偏移量（例如1秒）
            currentTimestamp = lastMessageTimestamp + 1;
        }

        message.setCreateTime(new Date(currentTimestamp * 1000)); // 转回毫秒级时间戳
        lastMessageTimestamp = currentTimestamp;

        if(conversationId != null) {
            message.setConversationId(conversationId);
        }

        chatgptMessageMapper.insertCurrentMessage(message);
    }

    // 私有方法，用于获取 API Key
    private String getKey(Boolean usePublicApi, User user) {
        if (usePublicApi) {
            if (user.getUserTokenCount() > 0) {
                return chatgptConfiguration.getGptKey();
            } else {
                throw new NoTokenCountException("API请求次数不足");
            }
        } else {
            return user.getUserApiKey();
        }
    }

    // 私有方法，用于初始化 ChatGPTStream
    private ChatGPTStream initializeChatGPTStream(String key) {
        return ChatGPTStream.builder()
                .timeout(50)
                .apiKey(key)
                .apiHost(chatgptConfiguration.getHost())
                .build()
                .init();
    }

    // 私有方法，用于准备 SseEmitter
    private SseEmitter prepareSseEmitter() {
        return new SseEmitter(-1L);
    }

    // 私有方法，用于准备消息列表
    private List<Message> prepareMessages(String conversationId, String prompt) {
        List<Message> messages = conversationId != null ?
                transform(chatgptMessageMapper.selectMessagesByConversationId(conversationId, Constants.MESSAGE_LIMIT_NUM)) : new ArrayList<>();
        messages.add(Message.of(prompt));
        return messages;
    }

    // 私有方法，用于构建 ChatCompletion 对象
    private ChatCompletion buildChatCompletion(List<Message> messages) {
        return ChatCompletion.builder()
                .messages(messages)
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
                .maxTokens(8192)
                .build();
    }

    // 私有方法，用于创建 GptEventSourceListener
    private GptEventSourceListener createListener(SseEmitter sseEmitter, Boolean usePublicApi, User user, String conversationId, String prompt) {
        GptEventSourceListener listener = new GptEventSourceListener(sseEmitter);

        listener.setOnComplete(msg -> {
            if(conversationId != null) {
                saveCurrentMessage(conversationId, prompt, Constants.DIRECTION_QUESTION);
                saveCurrentMessage(conversationId, msg, Constants.DIRECTION_ANSWER);
            }

            if (usePublicApi) {
                user.setUserTokenCount(user.getUserTokenCount() - 1);
                if (userMapper.updateUserTokenCount(user) <= 0) {
                    throw new RuntimeException("扣除公用API使用次数失败");
                }
            }

            SseHelper.send(sseEmitter, "[DONE]");
            SseHelper.complete(sseEmitter);
            System.out.println(msg);
        });

        return listener;
    }

    // 向 chatgpt 提问
    @Transactional
    @Override
    public synchronized SseEmitter sendMsgBySse(Boolean usePublicApi, String conversationId, String prompt) {
        User user = userMapper.selectUserById(SecurityContextHolderUtils.getUserId());

        String key = getKey(usePublicApi, user);

        ChatGPTStream chatGPTStream = initializeChatGPTStream(key);
        SseEmitter sseEmitter = prepareSseEmitter();

        List<Message> messages = prepareMessages(conversationId, prompt);

        ChatCompletion chatCompletion = buildChatCompletion(messages);

        GptEventSourceListener listener = createListener(sseEmitter, usePublicApi, user, conversationId, prompt);

        chatGPTStream.streamChatCompletion(chatCompletion, listener);

        return sseEmitter;
    }

    @Override
    public SseEmitter sendMsgBySseForNote(Boolean usePublicApi, String prompt) {
        User user = userMapper.selectUserById(SecurityContextHolderUtils.getUserId());

        String key = getKey(usePublicApi, user);

        ChatGPTStream chatGPTStream = initializeChatGPTStream(key);
        SseEmitter sseEmitter = prepareSseEmitter();

        List<Message> messages = prepareMessages(null, prompt);

        ChatCompletion chatCompletion = buildChatCompletion(messages);

        GptEventSourceListener listener = createListener(sseEmitter, usePublicApi, user, null, prompt);

        chatGPTStream.streamChatCompletion(chatCompletion, listener);

        return sseEmitter;
    }

    // 将问题从信息中分离出来
    public List<String> extractQuestions(String text) {
        List<String> questions = new ArrayList<>();
        int startIndex = 0;

        while (startIndex < text.length()) {
            int dotIndex = text.indexOf(".", startIndex);
            if (dotIndex == -1) {
                break;
            }

            int questionStartIndex = dotIndex + 1;
            int questionEndIndex = text.indexOf("\n", questionStartIndex);

            if (questionEndIndex == -1) {
                questionEndIndex = text.length();
            }

            String question = text.substring(questionStartIndex, questionEndIndex).trim();
            questions.add(question);

            startIndex = questionEndIndex + 1;
        }

        return questions;
    }

    private ChatCompletionResponse postChatgpt(List<Message> messages) {
        ChatGPT chatGPT = ChatGPT.builder()
                .apiKey(chatgptConfiguration.getGptKey())
                .timeout(900)
                .apiHost(chatgptConfiguration.getHost())
                .build()
                .init();

        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
                .messages(messages)
                .maxTokens(3000)
                .temperature(0.9)
                .build();

        return chatGPT.chatCompletion(chatCompletion);
    }

    // 私有方法，用于处理 chatgpt 相关问题的通用逻辑
    private List<String> processChatgptQuestions(String prompt, boolean refresh) {
        User user = userMapper.selectUserById(SecurityContextHolderUtils.getUserId());
        String key = RedisKey.KEY_USER_QUESTION.concat(user.getUserId()).concat(prompt);

        prompt = prompt.concat("，请给出 5 条相关问题");

        List<String> questions;

        List<Message> messages = new ArrayList<>();

        List<String> redisQuestionList = stringRedisTemplate.opsForList().range(key, 0, -1);

        if (refresh && redisQuestionList != null) {
            for (String question : redisQuestionList) {
                // 加入上下文消息列表
                messages.add(Message.ofAssistant(question));
            }
        }

        Message message = Message.of(prompt);
        messages.add(message);
        messages.add(Message.ofSystem("你是一名教师，你现在要为一个想要提问但是不知道问什么的学生提供相关题目"));

        ChatCompletionResponse response = postChatgpt(messages);

        Message res = response.getChoices().get(0).getMessage();

        questions = extractQuestions(res.getContent());

        stringRedisTemplate.opsForList().leftPushAll(key, questions.toArray(new String[0]));
        stringRedisTemplate.expire(key, 1, TimeUnit.HOURS);

        return questions;
    }

    @Override
    public List<String> queryQuestionByChatgpt(String prompt) {
        return processChatgptQuestions(prompt, false);
    }

    @Override
    public List<String> refreshQuestionByChatgpt(String prompt) {
        return processChatgptQuestions(prompt, true);
    }
}
