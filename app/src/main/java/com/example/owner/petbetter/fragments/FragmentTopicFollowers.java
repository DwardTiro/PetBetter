package com.example.owner.petbetter.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.interfaces.CheckUpdates;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by Kristian on 3/17/2018.
 */

public class FragmentTopicFollowers extends Fragment implements CheckUpdates{

    private User user;
    private String email;
    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private RecyclerView recyclerView;
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

    @Override
    public void onResult() {

    }
}
