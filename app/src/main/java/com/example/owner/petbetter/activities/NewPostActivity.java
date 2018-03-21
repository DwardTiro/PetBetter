package com.example.owner.petbetter.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
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
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.Follower;
import com.example.owner.petbetter.classes.Notifications;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.Topic;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    private ImageView newPostImage;
    private TextView newPostLocationName;
    private ImageButton postAddImage;
    private ImageButton postAddFacility;
    private EditText newPostTitle;
    private EditText newPostDesc;
    private Button newPostButton;
    private HerokuService service;
    private static final int IMG_REQUEST = 777;
    private Bitmap bitmap;
    private long idLink;
    private long vetId;
    private long topicid;
    private long postId;
    private int idType=0;

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

        newPostImage = (ImageView) findViewById(R.id.newPostImage);
        newPostLocationName = (TextView) findViewById(R.id.newPostLocationName);
        postAddImage = (ImageButton) findViewById(R.id.postAddImage);
        postAddFacility = (ImageButton) findViewById(R.id.postaddFacility);

        newPostLocationName.setText("");

        Bundle extras = getIntent().getExtras();
        topicId = extras.getLong("thisTopicId");
        //topicId = new Gson().fromJson(jsonMyObject, long);

        newPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newPostTitle.getText().toString()!=""&&newPostDesc.getText().toString()!=""){

                    String image = imageToString();

                    pId = generatePostId();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                    timeStamp = sdf.format(new Date());

                    createPost(pId,user.getUserId(), newPostTitle.getText().toString(), newPostDesc.getText().toString(),
                            topicId, timeStamp, image, (int) idLink, idType, 0, 0);
                    //change later
                    //add faci_id
                    uploadPost(getUnsyncedPosts());
                    //notifyMessage(nId, messageItem.getFromId(), user.getUserId(), 0, 2, timeStamp, sourceId);

                    topicFollowers = getTopicFollowers(topicId);

                    notifyTopicPost(nId, user.getUserId(), user.getUserId(), 0, 3, timeStamp, (int) topicId, 0);
                    syncNotifChanges(user.getUserId());
                    finish();
                }
            }
        });

        postAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        postAddFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewPostActivity.this, com.example.owner.petbetter.activities.SearchActivity.class);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 111);
            }
        });
    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
        ActivityCompat.requestPermissions(NewPostActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

    }

    private String imageToString(){
        try{
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            byte[] imgByte = byteArrayOutputStream.toByteArray();

            return Base64.encodeToString(imgByte,Base64.DEFAULT);
        }catch(NullPointerException npe){
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMG_REQUEST && resultCode == RESULT_OK && data!=null){
            Uri path = data.getData();
            try {


                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                /*
                if(bitmap.getHeight()>250||bitmap.getWidth()>250){
                    bitmap = Bitmap.createScaledBitmap(bitmap,250,250,false);
                }*/
                newPostImage.setImageBitmap(bitmap);



            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(requestCode==111 && resultCode == RESULT_OK){
            System.out.println("REQUEST CODE 111");
            Bundle bundle = data.getExtras();
            idLink = bundle.getLong("faciId");
            idType = bundle.getInt("id_type");
            System.out.println("REQUEST CODE 111 ID: " + idLink);

            if(idType==1){
                Veterinarian veterinarian = getVeterinarianFromId(idLink);
                //User user = getUserWithId(idLink);
                if(user!=null){
                    newPostLocationName.setVisibility(View.VISIBLE);
                    newPostLocationName.setText(veterinarian.getName());
                }
            }

            if(idType==2){
                Facility facility = getFacility(idLink);
                if(facility!=null){
                    newPostLocationName.setVisibility(View.VISIBLE);
                    newPostLocationName.setText(facility.getFaciName());
                    System.out.println("FACI NAME POST "+facility.getFaciName());
                }
            }

            if(idType==3){
                Topic topic = getTopic(idLink);
                if(topic!=null){
                    newPostLocationName.setVisibility(View.VISIBLE);
                    newPostLocationName.setText(topic.getTopicName());
                }
            }

            if(idType==4){
                Post post = getPost(idLink);
                if(post!=null){
                    newPostLocationName.setVisibility(View.VISIBLE);
                    newPostLocationName.setText(post.getTopicName());
                }
            }


        }
    }

    public void syncNotifChanges(final long userId){

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

    private Facility getFacility(long faciId){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        Facility result = petBetterDb.getFacility((int) faciId);
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
                            String postPhoto, int idLink, int idType, int isDeleted, int isSynced){
        long  result;

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        result = petBetterDb.createPost(pId, userId, postTitle, postDesc, topicId, timeStamp, postPhoto, idLink, idType, isDeleted, isSynced);
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
