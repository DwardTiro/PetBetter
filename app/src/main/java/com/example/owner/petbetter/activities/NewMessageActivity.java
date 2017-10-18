package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.Message;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.fragments.FragmentMessages;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Kristian on 10/16/2017.
 */

public class NewMessageActivity extends AppCompatActivity {


    EditText newMsgSendTo;
    EditText newMsgContent;
    Button newMsgSendButton;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user, usertwo;
    private int mId, mrId, nId;
    private String timeStamp;
    private ArrayList<Message> mList;
    private boolean alreadyExist= false;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        Toolbar toolbar = (Toolbar) findViewById(R.id.newMsgToolbar);
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_new_message);

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

                    if(alreadyExist==true){
                        mrId = generateMessageRepId();
                        addMessageRep(mrId, (int) user.getUserId(), mId,
                                newMsgContent.getText().toString(), 1, timeStamp);

                    }
                    else{
                        createMessage(mId, user.getUserId(), usertwo.getUserId());
                        mrId = generateMessageRepId();
                        addMessageRep(mrId, (int) user.getUserId(), mId,
                                newMsgContent.getText().toString(), 1, timeStamp);

                    }

                    nId = generateNotifsId();
                    notifyMessage(nId, usertwo.getUserId(), user.getUserId(), 0, 2, timeStamp);
                    finish();
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
    private long notifyMessage(int notifId, long toId, long userId, int isRead, int type, String timeStamp){
        long  result;

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }


        result = petBetterDb.notifyMessage(notifId, toId, userId, isRead, type, timeStamp);
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
