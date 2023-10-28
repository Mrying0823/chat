package org.mrying.chat.service;

import org.mrying.chat.model.Conversation;

import java.util.List;

/**
 * @author 邓和颖
 * @since 2023/10/14 17:00
 */
public interface ConversationService {

    // 新建 chatgpt 会话
    String createConversation(Conversation conversation);

    // 根据用户 id 查询所有会话
    List<Conversation> queryConversationByUserId(String userId);
}
