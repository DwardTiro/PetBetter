package com.example.owner.petbetter.classes;

/**
 * Created by owner on 28/7/2017.
 */

public class Veterinarian extends User {
    String specialty;
    int rating;

    public Veterinarian(String lastName, String firstName, String middleName, String mobileNumber, String phoneNumber,
                        String userName, String email, String password, int age, String specialty) {
        super(lastName, firstName, middleName, mobileNumber, phoneNumber, userName, email, password, age);
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
