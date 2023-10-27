package org.mrying.chat.service;

import org.mrying.chat.model.ChatgptMessage;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * @author 邓和颖
 * @since 2023/10/14 19:35
 */
public interface ChatgptService {

    // 向 chatgpt 提问
    SseEmitter sendMsgBySse(Boolean publicApi,String conversationId,String prompt);

    // 根据 conversationId 查询某个会话的若干条对话
    List<ChatgptMessage> queryMessagesByConversationId(String conversationId,Integer limit);
}
