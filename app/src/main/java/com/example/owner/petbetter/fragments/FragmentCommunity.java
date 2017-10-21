package com.example.owner.petbetter.fragments;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.adapters.BookmarkListingAdapter;
import com.example.owner.petbetter.adapters.CommunityAdapter;
import com.example.owner.petbetter.classes.Marker;
import com.example.owner.petbetter.classes.Topic;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class FragmentCommunity extends Fragment {
    private CommunityAdapter communityAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Topic> topicList;
    private TextView nameTextView;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    private String email;
    private FloatingActionButton fab;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_community,container, false);
        //If code above doesn't work inflate homeactivity instead.

        systemSessionManager = new SystemSessionManager(getActivity());
        if(systemSessionManager.checkLogin())
            getActivity().finish();
        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();

        email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);


        recyclerView = (RecyclerView) view.findViewById(R.id.topicListing);
        topicList = getTopics();
        for(int i = 0;i<topicList.size();i++){
            topicList.get(i).setFollowerCount(getFollowerCount((int) topicList.get(i).getId()));
        }
        System.out.println("Size of postList "+topicList.size());

        communityAdapter = new CommunityAdapter(getActivity(), topicList, user, new CommunityAdapter.OnItemClickListener() {
            @Override public void onItemClick(Topic item) {

                Intent intent = new Intent(getActivity(), com.example.owner.petbetter.activities.TopicContentActivity.class);
                intent.putExtra("thisTopic", new Gson().toJson(item));
                startActivity(intent);

            }
        });
        //homeAdapter = new HomeAdapter(getActivity(), postList);
        communityAdapter.notifyItemRangeChanged(0, communityAdapter.getItemCount());
        recyclerView.setAdapter(communityAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fab = (FloatingActionButton) view.findViewById(R.id.fabCom);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), com.example.owner.petbetter.activities.NewTopicActivity.class);
                startActivity(intent);
            }
        });

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

    private ArrayList<Topic> getTopics(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Topic> result = petBetterDb.getTopics();
        petBetterDb.closeDatabase();

        return result;
    }

    private int getFollowerCount(int topicId){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        int result = petBetterDb.getFollowerCount(topicId);
        petBetterDb.closeDatabase();

        return result;
    }
}
