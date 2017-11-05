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
import com.example.owner.petbetter.adapters.PostRepAdapter;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.PostRep;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by owner on 3/11/2017.
 */

public class FragmentCommentThread extends Fragment {

    private PostRepAdapter postRepAdapter;
    private RecyclerView recyclerView;
    private ArrayList<PostRep> postRepList, postChildList;
    private TextView nameTextView;

    private User postUser;
    private Post postItem;
    private PostRep postRepItem;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    private String email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_comment_thread,container, false);
        //If code above doesn't work inflate homeactivity instead.

        /*
        String jsonMyObject;
        Bundle extras = getIntent().getExtras();
        jsonMyObject = extras.getString("thisVet");
        */
        initializeDatabase();
        Bundle bundle = this.getArguments();
        long postRepId = bundle.getLong("postRepId");

        System.out.println("COMMENTTHREAD POSTREP ID IS: "+ postRepId);

        postRepItem = getPostRepFromId(postRepId);


        systemSessionManager = new SystemSessionManager(getActivity());
        if(systemSessionManager.checkLogin())
            getActivity().finish();
        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();

        email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);

        recyclerView = (RecyclerView) view.findViewById(R.id.comment_thread_listing);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        postRepList = getPostRepsFromParent(postRepItem.getId());
        //postChildList = getPostRepsFromParent(postItem.getId())


        if(postRepList.size()>0){
            postRepAdapter = new PostRepAdapter(getActivity(), postRepList, user, new PostRepAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(PostRep item) {
                    Intent intent = new Intent(getActivity(), com.example.owner.petbetter.activities.PostRepActivity.class);
                    intent.putExtra("thisParent", new Gson().toJson(item));
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

    public ArrayList<PostRep> getPostRepsFromParent(long postId){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<PostRep> result = petBetterDb.getPostRepsFromParent(postId);
        petBetterDb.closeDatabase();
        return result;
    }

    public PostRep getPostRepFromId(long postRepId){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        PostRep result = petBetterDb.getPostRepFromId(postRepId);
        petBetterDb.closeDatabase();

        return result;
    }
}
