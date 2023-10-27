package org.mrying.chat.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.mrying.chat.model.Conversation;

@Mapper
public interface ConversationMapper {

    // 保存创建的会话
    int InsertNewConversation(Conversation conversation);
}