package com.example.owner.petbetter.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.owner.petbetter.interfaces.CheckUpdates;

/**
 * Created by owner on 27/2/2018.
 */

public class NotificationReceiver extends BroadcastReceiver {

    private CheckUpdates checkUpdates;

    public NotificationReceiver(){
        super();
    }

    public NotificationReceiver(CheckUpdates checkUpdates){
        this.checkUpdates = checkUpdates;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        try{
            if(intent.getAction().equals(Intent.ACTION_ATTACH_DATA)){
                checkUpdates.onResult();
                System.out.println("Do we get here server boi?");
            }
        }catch(NullPointerException npe){
            Intent background = new Intent(context, MyService.class);
            context.startService(background);
        }
    }
}
