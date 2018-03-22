package com.example.owner.petbetter.classes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by owner on 28/7/2017.
 */

public class Veterinarian extends User {

    @SerializedName("specialty")
    int specialty;

    @SerializedName("_id")
    int id;

    @SerializedName("rating")
    float rating;

    @SerializedName("education")
    String education;

    @SerializedName("is_licensed")
    int isLicensed;

    @SerializedName("profile_desc")
    String profileDesc;

    public Veterinarian(int id, long userId, String firstName, String lastName, String mobileNumber, String phoneNumber,
                        String email, String password, int age, int userType, String userPhoto, int specialty, float rating,
                        String education, int isLicensed, String profileDesc) {
        super(userId, firstName, lastName, mobileNumber, phoneNumber, email, password, age, userType, userPhoto);
        this.id = id;
        this.specialty = specialty;
        this.rating = rating;
        this.education = education;
        this.isLicensed = isLicensed;
        this.profileDesc = profileDesc;
    }

    public int getSpecialty() {
        return specialty;
    }

    public void setSpecialty(int specialty) {
        this.specialty = specialty;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public int getIsLicensed() {
        return isLicensed;
    }

    public void setIsLicensed(int isLicensed) {
        this.isLicensed = isLicensed;
    }

    public String getProfileDesc() {
        return profileDesc;
    }

    public void setProfileDesc(String profileDesc) {
        this.profileDesc = profileDesc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return super.getName();
    }


    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
