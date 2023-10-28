package org.mrying.chat.model;

import java.util.Date;

/**
 * @author 邓和颖
 */
public class Conversation {
    private String conversationId;

    private String userId;

    private Integer conversationType;

    private Date createTime;

    private String conversationName;

    private String firstMessage;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getConversationType() {
        return conversationType;
    }

    public void setConversationType(Integer conversationType) {
        this.conversationType = conversationType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getConversationName() {
        return conversationName;
    }

    public void setConversationName(String conversationName) {
        this.conversationName = conversationName;
    }

    public String getFirstMessage() {
        return firstMessage;
    }

    public void setFirstMessage(String firstMessage) {
        this.firstMessage = firstMessage;
    }
}