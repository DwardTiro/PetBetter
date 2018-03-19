package com.example.owner.petbetter.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.adapters.FollowerAdapter;
import com.example.owner.petbetter.classes.Follower;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.interfaces.CheckUpdates;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kristian on 3/17/2018.
 */

public class FragmentTopicFollowers extends Fragment implements CheckUpdates{

    private User user;
    private String email;
    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private RecyclerView recyclerView;
    private ArrayList<Follower> followerList;
    private FollowerAdapter followerAdapter;
    private long topicId;
    private HerokuService service;


    public FragmentTopicFollowers() {
    }

    @SuppressLint("ValidFragment")
    public FragmentTopicFollowers(ArrayList<Follower> followerList) {
        this.followerList = followerList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container , Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_topic_followers, container, false);

        systemSessionManager = new SystemSessionManager(getActivity());
        if(systemSessionManager.checkLogin())
            getActivity().finish();
        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();

        email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);

        recyclerView = (RecyclerView) view.findViewById(R.id.followerListing);
        followerAdapter = new FollowerAdapter(getActivity(), followerList,new FollowerAdapter.OnItemClickListener() {
            @Override public void onItemClick(Follower item) {
                //Execute command here
            }
        });
        followerAdapter.notifyItemRangeChanged(0, followerAdapter.getItemCount());
        recyclerView.setAdapter(followerAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //getPendingFollowers(topicId);

        return view;

    }

    public void getFollowers(long topicId){
        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final Call<ArrayList<Follower>> call = service.getAcceptedFollowers(topicId);
        call.enqueue(new Callback<ArrayList<Follower>>() {
            @Override
            public void onResponse(Call<ArrayList<Follower>> call, Response<ArrayList<Follower>> response) {
                if(response.isSuccessful()){
                    followerList = response.body();
                    followerAdapter = new FollowerAdapter(getActivity(), followerList, new FollowerAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Follower item) {

                        }
                    });
                    followerAdapter.notifyItemRangeChanged(0, followerAdapter.getItemCount());
                    recyclerView.setAdapter(followerAdapter);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Follower>> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
    }

    public void getPendingFollowers(long topicId){
        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final Call<ArrayList<Follower>> call2 = service.getPendingFollowers(topicId);
        call2.enqueue(new Callback<ArrayList<Follower>>() {
            @Override
            public void onResponse(Call<ArrayList<Follower>> call, Response<ArrayList<Follower>> response) {
                if(response.isSuccessful()){
                    //get back here boys
                    followerList = response.body();

                    followerAdapter = new FollowerAdapter(getActivity(), followerList,new FollowerAdapter.OnItemClickListener() {
                        @Override public void onItemClick(Follower item) {
                            //Execute command here
                        }
                    });
                    followerAdapter.notifyItemRangeChanged(0, followerAdapter.getItemCount());
                    recyclerView.setAdapter(followerAdapter);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Follower>> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());

            }
        });
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

    @Override
    public void onResult() {

    }
}
