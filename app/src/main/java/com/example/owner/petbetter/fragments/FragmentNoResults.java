package com.example.owner.petbetter.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.activities.CommActivity;
import com.example.owner.petbetter.activities.MessagesActivity;
import com.example.owner.petbetter.activities.TopicContentActivity;
import com.example.owner.petbetter.activities.UserActivity;
import com.example.owner.petbetter.adapters.NotificationsAdapter;
import com.example.owner.petbetter.classes.Follower;
import com.example.owner.petbetter.classes.Message;
import com.example.owner.petbetter.classes.Notifications;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.Topic;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.interfaces.CheckUpdates;
import com.example.owner.petbetter.services.NotificationReceiver;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by owner on 7/10/2017.
 */

public class FragmentNoResults extends Fragment  {

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
    HerokuService service;

    private TextView tvNoResult;
    private int type;

    public FragmentNoResults() {
    }

    @SuppressLint("ValidFragment")
    public FragmentNoResults(int type) {
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_no_results,container, false);

        systemSessionManager = new SystemSessionManager(getActivity());
        if(systemSessionManager.checkLogin())
            getActivity().finish();
        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        tvNoResult = (TextView) view.findViewById(R.id.tvNoResult);

        if(getActivity() instanceof TopicContentActivity){
            tvNoResult.setText("This topic doesn't have a post yet.");
        }

        if(getActivity() instanceof UserActivity){
            if(type==1){
                tvNoResult.setText("This user hasn't posted anything");
            }
            if(type==2){
                tvNoResult.setText("This user hasn't created a topic");
            }

        }

        if(getActivity() instanceof CommActivity){
            tvNoResult.setText("Follow topics to view posts");
        }

        if(getActivity() instanceof MessagesActivity){
            tvNoResult.setText("You haven't exchanged messages with that user");
        }

        return view;
    }

}
