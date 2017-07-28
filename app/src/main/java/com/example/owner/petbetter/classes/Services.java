package com.example.owner.petbetter.classes;

/**
 * Created by owner on 28/7/2017.
 */

public class Services {
    String serviceName;
    int servicePrice;

    public Sevices(String serviceName, int servicePrice) {
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(int servicePrice) {
        this.servicePrice = servicePrice;
    }
}
