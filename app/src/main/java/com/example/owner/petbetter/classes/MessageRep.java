package com.example.owner.petbetter.classes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by owner on 4/10/2017.
 */

public class MessageRep {

    @SerializedName("_id")
    private long id;

    @SerializedName("user_id")
    private long userId;

    @SerializedName("sender_id")
    private long senderId;

    @SerializedName("message_id")
    private int messageId;

    @SerializedName("rep_content")
    private String repContent;

    @SerializedName("is_sent")
    private int isSent;

    @SerializedName("date_performed")
    private String datePerformed;

    @SerializedName("message_photo")
    private String messagePhoto;


    private String userName;

    public MessageRep(long id, long userId, long senderId, int messageId, String repContent, int isSent, String datePerformed) {
        this.id = id;
        this.userId = userId;
        this.senderId = senderId;
        this.messageId = messageId;
        this.repContent = repContent;
        this.isSent = isSent;
        this.datePerformed = datePerformed;
    }

    public MessageRep(long id, long userId, long senderId, int messageId, String repContent, int isSent, String datePerformed, String messagePhoto) {
        this.id = id;
        this.userId = userId;
        this.senderId = senderId;
        this.messageId = messageId;
        this.repContent = repContent;
        this.isSent = isSent;
        this.datePerformed = datePerformed;
        this.messagePhoto = messagePhoto;
    }

    public MessageRep(long id, long userId, long senderId, int messageId, String repContent, int isSent, String datePerformed,
                      String firstName, String lastName, String messagePhoto) {
        this.id = id;
        this.userId = userId;
        this.senderId = senderId;
        this.messageId = messageId;
        this.repContent = repContent;
        this.isSent = isSent;
        this.datePerformed = datePerformed;
        this.userName = firstName+" "+lastName;
        this.messagePhoto = messagePhoto;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getIsSent() {
        return isSent;
    }

    public void setIsSent(int isSent) {
        this.isSent = isSent;
    }

    public String getDatePerformed() {
        return datePerformed;
    }

    public void setDatePerformed(String datePerformed) {
        this.datePerformed = datePerformed;
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

    public String getMessagePhoto() {
        return messagePhoto;
    }

    public void setMessagePhoto(String messagePhoto) {
        this.messagePhoto = messagePhoto;
    }
}
