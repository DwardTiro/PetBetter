package com.example.owner.petbetter.classes;

/**
 * Created by Kristian on 10/13/2017.
 */

public class Notifications {

    long id, userId, doerId;
    int isRead, type;
    String datePerformed, doerName;

    public Notifications(long id, long userId, long doerId, int isRead, int type, String datePerformed, String first_name, String last_name) {
        this.id = id;
        this.userId = userId;
        this.doerId = doerId;
        this.isRead = isRead;
        this.type = type;
        this.datePerformed = datePerformed;
        this.doerName= first_name+" "+last_name;
    }

    public String getDoerName() {
        return doerName;
    }

    public void setDoerName(String doerName) {
        this.doerName = doerName;
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



