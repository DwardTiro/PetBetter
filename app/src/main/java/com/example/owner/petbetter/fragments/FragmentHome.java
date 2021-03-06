package com.example.owner.petbetter.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import com.example.owner.petbetter.activities.MonitorVetsActivity;
import com.example.owner.petbetter.activities.SearchActivity;
import com.example.owner.petbetter.activities.UserActivity;
import com.example.owner.petbetter.adapters.HomeAdapter;
import com.example.owner.petbetter.adapters.MonitorAdapter;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.Topic;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.interfaces.CheckUpdates;
import com.example.owner.petbetter.interfaces.PlaceInfoListener;
import com.example.owner.petbetter.services.NotificationReceiver;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private PopupWindow popUpConfirmationWindow;
    private long topicId;
    private int type = 1;
    private boolean isLinked = false;
    private int checkType = 0;

    HerokuService service;

    public FragmentHome() {
    }

    @SuppressLint("ValidFragment")
    public FragmentHome(ArrayList<Post> postList) {
        this.postList = postList;
    }

    @SuppressLint("ValidFragment")
    public FragmentHome(ArrayList<Post> postList, boolean isLinked) {
        this.postList = postList;
        this.isLinked = isLinked;
    }

    @SuppressLint("ValidFragment")
    public FragmentHome(ArrayList<Post> postList, int checkType) {
        this.postList = postList;
        this.checkType = checkType;
    }

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

        Bundle bundle = this.getArguments();

        try{
            if(postList==null){
                if(bundle.isEmpty()){
                    postList = getPosts();
                    type = 2;
                }
                else{
                    topicId = bundle.getLong("topicId");
                    postList = getTopicPosts(topicId);
                    type = 3;
                }
            }
        }catch(NullPointerException npe){
            postList = getPosts();
            type = 2;
        }

        if(getActivity() instanceof MonitorVetsActivity){
            getAllPosts();
        }




        recyclerView = (RecyclerView) view.findViewById(R.id.postListing);

        System.out.println("Size of postList "+postList.size());

        if(isLinked){
            homeAdapter = new HomeAdapter(getActivity(), postList, user, new HomeAdapter.OnItemClickListener() {
                    @Override public void onItemClick(Post item) {

                        Intent intent = new Intent(getActivity(), com.example.owner.petbetter.activities.NewPostActivity.class);
                        //System.out.println("REQUEST CODE ID BEFORE PASSING "+item.getId());
                        //intent.putExtra("faciId", new Gson().toJson(item.getId()));
                        intent.putExtra("faciId", item.getId());
                        intent.putExtra("id_type", 4);
                        getActivity().setResult(getActivity().RESULT_OK, intent);
                        getActivity().finish();
                    }
                });
            homeAdapter.notifyItemRangeChanged(0, homeAdapter.getItemCount());
            recyclerView.setAdapter(homeAdapter);

        }
        if(getActivity() instanceof MonitorVetsActivity){
            MonitorAdapter monitorAdapter = new MonitorAdapter(getActivity(), postList, 4, new MonitorAdapter.OnItemClickListener() {
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
                    Intent intent = new Intent(getActivity(), com.example.owner.petbetter.activities.PostContentActivity.class);
                    System.out.println("PLEASE BAKIT KA GANYAN "+item.getId());
                    intent.putExtra("thisPost", new Gson().toJson(item));
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(monitorAdapter);
        }
        else{
            System.out.println("MAH NIGGA "+postList.size());
            homeAdapter = new HomeAdapter(getActivity(), postList, user, new HomeAdapter.OnItemClickListener() {
                @Override public void onItemClick(Post item) {
                    //Execute command here
                    Intent intent = new Intent(getActivity(), com.example.owner.petbetter.activities.PostContentActivity.class);
                    System.out.println("PLEASE BAKIT KA GANYAN "+item.getId());
                    intent.putExtra("thisPost", new Gson().toJson(item));
                    startActivity(intent);
                }
            });
            System.out.println("MAH NIGGA HOMIE "+homeAdapter.getItemCount());
            homeAdapter.notifyItemRangeChanged(0, homeAdapter.getItemCount());
            recyclerView.setAdapter(homeAdapter);
        }

        //homeAdapter = new HomeAdapter(getActivity(), postList);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
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

    private void initializeDatabase() {

        petBetterDb = new DataAdapter(getActivity());

        try {
            petBetterDb.createDatabase();
        } catch(SQLException e ){
            e.printStackTrace();
        }

    }

    public void getAllPosts(){
        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        System.out.println("WE HERE BOOIII");

        final Call<ArrayList<Post>> call = service.getAllPosts();
        call.enqueue(new Callback<ArrayList<Post>>() {
            @Override
            public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
                if(response.isSuccessful()){
                    postList = response.body();

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
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
        if(postList.size()!=getPosts().size()&&(!(getActivity() instanceof SearchActivity||getActivity() instanceof MonitorVetsActivity||
                getActivity() instanceof UserActivity))&&(checkType==3||checkType==4)){
            postList = getPosts();
            homeAdapter.updateList(postList);
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
