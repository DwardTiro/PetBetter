package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.Follower;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.Topic;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPostActivity extends AppCompatActivity {

    EditText newPostTitle;
    EditText newPostDesc;
    Button newPostButton;
    HerokuService service;


    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user, usertwo;
    private String timeStamp;
    private int pId, nId;
    private long topicId;
    private ArrayList<Follower> topicFollowers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        Toolbar toolbar = (Toolbar) findViewById(R.id.createPostToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        systemSessionManager = new SystemSessionManager(this);

        if(systemSessionManager.checkLogin())
            finish();

        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();

        String email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);

        newPostTitle = (EditText) findViewById(R.id.newPostTitle);
        newPostDesc = (EditText) findViewById(R.id.newPostDesc);
        newPostButton = (Button) findViewById(R.id.createPostButton);

        Bundle extras = getIntent().getExtras();
        topicId = extras.getLong("thisTopicId");
        //topicId = new Gson().fromJson(jsonMyObject, long);

        newPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newPostTitle.getText().toString()!=""&&newPostDesc.getText().toString()!=""){

                    pId = generatePostId();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                    timeStamp = sdf.format(new Date());

                    createPost(pId,user.getUserId(), newPostTitle.getText().toString(), newPostDesc.getText().toString(),
                            topicId, timeStamp, 0, 0);

                    uploadPost(getUnsyncedPosts());

                    //notifyMessage(nId, messageItem.getFromId(), user.getUserId(), 0, 2, timeStamp, sourceId);

                    topicFollowers = getTopicFollowers(topicId);

                    for(int i = 0;i<topicFollowers.size();i++){
                        if(topicFollowers.get(i).getUserId()!=user.getUserId()){
                            nId = generateNotifsId();
                            notifyTopicPost(nId, topicFollowers.get(i).getUserId(), user.getUserId(), 0, 3, timeStamp, (int) topicId, 0);
                        }
                    }

                    finish();
                }
            }
        });
    }

    private void uploadPost(ArrayList<Post> posts){
        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        System.out.println("HOW MANY POSTS? "+posts.size());

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(posts);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addPosts(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                dataSynced(9);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
                Toast.makeText(NewPostActivity.this, "Unable to upload posts on server", Toast.LENGTH_LONG);
            }
        });
    }

    private void initializeDatabase() {

        petBetterDb = new DataAdapter(this);

        try {
            petBetterDb.createDatabase();

        } catch(SQLException e ){
            e.printStackTrace();
        }

    }

    private User getUser(String email){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        User result = petBetterDb.getUser(email);
        petBetterDb.closeDatabase();

        return result;
    }

    private void dataSynced(int n) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        petBetterDb.dataSynced(n);
        petBetterDb.closeDatabase();
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

    public int generatePostId(){
        ArrayList<Integer> storedIds;
        int markerId = 1;

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        storedIds = petBetterDb.getPostIds();
        petBetterDb.closeDatabase();


        if(storedIds.isEmpty()) {
            return markerId;
        } else {
            while (storedIds.contains(markerId)){
                markerId += 1;
            }

            return markerId;
        }
    }
    /*
    * pId,user.getUserId(), newPostTitle.getText().toString(), newPostDesc.getText().toString(),
                            topicId, timeStamp, 0
    * */

    private long createPost(int pId, long userId, String postTitle, String postDesc, long topicId, String timeStamp,
                            int isDeleted, int isSynced){
        long  result;

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        result = petBetterDb.createPost(pId, userId, postTitle, postDesc, topicId, timeStamp, isDeleted, isSynced);
        petBetterDb.closeDatabase();


        return result;
    }

    public int generateNotifsId(){
        ArrayList<Integer> storedIds;
        int markerId = 1;

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        storedIds = petBetterDb.getNotifIds();
        petBetterDb.closeDatabase();


        if(storedIds.isEmpty()) {
            return markerId;
        } else {
            while (storedIds.contains(markerId)){
                markerId += 1;
            }

            return markerId;
        }
    }

    private ArrayList<Follower> getTopicFollowers(long topicId){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Follower> result = petBetterDb.getTopicFollowers(topicId);
        petBetterDb.closeDatabase();

        return result;
    }

    //nId, topicFollowers.get(i).getUserId(), user.getUserId(), 0, 3, timeStamp

    public long notifyTopicPost(int notifId, long toId, long userId, int isRead, int type, String timeStamp, int topicId, int isSynced){


        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        long result = petBetterDb.notifyUser(notifId, toId, userId, isRead, type, timeStamp, topicId, isSynced);
        petBetterDb.closeDatabase();

        return result;
    }
    public void createPostBackClicked(View view){
        finish();

    }

}
