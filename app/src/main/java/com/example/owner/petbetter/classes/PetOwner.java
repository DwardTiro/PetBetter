package com.example.owner.petbetter.classes;

/**
 * Created by owner on 28/7/2017.
 */

public class PetOwner extends User {
    public PetOwner(long userId, String lastName, String firstName, String mobileNumber, String phoneNumber,
                    String email, String password, int age, int userType, String userPhoto) {
        super(userId, lastName, firstName, mobileNumber, phoneNumber, email, password, age, userType, userPhoto);
    }
    public void serviceEval(){

    }
}
