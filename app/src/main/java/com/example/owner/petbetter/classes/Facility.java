package com.example.owner.petbetter.classes;

/**
 * Created by owner on 28/7/2017.
 */

import java.util.ArrayList;

public class Facility {
    private String vetId, openTime, closeTime, faciType, faciName;
    private boolean isOpen;
    private ArrayList<Services> services;

    public Facility(String vetId, String openTime, String closeTime, String faciType, String faciName, boolean isOpen, ArrayList<Services> services) {
        this.vetId = vetId;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.faciType = faciType;
        this.faciName = faciName;
        this.isOpen = isOpen;
        this.services = services;
    }

    public String getVetId() {
        return vetId;
    }

    public void setVetId(String vetId) {
        this.vetId = vetId;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getFaciType() {
        return faciType;
    }

    public void setFaciType(String faciType) {
        this.faciType = faciType;
    }

    public String getFaciName() {
        return faciName;
    }

    public void setFaciName(String faciName) {
        this.faciName = faciName;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public void addServices(){

    }

    public void removeServices(){

    }

    public void displayServices(){

    }
}
