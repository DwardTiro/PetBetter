package com.example.owner.petbetter.classes;

/**
 * Created by owner on 28/7/2017.
 */

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Facility {

    @SerializedName("faci_id")
    private long id;

    @SerializedName("rating")
    private float rating;

    @SerializedName("faci_name")
    private String faciName;

    @SerializedName("location")
    private String location;

    @SerializedName("contact_info")
    private String contactInfo;

    @SerializedName("faci_photo")
    private String faciPhoto;

    @SerializedName("is_disabled")
    private int isDisabled;


    public Facility(int id, String faciName, String location, String contactInfo, float rating, int isDisabled) {
        this.id = id;
        this.faciName = faciName;
        this.location = location;
        this.contactInfo = contactInfo;
        this.rating = rating;
        this.isDisabled = isDisabled;
    }

    public Facility(int id, String faciName, String location, String contactInfo,
                    float rating, String faciPhoto, int isDisabled) {
        this.id = id;
        this.faciName = faciName;
        this.location = location;
        this.contactInfo = contactInfo;
        this.rating = rating;
        this.faciPhoto = faciPhoto;
        this.isDisabled = isDisabled;
    }

    public int getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(int isDisabled) {
        this.isDisabled = isDisabled;
    }

    public String getFaciPhoto() {
        return faciPhoto;
    }

    public void setFaciPhoto(String faciPhoto) {
        this.faciPhoto = faciPhoto;
    }

    public float getRating(){
        return rating;
    }
    public void setRating(float rating){
        this.rating = rating;
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

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
}
