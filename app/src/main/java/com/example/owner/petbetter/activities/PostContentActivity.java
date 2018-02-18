package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.Notifications;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.PostRep;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.fragments.FragmentPostReps;
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

/**
 * Created by Kristian on 10/12/2017.
 */

public class PostContentActivity extends AppCompatActivity {

    private ImageView profilePic;
    private TextView profileName;
    private TextView postTimeStamp;
    private TextView postTitle;
    private TextView homeListContent;



    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user, postUser;
    private Post postItem;
    private String email;
    private Button commentButton;
    private EditText commentText;
    private String timeStamp;
    private int nId;
    HerokuService service;

    @Override
    protected void onCreate(Bundle SavedInstance){
        super.onCreate(SavedInstance);
        setContentView(R.layout.activity_post_content);

        Toolbar toolbar = (Toolbar) findViewById(R.id.viewPostToolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        profilePic = (ImageView) findViewById(R.id.homeListUserPicture);
        profileName = (TextView) findViewById(R.id.homeListUserName);
        postTimeStamp = (TextView) findViewById(R.id.homeListTimeStamp);
        postTitle = (TextView) findViewById(R.id.homeListPostTitle);
        homeListContent = (TextView) findViewById(R.id.homeListContent);
        commentButton = (Button) findViewById(R.id.homeListPostCommentButton);
        commentText = (EditText) findViewById(R.id.homeListCommentBox);


        systemSessionManager = new SystemSessionManager(this);
        if(systemSessionManager.checkLogin())
            finish();
        HashMap<String,String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();

        String jsonMyObject;
        Bundle extras = getIntent().getExtras();

        jsonMyObject = extras.getString("thisPost");

        postItem = new Gson().fromJson(jsonMyObject, Post.class);

        postUser = getPostUser(postItem.getUserId());
        Bundle bundle = new Bundle();
        System.out.println("POST ID BEFORE SEND "+postItem.getUserId());
        bundle.putLong("postId", postItem.getId());
        FragmentPostReps fragment = new FragmentPostReps();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.postrep_container,fragment).commit();

        profileName.setText(postUser.getFirstName()+" "+postUser.getLastName());
        postTitle.setText(postItem.getTopicName());
        homeListContent.setText(postItem.getTopicContent());

        email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);



        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(commentText.getText().toString())){

                    int postrepid = generatePostRepId();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                    timeStamp = sdf.format(new Date());

                    addPostRep(postrepid, (int) user.getUserId(), (int) postItem.getId(), 0, commentText.getText().toString(),timeStamp);
                    uploadPostReps(getUnsyncedPostReps());

                    nId = generateNotifsId();
                    notifyPostRep(nId, postItem.getUserId(), user.getUserId(), 0, 1, timeStamp, (int) postItem.getId(), 0);
                    uploadNotifications(getUnsyncedNotifications());

                    Intent intent = new Intent(PostContentActivity.this, com.example.owner.petbetter.activities.PostContentActivity.class);
                    intent.putExtra("thisPost", new Gson().toJson(postItem));
                    finish();
                    startActivity(intent);
                }
            }
        });

    }

    public void viewPostBackButtonClicked(View view){
        finish();
    }

    private void initializeDatabase() {

        petBetterDb = new DataAdapter(this);

        try {
            petBetterDb.createDatabase();
        } catch(SQLException e ){
            e.printStackTrace();
        }
    }

    private void uploadPostReps(ArrayList<PostRep> postreps){
        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        System.out.println("HOW MANY POSTREPS? "+postreps.size());

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(postreps);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addPostReps(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                dataSynced(10);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
                Toast.makeText(PostContentActivity.this, "Unable to upload postreps on server", Toast.LENGTH_LONG);
            }
        });
    }

    private void uploadNotifications(ArrayList<Notifications> notifs){
        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        System.out.println("HOW MANY NOTIFICATIONS? "+notifs.size());

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(notifs);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addNotifications(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                dataSynced(10);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
                Toast.makeText(PostContentActivity.this, "Unable to upload notifications on server", Toast.LENGTH_LONG);
            }
        });
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

    private void dataSynced(int n){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        petBetterDb.dataSynced(n);
        petBetterDb.closeDatabase();
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

    public int generatePostRepId(){
        ArrayList<Integer> storedIds;
        int postRepId = 1;

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        storedIds = petBetterDb.getPostRepIds();
        petBetterDb.closeDatabase();


        if(storedIds.isEmpty()) {
            return postRepId;
        } else {
            while (storedIds.contains(postRepId)){
                postRepId += 1;
            }
            return postRepId;
        }
    }

    private long addPostRep(int postRepId, int userId, int postId, int parentId, String repContent, String datePerformed){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        long result = petBetterDb.addPostRep(postRepId, userId, postId, parentId, repContent, datePerformed);
        petBetterDb.closeDatabase();

        return result;
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

    private User getPostUser(long userId){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        User result = petBetterDb.getPostUser(userId);
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

    public long notifyPostRep(int notifId, long toId, long userId, int isRead, int type, String timeStamp, int postId, int isSynced){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        long result = petBetterDb.notifyUser(notifId, toId, userId, isRead, type, timeStamp, postId, isSynced);
        petBetterDb.closeDatabase();

        return result;
    }

}
