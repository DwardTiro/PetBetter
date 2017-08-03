package com.example.owner.petbetter.classes;

/**
 * Created by owner on 28/7/2017.
 */

public class Marker {
    String location, country, name;
    long id, userId;
    int postalCode;
    double longitude, latitude;

    public Marker(long id, String location, String country, double longitude, double latitude, int postalCode, long userId, String name) {
        this.id = id;
        this.location = location;
        this.country = country;
        this.longitude = longitude;
        this.latitude = latitude;
        this.postalCode = postalCode;
        this.userId = userId;
        this.name = name;
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

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
