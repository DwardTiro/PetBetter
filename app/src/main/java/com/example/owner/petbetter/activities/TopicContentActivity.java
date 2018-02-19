package com.example.owner.petbetter.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.Follower;
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

public class TopicContentActivity extends AppCompatActivity {

    Topic topicItem;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    private String email;

    private TextView topicContentName;
    private ImageButton followButton;
    private int nId;
    private String timeStamp;
    HerokuService service;

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

                    service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
                    final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

                    final Call<Void> call = service.deleteFollower(topicItem.getId(), user.getUserId());
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            //User thisUser = response.body();
                            if(response.isSuccessful()){
                                dataSynced(3);
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.d("onFailure", t.getLocalizedMessage());
                        }
                    });
                }
                else{
                    followButton.setBackgroundColor(Color.YELLOW);
                    followButton.setImageResource(R.mipmap.ic_check_black_24dp);
                    int fId = generateFollowerId();
                    addFollower(fId, (int) topicItem.getId(), (int) user.getUserId(), 0);
                    uploadFollower(getUnsyncedFollowers());

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                    timeStamp = sdf.format(new Date());
                    nId = generateNotifsId();
                    notifyFollower(nId, topicItem.getCreatorId(), user.getUserId(),0, 4, timeStamp, (int) topicItem.getId(), 0);
                }

            }
        });
    }

    public void viewPostBackButtonClicked(View view){
        finish();
    }

    private long addFollower(int followerId, int topicId, int userId, int isSynced){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        long result = petBetterDb.addFollower(followerId, topicId, userId, isSynced);
        petBetterDb.closeDatabase();

        return result;
    }

    private void uploadFollower(ArrayList<Follower> followers){
        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        System.out.println("HOW MANY FOLLOWERS? "+followers.size());

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(followers);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addFollowers(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                dataSynced(3);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
                Toast.makeText(TopicContentActivity.this, "Unable to upload followers on server", Toast.LENGTH_LONG);
            }
        });
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

    private void dataSynced(int n){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        petBetterDb.dataSynced(n);
        petBetterDb.closeDatabase();
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

    public long notifyFollower(int notifId, long toId, long userId, int isRead, int type, String timeStamp, int topicId, int isSynced){


        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        long result = petBetterDb.notifyUser(notifId, toId, userId, isRead, type, timeStamp, topicId, isSynced);
        petBetterDb.closeDatabase();

        return result;
    }
}
