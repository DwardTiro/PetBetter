package com.example.owner.petbetter.classes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by owner on 28/7/2017.
 */

public class Veterinarian extends User {

    @SerializedName("specialty")
    String specialty;

    @SerializedName("_id")
    int id;

    @SerializedName("rating")
    float rating;

    public Veterinarian(int id, long userId, String firstName, String lastName, String mobileNumber, String phoneNumber,
                        String email, String password, int age, int userType, String userPhoto, String specialty, float rating) {
        super(userId, firstName, lastName, mobileNumber, phoneNumber, email, password, age, userType, userPhoto);
        this.id = id;
        this.specialty = specialty;
        this.rating = rating;
        //this.phoneNumber = phoneNumber;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpecialty() {
        return specialty;
    }

    public String getName() {
        return super.getName();
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
