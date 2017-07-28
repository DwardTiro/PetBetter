package com.example.owner.petbetter.classes;

/**
 * Created by owner on 28/7/2017.
 */

public class Pet {
    private String name, classification, breed;
    float height, weight;

    public Pet(String name, String classification, String breed, float height, float weight) {
        this.name = name;
        this.classification = classification;
        this.breed = breed;
        this.height = height;
        this.weight = weight;
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
