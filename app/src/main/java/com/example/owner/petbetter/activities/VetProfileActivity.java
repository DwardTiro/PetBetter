package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.Message;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kristian on 8/8/2017.
 */

public class VetProfileActivity extends AppCompatActivity {

    private ImageView profileBG;
    private TextView vetName;
    private TextView vetLandline;
    private TextView vetSpecialty;
    private TextView vetRating;
    private Button rateVetButton;
    private Button messageVetButton;


    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    private String email;
    private Veterinarian vetItem;
    private Message messageItem;
    private long mId;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_vet_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView profileName = (TextView) findViewById(R.id.profileToolbarName);



        profileBG = (ImageView) findViewById(R.id.profileImage);
        vetName = (TextView) findViewById(R.id.profileName);
        vetLandline = (TextView) findViewById(R.id.profileLandLine);
        vetSpecialty = (TextView) findViewById(R.id.vetSpecialty);
        vetRating = (TextView) findViewById(R.id.vetListRating);
        rateVetButton = (Button) findViewById(R.id.rateVetButton);
        messageVetButton = (Button) findViewById(R.id.messageVetButton);

        systemSessionManager = new SystemSessionManager(this);
        if(systemSessionManager.checkLogin())
            finish();
        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();

        email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);

        final String jsonMyObject;
        Bundle extras = getIntent().getExtras();
        jsonMyObject = extras.getString("thisVet");

        /*
        messageItem.setFromName(user.getName());
        messageItem.setFromId(user.getUserId());
        messageItem.setUserId(vetItem.getUserId());
        */


        vetItem = new Gson().fromJson(jsonMyObject,Veterinarian.class);

        profileName.setText(vetItem.getFirstName()+" " +vetItem.getLastName());
        vetName.setText(vetItem.getFirstName()+" "+vetItem.getLastName());
        vetLandline.setText(vetItem.getMobileNumber());
        vetSpecialty.setText(vetItem.getSpecialty());
        vetRating.setText(String.valueOf(vetItem.getRating()));



        mId = getMessageId(user.getUserId(), vetItem.getUserId());

        rateVetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(),RateVetActivity.class);
                intent.putExtra("thisVet", jsonMyObject);
                startActivity(intent);

            }
        });
        messageVetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mId!=0){
                    messageItem = getMessage(mId);
                }
                else{
                    mId = generateMessageId();
                    createMessage((int) mId, user.getUserId(), vetItem.getUserId());
                    messageItem = new Message(mId, vetItem.getUserId(),user.getUserId(), user.getFirstName(), user.getLastName());
                }

                Intent intent = new Intent(view.getContext(),MessageActivity.class);
                intent.putExtra("thisMessage", new Gson().toJson(messageItem));
                startActivity(intent);

            }
        });


        //Toast.makeText(this, "Vet's Name: "+vetItem.getName() + ". Delete this toast. Just to help you see where vet variable is", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onResume(){
        super.onResume();

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

    //back function found in toolbar profile for Image Button profileToolbarBack
    public void profileBackClicked(View view){
       finish();
    }

    private long getMessageId(long fromId, long toId){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        long result = petBetterDb.getMessageId(fromId, toId);
        petBetterDb.closeDatabase();

        return result;
    }

    private Message getMessage(long messageId){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        Message result = petBetterDb.getMessage(messageId);
        petBetterDb.closeDatabase();

        return result;
    }

    public int generateMessageId(){
        ArrayList<Integer> storedIds;
        int messageRepId = 1;

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        storedIds = petBetterDb.getMessageIds();
        petBetterDb.closeDatabase();


        if(storedIds.isEmpty()) {
            return messageRepId;
        } else {
            while (storedIds.contains(messageRepId)){
                messageRepId += 1;
            }
            return messageRepId;
        }
    }

    private long createMessage(int messageId, long userId, long toId){
        long  result;

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        result = petBetterDb.createMessage(messageId, userId, toId);
        petBetterDb.closeDatabase();



        return result;
    }
    //Integrate to db to display stuff on page

}
