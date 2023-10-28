package org.mrying.chat.mapper;

import org.apache.ibatis.annotations.Mapper;
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
}