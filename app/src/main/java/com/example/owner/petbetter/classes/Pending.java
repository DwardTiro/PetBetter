package com.example.owner.petbetter.classes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by owner on 23/3/2018.
 */

public class Pending {
    @SerializedName("_id")
    long id;

    @SerializedName("foreign_id")
    long foreignId;

    @SerializedName("type")
    int type;

    @SerializedName("is_approved")
    int isApproved;

    public Pending(long id, long foreignId, int type, int isApproved) {
        this.id = id;
        this.foreignId = foreignId;
        this.type = type;
        this.isApproved = isApproved;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getForeignId() {
        return foreignId;
    }

    public void setForeignId(long foreignId) {
        this.foreignId = foreignId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(int isApproved) {
        this.isApproved = isApproved;
    }
}
