package com.example.owner.petbetter.classes;

/**
 * Created by Kristian on 10/13/2017.
 */

public class Notifications {

    int notifProfilePic;
    String notifProfileName;
    String notifTimeStamp;
    String notifPostTitle;

    public int getNotifProfilePic(){
        return this.notifProfilePic;
    }
    public String getNotifProfileName() {
        return this.notifProfileName;
    }

    public String getNotifTimeStamp() {
        return this.notifTimeStamp;
    }

    public String getNotifPostTitle() {
        return this.notifPostTitle;
    }

    public void setNotifProfilePic(int profPic){
        this.notifProfilePic=profPic;
    }
    public void setNotifProfileName(String profileName) {
        this.notifProfileName= profileName;
    }

    public void setNotifTimeStamp(String timeStamp) {

        this.notifTimeStamp=timeStamp;
    }

    public void setNotifPostTitle(String postTitle) {

        this.notifPostTitle = postTitle;
    }
}



