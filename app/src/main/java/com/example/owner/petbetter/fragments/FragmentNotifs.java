package com.example.owner.petbetter.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.owner.petbetter.R;
import com.example.owner.petbetter.adapters.NotificationsAdapter;
import com.example.owner.petbetter.classes.Message;
import com.example.owner.petbetter.classes.Notifications;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.Topic;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by owner on 7/10/2017.
 */

public class FragmentNotifs extends Fragment {

    private RecyclerView recyclerView;
    private NotificationsAdapter notifAdapter;
    private ArrayList<Notifications> notifList;


    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    private String email;
    private Post postItem;
    private Message messageItem;
    private Topic topicItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifs,container, false);

        systemSessionManager = new SystemSessionManager(getActivity());
        if(systemSessionManager.checkLogin())
            getActivity().finish();
        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();

        email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);

        recyclerView = (RecyclerView) view.findViewById(R.id.fragmentNotifs);

        notifList = getNotifications(user.getUserId());
        notifAdapter = new NotificationsAdapter(getActivity(), notifList,new NotificationsAdapter.OnItemClickListener() {
            @Override public void onItemClick(Notifications item) {
                //Execute command here
                notifRead(item.getId());
                if(item.getType()==1){
                    Intent intent = new Intent(getActivity(), com.example.owner.petbetter.activities.PostContentActivity.class);
                    postItem = getPost(item.getSourceId());
                    intent.putExtra("thisPost", new Gson().toJson(postItem));
                    startActivity(intent);
                }
                if(item.getType()==2){
                    Intent intent = new Intent(getActivity(), com.example.owner.petbetter.activities.MessageActivity.class);
                    messageItem = getMessage(item.getSourceId());
                    intent.putExtra("thisMessage", new Gson().toJson(messageItem));
                    startActivity(intent);
                }
                if(item.getType()==3||item.getType()==4){
                    Intent intent = new Intent(getActivity(), com.example.owner.petbetter.activities.TopicContentActivity.class);
                    topicItem = getTopic(item.getSourceId());
                    intent.putExtra("thisTopic", new Gson().toJson(topicItem));
                    startActivity(intent);
                }

                System.out.println("Yay you clicked a notif");
            }
        });
        notifAdapter.notifyItemRangeChanged(0, notifAdapter.getItemCount());
        recyclerView.setAdapter(notifAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    private void initializeDatabase() {

        petBetterDb = new DataAdapter(getActivity());

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


    public ArrayList<Notifications> getNotifications(long userId){

        //modify this method in such a way that it only gets bookmarks tagged by user. Separate from facilities.
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Notifications> result = petBetterDb.getNotifications(userId);
        petBetterDb.closeDatabase();
        return result;
    }

    public long notifRead(long notifId){

        //modify this method in such a way that it only gets bookmarks tagged by user. Separate from facilities.
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        long result = petBetterDb.notifRead(notifId);
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
}
