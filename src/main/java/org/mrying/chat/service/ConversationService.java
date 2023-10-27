package org.mrying.chat.service;

import org.mrying.chat.model.Conversation;

/**
 * @author 邓和颖
 * @since 2023/10/14 17:00
 */
public interface ConversationService {

    // 新建 chatgpt 会话
    String createConversation(Conversation conversation);
}
