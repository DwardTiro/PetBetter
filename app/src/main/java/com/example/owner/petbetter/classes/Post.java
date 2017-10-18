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
    private long topicId;
    private String dateCreated;
    private int isDeleted;

    public Post(long id, long userId, String topicName, String topicContent, long topicId) {
        this.id = id;
        this.userId = userId;
        this.topicName = topicName;
        this.topicContent = topicContent;
        //this.topicUser = firstName+" "+lastName;
        this.topicId = topicId;
    }

    public Post(long id, long userId, String topicName, String topicContent) {
        this.id = id;
        this.userId = userId;
        this.topicName = topicName;
        this.topicContent = topicContent;
    }

    public Post(long id, long userId, String topicName, String topicContent, String dateCreated, String firstName, String lastName,
                int isDeleted) {
        this.id = id;
        this.userId = userId;
        this.topicName = topicName;
        this.topicContent = topicContent;
        this.dateCreated = dateCreated;
        this.topicUser = firstName +" "+ lastName;
        this.isDeleted = isDeleted;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTopicId() {
        return topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getId() {
        return id;
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
