package com.example.owner.petbetter.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.example.owner.petbetter.activities.VeterinarianHomeActivity;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.interfaces.CheckUpdates;

/**
 * Created by owner on 27/2/2018.
 */

public class NotificationReceiver extends BroadcastReceiver {

    private CheckUpdates checkUpdates;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    public NotificationReceiver(){
        super();
    }

    public NotificationReceiver(CheckUpdates checkUpdates){
        this.checkUpdates = checkUpdates;
    }



    @Override
    public void onReceive(Context context, Intent intent) {

        try{
            if(intent.getAction().equals("START_BROADCAST")){
                Intent notifAlarm = new Intent(context, NotificationReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(context,0,notifAlarm, PendingIntent.FLAG_NO_CREATE);
                boolean alarmRunning = pendingIntent!=null;
                if(alarmRunning==false){
                    pendingIntent = PendingIntent.getBroadcast(context, 0, notifAlarm, 0);
                    alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 10000, pendingIntent);
                    //alarmManager.cancel(pendingIntent);
                    //1800000
                }
                System.out.println("BROADCAST STARTED");
            }

            if(intent.getAction().equals(Intent.ACTION_ATTACH_DATA)){
                checkUpdates.onResult();
            }
            if(intent.getAction().equals("com.package.ACTION_LOGOUT")){

                System.out.println("LOG OUT PREP BOI ");
                alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
                System.out.println("LOG OUT BOI ");
            }
        }catch(NullPointerException npe){
            Intent background = new Intent(context, MyService.class);
            context.startService(background);

        }
    }
}
