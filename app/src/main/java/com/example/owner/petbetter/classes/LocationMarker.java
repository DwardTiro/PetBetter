package com.example.owner.petbetter.classes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by owner on 28/7/2017.
 */

public class LocationMarker {

    @SerializedName("bldg_name")
    String bldgName;

    @SerializedName("location")
    String location;

    @SerializedName("_id")
    long id;

    @SerializedName("user_id")
    long userId;

    @SerializedName("longitude")
    double longitude;

    @SerializedName("latitude")
    double latitude;

    @SerializedName("type")
    int type;

    @SerializedName("faci_id")
    long faciId;

    public LocationMarker(long id, String bldgName, double longitude, double latitude, String location, long userId, int type) {
        this.id = id;
        this.bldgName = bldgName;
        this.longitude = longitude;
        this.latitude = latitude;
        this.location = location;
        this.userId = userId;
        this.type = type;
    }

    public LocationMarker(long id, String bldgName, double longitude, double latitude, String location, long userId, int type, long faciId) {
        this.id = id;
        this.bldgName = bldgName;
        this.longitude = longitude;
        this.latitude = latitude;
        this.location = location;
        this.userId = userId;
        this.type = type;
        this.faciId = faciId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBldgName() {
        return bldgName;
    }

    public void setBldgName(String bldgName) {
        this.bldgName = bldgName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public long getFaciId() {
        return faciId;
    }

    public void setFaciId(long faciId) {
        this.faciId = faciId;
    }
}