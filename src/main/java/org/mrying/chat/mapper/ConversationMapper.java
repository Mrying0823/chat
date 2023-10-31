package org.mrying.chat.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.mrying.chat.model.Conversation;

import java.util.List;

/**
 * @author 邓和颖
 */
@Mapper
public interface ConversationMapper {

    // 保存创建的会话
    int insertNewConversation(Conversation conversation);

    // 根据用户 id 查询所有会话
    List<Conversation> selectConversationByUserId(String userId);

    // 根据会话 id 删除 chatgpt 会话及会话内容
    int deleteConversationById(String conversationId);

    // 根据 conversationId 查询会话并修改名称
    int updateConversationNameById(@Param("conversationId") String conversationId, @Param("conversationName") String conversationName);
}