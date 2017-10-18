package com.example.owner.petbetter.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.adapters.CommunityAdapter;
import com.example.owner.petbetter.adapters.HomeAdapter;
import com.example.owner.petbetter.adapters.MessageAdapter;
import com.example.owner.petbetter.classes.Message;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by owner on 19/10/2017.
 */

public class FragmentUserPosts extends Fragment {
    private HomeAdapter homeAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Post> postList;
    private TextView nameTextView;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    private String email;
    private FloatingActionButton fab;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_user_posts,container, false);
        //If code above doesn't work inflate homeactivity instead.
        System.out.println("ARE WE HERE DOE?");
        systemSessionManager = new SystemSessionManager(getActivity());
        if(systemSessionManager.checkLogin())
            getActivity().finish();
        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();

        email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);

        recyclerView = (RecyclerView) view.findViewById(R.id.userPostListing);
        //cause of error. change to where the recyclerview is.
        postList = getPostsWithUserId(user.getUserId());

        homeAdapter = new HomeAdapter(getActivity(), postList,new HomeAdapter.OnItemClickListener() {
            @Override public void onItemClick(Post item) {
                //Execute command here
                Intent intent = new Intent(getActivity(), com.example.owner.petbetter.activities.PostContentActivity.class);
                intent.putExtra("thisPost", new Gson().toJson(item));
                startActivity(intent);
            }
        });
        //messageAdapter = new MessageAdapter(getActivity(), messageList);
        homeAdapter.notifyItemRangeChanged(0, homeAdapter.getItemCount());
        recyclerView.setAdapter(homeAdapter);
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


    public ArrayList<Post> getPostsWithUserId(long userId){

        //modify this method in such a way that it only gets bookmarks tagged by user. Separate from facilities.
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Post> result = petBetterDb.getPostsWithUserId(userId);
        petBetterDb.closeDatabase();
        return result;
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

}
