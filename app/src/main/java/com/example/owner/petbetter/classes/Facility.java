package com.example.owner.petbetter.classes;

/**
 * Created by owner on 28/7/2017.
 */

import java.util.ArrayList;

public class Facility {
    private long id;
    private int vetId;
    private float rating;
    private String faciName, location, hoursOpen, hoursClose, contactInfo;
    private double longitude, latitude;

    public Facility(int id, String faciName, String location, String hoursOpen, String hoursClose, String contactInfo, int vetId, float rating) {
        this.id = id;
        this.faciName = faciName;
        this.location = location;
        this.hoursOpen = hoursOpen;
        this.hoursClose = hoursClose;
        this.contactInfo = contactInfo;
        this.vetId = vetId;
        this.rating = rating;
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

    public float getRating(){
        return rating;
    }
    public void setRating(float rating){
        this.rating = rating;
    }

    public int getVetId() {
        return vetId;
    }

    public void setVetId(int vetId) {
        this.vetId = vetId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFaciName() {
        return faciName;
    }

    public void setFaciName(String faciName) {
        this.faciName = faciName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
}
