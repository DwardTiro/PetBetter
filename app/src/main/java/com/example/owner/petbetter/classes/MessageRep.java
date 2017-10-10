package com.example.owner.petbetter.classes;

/**
 * Created by owner on 4/10/2017.
 */

public class MessageRep {
    private long id, userId;
    private int messageId;
    private String repContent;
    private int isSent;

    public MessageRep(long id, long userId, int messageId, String repContent, int isSent) {
        this.id = id;
        this.userId = userId;
        this.messageId = messageId;
        this.repContent = repContent;
        this.isSent = isSent;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getRepContent() {
        return repContent;
    }

    public void setRepContent(String repContent) {
        this.repContent = repContent;
    }

    public int isSent() {
        return isSent;
    }

    public void setSent(int sent) {
        isSent = sent;
    }
}
