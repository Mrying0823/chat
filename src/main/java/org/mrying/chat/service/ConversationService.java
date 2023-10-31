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

    // 根据会话 id 删除 chatgpt 会话及会话内容
    int deleteConversationById(String conversationId);

    // 根据 conversationId 查询会话并修改名称
    int editConversationNameById(String conversationId,String conversationName);
}
