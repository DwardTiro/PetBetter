package com.example.owner.petbetter.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.owner.petbetter.R;

/**
 * Created by owner on 7/10/2017.
 */

public class FragmentMessagesHome extends Fragment{

    private Button btnNotif;
    private Button btnInbox;
    private Button btnPosts;
    private FrameLayout myContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_messages_home,container, false);
        //If code above doesn't work inflate homeactivity instead.

        btnNotif = (Button) view.findViewById(R.id.btnNotif);
        btnInbox = (Button) view.findViewById(R.id.btnInbox);
        btnPosts = (Button) view.findViewById(R.id.btnPosts);
        myContainer = (FrameLayout) view.findViewById(R.id.fragment_child_container);

        btnNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Fragment 1");
                btnNotif.setBackgroundColor(Color.WHITE);
                btnNotif.setTextColor(Color.BLACK);
                btnInbox.setBackgroundResource(R.color.main_Color);
                btnPosts.setBackgroundResource(R.color.main_Color);


                //FragmentNotifs
                myContainer.removeAllViews();
                FragmentNotifs fragment3 = new FragmentNotifs();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.add(R.id.fragment_child_container, fragment3).commit();
                //view.getChildFragmentManager().beginTransaction().add(R.id.comm_container,fragment3).commit();
            }
        });

        btnInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Fragment 1");
                btnInbox.setBackgroundColor(Color.WHITE);
                btnInbox.setTextColor(Color.BLACK);
                btnNotif.setBackgroundResource(R.color.main_Color);
                btnPosts.setBackgroundResource(R.color.main_Color);


                //FragmentNotifs
                myContainer.removeAllViews();
                FragmentMessages fragment3 = new FragmentMessages();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.add(R.id.fragment_child_container, fragment3).commit();
                //getSupportFragmentManager().
                //view.getSupFragmentManager().beginTransaction().add(R.id.comm_container,fragment3).commit();
            }
        });

        btnPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Fragment 1");
                btnPosts.setBackgroundColor(Color.WHITE);
                btnPosts.setTextColor(Color.BLACK);
                btnInbox.setBackgroundResource(R.color.main_Color);
                btnNotif.setBackgroundResource(R.color.main_Color);


                //FragmentNotifs
                myContainer.removeAllViews();
                FragmentUserPosts fragment3 = new FragmentUserPosts();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.add(R.id.fragment_child_container, fragment3).commit();
                //view.getSupportFragmentManager().beginTransaction().add(R.id.comm_container,fragment3).commit();
            }
        });

        return view;
    }

}
