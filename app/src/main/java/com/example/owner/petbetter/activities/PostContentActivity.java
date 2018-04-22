package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.Bookmark;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.Notifications;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.PostRep;
import com.example.owner.petbetter.classes.Rating;
import com.example.owner.petbetter.classes.Topic;
import com.example.owner.petbetter.classes.Upvote;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.fragments.FragmentPostReps;
import com.example.owner.petbetter.interfaces.CheckUpdates;
import com.example.owner.petbetter.services.NotificationReceiver;
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

import static com.example.owner.petbetter.ServiceGenerator.BASE_URL;

/**
 * Created by Kristian on 10/12/2017.
 */

public class PostContentActivity extends AppCompatActivity {

    private ImageView profilePic;
    private TextView profileName;
    private TextView postTimeStamp;
    private TextView postTitle;
    private TextView homeListContent;
    private ImageButton upPostButton;
    private TextView upCounter;
    private ImageButton downPostButton;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user, postUser;
    private Post postItem;
    private String email;
    private Button commentButton;
    private EditText commentText;
    private String timeStamp;
    private int nId;
    private Upvote vote;
    private SwipeRefreshLayout refreshPostContent;
    private FragmentPostReps fragment;
    private ImageButton bookMarkPost;
    HerokuService service;
    private Toolbar toolbar;
    private NestedScrollView postScrollView;
    private boolean isBookmarked = false;
    private ImageView postImage;
    private ImageView postImageFrame;
    private Button locButton;
    private ImageView locationImage;
    private Facility facility;
    private Veterinarian veterinarian;
    private Topic topic;
    private Post post;
    private ImageView userIdentifier;

    @Override
    protected void onNewIntent(Intent intent) {
        try {
            //on click notification
            Bundle extras = getIntent().getExtras();
            String jsonMyObject;
            jsonMyObject = extras.getString("notifPost");

            Notifications notifItem = new Gson().fromJson(jsonMyObject, Notifications.class);
            notifRead(notifItem.getId());
        } catch (Exception e) {
            Log.e("onclick", "Exception onclick" + e);
        }
    }

