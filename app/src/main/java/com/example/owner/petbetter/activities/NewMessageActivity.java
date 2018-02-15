package com.example.owner.petbetter.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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
import com.example.owner.petbetter.classes.Message;
import com.example.owner.petbetter.classes.MessageRep;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.fragments.FragmentMessages;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;

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

/**
 * Created by Kristian on 10/16/2017.
 */

public class NewMessageActivity extends AppCompatActivity {


    EditText newMsgSendTo;
    EditText newMsgContent;
    Button newMsgSendButton;
    private ImageButton newMsgAddPhoto;
    private static final int IMG_REQUEST = 777;
    private Bitmap bitmap;
    private ImageView imageView;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user, usertwo;
    private int mId, mrId, nId;
    private String timeStamp;
    private ArrayList<Message> mList;
    private boolean alreadyExist= false;
    HerokuService service;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_new_message);

        Toolbar toolbar = (Toolbar) findViewById(R.id.viewPostToolbar);
        setSupportActionBar(toolbar);
        final TextView activityTitle = (TextView) findViewById(R.id.activity_title);
        activityTitle.setText("New Message");
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        systemSessionManager = new SystemSessionManager(this);

        if(systemSessionManager.checkLogin())
            finish();

        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();

        String email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);

        newMsgSendTo = (EditText) findViewById(R.id.newMsgSendTo);
        newMsgContent = (EditText) findViewById(R.id.newMsgContent);
        newMsgSendButton = (Button) findViewById(R.id.newMsgSendBtn);
        newMsgAddPhoto = (ImageButton) findViewById(R.id.newMsgAddPhoto);

        newMsgAddPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                selectImage();
            }
        });

        newMsgSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newMsgContent.getText().toString()!=""&&newMsgSendTo.getText().toString()!=""){
                    usertwo = getUser(newMsgSendTo.getText().toString());
                    mId = generateMessageId();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                    timeStamp = sdf.format(new Date());

                    //check first if message exists before creating
                    mList = getMessages(user.getUserId());
                    for(int i=0;i<mList.size();i++){
                        if(mList.get(i).getUserId()==user.getUserId()||mList.get(i).getFromId()==user.getUserId()){
                            alreadyExist= true;
                            mId = (int) mList.get(i).getId();
                        }
                    }

                    String image = imageToString();

                    if(alreadyExist==true){
                        mrId = generateMessageRepId();
                        addMessageRep(mrId, (int) user.getUserId(), mId,
                                newMsgContent.getText().toString(), 1, timeStamp, image, 0);
                        uploadMessageRep(getUnsyncedMessageReps());



                    }
                    else{
                        createMessage(mId, user.getUserId(), usertwo.getUserId());
                        uploadMessage(getUnsyncedMessages());
                        mrId = generateMessageRepId();
                        addMessageRep(mrId, (int) user.getUserId(), mId,
                                newMsgContent.getText().toString(), 1, timeStamp, image, 0);
                        uploadMessageRep(getUnsyncedMessageReps());

                    }

                    nId = generateNotifsId();
                    notifyMessage(nId, usertwo.getUserId(), user.getUserId(), 0, 2, timeStamp, mId);
                    finish();
                }
            }
        });

    }

    private String imageToString(){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte,Base64.DEFAULT);
    }


    private void uploadMessage(ArrayList<Message> messages){
        //herokuservice
        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        Gson gson = new Gson();
        String jsonArray = gson.toJson(messages);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addMessages(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                dataSynced(5);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
                Toast.makeText(NewMessageActivity.this, "Unable to upload message on server", Toast.LENGTH_LONG);
            }
        });

    }

    private void uploadMessageRep(ArrayList<MessageRep> messageReps){
        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        Gson gson = new Gson();
        String jsonArray = gson.toJson(messageReps);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addMessageReps(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                dataSynced(6);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
                Toast.makeText(NewMessageActivity.this, "Unable to upload messagereps on server", Toast.LENGTH_LONG);
            }
        });
    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
        ActivityCompat.requestPermissions(NewMessageActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMG_REQUEST && resultCode == RESULT_OK && data!=null){
            Uri path = data.getData();
            try {


                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted and now can proceed
                    //a sample method called

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(NewMessageActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // add other cases for more permissions
        }
    }
    */

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

    //nId, usertwo.getUserId(), user.getUserId(), 0, 2, timeStamp
    private long notifyMessage(int notifId, long toId, long userId, int isRead, int type, String timeStamp, int mId){
        long  result;

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }


        result = petBetterDb.notifyUser(notifId, toId, userId, isRead, type, timeStamp, mId);
        petBetterDb.closeDatabase();


        return result;
    }

    private ArrayList<MessageRep> getUnsyncedMessageReps(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<MessageRep> result = petBetterDb.getUnsyncedMessageReps();
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Message> getUnsyncedMessages(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Message> result = petBetterDb.getUnsyncedMessages();
        petBetterDb.closeDatabase();

        return result;
    }

    public int generateMessageId(){
        ArrayList<Integer> storedIds;
        int markerId = 1;

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        storedIds = petBetterDb.getMessageIds();
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

    public int generateMessageRepId(){
        ArrayList<Integer> storedIds;
        int markerId = 1;

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        storedIds = petBetterDb.generateMessageRepIds();
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

    private long addMessageRep(int messageRepId, int userId, int messageId, String repContent, int isSent, String datePerformed,
                               String image, int isSynced){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        long result = petBetterDb.addMessageRep(messageRepId, userId, messageId, repContent, isSent, datePerformed, image, isSynced);
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

    public ArrayList<Message> getMessages(long userId){

        //modify this method in such a way that it only gets bookmarks tagged by user. Separate from facilities.
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Message> result = petBetterDb.getMessages(userId);
        petBetterDb.closeDatabase();
        User tempUser;

        for(int i=0;i<result.size();i++){
            tempUser = getUserWithId(result.get(i).getUserId());
            if(tempUser.getUserId()==user.getUserId()){
                tempUser = getUserWithId(result.get(i).getFromId());
                result.get(i).setFromName(tempUser.getName());
            }
            else{
                tempUser = getUserWithId(result.get(i).getUserId());
                result.get(i).setFromName(tempUser.getName());
            }
        }

        return result;
    }

    private User getUserWithId(long id){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        User result = petBetterDb.getUserWithId((int) id);
        petBetterDb.closeDatabase();

        return result;
    }
}
