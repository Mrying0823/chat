package org.mrying.chat.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.mrying.chat.model.ChatgptMessage;

import java.util.List;

/**
 * @author 邓和颖
 */
@Mapper
public interface ChatgptMessageMapper {

    // 保存当前对话内容
    int insertCurrentMessage(ChatgptMessage message);

    // 根据 conversationId 查询某个会话的若干条对话
    List<ChatgptMessage> selectMessagesByConversationId(@Param("conversationId") String conversationId, @Param("limit") Integer limit);
}