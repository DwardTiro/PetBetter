package com.example.owner.petbetter.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by Kristian on 10/12/2017.
 */

public class HomeListContentActivity extends AppCompatActivity {

    private ImageView profilePic;
    private TextView profileName;
    private TextView postTimeStamp;
    private TextView postTitle;
    private TextView homeListContent;



    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    private Post postItem;

    @Override
    protected void onCreate(Bundle SavedInstance){
        super.onCreate(SavedInstance);
        setContentView(R.layout.activity_home_listing_content);

        profilePic = (ImageView) findViewById(R.id.homeListUserPicture);
        profileName = (TextView) findViewById(R.id.homeListUserName);
        postTimeStamp = (TextView) findViewById(R.id.homeListTimeStamp);
        postTitle = (TextView) findViewById(R.id.homeListPostTitle);
        homeListContent = (TextView) findViewById(R.id.homeListContent);

        systemSessionManager = new SystemSessionManager(this);
        if(systemSessionManager.checkLogin())
            finish();
        HashMap<String,String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();

        String jsonMyObject;
        Bundle extras = getIntent().getExtras();

        jsonMyObject = extras.getString("postContent");

        postItem = new Gson().fromJson(jsonMyObject, Post.class);

        //profileName.setText(postItem.get);
        postTitle.setText(postItem.getTopicName());
        homeListContent.setText(postItem.getTopicContent());

    }

    private void initializeDatabase() {

        petBetterDb = new DataAdapter(this);

        try {
            petBetterDb.createDatabase();
        } catch(SQLException e ){
            e.printStackTrace();
        }

    }

}
