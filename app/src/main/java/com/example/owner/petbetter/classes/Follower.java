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

    public Follower(long id, long topicId, long userId) {
        this.id = id;
        this.topicId = topicId;
        this.userId = userId;
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
