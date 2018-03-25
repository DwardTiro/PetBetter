package com.example.owner.petbetter.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.adapters.BookmarkListingAdapter;
import com.example.owner.petbetter.adapters.PendingAdapter;
import com.example.owner.petbetter.classes.LocationMarker;
import com.example.owner.petbetter.classes.Pending;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


public class FragmentPending extends Fragment{

    private PendingAdapter pendingAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Pending> pendingList;
    private ArrayList<Pending> newPendingList = new ArrayList<Pending>();
    private TextView nameTextView;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    private String email;
    private int type = 0;

    public FragmentPending() {
    }

    @SuppressLint("ValidFragment")
    public FragmentPending(ArrayList<Pending> pendingList) {
        this.pendingList = pendingList;
    }

    @SuppressLint("ValidFragment")
    public FragmentPending(ArrayList<Pending> pendingList, int type) {
        this.pendingList = pendingList;
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_pending_listing,container, false);
        //If code above doesn't work inflate homeactivity instead.

        systemSessionManager = new SystemSessionManager(getActivity());
        if(systemSessionManager.checkLogin())
            getActivity().finish();
        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();

        email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);


        if(type!=0){
            for(Pending pending:pendingList){
                if(pending.getType()==type){
                    newPendingList.add(pending);
                }
            }
        }


        recyclerView = (RecyclerView) view.findViewById(R.id.pendingListing);
        //pendingList = getBookmarks(user.getUserId(), 1);
        //System.out.println("Size of list "+pendingList.size());
        pendingAdapter = new PendingAdapter(getActivity(), newPendingList,new PendingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Pending item) {

            }
        });
        pendingAdapter.notifyItemRangeChanged(0, pendingAdapter.getItemCount());
        recyclerView.setAdapter(pendingAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        System.out.println("We did this I guess");
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


    public ArrayList<LocationMarker> getBookmarks(long userId, int type){

        //modify this method in such a way that it only gets bookmarks tagged by user. Separate from facilities.
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<LocationMarker> result = petBetterDb.getBookmarks(userId, type);
        petBetterDb.closeDatabase();
        return result;
    }


    //create a list that will be getting stuff from data and follow the video on how it will pass the data to adapter.

}
