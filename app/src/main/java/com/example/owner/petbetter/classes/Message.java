package com.example.owner.petbetter.classes;

import com.example.owner.petbetter.database.DataAdapter;
import com.google.gson.annotations.SerializedName;

import java.sql.SQLException;

/**
 * Created by owner on 4/10/2017.
 */

public class Message {

    @SerializedName("_id")
    private long id;

    @SerializedName("user_one")
    private long userId;

    @SerializedName("user_two")
    private long fromId;

    @SerializedName("is_allowed")
    private long isAllowed;


    private String fromName;
    private String messageContent;

    public Message(long id, long userId, long fromId, int isAllowed) {
        this.id = id;
        this.userId = userId;
        this.fromId = fromId;
        this.isAllowed = isAllowed;
    }

    public Message(long id, long userId, long fromId, int isAllowed, String firstName, String lastName) {
        this.id = id;
        this.userId = userId;
        this.fromId = fromId;
        this.isAllowed = isAllowed;
        this.fromName = firstName + " " + lastName;
    }

    public long getIsAllowed() {
        return isAllowed;
    }

    public void setIsAllowed(long isAllowed) {
        this.isAllowed = isAllowed;
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

    public long getFromId() {
        return fromId;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    public String getMessageContent() {

        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }
}
