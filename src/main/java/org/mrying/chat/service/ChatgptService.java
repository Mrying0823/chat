package org.mrying.chat.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author 邓和颖
 * @since 2023/10/14 19:35
 */
public interface ChatgptService {

    // 向 chatgpt 提问
    SseEmitter sendMsgBySse(Boolean publicApi,String conversationId,String prompt);
}
