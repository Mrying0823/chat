package org.mrying.chat.service;

import org.mrying.chat.model.ChatgptMessage;

import java.util.List;

/**
 * @author 邓和颖
 * @since 2023/10/29 20:58
 */
public interface ChatgptMessageService {

    // 根据 conversationId 查询某个会话的所有对话
    List<ChatgptMessage> queryAllMessagesByConversationId(String conversationId);
}
