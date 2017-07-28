package com.example.owner.petbetter.classes;

/**
 * Created by owner on 28/7/2017.
 */

public class Address {
    String userId, location, country, postalCode;
    int houseNum;

    public Address(String userId, String location, String country, String postalCode, int houseNum) {
        this.userId = userId;
        this.location = location;
        this.country = country;
        this.postalCode = postalCode;
        this.houseNum = houseNum;
    }

    public Address(String userId){
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public int getHouseNum() {
        return houseNum;
    }

    public void setHouseNum(int houseNum) {
        this.houseNum = houseNum;
    }
}
