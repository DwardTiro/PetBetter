package com.example.owner.petbetter.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.activities.BookmarksActivity;
import com.example.owner.petbetter.adapters.ClinicListingAdapter;
import com.example.owner.petbetter.adapters.HomeAdapter;
import com.example.owner.petbetter.classes.Bookmark;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.interfaces.CheckUpdates;
import com.example.owner.petbetter.interfaces.PlaceInfoListener;
import com.example.owner.petbetter.services.NotificationReceiver;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by owner on 7/10/2017.
 */

public class FragmentPosts extends Fragment implements CheckUpdates {

    private HomeAdapter homeAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Post> postList;
    private TextView nameTextView;

    private NotificationReceiver notifReceiver = new NotificationReceiver(this);
    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    private String email;
    private long topicId;
    //private FloatingActionButton fab;
    private boolean allowRefresh = false;
    private PopupWindow popUpConfirmationWindow;
    HerokuService service;

    public FragmentPosts() {
    }

    @SuppressLint("ValidFragment")
    public FragmentPosts(ArrayList<Post> postList) {
        this.postList = postList;
    }

    @Override
    public void onPause() {
        super.onPause();

        getActivity().unregisterReceiver(notifReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.topicContentListing);

        systemSessionManager = new SystemSessionManager(getActivity());
        if (systemSessionManager.checkLogin())
            getActivity().finish();
        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();


        //adapter needs to be attached
        email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);

        Bundle bundle = this.getArguments();

        if (postList == null && !(getActivity() instanceof BookmarksActivity)) {
            topicId = bundle.getLong("topicId");
            postList = getTopicPosts(topicId);
        }
        if (getActivity() instanceof BookmarksActivity) {
            final HerokuService bookmarkService = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
            Call<ArrayList<Post>> call = bookmarkService.getPostBookmarks(user.getUserId());
            call.enqueue(new Callback<ArrayList<Post>>() {
                @Override
                public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
                    if (response.isSuccessful() && !(response.body() == null)) {
                        ArrayList<Post> bookMarkList = response.body();
                        //faciList = response.body();
                        System.out.println("Postlist size is" + bookMarkList.size());
                        homeAdapter = new HomeAdapter(getActivity(), bookMarkList, user, new HomeAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Post item) {
                                //Execute command here
                                Intent intent = new Intent(getActivity(), com.example.owner.petbetter.activities.PostContentActivity.class);
                                System.out.println("PLEASE BAKIT KA GANYAN " + item.getId());
                                intent.putExtra("thisPost", new Gson().toJson(item));
                                startActivity(intent);
                            }
                        });
                        recyclerView.setAdapter(homeAdapter);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Post>> call, Throwable t) {

                }
            });
        }else
        {

        // System.out.println("Size of postList "+postList.size());

        homeAdapter = new HomeAdapter(getActivity(), postList, user, new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Post item) {
                //Execute command here
                Intent intent = new Intent(getActivity(), com.example.owner.petbetter.activities.PostContentActivity.class);
                System.out.println("PLEASE BAKIT KA GANYAN " + item.getId());
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
    }
        /*
        fab = (FloatingActionButton) view.findViewById(R.id.fabPosts);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), com.example.owner.petbetter.activities.NewPostActivity.class);
                intent.putExtra("thisTopicId", topicId);
                startActivity(intent);
                allowRefresh = true;
            }
        });
        */
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


        getActivity().registerReceiver(this.notifReceiver, new IntentFilter(Intent.ACTION_ATTACH_DATA));
        onResult();

        if(allowRefresh){
            System.out.println("WHEN DO WE ENTER THIS?");
            allowRefresh = false;
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }

    private void initializeDatabase() {

        petBetterDb = new DataAdapter(getActivity());

        try {
            petBetterDb.createDatabase();
        } catch(SQLException e ){
            e.printStackTrace();
        }

    }

    public ArrayList<Post> getTopicPosts(long topicId){

        //modify this method in such a way that it only gets bookmarks tagged by user. Separate from facilities.
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Post> result = petBetterDb.getTopicPosts(topicId);
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
        if(!(getActivity() instanceof BookmarksActivity)){
            if(postList.size()!=getTopicPosts(topicId).size()) {
                postList = getTopicPosts(topicId);
                homeAdapter.updateList(postList);
            }
        }
    }


    private long deletePost(long postId){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        long result = petBetterDb.deletePost(postId);
        petBetterDb.closeDatabase();

        return result;
    }

    private void dataSynced(int n){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        petBetterDb.dataSynced(n);
        petBetterDb.closeDatabase();

    }

    public void update(int position){
        postList.remove(position);
        homeAdapter.notifyDataSetChanged();
    }
}
