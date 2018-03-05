package com.example.owner.petbetter.services;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.owner.petbetter.interfaces.CheckLogout;
import com.example.owner.petbetter.interfaces.CheckUpdates;

/**
 * Created by owner on 27/2/2018.
 */

public class NotificationReceiver extends BroadcastReceiver {

    private CheckUpdates checkUpdates;
    private CheckLogout checkLogout;

    public NotificationReceiver(){
        super();
    }

    public NotificationReceiver(CheckUpdates checkUpdates){
        this.checkUpdates = checkUpdates;
    }

    public NotificationReceiver(CheckLogout checkLogout){
        this.checkLogout = checkLogout;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        try{
            if(intent.getAction().equals(Intent.ACTION_ATTACH_DATA)){
                checkUpdates.onResult();
            }
            if(intent.getAction().equals("com.package.ACTION_LOGOUT")){
                System.out.println("LOG OUT PREP BOI");
                context.stopService(new Intent(context, MyService.class));
                checkLogout.onLogout();
                System.out.println("LOG OUT BOI ");
            }
        }catch(NullPointerException npe){
            Intent background = new Intent(context, MyService.class);
            context.startService(background);

        }
    }
}
