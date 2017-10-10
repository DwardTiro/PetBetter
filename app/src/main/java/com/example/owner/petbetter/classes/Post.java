package com.example.owner.petbetter.classes;

/**
 * Created by owner on 4/10/2017.
 */

public class Post {
    private long id;
    private long userId;
    private String topicName;
    private String topicContent;
    private String topicUser;

    public Post(long id, long userId, String topicName, String topicContent, String topicUser) {
        this.id = id;
        this.userId = userId;
        this.topicName = topicName;
        this.topicContent = topicContent;
        this.topicUser = topicUser;
    }

    public Post(long id, long userId, String topicName, String topicContent) {
        this.id = id;
        this.userId = userId;
        this.topicName = topicName;
        this.topicContent = topicContent;
    }

    public Post(long id, long userId, String topicName, String topicContent, String firstName, String lastName) {
        this.id = id;
        this.userId = userId;
        this.topicName = topicName;
        this.topicContent = topicContent;
        this.topicUser = firstName +" "+ lastName;
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

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicContent() {
        return topicContent;
    }

    public void setTopicContent(String topicContent) {
        this.topicContent = topicContent;
    }

    public String getTopicUser() {
        return topicUser;
    }

    public void setTopicUser(String topicUser) {
        this.topicUser = topicUser;
    }
}
