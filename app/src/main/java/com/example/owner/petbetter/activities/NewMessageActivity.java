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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.example.owner.petbetter.classes.Notifications;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.fragments.FragmentMessages;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
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

import static com.example.owner.petbetter.R.layout.simple_list_item_1;

/**
 * Created by Kristian on 10/16/2017.
 */

public class NewMessageActivity extends AppCompatActivity {


    private AutoCompleteTextView newMsgSendTo;
    EditText newMsgContent;
    Button newMsgSendButton;
    private ImageButton newMsgAddPhoto;
    private static final int IMG_REQUEST = 777;
    private Bitmap bitmap;
    private ArrayAdapter<String> adapterSuggestions;
    private ArrayList<String> suggestions;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user, usertwo;
    private int mId, mrId, nId;
    private String timeStamp;
    private ArrayList<Message> mList;
    private boolean alreadyExist= false;
    private ImageView addTopic;
    private String image;
    HerokuService service;
    HerokuService service2;
    HerokuService service3;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_new_message);

        Toolbar toolbar = (Toolbar) findViewById(R.id.viewPostToolbar);
        setSupportActionBar(toolbar);
        final TextView activityTitle = (TextView) findViewById(R.id.activity_title);
        addTopic = (ImageView) findViewById(R.id.topicNewPost);
        addTopic.setVisibility(View.GONE);
        activityTitle.setText("New Message");
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        systemSessionManager = new SystemSessionManager(this);

        if(systemSessionManager.checkLogin())
            finish();

        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();

        String email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);

        syncUsers();
        newMsgSendTo = (AutoCompleteTextView) findViewById(R.id.newMsgSendTo);
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
                image = imageToString();
                User check = getUser(newMsgSendTo.getText().toString());
                if(newMsgContent.getText().toString()!=""&&newMsgSendTo.getText().toString()!=""&&
                        check.getUserId()!=user.getUserId()){
                    usertwo = getUser(newMsgSendTo.getText().toString());

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                    timeStamp = sdf.format(new Date());


                    service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
                    final Call<ArrayList<Message>> call2 = service2.getMessages(user.getUserId());
                    call2.enqueue(new Callback<ArrayList<Message>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
                            if(response.isSuccessful()){
                                System.out.println("response size messages "+response.body().size());
                                mList = response.body();
                                setMessages(response.body());

                                for(Message message : mList){
                                    if(message.getUserId()==user.getUserId()&&message.getFromId()==usertwo.getUserId()||
                                            message.getUserId()==usertwo.getUserId()&&message.getFromId()==user.getUserId()){
                                        alreadyExist = true;
                                    }
                                }
                                if(alreadyExist==true){
                                    mrId = generateMessageRepId();
                                    addMessageRep(mrId, (int) usertwo.getUserId(),(int) user.getUserId(), mId,
                                            newMsgContent.getText().toString(), 1, timeStamp, image, 0);
                                    //uploadMessageRep(getUnsyncedMessageReps());
                                    syncMessageRepChanges();
                                    System.out.println("We go here wrong?");
                                }
                                if(alreadyExist==false){
                                    createMessage(mId, user.getUserId(), usertwo.getUserId());
                                    //uploadMessage(getUnsyncedMessages());
                                    syncMessageChanges(user.getUserId());

                                }

                                nId = generateNotifsId();
                                notifyMessage(nId, usertwo.getUserId(), user.getUserId(), 0, 2, timeStamp, mId, 0);
                                uploadNotifications(getUnsyncedNotifications());

                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Message>> call, Throwable t) {
                            Log.d("onFailure", t.getLocalizedMessage());

                            String image = imageToString();
                            createMessage(mId, user.getUserId(), usertwo.getUserId());
                            //uploadMessage(getUnsyncedMessages());
                            syncMessageChanges(user.getUserId());
                            /*
                            mrId = generateMessageRepId();
                            System.out.println("MESSAGE ID PAR "+mId);
                            addMessageRep(mrId, (int) usertwo.getUserId(),(int) user.getUserId(), mId,
                                    newMsgContent.getText().toString(), 1, timeStamp, image, 0);
                            //uploadMessageRep(getUnsyncedMessageReps());
                            syncMessageRepChanges();
                            */
                            finish();
                        }
                    });
                }
                else{
                    if(check==null){
                        Toast.makeText(NewMessageActivity.this, "That user does not exist.", Toast.LENGTH_SHORT);
                    }
                    else{
                        Toast.makeText(NewMessageActivity.this, "Make sure that all texts are filled.", Toast.LENGTH_SHORT);
                    }
                }
            }
        });

        service3 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        /*

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson("");

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        */
        final Call<ArrayList<String>> call = service3.queryEmail();
        call.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                suggestions = response.body();

                adapterSuggestions = new ArrayAdapter<String>(NewMessageActivity.this, simple_list_item_1, suggestions);
                //ArrayAdapter<Veterinarian> adapter = new ArrayAdapter<Veterinarian>(this,R.layout.,vetList);
                newMsgSendTo.setThreshold(1);
                newMsgSendTo.setAdapter(adapterSuggestions);
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                Log.d("onFailure query", t.getLocalizedMessage());
                Toast.makeText(NewMessageActivity.this, "Unable to get vets from server", Toast.LENGTH_LONG);
            }
        });
    }

    public void syncUsers(){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        final Call<ArrayList<User>> call = service.getUsers();
        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if(response.isSuccessful()){
                    setUsers(response.body());
                    dataSynced(9);

                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
    }

    public long setUsers(ArrayList<User> userList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setUsers(userList);
        petBetterDb.closeDatabase();

        return result;
    }

    public void syncMessageChanges(final long userId){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        ArrayList<Message> unsyncedMessages = getUnsyncedMessages();
        System.out.println("UNSYNCED MESSAGES: "+unsyncedMessages.size());
        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(unsyncedMessages);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addMessages(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    System.out.println("MESSAGES ADDED YEY");
                    dataSynced(5);

                    final Call<ArrayList<Message>> call2 = service2.getMessages(userId);
                    call2.enqueue(new Callback<ArrayList<Message>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
                            if(response.isSuccessful()){
                                System.out.println("response size messages "+response.body().size());
                                setMessages(response.body());
                                mId = (int) response.body().get(response.body().size()-1).getId();
                                mrId = generateMessageRepId();
                                System.out.println("MESSAGE ID PAR "+mId);
                                addMessageRep(mrId, (int) usertwo.getUserId(),(int) user.getUserId(), mId,
                                        newMsgContent.getText().toString(), 1, timeStamp, image, 0);
                                //uploadMessageRep(getUnsyncedMessageReps());
                                syncMessageRepChanges();
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Message>> call, Throwable t) {
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

    public long setMessages(ArrayList<Message> messageList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setMessages(messageList);
        petBetterDb.closeDatabase();

        return result;
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

    public void syncMessageRepChanges(){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        ArrayList<MessageRep> unsyncedMessages = getUnsyncedMessageReps();
        System.out.println("UNSYNCED MESSAGEREPS: "+unsyncedMessages.size());
        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(unsyncedMessages);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addMessageReps(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    System.out.println("MESSAGEREPS ADDED YEY");
                    dataSynced(6);

                    final Call<ArrayList<MessageRep>> call2 = service2.getMessageReps();
                    call2.enqueue(new Callback<ArrayList<MessageRep>>() {
                        @Override
                        public void onResponse(Call<ArrayList<MessageRep>> call, Response<ArrayList<MessageRep>> response) {
                            if(response.isSuccessful()){
                                System.out.println("response size messagereps "+response.body().size());
                                setMessageReps(response.body());
                                System.out.println("EYY REP: "+response.body().get(6).getRepContent()+" "
                                        +response.body().get(6).getMessagePhoto());
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<MessageRep>> call, Throwable t) {
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

    public long setMessageReps(ArrayList<MessageRep> messageRepList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setMessageReps(messageRepList);
        petBetterDb.closeDatabase();

        return result;
    }

    private void uploadNotifications(ArrayList<Notifications> notifications){
        //herokuservice
        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(notifications);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addNotifications(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                dataSynced(7);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
                Toast.makeText(NewMessageActivity.this, "Unable to upload notification on server", Toast.LENGTH_LONG);
            }
        });

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
                if(bitmap.getHeight()>250||bitmap.getWidth()>250){
                    bitmap = Bitmap.createScaledBitmap(bitmap,250,250,false);
                }

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
    private long notifyMessage(int notifId, long toId, long userId, int isRead, int type, String timeStamp, int mId, int isSynced){
        long  result;

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }


        result = petBetterDb.notifyUser(notifId, toId, userId, isRead, type, timeStamp, mId, isSynced);
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

    private long addMessageRep(int messageRepId, int userId, int senderId, int messageId, String repContent, int isSent, String datePerformed,
                               String image, int isSynced){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        long result = petBetterDb.addMessageRep(messageRepId, userId, senderId, messageId, repContent, isSent, datePerformed, image, isSynced);
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
