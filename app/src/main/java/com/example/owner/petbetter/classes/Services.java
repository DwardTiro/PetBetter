package com.example.owner.petbetter.classes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by owner on 28/7/2017.
 */

public class Services {

    @SerializedName("_id")
    int id;

    @SerializedName("faci_id")
    long faciId;

    @SerializedName("service_name")
    String serviceName;

    @SerializedName("service_price")
    float servicePrice;

    @SerializedName("is_deleted")
    int isDeleted;

    public Services(int id, long faciId, String serviceName, float servicePrice, int isDeleted) {
        this.id = id;
        this.faciId = faciId;
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
        this.isDeleted = isDeleted;
        //linked to facilities
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getFaciId() {
        return faciId;
    }

    public void setFaciId(long faciId) {
        this.faciId = faciId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public float getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(float servicePrice) {
        this.servicePrice = servicePrice;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}
