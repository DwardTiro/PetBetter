package com.example.owner.petbetter.classes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Kristian on 10/13/2017.
 */

public class Notifications {

    @SerializedName("_id")
    long id;

    @SerializedName("user_id")
    long userId;

    @SerializedName("doer_id")
    long doerId;

    @SerializedName("source_id")
    long sourceId;

    @SerializedName("is_read")
    int isRead;

    @SerializedName("type")
    int type;

    @SerializedName("date_performed")
    String datePerformed;

    @SerializedName("first_name")
    String firstName;

    @SerializedName("last_name")
    String lastName;

    /*
    public Notifications(long id, long userId, long doerId, int isRead, int type, String datePerformed, long sourceId) {
        this.id = id;
        this.userId = userId;
        this.doerId = doerId;
        this.isRead = isRead;
        this.type = type;
        this.datePerformed = datePerformed;
        this.sourceId = sourceId;
    }*/

    public Notifications(long id, long userId, long doerId, int isRead, int type, String datePerformed, long sourceId, String firstName, String lastName) {
        this.id = id;
        this.userId = userId;
        this.doerId = doerId;
        this.isRead = isRead;
        this.type = type;
        this.datePerformed = datePerformed;
        this.sourceId = sourceId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public long getSourceId() {
        return sourceId;
    }

    public void setSourceId(long sourceId) {
        this.sourceId = sourceId;
    }

    public String getDoerName() {
        return this.firstName+" "+this.lastName;
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

    public long getDoerId() {
        return doerId;
    }

    public void setDoerId(long doerId) {
        this.doerId = doerId;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDatePerformed() {
        return datePerformed;
    }

    public void setDatePerformed(String datePerformed) {
        this.datePerformed = datePerformed;
    }
}



