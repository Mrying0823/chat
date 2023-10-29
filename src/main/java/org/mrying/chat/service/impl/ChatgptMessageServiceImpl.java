package org.mrying.chat.service.impl;

import org.mrying.chat.mapper.ChatgptMessageMapper;
import org.mrying.chat.model.ChatgptMessage;
import org.mrying.chat.service.ChatgptMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 邓和颖
 * @since 2023/10/29 20:59
 */
@Service("chatgptMessageService")
public class ChatgptMessageServiceImpl implements ChatgptMessageService {

    private ChatgptMessageMapper chatgptMessageMapper;

    @Autowired
    public void setChatgptMessageMapper(ChatgptMessageMapper chatgptMessageMapper) {
        this.chatgptMessageMapper = chatgptMessageMapper;
    }

    // 根据 conversationId 查询某个会话的所有对话
    @Override
    public List<ChatgptMessage> queryAllMessagesByConversationId(String conversationId) {
        return chatgptMessageMapper.selectAllMessagesByConversationId(conversationId);
    }
}
