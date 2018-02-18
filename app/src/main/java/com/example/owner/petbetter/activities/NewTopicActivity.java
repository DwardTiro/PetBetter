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
import com.example.owner.petbetter.classes.Topic;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.fragments.FragmentCommunity;
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

public class NewTopicActivity extends AppCompatActivity {

    EditText newTopicTitle;
    EditText newTopicDesc;
    Button newTopicButton;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user, usertwo;
    private int mId, tId, nId;
    private String timeStamp;
    private ArrayList<Topic> tList;
    private boolean alreadyExist= false;
    HerokuService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_topic);

        systemSessionManager = new SystemSessionManager(this);

        if(systemSessionManager.checkLogin())
            finish();

        Toolbar toolbar = (Toolbar) findViewById(R.id.createTopicToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();

        String email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);

        newTopicTitle = (EditText) findViewById(R.id.newTopicTitle);
        newTopicDesc = (EditText) findViewById(R.id.newTopicDesc);
        newTopicButton = (Button) findViewById(R.id.createButton);

        newTopicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(newTopicTitle.getText().toString().equals("")&&newTopicDesc.getText().toString().equals(""))){

                    newTopicButton.setEnabled(true);
                    tId = generateTopicId();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                    timeStamp = sdf.format(new Date());

                    createTopic(tId,user.getUserId(), newTopicTitle.getText().toString(), newTopicDesc.getText().toString(),
                            timeStamp, 0, 0);
                    uploadTopic(getUnsyncedTopics());

                    finish();
                    //Intent intent = new Intent(v.getContext(),com.example.owner.petbetter.activities.CommActivity.class);
                    //startActivity(intent);
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

    private void uploadTopic(ArrayList<Topic> topics){
        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        System.out.println("HOW MANY TOPICS? "+topics.size());

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(topics);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addTopics(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                dataSynced(9);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
                Toast.makeText(NewTopicActivity.this, "Unable to upload topics on server", Toast.LENGTH_LONG);
            }
        });
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

    private ArrayList<Topic> getUnsyncedTopics(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Topic> result = petBetterDb.getUnsyncedTopics();
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


    public int generateTopicId(){
        ArrayList<Integer> storedIds;
        int markerId = 1;

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        storedIds = petBetterDb.getTopicIds();
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
    * tId,user.getUserId(), newTopicTitle.getText().toString(), newTopicDesc.getText().toString(),
                            timeStamp, 0
    * */
    private long createTopic(int topicId, long userId, String topicTitle, String topicDesc, String timeStamp, int isDeleted,
                             int isSynced){
        long  result;

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        result = petBetterDb.createTopic(topicId, userId, topicTitle, topicDesc, timeStamp, isDeleted, isSynced);
        petBetterDb.closeDatabase();


        return result;
    }
    public void createTopicBackClicked(View view){
        finish();
        Intent intent = new Intent(view.getContext(),com.example.owner.petbetter.activities.CommActivity.class);
        startActivity(intent);
    }
}
