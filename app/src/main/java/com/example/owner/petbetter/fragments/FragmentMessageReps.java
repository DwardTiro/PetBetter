package com.example.owner.petbetter.fragments;

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
import com.example.owner.petbetter.adapters.MessageRepAdapter;
import com.example.owner.petbetter.classes.Marker;
import com.example.owner.petbetter.classes.MessageRep;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by owner on 15/10/2017.
 */

public class FragmentMessageReps extends Fragment {

    private MessageRepAdapter messageRepAdapter;
    private RecyclerView recyclerView;
    private ArrayList<MessageRep> messageRepList;
    private TextView nameTextView;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    private String email;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_messagereps_listing,container, false);
        //If code above doesn't work inflate homeactivity instead.

        systemSessionManager = new SystemSessionManager(getActivity());
        if(systemSessionManager.checkLogin())
            getActivity().finish();
        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();

        email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);

        Bundle bundle = this.getArguments();
        long messageId = bundle.getLong("messageId");

        recyclerView = (RecyclerView) view.findViewById(R.id.messageRepListing);
        messageRepList = getMessageReps(messageId);
        System.out.println("Size of list "+messageRepList.size());
        messageRepAdapter = new MessageRepAdapter(getActivity(), messageRepList,new MessageRepAdapter.OnItemClickListener() {
            @Override public void onItemClick(MessageRep item) {
                //Execute command here
            }
        });
        messageRepAdapter.notifyItemRangeChanged(0, messageRepAdapter.getItemCount());
        recyclerView.setAdapter(messageRepAdapter);
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


    public ArrayList<MessageRep> getMessageReps(long messageId){

        //modify this method in such a way that it only gets bookmarks tagged by user. Separate from facilities.
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<MessageRep> result = petBetterDb.getMessageReps(messageId);
        petBetterDb.closeDatabase();
        return result;
    }
}
