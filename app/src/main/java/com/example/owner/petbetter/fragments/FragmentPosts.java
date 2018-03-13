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
import com.example.owner.petbetter.adapters.HomeAdapter;
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

public class FragmentPosts extends Fragment implements CheckUpdates, PlaceInfoListener {

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
        View view = inflater.inflate(R.layout.fragment_posts,container, false);

        systemSessionManager = new SystemSessionManager(getActivity());
        if(systemSessionManager.checkLogin())
            getActivity().finish();
        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();


        //adapter needs to be attached
        email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);

        Bundle bundle = this.getArguments();

        if(postList==null){
            topicId = bundle.getLong("topicId");
            postList = getTopicPosts(topicId);
        }


        recyclerView = (RecyclerView) view.findViewById(R.id.topicContentListing);


        System.out.println("Size of postList "+postList.size());

        homeAdapter = new HomeAdapter(getActivity(), postList, user, new HomeAdapter.OnItemClickListener() {
            @Override public void onItemClick(Post item) {
                //Execute command here
                Intent intent = new Intent(getActivity(), com.example.owner.petbetter.activities.PostContentActivity.class);
                System.out.println("PLEASE BAKIT KA GANYAN "+item.getId());
                intent.putExtra("thisPost", new Gson().toJson(item));
                startActivity(intent);
            }
        }, this);
        //homeAdapter = new HomeAdapter(getActivity(), postList);
        homeAdapter.notifyItemRangeChanged(0, homeAdapter.getItemCount());
        recyclerView.setAdapter(homeAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
        if(postList.size()!=getTopicPosts(topicId).size()){
            postList = getTopicPosts(topicId);
            homeAdapter.updateList(postList);

        }
    }

    @Override
    public void onPopupMenuClicked(final View view, final int pos) {
        final Post thisPost = postList.get(pos);
        PopupMenu options = new PopupMenu(this.getContext(), view);
        MenuInflater inflater = options.getMenuInflater();
        inflater.inflate(R.menu.post_topic_menu, options.getMenu());
        System.out.println("Options menu clicked in posts");

        options.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.menu_edit_post_topic:
                        return true;
                    case R.id.menu_delete_post_topic:
                        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View popUpConfirmation = inflater.inflate(R.layout.popup_confirmation_delete_post, null);

                        popUpConfirmation.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

                        popUpConfirmationWindow = new PopupWindow(popUpConfirmation, 750, 360, true);
                        popUpConfirmationWindow.showAtLocation(popUpConfirmation, Gravity.CENTER, 0, 0);

                        Button cancelButton = (Button) popUpConfirmationWindow.getContentView().findViewById(R.id.popUpPostCancelButton);

                        Button deleteButton = (Button) popUpConfirmationWindow.getContentView().findViewById(R.id.popUpPostDeleteButton);
                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popUpConfirmationWindow.dismiss();
                            }
                        });
                        deleteButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                deletePost(thisPost.getId());

                                service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
                                final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

                                final Call<Void> call = service.deletePost(thisPost.getUserId(), thisPost.getDateCreated());
                                call.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        //User thisUser = response.body();
                                        if(response.isSuccessful()){
                                            dataSynced(9);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Log.d("onFailure", t.getLocalizedMessage());
                                    }
                                });

                                update(pos);
                                popUpConfirmationWindow.dismiss();
                            }
                        });
                        return true;
                    default:
                        return false;
                }
            }
        });

        options.show();
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
