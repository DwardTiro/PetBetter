package com.example.owner.petbetter.classes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by owner on 22/3/2018.
 */

public class FacilityMembership {
    @SerializedName("_id")
    long id;

    @SerializedName("faci_id")
    long faciId;

    @SerializedName("vet_id")
    long vetId;

    public FacilityMembership(long id, long faciId, long vetId) {
        this.id = id;
        this.faciId = faciId;
        this.vetId = vetId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFaciId() {
        return faciId;
    }

    public void setFaciId(long faciId) {
        this.faciId = faciId;
    }

    public long getVetId() {
        return vetId;
    }

    public void setVetId(long vetId) {
        this.vetId = vetId;
    }
}
