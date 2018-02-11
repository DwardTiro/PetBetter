package com.example.owner.petbetter.classes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by owner on 28/7/2017.
 */

public class Services {

    @SerializedName("_id")
    long id;

    @SerializedName("faci_id")
    long faciId;

    @SerializedName("service_name")
    String serviceName;

    @SerializedName("service_price")
    float servicePrice;

    public Services(long id, long faciId, String serviceName, float servicePrice) {
        this.id = id;
        this.faciId = faciId;
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
        //linked to facilities
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
}
