package com.example.owner.petbetter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.owner.petbetter.R;
import com.example.owner.petbetter.adapters.NotificationsAdapter;
import com.example.owner.petbetter.classes.Notifications;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by owner on 7/10/2017.
 */

public class FragmentNotifs extends Fragment {

    private RecyclerView recyclerView;
    private NotificationsAdapter notificationsAdapter;
    private ArrayList<Notifications> notifs;
    private ImageView notifProfilePicture;
    private TextView notifProfileName;
    private TextView notifPostTitle;
    private TextView notifTimeStamp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifs,container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.fragmentNotifs);
        notificationsAdapter = new NotificationsAdapter(getActivity(),getData());
        recyclerView.setAdapter(notificationsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    public static ArrayList<Notifications> getData(){

        ArrayList<Notifications> data = new ArrayList<>();
        int[] icons = {R.drawable.app_icon,R.drawable.app_icon};
        String[] names = {"John Ivanhoe", "Christ Mas"};
        String[] title = {"My Pet is a Dog.", "I love cats."};
        String[] timestamp = {Calendar.getInstance().getTime().toString(),Calendar.getInstance().getTime().toString()};

        for(int i = 0; i< icons.length && i< names.length && i<title.length&& i<timestamp.length; i++){

            Notifications notif = new Notifications();
            notif.setNotifProfilePic(icons[i]);
            notif.setNotifProfileName(names[i]);
            notif.setNotifTimeStamp(timestamp[i]);
            notif.setNotifPostTitle(title[i]);

            data.add(notif);
        }

        return data;
    }

}
