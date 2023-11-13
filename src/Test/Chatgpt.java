import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.ChatGPTStream;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.ChatCompletionResponse;
import com.plexpt.chatgpt.entity.chat.Message;
import com.plexpt.chatgpt.listener.SseStreamListener;
import com.plexpt.chatgpt.util.SseHelper;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.mrying.chat.constants.Constants;
import org.mrying.chat.event.GptEventSourceListener;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 邓和颖
 * @since 2023/11/9 20:49
 */
@Log
public class Chatgpt {
    @Test
    public void chatgptTest() {
//        ChatGPT chatGPT = ChatGPT.builder()
//                .apiKey("sk-BVS6lkw0gCx6iA3PMwxXWKMBPqZy46K4bHbaHgqzc7KwEZse")
//                .timeout(900)
//                .apiHost("https://api.chatanywhere.com.cn/") //反向代理地址
//                .build()
//                .init();
//
//        Message system = Message.ofSystem("你现在是一个诗人，专门写七言绝句");
//        Message message = Message.of("写一段七言绝句诗，题目是：火锅！");
//
//        ChatCompletion chatCompletion = ChatCompletion.builder()
//                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
//                .messages(Arrays.asList(system, message))
//                .maxTokens(3000)
//                .temperature(0.9)
//                .build();
//        ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
//        Message res = response.getChoices().get(0).getMessage();
//        System.out.println(res);

/*        ChatGPT chatGPT = ChatGPT.builder()
                .apiKey("sk-BVS6lkw0gCx6iA3PMwxXWKMBPqZy46K4bHbaHgqzc7KwEZse")
                .apiHost("https://api.chatanywhere.com.cn/") //反向代理地址
                .build()
                .init();

        String res = chatGPT.chat("写一段七言绝句诗，题目是：火锅！");
        System.out.println(res);*/

/*        ChatGPTStream chatGPTStream = ChatGPTStream.builder()
                .timeout(600)
                .apiKey("sk-BVS6lkw0gCx6iA3PMwxXWKMBPqZy46K4bHbaHgqzc7KwEZse")
                .apiHost("https://api.chatanywhere.com.cn/")
                .build()
                .init();

        SseEmitter sseEmitter = new SseEmitter(-1L);

        SseStreamListener listener = new SseStreamListener(sseEmitter);
        Message message = Message.of("写一段七言绝句诗，题目是：火锅！");

        listener.setOnComplate(msg -> {
            //回答完成，可以做一些事情
            System.out.println(msg);
        });
        chatGPTStream.streamChatCompletion(Arrays.asList(message), listener);*/

        ChatGPTStream chatGPTStream = ChatGPTStream.builder()
                .timeout(50)
                .apiKey("sk-BVS6lkw0gCx6iA3PMwxXWKMBPqZy46K4bHbaHgqzc7KwEZse")
                .apiHost("https://api.chatanywhere.com.cn/")
                .build()
                .init();

        // 4、实例化流式输出类，设置监听，使得在所有消息输出完成后回调
        // 设置一个超时时间为负无穷大（-1），永不超时，一直等待直到操作完成
        // 有限时间会导致长回答生成中断，服务器端回调前单方面中断，报错 ResponseBodyEmitter has already completed
        SseEmitter sseEmitter = new SseEmitter(-1L);
        GptEventSourceListener listener = new GptEventSourceListener(sseEmitter);

        // 5、加入历史消息记录，提供上下文信息
        List<Message> messages = new ArrayList<>();

        // 6、加入本次提问问题
        Message message = Message.of("JAVA");
        messages.add(message);

        ChatCompletion chatCompletion = ChatCompletion.builder()
                .messages(messages)
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
                .maxTokens(8192)
                .build();

        // 7、设置完成时的回调函数
        listener.setOnComplete(msg -> {
            // 保存历史信息
            SseHelper.complete(sseEmitter);
            System.out.println(msg);
        });

        // 8、提问
        chatGPTStream.streamChatCompletion(chatCompletion,listener);
    }
}
