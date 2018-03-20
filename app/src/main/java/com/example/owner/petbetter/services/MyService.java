package com.example.owner.petbetter.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.example.owner.petbetter.activities.MessageActivity;
import com.example.owner.petbetter.activities.MessagesActivity;
import com.example.owner.petbetter.activities.PostContentActivity;
import com.example.owner.petbetter.activities.TopicContentActivity;
import com.example.owner.petbetter.adapters.MessageAdapter;
import com.example.owner.petbetter.adapters.MessageRepAdapter;
import com.example.owner.petbetter.classes.Follower;
import com.example.owner.petbetter.classes.Message;
import com.example.owner.petbetter.classes.MessageRep;
import com.example.owner.petbetter.classes.Notifications;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.PostRep;
import com.example.owner.petbetter.classes.Topic;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.interfaces.CheckUpdates;
import com.google.android.gms.nearby.messages.Messages;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import okhttp3.RequestBody;
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
    private NotificationReceiver notifReceiver = new NotificationReceiver();

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
            final Call<ArrayList<Post>> call2 = service.getPosts();

            call2.enqueue(new Callback<ArrayList<Post>>() {
                @Override
                public void onResponse(Call<ArrayList<Post>> call2, Response<ArrayList<Post>> response) {
                    if(response.isSuccessful()){
                        if(response.body().size()!=getPosts().size()){
                            syncPostChanges();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Post>> call2, Throwable t) {

                }
            });

            final Call<ArrayList<PostRep>> call3 = service.getAllPostReps();

            call3.enqueue(new Callback<ArrayList<PostRep>>() {
                @Override
                public void onResponse(Call<ArrayList<PostRep>> call3, Response<ArrayList<PostRep>> response) {
                    if(response.isSuccessful()){
                        if(response.body().size()!= getAllPostReps().size()){
                            syncPostRepChanges();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<PostRep>> call3, Throwable t) {

                }
            });

            final Call<ArrayList<Topic>> call4 = service.getTopics();

            call4.enqueue(new Callback<ArrayList<Topic>>() {
                @Override
                public void onResponse(Call<ArrayList<Topic>> call4, Response<ArrayList<Topic>> response) {
                    if(response.isSuccessful()){
                        if(response.body().size()!= getTopics().size()){
                            syncTopicChanges();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Topic>> call4, Throwable t) {

                }
            });

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

                                    if(notifArray.get(val).getType()==1 && (notifArray.get(val).getDoerId()==user.getUserId())){
                                        appNotif.setSmallIcon(R.drawable.app_icon)
                                                .setTicker(notifArray.get(val).getDoerName()+" has replied to your post")
                                                .setWhen(System.currentTimeMillis()).setContentTitle(notifArray.get(val).getDoerName())
                                                .setContentText("has replied to your post");

                                        Intent intentNotif = new Intent(MyService.this, PostContentActivity.class);
                                        Post postItem = getPost(notifArray.get(val).getSourceId());
                                        intentNotif.putExtra("thisPost", new Gson().toJson(postItem));
                                        PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, intentNotif, PendingIntent.FLAG_UPDATE_CURRENT);
                                        appNotif.setContentIntent(pendingIntent);
                                    }
                                    if(notifArray.get(val).getType()==2 && (notifArray.get(val).getDoerId()==user.getUserId())){
                                        syncMessageChanges(userId);
                                        syncMessageRepChanges();
                                        appNotif.setSmallIcon(R.drawable.app_icon)
                                                .setTicker(notifArray.get(val).getDoerName()+" has messaged you")
                                                .setWhen(System.currentTimeMillis()).setContentTitle(notifArray.get(val).getDoerName())
                                                .setContentText("has messaged you");

                                        Intent intentNotif = new Intent(MyService.this, MessagesActivity.class);
                                        intentNotif.putExtra("notifMessage", new Gson().toJson(notifArray.get(val)));
                                        PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, intentNotif, PendingIntent.FLAG_UPDATE_CURRENT);
                                        appNotif.setContentIntent(pendingIntent);

                                        //getApplicationContext().registerReceiver(notifReceiver, new IntentFilter(Intent.ACTION_ATTACH_DATA));
                                    }
                                    if(notifArray.get(val).getType()==3 && (notifArray.get(val).getDoerId()==user.getUserId())){
                                        Topic topic = getTopic(notifArray.get(val).getSourceId());
                                        appNotif.setSmallIcon(R.drawable.app_icon)
                                                .setTicker(notifArray.get(val).getDoerName()+" has posted in "+topic.getTopicName())
                                                .setWhen(System.currentTimeMillis()).setContentTitle(notifArray.get(val).getDoerName())
                                                .setContentText(" has posted in "+topic.getTopicName());

                                        Intent intentNotif = new Intent(MyService.this, PostContentActivity.class);
                                        Post postItem = getPost(notifArray.get(val).getSourceId());
                                        intentNotif.putExtra("thisPost", new Gson().toJson(postItem));
                                        PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, intentNotif, PendingIntent.FLAG_UPDATE_CURRENT);
                                        appNotif.setContentIntent(pendingIntent);
                                    }
                                    if(notifArray.get(val).getType()==4 && (notifArray.get(val).getDoerId()==user.getUserId())){
                                        Topic topic = getTopic(notifArray.get(val).getSourceId());

                                        appNotif.setSmallIcon(R.drawable.app_icon)
                                                .setTicker(notifArray.get(val).getDoerName()+" wants to follow your topic ")
                                                .setWhen(System.currentTimeMillis()).setContentTitle(notifArray.get(val).getDoerName())
                                                .setContentText(" has requested to follow "+topic.getTopicName());

                                        Intent intentNotif = new Intent(MyService.this, TopicContentActivity.class);
                                        intentNotif.putExtra("thisTopic", new Gson().toJson(topic));
                                        PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, intentNotif, PendingIntent.FLAG_UPDATE_CURRENT);
                                        appNotif.setContentIntent(pendingIntent);
                                    }
                                    if(notifArray.get(val).getType()==5 && (notifArray.get(val).getDoerId()==user.getUserId())){
                                        Topic topic = getTopic(notifArray.get(val).getSourceId());

                                        appNotif.setSmallIcon(R.drawable.app_icon)
                                                .setTicker("Congratulations! ")
                                                .setWhen(System.currentTimeMillis()).setContentTitle(notifArray.get(val).getDoerName())
                                                .setContentText(" has approved your follow request in "+topic.getTopicName());

                                        Intent intentNotif = new Intent(MyService.this, TopicContentActivity.class);
                                        intentNotif.putExtra("thisTopic", new Gson().toJson(topic));
                                        PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, intentNotif, PendingIntent.FLAG_UPDATE_CURRENT);
                                        appNotif.setContentIntent(pendingIntent);
                                    }
                                    //



                                    //change MainActivity to which activity you really wanna go
                                    /*
                                    Intent intent = new Intent(MyService.this, MainActivity.class);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    appNotif.setContentIntent(pendingIntent);*/

                                    //this builds the notification and issues it.
                                    NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    nManager.notify(getUniqueId(), appNotif.build());
                                    setNotifications(response.body());
                                    dataSynced(7);
                                    //getApplicationContext().unregisterReceiver(notifReceiver);
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

            //might need a separate call and service variable for this one

            System.out.println("BACKGROUND THING");
            stopSelf();
        }
    };

    public void syncTopicChanges(){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        System.out.println("WE HERE BOOIII");
        ArrayList<Topic> unsyncedTopics = getUnsyncedTopics();

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(unsyncedTopics);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addTopics(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    System.out.println("TOPICS ADDED YEY");
                    dataSynced(9);

                    final Call<ArrayList<Topic>> call2 = service2.getTopics();
                    call2.enqueue(new Callback<ArrayList<Topic>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Topic>> call, Response<ArrayList<Topic>> response) {
                            if(response.isSuccessful()){
                                setTopics(response.body());

                                Intent intent = new Intent().setAction(Intent.ACTION_ATTACH_DATA);
                                notifReceiver.onReceive(context, intent);
                                context.sendBroadcast(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Topic>> call, Throwable t) {
                            Log.d("onFailure", t.getLocalizedMessage());

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
    }

    public void syncPostChanges(){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        System.out.println("WE HERE BOOIII");
        ArrayList<Post> unsyncedPosts = getUnsyncedPosts();

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(unsyncedPosts);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addPosts(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    System.out.println("POSTS ADDED YEY");
                    dataSynced(9);

                    final Call<ArrayList<Post>> call2 = service2.getPosts();
                    call2.enqueue(new Callback<ArrayList<Post>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
                            if(response.isSuccessful()){
                                setPosts(response.body());
                                Intent intent = new Intent().setAction(Intent.ACTION_ATTACH_DATA);
                                notifReceiver.onReceive(context, intent);
                                context.sendBroadcast(intent);

                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
                            Log.d("onFailure", t.getLocalizedMessage());

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
    }

    public void syncPostRepChanges(){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        System.out.println("WE HERE BOOIII");
        ArrayList<PostRep> unsyncedPostReps = getUnsyncedPostReps();

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(unsyncedPostReps);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addPostReps(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    System.out.println("POSTREPS ADDED YEY");
                    dataSynced(10);

                    final Call<ArrayList<PostRep>> call2 = service2.getAllPostReps();
                    call2.enqueue(new Callback<ArrayList<PostRep>>() {
                        @Override
                        public void onResponse(Call<ArrayList<PostRep>> call, Response<ArrayList<PostRep>> response) {
                            if(response.isSuccessful()){
                                setPostReps(response.body());

                                Intent intent = new Intent().setAction(Intent.ACTION_ATTACH_DATA);
                                notifReceiver.onReceive(context, intent);
                                context.sendBroadcast(intent);

                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<PostRep>> call, Throwable t) {
                            Log.d("onFailure", t.getLocalizedMessage());

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
    }

    public void syncMessageChanges(final long userId){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        ArrayList<Message> unsyncedMessages = getUnsyncedMessages();
        System.out.println("UNSYNCED MESSAGES: "+unsyncedMessages.size());
        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(unsyncedMessages);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addMessages(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    System.out.println("MESSAGES ADDED YEY");
                    dataSynced(5);

                    final Call<ArrayList<Message>> call2 = service2.getMessages(userId);
                    call2.enqueue(new Callback<ArrayList<Message>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
                            if(response.isSuccessful()){
                                System.out.println("response size messages "+response.body().size());
                                setMessages(response.body());
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Message>> call, Throwable t) {
                            Log.d("onFailure", t.getLocalizedMessage());

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
    }

    public void syncMessageRepChanges(){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        ArrayList<MessageRep> unsyncedMessages = getUnsyncedMessageReps();
        System.out.println("UNSYNCED MESSAGEREPS: "+unsyncedMessages.size());
        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(unsyncedMessages);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addMessageReps(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    System.out.println("MESSAGEREPS ADDED YEY");
                    dataSynced(6);

                    final Call<ArrayList<MessageRep>> call2 = service2.getMessageReps();
                    call2.enqueue(new Callback<ArrayList<MessageRep>>() {
                        @Override
                        public void onResponse(Call<ArrayList<MessageRep>> call, Response<ArrayList<MessageRep>> response) {
                            if(response.isSuccessful()){
                                System.out.println("response size messagereps "+response.body().size());
                                setMessageReps(response.body());

                                Intent intent = new Intent().setAction(Intent.ACTION_ATTACH_DATA);
                                notifReceiver.onReceive(context, intent);
                                context.sendBroadcast(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<MessageRep>> call, Throwable t) {
                            Log.d("onFailure", t.getLocalizedMessage());

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
    }

    private ArrayList<Topic> getUnsyncedTopics(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Topic> result = petBetterDb.getUnsyncedTopics();
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<MessageRep> getUnsyncedMessageReps(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<MessageRep> result = petBetterDb.getUnsyncedMessageReps();
        petBetterDb.closeDatabase();

        return result;
    }

    private Post getPost(long postId){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        Post result = petBetterDb.getPost(postId);
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<PostRep> getAllPostReps(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<PostRep> result = petBetterDb.getAllPostReps();
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Post> getPosts(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Post> result = petBetterDb.getPosts();
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Topic> getTopics(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Topic> result = petBetterDb.getTopics();
        petBetterDb.closeDatabase();

        return result;
    }


    private ArrayList<Post> getUnsyncedPosts(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Post> result = petBetterDb.getUnsyncedPosts();
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<PostRep> getUnsyncedPostReps(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<PostRep> result = petBetterDb.getUnsyncedPostReps();
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Message> getMessages(long userId){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Message> result = petBetterDb.getMessages(userId);
        petBetterDb.closeDatabase();

        return result;
    }

    public long setTopics(ArrayList<Topic> topicList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setTopics(topicList);
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<MessageRep> getMessageRepsFromUser(long userId){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<MessageRep> result = petBetterDb.getMessageRepsFromUser(userId);
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Message> getUnsyncedMessages(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Message> result = petBetterDb.getUnsyncedMessages();
        petBetterDb.closeDatabase();

        return result;
    }

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

    public long setPosts(ArrayList<Post> postList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setPosts(postList);
        petBetterDb.closeDatabase();

        return result;
    }

    public long setPostReps(ArrayList<PostRep> postRepList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setPostReps(postRepList);
        petBetterDb.closeDatabase();

        return result;
    }

    public long setMessages(ArrayList<Message> messageList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setMessages(messageList);
        petBetterDb.closeDatabase();

        return result;
    }

    public long setMessageReps(ArrayList<MessageRep> messagerepList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setMessageReps(messagerepList);
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
