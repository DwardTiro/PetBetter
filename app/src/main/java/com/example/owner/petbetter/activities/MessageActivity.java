package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.Message;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.fragments.FragmentMessageReps;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class MessageActivity extends AppCompatActivity {

    private Message messageItem;
    private TextView fromText;
    private FrameLayout container;
    private Button replyButton;
    private EditText messageText;
    private ImageButton addPhotoButton;
    private static final int IMG_REQUEST = 777;
    private Bitmap bitmap;
    private ImageView imageView;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user, messageUser;
    private String timeStamp, email;
    private int nId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = (Toolbar) findViewById(R.id.viewPostToolbar);
        final TextView activityTitle = (TextView) findViewById(R.id.activity_title);
        activityTitle.setText("View Message");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        String jsonMyObject;
        Bundle extras = getIntent().getExtras();
        jsonMyObject = extras.getString("thisMessage");
        messageItem = new Gson().fromJson(jsonMyObject, Message.class);

        fromText = (TextView) findViewById(R.id.fromTextView);
        container = (FrameLayout) findViewById(R.id.message_container);
        replyButton = (Button) findViewById(R.id.replyButton);
        messageText = (EditText) findViewById(R.id.messageText);
        addPhotoButton = (ImageButton) findViewById(R.id.addPhotoButton);

        systemSessionManager = new SystemSessionManager(this);
        if(systemSessionManager.checkLogin())
            this.finish();
        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();

        email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);

        fromText.setText(messageItem.getFromName());
/*
        Bundle bundle = new Bundle();
        System.out.println("POST ID BEFORE SEND "+postItem.getUserId());
        bundle.putLong("postId", postItem.getId());
        FragmentPostReps fragment = new FragmentPostReps();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.postrep_container,fragment).commit();
*/
        Bundle bundle = new Bundle();
        bundle.putLong("messageId", messageItem.getId());
        FragmentMessageReps fragment1 = new FragmentMessageReps();
        fragment1.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.message_container, fragment1).commit();

        addPhotoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                selectImage();
            }
        });

        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(messageText.getText().toString())) {

                    int messageRepId = generateMessageRepId();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                    timeStamp = sdf.format(new Date());

                    addMessageRep(messageRepId, (int) user.getUserId(), (int) messageItem.getId(),
                            messageText.getText().toString(), 1, timeStamp);



                    nId = generateNotifsId();

                    if(messageItem.getUserId()==user.getUserId())
                        notifyMessage(nId, messageItem.getFromId(), user.getUserId(), 0, 2, timeStamp, (int) messageItem.getId());
                    else
                        notifyMessage(nId, messageItem.getUserId(), user.getUserId(), 0, 2, timeStamp, (int) messageItem.getId());

                    Intent intent = new Intent(MessageActivity.this, com.example.owner.petbetter.activities.MessageActivity.class);
                    intent.putExtra("thisMessage", new Gson().toJson(messageItem));
                    finish();
                    startActivity(intent);
                }
            }
        });
    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMG_REQUEST && resultCode == RESULT_OK && data!=null){
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void viewPostBackButtonClicked(View view){
        finish();
    }
    private void initializeDatabase() {

        petBetterDb = new DataAdapter(this);

        try {
            petBetterDb.createDatabase();
        } catch(SQLException e ){
            e.printStackTrace();
        }

    }

    public int generateMessageRepId(){
        ArrayList<Integer> storedIds;
        int messageRepId = 1;

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        storedIds = petBetterDb.getMessageRepIds();
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

    private long addMessageRep(int messageRepId, int userId, int messageId, String repContent, int isSent, String datePerformed){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        long result = petBetterDb.addMessageRep(messageRepId, userId, messageId, repContent, isSent, datePerformed);
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

            return markerId;
        }
    }

    private long notifyMessage(int notifId, long toId, long userId, int isRead, int type, String timeStamp, int messageId){
        long  result;

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }


        result = petBetterDb.notifyUser(notifId, toId, userId, isRead, type, timeStamp, messageId);
        petBetterDb.closeDatabase();


        return result;
    }
}

