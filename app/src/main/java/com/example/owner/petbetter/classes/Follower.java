package com.example.owner.petbetter.classes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by owner on 21/10/2017.
 */

public class Follower {

    @SerializedName("_id")
    long id;

    @SerializedName("topic_id")
    long topicId;

    @SerializedName("user_id")
    long userId;

    @SerializedName("is_allowed")
    int isAllowed;

    public Follower(long id, long topicId, long userId, int isAllowed) {
        this.id = id;
        this.topicId = topicId;
        this.userId = userId;
        this.isAllowed = isAllowed;
    }

    public int getIsAllowed() {
        return isAllowed;
    }

    public void setIsAllowed(int isAllowed) {
        this.isAllowed = isAllowed;
    }

    public long getId() {
        return id;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
