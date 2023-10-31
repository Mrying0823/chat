package org.mrying.chat.service.impl;

import org.mrying.chat.mapper.ChatgptMessageMapper;
import org.mrying.chat.mapper.ConversationMapper;
import org.mrying.chat.model.Conversation;
import org.mrying.chat.service.ConversationService;
import org.mrying.chat.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author 邓和颖
 * @since 2023/10/14 17:04
 */
@Service("conversationService")
public class ConversationServiceImpl implements ConversationService {

    private ConversationMapper conversationMapper;

    private ChatgptMessageMapper chatgptMessageMapper;

    @Autowired
    public void setConversationMapper(ConversationMapper conversationMapper) {
        this.conversationMapper = conversationMapper;
    }

    @Autowired
    public void setChatgptMessageMapper(ChatgptMessageMapper chatgptMessageMapper) {
        this.chatgptMessageMapper = chatgptMessageMapper;
    }

    // 创建 chatgpt 会话
    @Override
    public String createConversation(Conversation conversation) {
        // 设置创建人，暂时不设置
        conversation.setUserId("34382773848d4c86acb1f960c4681530");
        // 设置创建时间
        conversation.setCreateTime(new Date());
        // 设置会话 ID
        conversation.setConversationId(UUIDUtils.getUUID());
        // 保存创建的会话
        conversationMapper.insertNewConversation(conversation);
        // 返回会话 ID
        return conversation.getConversationId();
    }

    // 根据用户 id 查询所有会话
    @Override
    public List<Conversation> queryConversationByUserId(String userId) {
        return conversationMapper.selectConversationByUserId(userId);
    }

    // 根据 conversationId 删除 chatgpt 会话及会话内容
    @Transactional
    @Override
    public synchronized int deleteConversationById(String conversationId) {

        int result;

        // 根据 conversationId 删除 chatgpt 会话
        result = conversationMapper.deleteConversationById(conversationId);

        // 根据 conversationId 删除 chatgpt 会话内容
        if(result > 0) {
            result = chatgptMessageMapper.deleteMessageByConversationId(conversationId);
        }

        return result;
    }

    // 根据 conversationId 查询会话并修改名称
    @Override
    public int editConversationNameById(String conversationId, String conversationName) {
        return conversationMapper.updateConversationNameById(conversationId,conversationName);
    }
}
