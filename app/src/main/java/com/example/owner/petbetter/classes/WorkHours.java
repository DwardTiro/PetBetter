package com.example.owner.petbetter.classes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by owner on 9/4/2018.
 */

public class WorkHours {
    @SerializedName("_id")
    int id;

    @SerializedName("faci_id")
    int faciId;

    @SerializedName("days")
    String days;

    @SerializedName("hours_open")
    String hoursOpen;

    @SerializedName("hours_close")
    String hoursClose;

    @SerializedName("is_deleted")
    int isDeleted;

    public WorkHours(int id, int faciId, String days, String hoursOpen, String hoursClose, int isDeleted) {
        this.id = id;
        this.faciId = faciId;
        this.days = days;
        this.hoursOpen = hoursOpen;
        this.hoursClose = hoursClose;
        this.isDeleted = isDeleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFaciId() {
        return faciId;
    }

    public void setFaciId(int faciId) {
        this.faciId = faciId;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getHoursOpen() {
        return hoursOpen;
    }

    public void setHoursOpen(String hoursOpen) {
        this.hoursOpen = hoursOpen;
    }

    public String getHoursClose() {
        return hoursClose;
    }

    public void setHoursClose(String hoursClose) {
        this.hoursClose = hoursClose;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}
