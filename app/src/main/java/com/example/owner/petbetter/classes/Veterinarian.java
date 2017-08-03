package com.example.owner.petbetter.classes;

/**
 * Created by owner on 28/7/2017.
 */

public class Veterinarian extends User {
    String specialty;
    int rating;

    public Veterinarian(long userId, String lastName, String firstName, String mobileNumber, String phoneNumber,
                        String email, String password, int age, int userType, String specialty) {
        super(userId, lastName, firstName, mobileNumber, phoneNumber, email, password, age, userType);
        this.specialty = specialty;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
