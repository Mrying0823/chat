package org.mrying.chat.model;

import java.util.Date;

/**
 * @author 邓和颖
 */
public class ChatgptMessage {
    private String messageId;

    private Integer messageDirection;

    private String conversationId;

    private String content;

    private Date createTime;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Integer getMessageDirection() {
        return messageDirection;
    }

    public void setMessageDirection(Integer messageDirection) {
        this.messageDirection = messageDirection;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}