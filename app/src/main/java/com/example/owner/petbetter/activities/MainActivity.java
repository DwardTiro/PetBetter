package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.TypefaceUtil;
import com.example.owner.petbetter.activities.SignUpActivity;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.Follower;
import com.example.owner.petbetter.classes.Marker;
import com.example.owner.petbetter.classes.Message;
import com.example.owner.petbetter.classes.MessageRep;
import com.example.owner.petbetter.classes.Notifications;
import com.example.owner.petbetter.classes.Pet;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.PostRep;
import com.example.owner.petbetter.classes.Services;
import com.example.owner.petbetter.classes.Topic;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.example.owner.petbetter.activities.HomeActivity;
import com.google.android.gms.gcm.Task;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText editEmail;
    private EditText editPassword;
    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private TextView textInfo;
    HerokuService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceUtil.overrideFont(getApplicationContext(),"SERIF","fonts/Roboto-Regular.ttf");

        setContentView(R.layout.activity_main);

        systemSessionManager = new SystemSessionManager(getApplicationContext());

        loginButton = (Button) findViewById(R.id.loginButton);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);
        textInfo = (TextView) findViewById(R.id.textInfo);
        textInfo.setVisibility(View.INVISIBLE);
        initializeDatabase();

        editPassword.setTypeface(Typeface.DEFAULT);
        editPassword.setTransformationMethod(new PasswordTransformationMethod());

        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);


    }

    public void userLogin(View v){
        System.out.println("Im here");
        login(v);
        //Intent intent = new Intent(this, com.example.owner.petbetter.HomeActivity.class);
        //startActivity(intent);
    }
    private void initializeDatabase() {

        petBetterDb = new DataAdapter(this);

        try {
            petBetterDb.createDatabase();
        } catch(SQLException e ){
            e.printStackTrace();
        }

    }

    private void login(View v) {

        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        View focusView = null;


        if (checkEmailValidity(email)&& password.trim().length() > 0) {

            Call<User> call = service.checkLogin(email, password);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful()){
                        ExecutorService executorService = Executors.newSingleThreadExecutor();
                        final User user = response.body();
                        FutureTask<Boolean> futureTask = (FutureTask<Boolean>) executorService.submit(new Callable<Boolean>(){
                            @Override
                            public Boolean call() throws Exception{
                                boolean result = true;
                                User thisUser = user;


                                syncVetChanges();

                                syncClinicChanges();

                                syncFollowerChanges();

                                syncMarkerChanges(thisUser.getUserId());

                                syncMessageChanges(thisUser.getUserId());

                                syncMessageRepChanges();

                                syncNotifChanges(thisUser.getUserId());

                                syncPetChanges(thisUser.getUserId());

                                syncPostChanges();

                                syncPostRepChanges();

                                syncServiceChanges();

                                syncTopicChanges();



                                return result;
                            }
                        });
                        executorService.shutdown();


                        try {
                            /*
                            long futureTime = System.currentTimeMillis() + 2000;
                            while(System.currentTimeMillis() < futureTime){
                                System.out.println("From server wew: " + response.body().toString());
                                systemSessionManager.createUserSession(user.getEmail());
                                Intent intent = new Intent(MainActivity.this, com.example.owner.petbetter.activities.HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }*/
                            futureTask.get();
                            if(futureTask.isDone()){
                                System.out.println("From server wew: " + response.body().toString());
                                systemSessionManager.createUserSession(user.getEmail());
                                Intent intent = new Intent(MainActivity.this, com.example.owner.petbetter.activities.HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                    }
                    else{
                        textInfo.setText("Invalid Email or Password");
                        textInfo.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    textInfo.setText("Wrong username/password");
                    textInfo.setVisibility(View.VISIBLE);
                    Log.d("onFailure", t.getLocalizedMessage());
                }
            });
/*
            if (!checkEmailValidity(email)||email.equals("")) {
  //System.out.println("Invalid email");
                textInfo.setText("Invalid email or password");
                textInfo.setVisibility(View.VISIBLE);
            } else {

                try{
                    if (checkLogin(email, password)) {
                        systemSessionManager.createUserSession(email);
                        Intent intent = new Intent(MainActivity.this, com.example.owner.petbetter.activities.HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        finish();
                    } else {
                        //System.out.println("Invalid email or password");
                        textInfo.setText("Invalid Email or Password");
                        textInfo.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e){
                    textInfo.setText("Invalid Email or Password");
                    textInfo.setVisibility(View.VISIBLE);
                }


            }
*/
        } else {

            if (email.trim().length() == 0) {
                //System.out.println("Email Required");
                textInfo.setText("Email Required");
                textInfo.setVisibility(View.VISIBLE);
            } else if (password.trim().length() == 0) {

                //System.out.println("Password Required");
                textInfo.setText("Password Required");
                textInfo.setVisibility(View.VISIBLE);
            }
        }

    }



    public void syncVetChanges(){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        System.out.println("WE HERE BOOIII");
        ArrayList<Veterinarian> unsyncedVets = getUnsyncedVets();

        Gson gson = new Gson();
        String jsonArray = gson.toJson(unsyncedVets);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addVets(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    System.out.println("VETS ADDED YEY");
                    dataSynced(1);

                    final Call<ArrayList<Veterinarian>> call2 = service2.getVeterinarians();
                    call2.enqueue(new Callback<ArrayList<Veterinarian>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Veterinarian>> call, Response<ArrayList<Veterinarian>> response) {
                            if(response.isSuccessful()){
                                setVeterinarians(response.body());

                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Veterinarian>> call, Throwable t) {
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

        Gson gson = new Gson();
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

    public void syncTopicChanges(){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        System.out.println("WE HERE BOOIII");
        ArrayList<Topic> unsyncedTopics = getUnsyncedTopics();

        Gson gson = new Gson();
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

    public void syncPostRepChanges(){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        System.out.println("WE HERE BOOIII");
        ArrayList<PostRep> unsyncedPostReps = getUnsyncedPostReps();

        Gson gson = new Gson();
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

    public void syncServiceChanges(){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        System.out.println("WE HERE BOOIII");
        ArrayList<Services> unsyncedServices = getUnsyncedServices();

        Gson gson = new Gson();
        String jsonArray = gson.toJson(unsyncedServices);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addServices(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    System.out.println("SERVICES ADDED YEY");
                    dataSynced(11);

                    final Call<ArrayList<Services>> call2 = service2.getServices();
                    call2.enqueue(new Callback<ArrayList<Services>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Services>> call, Response<ArrayList<Services>> response) {
                            if(response.isSuccessful()){
                                setServices(response.body());

                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Services>> call, Throwable t) {
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

    public void syncMarkerChanges(final long userId){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        System.out.println("WE HERE BOOIII");
        ArrayList<Marker> unsyncedMarkers = getUnsyncedMarkers();
        Gson gson = new Gson();
        String jsonArray = gson.toJson(unsyncedMarkers);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addMarkers(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    System.out.println("MARKERS ADDED YEY");
                    dataSynced(4);

                    final Call<ArrayList<Marker>> call2 = service2.loadMarkers(userId);
                    call2.enqueue(new Callback<ArrayList<Marker>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Marker>> call, Response<ArrayList<Marker>> response) {
                            if(response.isSuccessful()){
                                setMarkers(response.body());

                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Marker>> call, Throwable t) {
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
        Gson gson = new Gson();
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
                                System.out.println("response size "+response.body().size());
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

    public void syncNotifChanges(final long userId){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        ArrayList<Notifications> unsyncedNotifs = getUnsyncedNotifications();
        System.out.println("UNSYNCED NOTIFS: "+unsyncedNotifs.size());
        Gson gson = new Gson();
        String jsonArray = gson.toJson(unsyncedNotifs);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addNotifications(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    System.out.println("NOTIFICATIONS ADDED YEY");
                    dataSynced(7);

                    final Call<ArrayList<Notifications>> call2 = service2.getNotifications(userId);
                    call2.enqueue(new Callback<ArrayList<Notifications>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Notifications>> call, Response<ArrayList<Notifications>> response) {
                            if(response.isSuccessful()){
                                System.out.println("notif response size "+response.body().size());
                                setNotifications(response.body());
                                //write this method. update dataSynced().
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Notifications>> call, Throwable t) {
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

    public void syncPetChanges(final long userId){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        ArrayList<Pet> unsyncedPets = getUnsyncedPets();
        System.out.println("UNSYNCED PETS: "+unsyncedPets.size());
        Gson gson = new Gson();
        String jsonArray = gson.toJson(unsyncedPets);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addPets(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    System.out.println("PETS ADDED YEY");
                    dataSynced(8);

                    final Call<ArrayList<Pet>> call2 = service2.getPets(userId);
                    call2.enqueue(new Callback<ArrayList<Pet>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Pet>> call, Response<ArrayList<Pet>> response) {
                            if(response.isSuccessful()){
                                System.out.println("pets response size "+response.body().size());
                                setPets(response.body());
                                //write this method. update dataSynced().
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Pet>> call, Throwable t) {
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
        Gson gson = new Gson();
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

    public void syncFollowerChanges(){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        System.out.println("WE HERE BOO");
        ArrayList<Follower> unsyncedFollowers = getUnsyncedFollowers();
        Gson gson = new Gson();
        String jsonArray = gson.toJson(unsyncedFollowers);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());

        final Call<Void> call = service.addFollowers(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    System.out.println("Followers ADDED YEY");
                    dataSynced(3);

                    final Call<ArrayList<Follower>> call2 = service2.getFollowers();
                    call2.enqueue(new Callback<ArrayList<Follower>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Follower>> call, Response<ArrayList<Follower>> response) {
                            if(response.isSuccessful()){
                                setFollowers(response.body());
                                //get back here boys

                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Follower>> call, Throwable t) {
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

    public void syncClinicChanges(){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        System.out.println("WE HERE BOOIII");
        ArrayList<Facility> unsyncedFacilities = getUnsyncedFacilities();
        Gson gson = new Gson();
        String jsonArray = gson.toJson(unsyncedFacilities);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addFacilities(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    System.out.println("FACILITIES ADDED YEY");
                    dataSynced(2);

                    final Call<ArrayList<Facility>> call2 = service2.getClinics();
                    call2.enqueue(new Callback<ArrayList<Facility>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Facility>> call, Response<ArrayList<Facility>> response) {
                            if(response.isSuccessful()){
                                System.out.println("Number of clinics from server: "+response.body().size());
                                setFacilities(response.body());
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Facility>> call, Throwable t) {
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

    private boolean checkLogin(String email, String password) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        boolean result = petBetterDb.checkLogin(email, password);
        petBetterDb.closeDatabase();


        return result;
    }

    private ArrayList<Facility> getClinics(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Facility> result = petBetterDb.getClinics();
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Veterinarian> getVeterinarians(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Veterinarian> result = petBetterDb.getVeterinarians();
        System.out.println("boi 1");
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<MessageRep> getAllMessageReps(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<MessageRep> result = petBetterDb.getAllMessageReps();
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
        System.out.println("boi 2");
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
        System.out.println("boi 3");
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Services> getServices(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Services> result = petBetterDb.getServices();
        System.out.println("boi 4");
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
        System.out.println("boi 5");
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Follower> getFollowers(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Follower> result = petBetterDb.getFollowers();
        System.out.println("boi 6");
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

    private ArrayList<MessageRep> getMessageRepsFromUser(long userId){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<MessageRep> result = petBetterDb.getMessageRepsFromUser(userId);
        System.out.println("boi 8");
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Notifications> getNotifications(long userId){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Notifications> result = petBetterDb.getNotifications(userId);
        System.out.println("boi 9");
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Marker> loadMarkers(long userId){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Marker> result = petBetterDb.loadMarkers(userId);
        System.out.println("boi 10");
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Pet> getPets(long userId){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Pet> result = petBetterDb.getPets(userId);
        System.out.println("boi 11");
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Veterinarian> getUnsyncedVets(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Veterinarian> result = petBetterDb.getUnsyncedVets();
        System.out.println("The number of veterinarians is: "+result.size());
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

    private ArrayList<Services> getUnsyncedServices(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Services> result = petBetterDb.getUnsyncedServices();
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

    private ArrayList<Pet> getUnsyncedPets(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Pet> result = petBetterDb.getUnsyncedPets();
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

    private ArrayList<Notifications> getUnsyncedNotifications(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Notifications> result = petBetterDb.getUnsyncedNotifications();
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Marker> getUnsyncedMarkers(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Marker> result = petBetterDb.getUnsyncedMarkers();
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Follower> getUnsyncedFollowers(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Follower> result = petBetterDb.getUnsyncedFollowers();
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Facility> getUnsyncedFacilities(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Facility> result = petBetterDb.getUnsyncedFacilities();
        petBetterDb.closeDatabase();

        return result;
    }

    private long addVet(int vetId, int userId, int rating){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        long result = petBetterDb.addVet(vetId, userId, rating);
        petBetterDb.closeDatabase();

        return result;
    }

    private void dataSynced(int n){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        petBetterDb.dataSynced(n);
        petBetterDb.closeDatabase();

    }

    public long setVeterinarians(ArrayList<Veterinarian> vetList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setVeterinarians(vetList);
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

    public long setServices(ArrayList<Services> serviceList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setServices(serviceList);
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

    public long setPets(ArrayList<Pet> petList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setPets(petList);
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

    public long setMessageReps(ArrayList<MessageRep> messageRepList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setMessageReps(messageRepList);
        petBetterDb.closeDatabase();

        return result;
    }

    public long setMarkers(ArrayList<Marker> markerList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setMarkers(markerList);
        petBetterDb.closeDatabase();

        return result;
    }

    public long setFollowers(ArrayList<Follower> followerList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setFollowers(followerList);
        petBetterDb.closeDatabase();

        return result;
    }

    public long setFacilities(ArrayList<Facility> faciList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setFacilities(faciList);
        petBetterDb.closeDatabase();

        return result;
    }

    public void signUp(View view){

        Intent intent = new Intent(this, SignUpUserTypeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
    private boolean checkEmailValidity(String email){

        String regEx = "^[\\w\\.-]+@([\\w\\-]+\\.)+[a-zA-Z]{2,4}$";

        Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();


    }


}
