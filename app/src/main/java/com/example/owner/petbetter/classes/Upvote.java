package com.example.owner.petbetter.classes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by owner on 8/3/2018.
 */

public class Upvote {
    @SerializedName("_id")
    long id;

    @SerializedName("feed_id")
    int feedId;

    @SerializedName("user_id")
    int userId;

    @SerializedName("value")
    int value;

    @SerializedName("type")
    int type;

    @SerializedName("is_synced")
    int isSynced;

    public Upvote(long id, int feedId, int userId, int value, int type, int isSynced) {
        this.id = id;
        this.feedId = feedId;
        this.userId = userId;
        this.value = value;
        this.type = type;
        this.isSynced = isSynced;
    }

    public Upvote(long id, int feedId, int userId, int value, int type) {
        this.id = id;
        this.feedId = feedId;
        this.userId = userId;
        this.value = value;
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getIs_synced() {
        return isSynced;
    }

    public void setIs_synced(int is_synced) {
        this.isSynced = is_synced;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getFeedId() {
        return feedId;
    }

    public void setFeedId(int feedId) {
        this.feedId = feedId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
