package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.adapters.FollowerAdapter;
import com.example.owner.petbetter.classes.Follower;
import com.example.owner.petbetter.classes.Message;
import com.example.owner.petbetter.classes.Notifications;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.Topic;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.fragments.FragmentHome;
import com.example.owner.petbetter.fragments.FragmentNoResults;
import com.example.owner.petbetter.fragments.FragmentPosts;
import com.example.owner.petbetter.fragments.FragmentTopicFollowers;
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

    private Topic topicItem;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    private String email;

    private TextView topicContentName;
    private Button followButton;
    private int nId;
    private String timeStamp;
    private SwipeRefreshLayout refreshTopicContent;
    private FragmentPosts fragment3;
    private Bundle bundle;

    private Button followersButton;
    private Button postsButton;
    private TextView requestsTextView;
    private int currFragment;
    private Follower check;
    private ImageButton topicNewPost;
    private Toolbar toolbar;
    private View toolbarItem;
    private ArrayList<Follower> topicFollowers;
    private FloatingActionButton fab;

    HerokuService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_content);


        toolbar = (Toolbar) findViewById(R.id.viewPostToolbar);
        setSupportActionBar(toolbar);
        final TextView activityTitle = (TextView) findViewById(R.id.activity_title);
        activityTitle.setText("View Topic");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        topicContentName = (TextView) findViewById(R.id.topicContentName);
        followButton = (Button) findViewById(R.id.followButton);
        refreshTopicContent = (SwipeRefreshLayout) findViewById(R.id.refreshTopicContent);
        followersButton = (Button) findViewById(R.id.followersButton);
        postsButton = (Button) findViewById(R.id.postsButton);
        requestsTextView = (TextView) findViewById(R.id.requestsTextView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        topicNewPost = (ImageButton) findViewById(R.id.topicNewPost);
        topicNewPost.setVisibility(View.GONE);


        systemSessionManager = new SystemSessionManager(this);
        if(systemSessionManager.checkLogin())
            finish();
        HashMap<String,String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();
        email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);

        String jsonMyObject;
        final Bundle extras = getIntent().getExtras();
        jsonMyObject = extras.getString("thisTopic");
        topicItem = new Gson().fromJson(jsonMyObject, Topic.class);

        topicContentName.setText(topicItem.getTopicName());
        bundle = new Bundle();
        bundle.putLong("topicId", topicItem.getId());
        postsButton.callOnClick();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopicContentActivity.this, com.example.owner.petbetter.activities.NewPostActivity.class);
                intent.putExtra("thisTopicId", topicItem.getId());
                startActivity(intent);
            }
        });

        postsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currFragment = 1;
                postsButton.setTextColor(getResources().getColor(R.color.myrtle_green));
                postsButton.setBackgroundResource(R.color.main_White);
                followersButton.setTextColor(getResources().getColor(R.color.colorWhite));
                followersButton.setBackgroundResource(R.color.medTurquoise);
                postsButton.setPaintFlags(postsButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                followersButton.setPaintFlags(followersButton.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));

                String jsonMyObject;
                Bundle extras = getIntent().getExtras();
                jsonMyObject = extras.getString("thisTopic");
                topicItem = new Gson().fromJson(jsonMyObject, Topic.class);

                topicContentName.setText(topicItem.getTopicName());
                bundle = new Bundle();
                bundle.putLong("topicId", topicItem.getId());


                FragmentNoResults fragmentpar = new FragmentNoResults();
                if(topicItem.getCreatorId() != user.getUserId())
                    getSupportFragmentManager().beginTransaction().replace(R.id.topic_container,fragmentpar).commitAllowingStateLoss();

                if(check!=null){
                    if(check.getIsAllowed()==1 || topicItem.getCreatorId() == user.getUserId()){

                        Fragment postsFragment = new FragmentPosts();
                        postsFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.topic_container, postsFragment)
                                .commitAllowingStateLoss();
                    }
                    else{
                        fragmentpar = new FragmentNoResults();
                        getSupportFragmentManager().beginTransaction().replace(R.id.topic_container,fragmentpar)
                                .commitAllowingStateLoss();
                    }
                }


            }
        });


        /*
        fragment3 = new FragmentPosts();
        fragment3.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.topic_container,fragment3).commitAllowingStateLoss();
        */

        followersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currFragment = 2;
                followersButton.setTextColor(getResources().getColor(R.color.myrtle_green));
                followersButton.setBackgroundResource(R.color.main_White);
                postsButton.setTextColor(getResources().getColor(R.color.colorWhite));
                postsButton.setBackgroundResource(R.color.medTurquoise);
                followersButton.setPaintFlags(followersButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                postsButton.setPaintFlags(postsButton.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));

                getTopicFollowers(topicItem.getId());


            }
        });

        refreshTopicContent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTopicContent.setRefreshing(true);
                if(currFragment==1&&check!=null){
                    if (check.getIsAllowed()==1){
                        syncPostChanges();
                        String jsonMyObject;
                        Bundle extras = getIntent().getExtras();
                        jsonMyObject = extras.getString("thisTopic");
                        topicItem = new Gson().fromJson(jsonMyObject, Topic.class);

                        topicContentName.setText(topicItem.getTopicName());
                        bundle = new Bundle();
                        bundle.putLong("topicId", topicItem.getId());
                    }
                }
                else if(currFragment == 2){
                    getTopicFollowers(topicItem.getId());
                }
                if(check==null||check.getIsAllowed()!=1){
                    toolbarItem.setVisibility(View.GONE);
                }
                refreshTopicContent.setRefreshing(false);
            }
        });

        if(checkIfFollower((int) topicItem.getId(), (int) user.getUserId())){
            followButton.setBackgroundColor(getResources().getColor(R.color.amazonite));
            check = getFollowerWithTopicUser(topicItem.getId(), user.getUserId());
            if(check.getIsAllowed()==1){
                //followButton.setBackgroundResource(R.mipmap.ic_check_black_24dp);
                followButton.setText("Approved");
                fab.setVisibility(View.VISIBLE);
                Fragment postsFragment = new FragmentPosts();
                postsFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.topic_container, postsFragment).commitAllowingStateLoss();
            }
            else{
               //followButton.setBackgroundResource(R.mipmap.ic_access_time_black_24dp);
                followButton.setText("Requested");
            }
        }
        else{
            followButton.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            followButton.setText("Follow");
        }


        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                followButton.setEnabled(false);
                followButton.postDelayed(new Runnable() {
                    @Override
                        public void run() {
                            followButton.setEnabled(true);
                        }
                    }, 500);
                    if(checkIfFollower((int) topicItem.getId(),(int) user.getUserId())){
                        followButton.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                        followButton.setText("Follow");
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

                    final Call<Void> call2 = service2.deleteNotification(topicItem.getId(), user.getUserId(), 4);
                    call2.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {

                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.d("onFailure", t.getLocalizedMessage());
                        }
                    });

                }
                else{
                    //followButton.setBackgroundResource(R.mipmap.ic_access_time_black_24dp);
                    followButton.setBackgroundColor(getResources().getColor(R.color.amazonite));

                    int fId = generateFollowerId();
                    if(topicItem.getCreatorId()==user.getUserId()){
                        addFollower(fId, (int) topicItem.getId(), (int) user.getUserId(), 1, 0);
                        followButton.setText("Approved");
                    }
                    else{
                        addFollower(fId, (int) topicItem.getId(), (int) user.getUserId(), 0, 0);
                        followButton.setText("Requested");
                    }
                    uploadFollower(getUnsyncedFollowers());

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                    timeStamp = sdf.format(new Date());
                    nId = generateNotifsId();
                    notifyFollower(nId, topicItem.getCreatorId(), user.getUserId(),0, 4, timeStamp, (int) topicItem.getId(), 0);
                    uploadNotification();

                }

            }
        });

        if (topicItem.getCreatorId() == user.getUserId()) {
            requestsTextView.setVisibility(View.VISIBLE);
            followButton.setVisibility(View.INVISIBLE);

            requestsTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(TopicContentActivity.this, com.example.owner.petbetter.activities.FollowerRequestsActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });
        }
        else{
            requestsTextView.setVisibility(View.INVISIBLE);
            followButton.setVisibility(View.VISIBLE);
        }

        postsButton.callOnClick();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(currFragment==1&&check!=null){
            if (check.getIsAllowed()==1){
                syncPostChanges();
                String jsonMyObject;
                Bundle extras = getIntent().getExtras();
                jsonMyObject = extras.getString("thisTopic");
                topicItem = new Gson().fromJson(jsonMyObject, Topic.class);

                topicContentName.setText(topicItem.getTopicName());
                bundle = new Bundle();
                bundle.putLong("topicId", topicItem.getId());
            }
        }
        else if(currFragment == 2){
            getTopicFollowers(topicItem.getId());
        }
        if(toolbarItem!=null&&(check==null||check.getIsAllowed()!=1)){
            toolbarItem.setVisibility(View.GONE);
        }
        refreshTopicContent.setRefreshing(false);
    }

    public void getTopicFollowers(long topicId){
        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final Call<ArrayList<Follower>> call2 = service.getAllowedFollowers(topicId);
        call2.enqueue(new Callback<ArrayList<Follower>>() {
            @Override
            public void onResponse(Call<ArrayList<Follower>> call, Response<ArrayList<Follower>> response) {
                if(response.isSuccessful()){
                    //get back here boys
                    topicFollowers = response.body();

                    Fragment followersFragment = new FragmentTopicFollowers(topicFollowers);
                    getSupportFragmentManager().beginTransaction().replace(R.id.topic_container, followersFragment)
                            .commitAllowingStateLoss();

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Follower>> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        toolbarItem = toolbar.findViewById(R.id.topicNewPost);

        if(topicItem.getCreatorId() == user.getUserId()){
            toolbarItem.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);
        }
        else{
            toolbarItem.setVisibility(View.GONE);
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void uploadNotification(){
        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        ArrayList<Notifications> unsyncedNotifs = getUnsyncedNotifications();
        System.out.println("UNSYNCED NOTIFS: "+unsyncedNotifs.size());
        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(unsyncedNotifs);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addNotifications(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    System.out.println("NOTIFICATIONS ADDED YEY");
                    dataSynced(7);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
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

    public void viewPostBackButtonClicked(View view){
        finish();
    }

    public void topicNewPostClicked(View view){

        Intent intent = new Intent(TopicContentActivity.this, com.example.owner.petbetter.activities.NewPostActivity.class);
        intent.putExtra("thisTopicId", topicItem.getId());
        startActivity(intent);
    }

    private long addFollower(int followerId, int topicId, int userId, int isAllowed, int isSynced){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        long result = petBetterDb.addFollower(followerId, topicId, userId, isAllowed, isSynced);
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

    public void syncPostChanges(){

        //final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        FragmentNoResults fragmentpar = new FragmentNoResults();
        getSupportFragmentManager().beginTransaction().replace(R.id.topic_container,fragmentpar)
                .commitAllowingStateLoss();

        final Call<ArrayList<Post>> call2 = service2.getPosts();
        call2.enqueue(new Callback<ArrayList<Post>>() {
            @Override
            public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
                if(response.isSuccessful()&&check!=null&&currFragment==1){
                    if(check.getIsAllowed()==1){
                        FragmentNoResults fragmentpar = new FragmentNoResults();
                        getSupportFragmentManager().beginTransaction().replace(R.id.topic_container,fragmentpar)
                                .commitAllowingStateLoss();

                        setPosts(response.body());
                        fragment3 = new FragmentPosts();
                        fragment3.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.topic_container,fragment3)
                                .commitAllowingStateLoss();
                        System.out.println("DO WE REPLACE FRAGMENT AT LEAST??");
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());

            }
        });

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

    private Follower getFollowerWithTopicUser(long topicId, long userId){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        Follower result = petBetterDb.getFollowerWithTopicUser(topicId,userId);
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
