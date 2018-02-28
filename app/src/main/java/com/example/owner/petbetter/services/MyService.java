package com.example.owner.petbetter.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.activities.MainActivity;
import com.example.owner.petbetter.classes.Follower;
import com.example.owner.petbetter.classes.Notifications;
import com.example.owner.petbetter.classes.Topic;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by owner on 27/2/2018.
 */

public class MyService extends Service {

    private boolean isRunning;
    private Context context;
    private Thread backgroundThread;
    private User user;
    //private HerokuService service;
    SharedPreferences sharedPreferences;
    private static final String KEY_ID = "user_id";
    private DataAdapter petBetterDb;
    private long userId;

    NotificationCompat.Builder appNotif;
    private static final AtomicInteger uniqueId = new AtomicInteger(0);

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        sharedPreferences = getSharedPreferences("prefs",Context.MODE_PRIVATE);
        this.context = this;
        this.isRunning = false;

        initializeDatabase();

        userId = sharedPreferences.getLong(KEY_ID,0);

        System.out.println("THIS SHOULD ONLY GO OUT ONCE"+sharedPreferences.getLong(KEY_ID,0));

        appNotif = new NotificationCompat.Builder(this);
        appNotif.setAutoCancel(true);
        this.backgroundThread = new Thread(myTask);
        /*
        long userId = sharedPreferences.getLong(KEY_ID);
        Call<User> call = service.getUserWithId(userId);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    user = response.body();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
        */
    }

    private Runnable myTask = new Runnable() {
        public void run() {
            // This is where you try to pull data from remotedb periodically and do notifs and messages
            final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
            final Call<ArrayList<Notifications>> call = service.getNotifications(userId);

            call.enqueue(new Callback<ArrayList<Notifications>>() {
                @Override
                public void onResponse(Call<ArrayList<Notifications>> call, Response<ArrayList<Notifications>> response) {
                    if(response.isSuccessful()) {
                        if(response.body().size()!=getNotifications(userId).size()){

                            if(response.body().size()>getNotifications(userId).size()){
                                ArrayList<Notifications> notifArray = response.body();
                                int val = getNotifications(userId).size();
                                while(val<notifArray.size()){

                                    if(notifArray.get(val).getType()==1){
                                        appNotif.setSmallIcon(R.drawable.app_icon)
                                                .setTicker(notifArray.get(val).getDoerName()+" has replied to your post")
                                                .setWhen(System.currentTimeMillis()).setContentTitle(notifArray.get(val).getDoerName())
                                                .setContentText("has replied to your post");
                                    }
                                    if(notifArray.get(val).getType()==2){
                                        appNotif.setSmallIcon(R.drawable.app_icon)
                                                .setTicker(notifArray.get(val).getDoerName()+" has messaged you")
                                                .setWhen(System.currentTimeMillis()).setContentTitle(notifArray.get(val).getDoerName())
                                                .setContentText("has messaged you");
                                    }
                                    if(notifArray.get(val).getType()==3){
                                        Topic topic = getTopic(notifArray.get(val).getSourceId());
                                        appNotif.setSmallIcon(R.drawable.app_icon)
                                                .setTicker(notifArray.get(val).getDoerName()+" has posted in "+topic.getTopicName())
                                                .setWhen(System.currentTimeMillis()).setContentTitle(notifArray.get(val).getDoerName())
                                                .setContentText(" has posted in "+topic.getTopicName());
                                    }
                                    if(notifArray.get(val).getType()==4){
                                        Follower follower = getFollowerWithId(notifArray.get(val).getSourceId());
                                        Topic topic = getTopic(follower.getTopicId());
                                        appNotif.setSmallIcon(R.drawable.app_icon)
                                                .setTicker(notifArray.get(val).getDoerName()+" followed your topic ")
                                                .setWhen(System.currentTimeMillis()).setContentTitle(notifArray.get(val).getDoerName())
                                                .setContentText(" has followed "+topic.getTopicName());
                                    }



                                    //change MainActivity to which activity you really wanna go
                                    Intent intent = new Intent(MyService.this, MainActivity.class);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    appNotif.setContentIntent(pendingIntent);

                                    //this builds the notification and issues it.
                                    NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    nManager.notify(getUniqueId(), appNotif.build());
                                    setNotifications(response.body());
                                    dataSynced(7);
                                    val++;
                                }
                            }
                            else{
                                setNotifications(response.body());
                                dataSynced(7);
                            }

                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Notifications>> call, Throwable t) {

                    Log.d("onFailure", t.getLocalizedMessage());
                }
            });

            System.out.println("THE BACKGROUND SERVICE IS RUNNING");
            stopSelf();
        }
    };

    @Override
    public void onDestroy() {
        this.isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!this.isRunning) {
            this.isRunning = true;
            this.backgroundThread.start();
        }
        return START_STICKY;
    }

    private void initializeDatabase() {

        petBetterDb = new DataAdapter(this);

        try {
            petBetterDb.createDatabase();
        } catch(SQLException e ){
            e.printStackTrace();
        }

    }

    private ArrayList<Notifications> getNotifications(long userId){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Notifications> result = petBetterDb.getNotifications(userId);
        petBetterDb.closeDatabase();

        return result;
    }

    public long setNotifications(ArrayList<Notifications> notifList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setNotifications(notifList);
        petBetterDb.closeDatabase();

        return result;
    }

    public void dataSynced(int n){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        petBetterDb.dataSynced(n);
        petBetterDb.closeDatabase();

    }

    public Topic getTopic(long topicId){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        Topic result = petBetterDb.getTopic(topicId);
        petBetterDb.closeDatabase();

        return result;
    }

    public Follower getFollowerWithId(long followerId){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        Follower result = petBetterDb.getFollowerWithId(followerId);
        petBetterDb.closeDatabase();

        return result;
    }

    public static int getUniqueId() {
        return uniqueId.incrementAndGet();
    }
}
