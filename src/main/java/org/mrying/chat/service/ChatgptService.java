package org.mrying.chat.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * @author 邓和颖
 * @since 2023/10/14 19:35
 */
public interface ChatgptService {

    // 向 chatgpt 提问
    SseEmitter sendMsgBySse(Boolean usePublicApi,String conversationId,String prompt);

    SseEmitter sendMsgBySseForNote(Boolean usePublicApi,String prompt);

    // 根据用户给的提示，chatgpt 生成 5 条相关问题
    List<String> queryQuestionByChatgpt(String prompt);

    // 根据用户给的提示，chatgpt 生成 5 条更专业的问题
    List<String> refreshQuestionByChatgpt(String prompt);
}
