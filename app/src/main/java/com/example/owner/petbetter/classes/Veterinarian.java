package com.example.owner.petbetter.classes;

/**
 * Created by owner on 28/7/2017.
 */

public class Veterinarian extends User {
    String specialty;
    int id;
    float rating;

    public Veterinarian(int id, long userId, String lastName, String firstName, String mobileNumber, String phoneNumber,
                        String email, String password, int age, int userType, String specialty, float rating) {
        super(userId, lastName, firstName, mobileNumber, phoneNumber, email, password, age, userType);
        this.id = id;
        this.specialty = specialty;
        this.rating = rating;
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
