package com.example.owner.petbetter.fragments;

import android.content.Intent;
import android.content.IntentFilter;
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
import com.example.owner.petbetter.adapters.HomeAdapter;
import com.example.owner.petbetter.classes.Marker;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.interfaces.CheckUpdates;
import com.example.owner.petbetter.services.NotificationReceiver;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class FragmentHome extends Fragment implements CheckUpdates {
    private HomeAdapter homeAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Post> postList;
    private TextView nameTextView;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    private String email;
    private NotificationReceiver notifReceiver = new NotificationReceiver(this);


    @Override
    public void onResume() {
        super.onResume();

        getActivity().registerReceiver(this.notifReceiver, new IntentFilter(Intent.ACTION_ATTACH_DATA));
        onResult();
    }

    @Override
    public void onPause() {
        super.onPause();

        getActivity().unregisterReceiver(notifReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_home,container, false);
        //If code above doesn't work inflate homeactivity instead.

        systemSessionManager = new SystemSessionManager(getActivity());
        if(systemSessionManager.checkLogin())
            getActivity().finish();
        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();


        //adapter needs to be attached
        email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);


        recyclerView = (RecyclerView) view.findViewById(R.id.postListing);
        postList = getPosts();
        System.out.println("Size of postList "+postList.size());

        homeAdapter = new HomeAdapter(getActivity(), postList, user, new HomeAdapter.OnItemClickListener() {
            @Override public void onItemClick(Post item) {
                //Execute command here
                Intent intent = new Intent(getActivity(), com.example.owner.petbetter.activities.PostContentActivity.class);
                System.out.println("PLEASE BAKIT KA GANYAN "+item.getId());
                intent.putExtra("thisPost", new Gson().toJson(item));
                startActivity(intent);
            }
        });
        //homeAdapter = new HomeAdapter(getActivity(), postList);
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

    public ArrayList<Post> getPosts(){

        //modify this method in such a way that it only gets bookmarks tagged by user. Separate from facilities.
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Post> result = petBetterDb.getPosts();
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

    @Override
    public void onResult() {
        if(postList.size()!=getPosts().size()){
            postList = getPosts();
            homeAdapter.updateList(postList);
        }
    }
}
