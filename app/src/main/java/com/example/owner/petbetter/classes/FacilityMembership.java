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

    @SerializedName("user_id")
    long userId;

    public FacilityMembership(long id, long faciId, long userId) {
        this.id = id;
        this.faciId = faciId;
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

}
