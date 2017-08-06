package com.example.owner.petbetter.classes;

/**
 * Created by owner on 28/7/2017.
 */

public class Marker {

    String bldgNum, street, bldgName, city, province;
    long id, userId;
    double longitude, latitude;

    public Marker(long id, String bldgNum, String street, String bldgName, String city, String province, double longitude, double latitude, long userId) {
        this.id = id;
        this.bldgNum = bldgNum;
        this.street = street;
        this.bldgName = bldgName;
        this.city = city;
        this.province = province;
        this.longitude = longitude;
        this.latitude = latitude;
        this.userId = userId;
    }

    public String getBldgNum() {
        return bldgNum;
    }

    public void setBldgNum(String bldgNum) {
        this.bldgNum = bldgNum;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBldgName() {
        return bldgName;
    }

    public void setBldgName(String bldgName) {
        this.bldgName = bldgName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
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
