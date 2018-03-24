package com.example.owner.petbetter.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.adapters.MonitorAdapter;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.Topic;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by owner on 24/3/2018.
 */

public class FragmentUser extends android.support.v4.app.Fragment{
    private MonitorAdapter monitorAdapter;
    private RecyclerView recyclerView;
    private ArrayList<User> userList;
    private TextView nameTextView;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    private boolean isLinked;


    public FragmentUser() {
    }

    @SuppressLint("ValidFragment")
    public FragmentUser(ArrayList<User> userList) {
        this.userList = userList;
    }

    @SuppressLint("ValidFragment")
    public FragmentUser(ArrayList<User> userList, boolean isLinked) {
        this.userList = userList;
        this.isLinked = isLinked;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_vet_listings,container, false);
        //If code above doesn't work inflate homeactivity instead.

        initializeDatabase();
        recyclerView = (RecyclerView) view.findViewById(R.id.vetListing);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(userList ==null){
            userList = getUsers();
        }



        monitorAdapter = new MonitorAdapter(getActivity(), userList, 1, new MonitorAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(User item) {

            }

            @Override
            public void onItemClick(Facility item) {

            }

            @Override
            public void onItemClick(Topic item) {

            }

            @Override
            public void onItemClick(Post item) {

            }
        });
        recyclerView.setAdapter(monitorAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        nameTextView = (TextView) view.findViewById(R.id.vetListName);

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

    public int generateVetId(){
        ArrayList<Integer> storedIds;
        int vetId = 1;

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        storedIds = petBetterDb.getVetIds();
        petBetterDb.closeDatabase();


        if(storedIds.isEmpty()) {
            return vetId;
        } else {
            while (storedIds.contains(vetId)){
                vetId += 1;
            }

            return vetId;
        }
    }


    public ArrayList<User> getUsers(){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<User> result = petBetterDb.getUsers();
        petBetterDb.closeDatabase();
        return result;
    }


    //create a list that will be getting stuff from data and follow the video on how it will pass the data to adapter.
    public void setNameTextView(String text){
        nameTextView.setText(text);
    }
}
