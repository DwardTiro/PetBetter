package com.example.owner.petbetter.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.Message;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.Topic;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.fragments.FragmentHome;
import com.example.owner.petbetter.fragments.FragmentPosts;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.android.gms.vision.text.Text;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class TopicContentActivity extends AppCompatActivity {

    Topic topicItem;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    private String email;

    private TextView topicContentName;
    private ImageButton followButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_content);


        Toolbar toolbar = (Toolbar) findViewById(R.id.viewPostToolbar);
        setSupportActionBar(toolbar);

        final TextView activityTitle = (TextView) findViewById(R.id.activity_title);

        topicContentName = (TextView) findViewById(R.id.topicContentName);
        followButton = (ImageButton) findViewById(R.id.followButton);
        activityTitle.setText("View Topic");
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        systemSessionManager = new SystemSessionManager(this);
        if(systemSessionManager.checkLogin())
            finish();
        HashMap<String,String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();
        email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);



        String jsonMyObject;
        Bundle extras = getIntent().getExtras();
        jsonMyObject = extras.getString("thisTopic");
        topicItem = new Gson().fromJson(jsonMyObject, Topic.class);

        topicContentName.setText(topicItem.getTopicName());
        Bundle bundle = new Bundle();
        bundle.putLong("topicId", topicItem.getId());
        FragmentPosts fragment3 = new FragmentPosts();
        fragment3.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.topic_container,fragment3).commit();

        if(checkIfFollower((int) topicItem.getId(), (int) user.getUserId())==true){
            followButton.setBackgroundColor(Color.YELLOW);
            followButton.setImageResource(R.mipmap.ic_check_black_24dp);
        }
        else{
            followButton.setBackgroundColor(Color.WHITE);
            followButton.setImageResource(R.mipmap.ic_add_black_24dp);
        }


        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkIfFollower((int) topicItem.getId(),(int) user.getUserId())==true){
                    followButton.setBackgroundColor(Color.WHITE);
                    followButton.setImageResource(R.mipmap.ic_add_black_24dp);
                    deleteFollower((int)topicItem.getId(), (int) user.getUserId());
                }
                else{
                    followButton.setBackgroundColor(Color.YELLOW);
                    followButton.setImageResource(R.mipmap.ic_check_black_24dp);
                    int fId = generateFollowerId();
                    addFollower(fId, (int) topicItem.getId(), (int) user.getUserId());
                }

            }
        });
    }

    public void viewPostBackButtonClicked(View view){
        finish();
    }

    private long addFollower(int followerId, int topicId, int userId){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        long result = petBetterDb.addFollower(followerId, topicId, userId);
        petBetterDb.closeDatabase();

        return result;
    }

    private boolean checkIfFollower(int topicId, int userId){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        boolean result = petBetterDb.checkIfFollower(topicId,userId);
        petBetterDb.closeDatabase();

        return result;
    }

    private void deleteFollower(int topicId, int userId){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        petBetterDb.deleteFollower(topicId, userId);
        petBetterDb.closeDatabase();
    }

    public int generateFollowerId(){
        ArrayList<Integer> storedIds;
        int postRepId = 1;

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        storedIds = petBetterDb.getFollowerIds();
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
}
