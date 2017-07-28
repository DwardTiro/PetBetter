package com.example.owner.petbetter.classes;

/**
 * Created by owner on 28/7/2017.
 */

public class User {
    private String lastName, firstName, middleName, mobileNumber, phoneNumber, userName, email, password;
    private int age;

    public User(String lastName, String firstName, String middleName, String mobileNumber, String phoneNumber, String userName,
                String email, String password, int age){
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.mobileNumber = mobileNumber;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void sendMessage(String message){
        //implement methods here
    }
}
