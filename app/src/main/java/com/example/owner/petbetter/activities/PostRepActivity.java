package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.PostRep;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.fragments.FragmentCommentThread;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class PostRepActivity extends AppCompatActivity {


    private ImageView profilePic;
    private TextView profileName;
    private TextView postTimeStamp;
    private TextView postTitle;
    private TextView homeListContent;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user, postUser;
    private PostRep postRepItem;
    private Post postItem;
    private String email;
    private Button commentButton;
    private EditText commentText;
    private String timeStamp;
    private int nId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_rep);


        profilePic = (ImageView) findViewById(R.id.commentThreadUserPicture);
        profileName = (TextView) findViewById(R.id.commentThreadUserName);
        postTimeStamp = (TextView) findViewById(R.id.commentThreadTimeStamp);
        postTitle = (TextView) findViewById(R.id.commentThreadPostTitle);
        homeListContent = (TextView) findViewById(R.id.commentThreadContent);
        commentButton = (Button) findViewById(R.id.commentThreadCommentButton);
        commentText = (EditText) findViewById(R.id.commentThreadCommentBox);


        Toolbar toolbar = (Toolbar) findViewById(R.id.commentThreadToolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        systemSessionManager = new SystemSessionManager(this);
        if(systemSessionManager.checkLogin())
            finish();
        HashMap<String,String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();


        String jsonMyObject;
        Bundle extras = getIntent().getExtras();

        jsonMyObject = extras.getString("thisParent");

        postRepItem = new Gson().fromJson(jsonMyObject, PostRep.class);

        jsonMyObject = extras.getString("commentPost");
        postItem = new Gson().fromJson(jsonMyObject, Post.class);

        profileName.setText(postRepItem.getUserName());
        postTimeStamp.setText(postRepItem.getDatePerformed());
        postTitle.setText("");
        homeListContent.setText(postRepItem.getRepContent());


        Bundle bundle = new Bundle();

        System.out.println("POST ID BEFORE SEND "+postRepItem.getUserId());
        bundle.putLong("postRepId", postRepItem.getId());
        //edit this stuff later^
        FragmentCommentThread fragment = new FragmentCommentThread();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.comment_container,fragment).commit();

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

                    if(postRepItem.getParentId()==0)
                        addPostRep(postrepid, (int) user.getUserId(), (int) postRepItem.getPostId(), (int) postRepItem.getId(),
                                commentText.getText().toString(),timeStamp);
                    else
                        addPostRep(postrepid, (int) user.getUserId(), (int) postRepItem.getPostId(), (int) postRepItem.getParentId(),
                                commentText.getText().toString(),timeStamp);
                    nId = generateNotifsId();
                    notifyPostRep(nId, postItem.getUserId(), user.getUserId(), 0, 1, timeStamp, (int) postItem.getId());

                    Intent intent = new Intent(PostRepActivity.this, com.example.owner.petbetter.activities.PostRepActivity.class);
                    intent.putExtra("thisParent", new Gson().toJson(postRepItem));
                    finish();
                    startActivity(intent);
                }
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

            return markerId

                    ;
        }
    }

    public long notifyPostRep(int notifId, long toId, long userId, int isRead, int type, String timeStamp, int postId){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        long result = petBetterDb.notifyUser(notifId, toId, userId, isRead, type, timeStamp, postId);
        petBetterDb.closeDatabase();

        return result;
    }
    public void viewPostBackButtonClicked(View view){
        finish();
    }
}