    @Override
    protected void onCreate(Bundle SavedInstance) {
        super.onCreate(SavedInstance);
        setContentView(R.layout.activity_post_content);

        toolbar = (Toolbar) findViewById(R.id.viewPostToolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        profilePic = (ImageView) findViewById(R.id.homeListUserPicture);
        profileName = (TextView) findViewById(R.id.homeListUserName);
        postTimeStamp = (TextView) findViewById(R.id.homeListTimeStamp);
        postTitle = (TextView) findViewById(R.id.homeListPostTitle);
        homeListContent = (TextView) findViewById(R.id.homeListContent);
        commentButton = (Button) findViewById(R.id.homeListPostCommentButton);
        commentText = (EditText) findViewById(R.id.homeListCommentBox);
        upPostButton = (ImageButton) findViewById(R.id.upPostButton);
        upCounter = (TextView) findViewById(R.id.upCounter);
        downPostButton = (ImageButton) findViewById(R.id.downPostButton);
        refreshPostContent = (SwipeRefreshLayout) findViewById(R.id.refreshPostContent);
        bookMarkPost = (ImageButton) findViewById(R.id.topicNewPost);
        postScrollView = (NestedScrollView) findViewById(R.id.postScrollView);
        postImage = (ImageView) findViewById(R.id.postImage);
        postImageFrame = (ImageView) findViewById(R.id.postImageFrame);
        locButton = (Button) findViewById(R.id.locationButton);
        locationImage = (ImageView) findViewById(R.id.locationImage);
        userIdentifier = (ImageView) findViewById(R.id.iconIdentifier);



        postScrollView.smoothScrollTo(0, 0);

        refreshPostContent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPostContent.setRefreshing(true);
                syncUpvoteChanges();
                syncPostRepChanges();
                refreshPostContent.setRefreshing(false);
            }
        });


        systemSessionManager = new SystemSessionManager(this);
        if (systemSessionManager.checkLogin())
            finish();
        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();

        String jsonMyObject;
        Bundle extras = getIntent().getExtras();

        /*
        try{
            jsonMyObject = extras.getString("thisPost");

            postItem = new Gson().fromJson(jsonMyObject, Post.class);

            postUser = getPostUser(postItem.getUserId());
        }catch(NullPointerException npe){
            jsonMyObject = extras.getString("notifPost");

            postItem = new Gson().fromJson(jsonMyObject, Post.class);
            postUser = getPostUser(postItem.getUserId());
        }*/

        jsonMyObject = extras.getString("thisPost");

        postItem = new Gson().fromJson(jsonMyObject, Post.class);

        postUser = getPostUser(postItem.getUserId());
        System.out.println("POSTITEM ID IS: " + postItem.getId());

        bookMarkPost.setImageResource(R.drawable.ic_bookmark_border_white_24dp);

        int voteCount = getVoteCount((int) postItem.getId(), 1);
        upCounter.setText(String.valueOf(voteCount));




        Bundle bundle = new Bundle();
        System.out.println("POST ID BEFORE SEND " + postItem.getUserId());
        bundle.putLong("postId", postItem.getId());
        fragment = new FragmentPostReps();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.postrep_container, fragment).commitAllowingStateLoss();

        if(postUser.getUserType() == 1){
            profileName.setText("Dr. "+postUser.getName()+", DVM.");
            userIdentifier.setImageResource(R.drawable.ic_local_hospital_black_18dp);
        }else if(postUser.getUserType() ==4){
            userIdentifier.setImageResource(R.drawable.ic_business_center_black_18dp);
            profileName.setText(postUser.getName());
        }
        else if(postUser.getUserType() == 3){
            profileName.setText("Administrator");
        }
        else
            profileName.setText(postUser.getName());
        postTitle.setText(postItem.getTopicName());
        homeListContent.setText(postItem.getTopicContent());

        email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);

        if(postItem.getPostPhoto() != null) {
            String newFileName = BASE_URL + postItem.getPostPhoto();
            Glide.with(PostContentActivity.this).load(newFileName).error(R.drawable.app_icon_yellow).into(postImage);
            postImage.setVisibility(View.VISIBLE);
            postImageFrame.setVisibility(View.VISIBLE);
            //postImage.setImageResource();
        }




        if (checkIfBookmark((int) postItem.getId(), (int) user.getUserId())) {
            bookMarkPost.setImageResource(R.drawable.ic_bookmark_white_24dp);
            isBookmarked=true;
        }else{
            bookMarkPost.setImageResource(R.drawable.ic_bookmark_border_white_24dp);
            isBookmarked=false;
        }
        bookMarkPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isBookmarked) {
                    bookMarkPost.setImageResource(R.drawable.ic_bookmark_white_24dp);
                    bookMarkPost.setEnabled(false);
                    int newId = generateBookmarkIds();
                    addPostBookmarkToDB(newId, postItem.getId(), user.getUserId());
                    Bookmark bookmark = new Bookmark(newId, postItem.getId(), 2, user.getUserId());

                    final HerokuService bookMarkService = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
                    Gson gson = new GsonBuilder().serializeNulls().create();
                    String jsonArray = gson.toJson(bookmark);
                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray);

                    //syncBookmarkChanges();

                    Call<Void> call = bookMarkService.addBookmark(body);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {

                            dataSync(16);

                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                        }
                    });

                }else{
                    bookMarkPost.setImageResource(R.drawable.ic_bookmark_border_white_24dp);
                    bookMarkPost.setEnabled(false);
                    deletePostBookmark(postItem.getId(),user.getUserId());
                    removeBookmark(postItem.getId(), user.getUserId());


                }
            }

        });


        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(commentText.getText().toString())) {

                    int postrepid = generatePostRepId();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                    timeStamp = sdf.format(new Date());

                    addPostRep(postrepid, (int) user.getUserId(), (int) postItem.getId(), 0, commentText.getText().toString(), timeStamp);
                    //uploadPostReps(getUnsyncedPostReps());
                    syncPostRepChanges();

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


        vote = getUserVote((int) postItem.getId(), (int) user.getUserId(), 1);

        if (vote != null) {
            if (vote.getValue() == 1) {
                upPostButton.setImageResource(R.mipmap.ic_thumb_up_black_24dp);
            } else {
                downPostButton.setImageResource(R.mipmap.ic_thumb_down_black_24dp);
            }
        }

        upPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                upPostButton.setEnabled(false);
                downPostButton.setEnabled(false);
                upPostButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        upPostButton.setEnabled(true);
                        downPostButton.setEnabled(true);
                    }
                }, 500);

                if (vote == null) {//0
                    int id = generateUpvoteId();
                    addVote(id, (int) postItem.getId(), (int) user.getUserId(), 1, 1, 0);
                    upPostButton.setImageResource(R.mipmap.ic_thumb_up_black_24dp);
                    downPostButton.setImageResource(R.drawable.ic_thumb_down_grey600_48dp);

                    syncUpvoteChanges();
                }
                try {
                    if (vote.getValue() == 1) {
                        removeVote(vote.getId());
                        upPostButton.setImageResource(R.drawable.ic_thumb_up_grey600_48dp);
                        vote = null;
                    }
                    if (vote.getValue() == -1) {
                        alterVote(vote.getId(), 1);
                        upPostButton.setImageResource(R.mipmap.ic_thumb_up_black_24dp);
                        downPostButton.setImageResource(R.drawable.ic_thumb_down_grey600_48dp);
                    }
                } catch (NullPointerException npe) {
                    vote = getUserVote((int) postItem.getId(), (int) user.getUserId(), 1);

                    if (vote != null) {
                        if (vote.getValue() == 1) {
                            upPostButton.setImageResource(R.mipmap.ic_thumb_up_black_24dp);
                            downPostButton.setImageResource(R.drawable.ic_thumb_down_grey600_48dp);
                        } else {
                            downPostButton.setImageResource(R.mipmap.ic_thumb_down_black_24dp);
                            upPostButton.setImageResource(R.drawable.ic_thumb_up_grey600_48dp);
                        }
                    } else {
                        upPostButton.setImageResource(R.drawable.ic_thumb_up_grey600_48dp);
                        downPostButton.setImageResource(R.drawable.ic_thumb_down_grey600_48dp);
                    }
                }


            }
        });

        downPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                upPostButton.setEnabled(false);
                downPostButton.setEnabled(false);
                upPostButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        upPostButton.setEnabled(true);
                        downPostButton.setEnabled(true);
                    }
                }, 3000);

                if (vote == null) {
                    int id = generateUpvoteId();
                    addVote(id, (int) postItem.getId(), (int) user.getUserId(), -1, 1, 0);
                    downPostButton.setImageResource(R.mipmap.ic_thumb_down_black_24dp);
                    upPostButton.setImageResource(R.drawable.ic_thumb_up_grey600_48dp);
                    syncUpvoteChanges();
                }
                try {
                    if (vote.getValue() == -1) {
                        removeVote(vote.getId());
                        downPostButton.setImageResource(R.drawable.ic_thumb_down_grey600_48dp);
                        vote = null;
                    }
                    if (vote.getValue() == 1) {
                        alterVote(vote.getId(), -1);
                        downPostButton.setImageResource(R.mipmap.ic_thumb_down_black_24dp);
                        upPostButton.setImageResource(R.drawable.ic_thumb_up_grey600_48dp);
                    }
                } catch (NullPointerException npe) {
                    vote = getUserVote((int) postItem.getId(), (int) user.getUserId(), 1);
                    if (vote != null) {
                        if (vote.getValue() == 1) {
                            upPostButton.setImageResource(R.mipmap.ic_thumb_up_black_24dp);
                            downPostButton.setImageResource(R.drawable.ic_thumb_down_grey600_48dp);
                        } else {
                            downPostButton.setImageResource(R.mipmap.ic_thumb_down_black_24dp);
                            upPostButton.setImageResource(R.drawable.ic_thumb_up_grey600_48dp);
                        }
                    } else {
                        upPostButton.setImageResource(R.drawable.ic_thumb_up_grey600_48dp);
                        downPostButton.setImageResource(R.drawable.ic_thumb_down_grey600_48dp);
                    }
                }

            }
        });

        try{
            int type = postItem.getIdType();
            if(type==1){
                veterinarian = getVeterinarianFromId(postItem.getIdLink());
                locButton.setText(veterinarian.getName());
            }
            if(type==2){
                facility = getFacility(postItem.getIdLink());
                locButton.setText(facility.getFaciName());
            }
            if(type==3){
                topic = getTopic(postItem.getIdLink());
                locButton.setText(topic.getTopicName());
            }
            if(type==4){
                post = getPost(postItem.getIdLink());
                locButton.setText(post.getTopicName());
            }
            if(type==0){
                locButton.setVisibility(View.GONE);
                locationImage.setVisibility(View.GONE);
            }
        }catch(NullPointerException npe){
            locButton.setVisibility(View.GONE);
            locationImage.setVisibility(View.GONE);
        }

        if(postUser.getUserPhoto()!=null){

            String newFileName = BASE_URL + postUser.getUserPhoto();
            System.out.println(newFileName);
            //String newFileName = "http://192.168.0.19/petbetter/"+thisMessageRep.getMessagePhoto();
            Glide.with(PostContentActivity.this).load(newFileName).error(R.drawable.userplacholder).into(profilePic);
            /*
            Picasso.with(inflater.getContext()).load("http://".concat(newFileName))
                    .error(R.drawable.back_button).into(holder.messageRepImage);*/
            //setImage(holder.messageRepImage, newFileName);

            profilePic.setVisibility(View.VISIBLE);
            profilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(PostContentActivity.this, com.example.owner.petbetter.activities.UserProfileActivity.class);
                    intent.putExtra("UserProfile", postUser.getUserId());
                    startActivity(intent);
                }
            });
        }
    }

    public void locButtonClicked(View view){
        if(postItem.getIdType()==1){
            Intent intent = new Intent(PostContentActivity.this, com.example.owner.petbetter.activities.VetProfileActivity.class);
            intent.putExtra("thisVet", new Gson().toJson(veterinarian));
            startActivity(intent);
        }
        if(postItem.getIdType()==2){
            Intent intent = new Intent(PostContentActivity.this, com.example.owner.petbetter.activities.PetClinicProfileActivity.class);
            intent.putExtra("thisClinic", new Gson().toJson(facility));
            startActivity(intent);
        }
        if(postItem.getIdType()==3){
            Intent intent = new Intent(PostContentActivity.this, com.example.owner.petbetter.activities.TopicContentActivity.class);
            intent.putExtra("thisTopic", new Gson().toJson(topic));
            startActivity(intent);
        }
        if(postItem.getIdType()==4){
            Intent intent = new Intent(PostContentActivity.this, com.example.owner.petbetter.activities.PostContentActivity.class);
            intent.putExtra("thisPost", new Gson().toJson(post));
            startActivity(intent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        View item = toolbar.findViewById(R.id.topicNewPost);
        item.setVisibility(View.VISIBLE);
        item.setEnabled(true);
        return super.onCreateOptionsMenu(menu);
    }

    private Facility getFacility(long id) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Facility result = petBetterDb.getFacility((int) id);
        petBetterDb.closeDatabase();

        return result;
    }

    private boolean checkIfBookmark(int item_id, int user_id) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        boolean result = petBetterDb.checkIfBookMark(item_id, user_id, 2);
        petBetterDb.closeDatabase();

        return result;
    }

    public void viewPostBackButtonClicked(View view) {
        finish();
    }

    private void initializeDatabase() {

        petBetterDb = new DataAdapter(this);

        try {
            petBetterDb.createDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void syncBookmarkChanges() {
        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        ArrayList<Bookmark> unsyncedBookmarks = getUnsyncedBookmarks();
        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(unsyncedBookmarks);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray);

        final Call<Void> call = service.addBookmarks(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                dataSync(16);
                bookMarkPost.setEnabled(true);
                isBookmarked = true;
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private ArrayList<Bookmark> getUnsyncedBookmarks() {
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Bookmark> result = petBetterDb.getUnsyncedBookmarks();
        petBetterDb.closeDatabase();

        return result;
    }

    private void dataSync(int n) {
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        petBetterDb.dataSynced(n);
        petBetterDb.closeDatabase();

    }

    public int generateBookmarkIds() {
        int newId;
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayList<Integer> ids = petBetterDb.generateBookmarkIds();
        if (ids.size() != 0) {
            newId = ids.get(ids.size() - 1);
            newId += 1;
        } else
            newId = 1;

        petBetterDb.closeDatabase();

        return newId;
    }

    public long addPostBookmarkToDB(long _id, long item_id, long user_id) {
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.addPostBookmark(_id, item_id, user_id);

        petBetterDb.closeDatabase();
        return result;
    }

    private void deletePostBookmark(long item_id, long user_id){
        try{
            petBetterDb.openDatabase();
        }catch (SQLException e){
            e.printStackTrace();
        }
        petBetterDb.deletePostBookmark(item_id, user_id);
        petBetterDb.closeDatabase();
    }

    private void removeBookmark(long item_id, long user_id){
        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final Call<Void> call = service.deleteBookmark(user_id, item_id, 2);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    //syncBookmarkChanges();
                    isBookmarked=false;
                    bookMarkPost.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private Veterinarian getVeterinarianFromId(long userId){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        Veterinarian result = petBetterDb.getVeterinarianFromId(userId);
        petBetterDb.closeDatabase();

        return result;
    }

    private Topic getTopic(long topicId){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        Topic result = petBetterDb.getTopic(topicId);
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


    public void removeVote(long id) {

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final Call<Void> call = service.removeVote(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    syncUpvoteChanges();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
    }

    public void alterVote(long id, int value) {

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final Call<Void> call = service.alterVote(id, value);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    syncUpvoteChanges();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
    }

    public void syncPostRepChanges() {

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
                if (response.isSuccessful()) {
                    dataSynced(10);

                    final Call<ArrayList<PostRep>> call2 = service2.getAllPostReps();
                    call2.enqueue(new Callback<ArrayList<PostRep>>() {
                        @Override
                        public void onResponse(Call<ArrayList<PostRep>> call, Response<ArrayList<PostRep>> response) {
                            if (response.isSuccessful()) {
                                setPostReps(response.body());
                                getSupportFragmentManager().beginTransaction().replace(R.id.postrep_container, fragment)
                                        .commitAllowingStateLoss();
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

    public void syncUpvoteChanges() {

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);


        ArrayList<Upvote> unsyncedUpvotes = getUnsyncedUpvotes();

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(unsyncedUpvotes);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addUpvotes(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    System.out.println("POSTS ADDED YEY");
                    dataSynced(15);

                    final Call<ArrayList<Upvote>> call2 = service2.getUpvotes();
                    call2.enqueue(new Callback<ArrayList<Upvote>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Upvote>> call, Response<ArrayList<Upvote>> response) {
                            if (response.isSuccessful()) {
                                setUpvotes(response.body());
                                upCounter.setText(String.valueOf(getVoteCount((int) postItem.getId(), 1)));
                                vote = getUserVote((int) postItem.getId(), (int) user.getUserId(), 1);
                                if (vote != null) {
                                    if (vote.getValue() == 1) {
                                        upPostButton.setImageResource(R.mipmap.ic_thumb_up_black_24dp);
                                        downPostButton.setImageResource(R.drawable.ic_thumb_down_grey600_48dp);
                                    } else {
                                        downPostButton.setImageResource(R.mipmap.ic_thumb_down_black_24dp);
                                        upPostButton.setImageResource(R.drawable.ic_thumb_up_grey600_48dp);
                                    }
                                } else {
                                    upPostButton.setImageResource(R.drawable.ic_thumb_up_grey600_48dp);
                                    downPostButton.setImageResource(R.drawable.ic_thumb_down_grey600_48dp);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Upvote>> call, Throwable t) {
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

    private ArrayList<Upvote> getUnsyncedUpvotes() {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Upvote> result = petBetterDb.getUnsyncedUpvotes();
        petBetterDb.closeDatabase();

        return result;
    }

    public long setUpvotes(ArrayList<Upvote> upvoteList) {
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setUpvotes(upvoteList);
        petBetterDb.closeDatabase();

        return result;
    }

    private void uploadPostReps(ArrayList<PostRep> postreps) {
        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        System.out.println("HOW MANY POSTREPS? " + postreps.size());

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

    private void uploadNotifications(ArrayList<Notifications> notifs) {
        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        System.out.println("HOW MANY NOTIFICATIONS? " + notifs.size());

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

    public long setPostReps(ArrayList<PostRep> postRepList) {
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setPostReps(postRepList);
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<PostRep> getUnsyncedPostReps() {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<PostRep> result = petBetterDb.getUnsyncedPostReps();
        petBetterDb.closeDatabase();

        return result;
    }

    private long addVote(long id, int feedId, int userId, int value, int type, int isSynced) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        long result = petBetterDb.addVote(id, feedId, userId, value, type, isSynced);
        petBetterDb.closeDatabase();

        return result;
    }

    private Upvote getUserVote(int feedId, int userId, int type) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Upvote result = petBetterDb.getUserVote(feedId, userId, type);
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

    private int getVoteCount(int feedId, int type) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int result = petBetterDb.getVoteCount(feedId, type);
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Notifications> getUnsyncedNotifications() {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Notifications> result = petBetterDb.getUnsyncedNotifications();
        petBetterDb.closeDatabase();

        return result;
    }

    public int generateUpvoteId() {
        ArrayList<Integer> storedIds;
        int upvoteId = 1;

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        storedIds = petBetterDb.generateUpvoteIds();
        petBetterDb.closeDatabase();


        if (storedIds.isEmpty()) {
            return upvoteId;
        } else {
            while (storedIds.contains(upvoteId)) {
                upvoteId += 1;
            }
            return upvoteId;
        }
    }

    public int generatePostRepId() {
        ArrayList<Integer> storedIds;
        int postRepId = 1;

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        storedIds = petBetterDb.getPostRepIds();
        petBetterDb.closeDatabase();


        if (storedIds.isEmpty()) {
            return postRepId;
        } else {
            while (storedIds.contains(postRepId)) {
                postRepId += 1;
            }
            return postRepId;
        }
    }

    private long addPostRep(int postRepId, int userId, int postId, int parentId, String repContent, String datePerformed) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        long result = petBetterDb.addPostRep(postRepId, userId, postId, parentId, repContent, datePerformed);
        petBetterDb.closeDatabase();

        return result;
    }

    private User getUser(String email) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        User result = petBetterDb.getUser(email);
        petBetterDb.closeDatabase();

        return result;
    }

    private User getPostUser(long userId) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        User result = petBetterDb.getPostUser(userId);
        petBetterDb.closeDatabase();

        return result;
    }

    public int generateNotifsId() {
        ArrayList<Integer> storedIds;
        int markerId = 1;

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        storedIds = petBetterDb.getNotifIds();
        petBetterDb.closeDatabase();


        if (storedIds.isEmpty()) {
            return markerId;
        } else {
            while (storedIds.contains(markerId)) {
                markerId += 1;
            }

            return markerId;
        }
    }

    public void notifRead(long notifId) {

        //modify this method in such a way that it only gets bookmarks tagged by user. Separate from facilities.
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        petBetterDb.notifRead(notifId);
        petBetterDb.closeDatabase();
    }

    public long notifyPostRep(int notifId, long toId, long userId, int isRead, int type, String timeStamp, int postId, int isSynced) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        long result = petBetterDb.notifyUser(notifId, toId, userId, isRead, type, timeStamp, postId, isSynced);
        petBetterDb.closeDatabase();

        return result;
    }
}
