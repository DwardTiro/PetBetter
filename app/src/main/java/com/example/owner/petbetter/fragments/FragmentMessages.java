package com.example.owner.petbetter.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.activities.MessagesActivity;
import com.example.owner.petbetter.adapters.FollowerAdapter;
import com.example.owner.petbetter.adapters.MessageAdapter;
import com.example.owner.petbetter.adapters.MessageRequestAdapter;
import com.example.owner.petbetter.classes.Message;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.interfaces.CheckUpdates;
import com.example.owner.petbetter.services.NotificationReceiver;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class FragmentMessages extends Fragment implements CheckUpdates {

    private MessageAdapter messageAdapter;
    private MessageRequestAdapter messageRequestAdapter;
    private FollowerAdapter followerAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Message> messageList;
    private TextView nameTextView;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    private String email;
    private FloatingActionButton fab;
    private boolean allowRefresh = false;
    private int task = 0;
    private NotificationReceiver notifReceiver = new NotificationReceiver(this);

    public FragmentMessages() {
    }

    @SuppressLint("ValidFragment")
    public FragmentMessages(ArrayList<Message> messageList, int task) {
        this.messageList = messageList;
        this.task = task;
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("Do we pause??");
        getActivity().unregisterReceiver(notifReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_messages,container, false);
        //If code above doesn't work inflate homeactivity instead.
        System.out.println("ARE WE HERE DOE?");
        systemSessionManager = new SystemSessionManager(getActivity());
        if(systemSessionManager.checkLogin())
            getActivity().finish();
        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();

        //receiver
        getActivity().registerReceiver(this.notifReceiver, new IntentFilter(Intent.ACTION_ATTACH_DATA));

        email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);

        recyclerView = (RecyclerView) view.findViewById(R.id.messagesListing);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        //cause of error. change to where the recyclerview is.

        if(messageList==null){
            messageList = getMessages(user.getUserId());
        }

        System.out.println("Size of list "+messageList.size());
        if(task==2){
            messageRequestAdapter = new MessageRequestAdapter(getActivity(), messageList,new MessageRequestAdapter.OnItemClickListener() {
                @Override public void onItemClick(Message item) {
                    //Execute command here
                }
            });
            recyclerView.setAdapter(messageRequestAdapter);
        }
        else{
            messageAdapter = new MessageAdapter(getActivity(), messageList,new MessageAdapter.OnItemClickListener() {
                @Override public void onItemClick(Message item) {
                    //Execute command here
                    Intent intent = new Intent(getActivity(), com.example.owner.petbetter.activities.MessageActivity.class);
                    System.out.println("Item: "+item.getMessageContent());
                    intent.putExtra("thisMessage", new Gson().toJson(item));
                    startActivity(intent);
                    allowRefresh = true;
                }
            });
            recyclerView.setAdapter(messageAdapter);
        }

        //messageAdapter = new MessageAdapter(getActivity(), messageList);
        //messageAdapter.notifyItemRangeChanged(0, messageAdapter.getItemCount());

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //getActivity().registerReceiver(this.notifReceiver, new IntentFilter(Intent.ACTION_ATTACH_DATA));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), com.example.owner.petbetter.activities.NewMessageActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(this.notifReceiver, new IntentFilter(Intent.ACTION_ATTACH_DATA));
        System.out.println("Do we resume??");
        onResult();
        /*
        if(allowRefresh){
            System.out.println("WHEN DO WE ENTER THIS?");
            allowRefresh = false;
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }*/
    }

    private void initializeDatabase() {

        petBetterDb = new DataAdapter(getActivity());

        try {
            petBetterDb.createDatabase();
        } catch(SQLException e ){
            e.printStackTrace();
        }

    }

    public ArrayList<Message> getMessages(long userId){

        //modify this method in such a way that it only gets bookmarks tagged by user. Separate from facilities.
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Message> result = petBetterDb.getMessages(userId);
        petBetterDb.closeDatabase();
        User tempUser;

        for(int i=0;i<result.size();i++){
            tempUser = getUserWithId(result.get(i).getUserId());
            if(tempUser.getUserId()==user.getUserId()){
                tempUser = getUserWithId(result.get(i).getFromId());
                result.get(i).setFromName(tempUser.getName());
            }
            else{
                tempUser = getUserWithId(result.get(i).getUserId());
                result.get(i).setFromName(tempUser.getName());
            }
        }

        return result;
    }

    private User getUserWithId(long id){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        User result = petBetterDb.getUserWithId((int) id);
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
        if(task!=1&&messageAdapter!=null) {

            messageList = getMessages(user.getUserId());
            messageAdapter.updateList(messageList);
            //messageAdapter.notifyDataSetChanged();
            System.out.println("task = "+ task);
        }
    }
}
