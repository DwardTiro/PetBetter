package com.example.owner.petbetter.classes;

/**
 * Created by owner on 28/7/2017.
 */

public class Marker {

    String bldgName, location;
    long id, userId;
    double longitude, latitude;

    public Marker(long id, String bldgName, double longitude, double latitude, String location, long userId) {
        this.id = id;
        this.bldgName = bldgName;
        this.longitude = longitude;
        this.latitude = latitude;
        this.location = location;
        this.userId = userId;
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
}
