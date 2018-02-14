package com.example.owner.petbetter.classes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by owner on 28/7/2017.
 */

public class Pet {

    @SerializedName("_id")
    long id;

    @SerializedName("user_id")
    long userId;

    @SerializedName("name")
    String name;

    @SerializedName("classification")
    String classification;

    @SerializedName("breed")
    String breed;

    @SerializedName("height")
    float height;

    @SerializedName("weight")
    float weight;

    public Pet(long id, long userId, String name, String classification, String breed, float height, float weight) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.classification = classification;
        this.breed = breed;
        this.height = height;
        this.weight = weight;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void showRecord(){

    }
}
