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
import com.example.owner.petbetter.activities.VetProfileActivity;
import com.example.owner.petbetter.adapters.PostRepAdapter;
import com.example.owner.petbetter.adapters.VetListingAdapter;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.PostRep;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.android.gms.vision.text.Text;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by owner on 14/10/2017.
 */

public class FragmentPostReps extends Fragment{
    private PostRepAdapter postRepAdapter;
    private RecyclerView recyclerView;
    private ArrayList<PostRep> postRepList, postChildList;
    private TextView nameTextView;
    private TextView emptyView;

    private User postUser;
    private Post postItem;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    private String email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_post_content_listing,container, false);
        //If code above doesn't work inflate homeactivity instead.

        /*
        String jsonMyObject;
        Bundle extras = getIntent().getExtras();
        jsonMyObject = extras.getString("thisVet");
        */
        initializeDatabase();
        Bundle bundle = this.getArguments();
        long postId = bundle.getLong("postId");

        System.out.println("POST ID IS: "+ postId);

        postItem = getPost(postId);


        systemSessionManager = new SystemSessionManager(getActivity());
        if(systemSessionManager.checkLogin())
            getActivity().finish();
        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();

        email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);

        recyclerView = (RecyclerView) view.findViewById(R.id.postRepListing);
        emptyView = (TextView) view.findViewById(R.id.emptyView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        System.out.println("topic name: "+postItem.getTopicName());
        postRepList = getPostReps(postItem.getId());
        //postChildList = getPostRepsFromParent(postItem.getId())

/*
        if (postRepList.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }*/
        if(postRepList.size()>0){
            postRepAdapter = new PostRepAdapter(getActivity(), postRepList, user, new PostRepAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(PostRep item) {
                    Intent intent = new Intent(getActivity(), com.example.owner.petbetter.activities.PostRepActivity.class);
                    intent.putExtra("thisParent", new Gson().toJson(item));
                    intent.putExtra("commentPost", new Gson().toJson(postItem));
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(postRepAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            nameTextView = (TextView) view.findViewById(R.id.vetListName);
        }

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

    public ArrayList<PostRep> getPostReps(long postId){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<PostRep> result = petBetterDb.getPostReps(postId);
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

}
