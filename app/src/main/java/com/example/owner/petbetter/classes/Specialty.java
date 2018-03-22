package com.example.owner.petbetter.classes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by owner on 22/3/2018.
 */

public class Specialty {
    @SerializedName("_id")
    long id;

    @SerializedName("_id")
    String specialization;

    @SerializedName("vet_id")
    long vetId;

    public Specialty(long id, String specialization, long vetId) {
        this.id = id;
        this.specialization = specialization;
        this.vetId = vetId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public long getVetId() {
        return vetId;
    }

    public void setVetId(long vetId) {
        this.vetId = vetId;
    }
}
