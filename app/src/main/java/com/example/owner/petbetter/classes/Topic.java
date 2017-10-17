package com.example.owner.petbetter.classes;

/**
 * Created by owner on 17/10/2017.
 */

public class Topic {
    long id, creatorId;
    String topicName, topicDesc, dateCreated;
    int isDeleted;
    private String creatorName;

    public Topic(long id, long creatorId, String topicName, String topicDesc, String dateCreated, int isDeleted, String firstName,
                 String lastName) {
        this.id = id;
        this.creatorId = creatorId;
        this.topicName = topicName;
        this.topicDesc = topicDesc;
        this.dateCreated = dateCreated;
        this.isDeleted = isDeleted;
        if(creatorId==0)
            this.creatorName="";
        else
            this.creatorName = firstName+" "+lastName;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicDesc() {
        return topicDesc;
    }

    public void setTopicDesc(String topicDesc) {
        this.topicDesc = topicDesc;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}
