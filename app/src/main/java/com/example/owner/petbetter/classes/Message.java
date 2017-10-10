package com.example.owner.petbetter.classes;

import com.example.owner.petbetter.database.DataAdapter;

import java.sql.SQLException;

/**
 * Created by owner on 4/10/2017.
 */

public class Message {

    private long id;
    private long userId, fromId;
    private String messageContent;
    private String fromName;

    public Message(long id, long userId, long fromId, String firstName, String lastName) {
        this.id = id;
        this.userId = userId;
        this.fromId = fromId;
        this.fromName = firstName + " " + lastName;
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
