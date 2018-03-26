package com.example.owner.petbetter.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.adapters.FollowerAdapter;
import com.example.owner.petbetter.classes.Follower;
import com.example.owner.petbetter.classes.Topic;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kristian on 3/18/2018.
 */

public class FollowerRequestsActivity extends AppCompatActivity {


    private User user;
    private String email;
    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private RecyclerView recyclerView;
    private ArrayList<Follower> followerList;
    private FollowerAdapter followerAdapter;
    private ImageButton newTopicButton;
    private long topicId;

    private Topic topicItem;
    private HerokuService service;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_follower_requests);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final TextView activityTitle = (TextView) findViewById(R.id.activity_title);
        activityTitle.setText("Follower Requests");


        recyclerView = (RecyclerView) findViewById(R.id.followerListing);
        newTopicButton = (ImageButton) findViewById(R.id.topicNewPost);
        newTopicButton.setVisibility(View.INVISIBLE);

        systemSessionManager = new SystemSessionManager(FollowerRequestsActivity.this);
        if(systemSessionManager.checkLogin())
            finish();
        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();

        email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);

        String jsonMyObject;
        final Bundle extras = getIntent().getExtras();
        jsonMyObject = extras.getString("thisTopic");
        topicItem = new Gson().fromJson(jsonMyObject, Topic.class);

        getPendingFollowers(topicItem.getId());

    }

    public void getPendingFollowers(long topicId){
        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final Call<ArrayList<Follower>> call2 = service.getPendingFollowers(topicId);
        call2.enqueue(new Callback<ArrayList<Follower>>() {
            @Override
            public void onResponse(Call<ArrayList<Follower>> call, Response<ArrayList<Follower>> response) {
                if(response.isSuccessful()){
                    //get back here boys
                    followerList = response.body();

                    followerAdapter = new FollowerAdapter(FollowerRequestsActivity.this, followerList,new FollowerAdapter.OnItemClickListener() {
                        @Override public void onItemClick(Follower item) {
                            //Execute command here
                        }
                    }, user);
                    followerAdapter.notifyItemRangeChanged(0, followerAdapter.getItemCount());
                    recyclerView.setAdapter(followerAdapter);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(FollowerRequestsActivity.this));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Follower>> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());

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

    public void viewPostBackButtonClicked(View view){
        finish();
    }

}
