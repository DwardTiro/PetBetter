package com.example.owner.petbetter.fragments;

import android.content.Intent;
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
import com.example.owner.petbetter.adapters.FaciListingAdapter;
import com.example.owner.petbetter.adapters.VetListingAdapter;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.Marker;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class FragmentFacilityListing extends Fragment {


    private FaciListingAdapter faciListingAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Marker> faciList;
    private TextView nameTextView;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    private String email;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_facility_listing, container, false);
        //If code above doesn't work inflate homeactivity instead.


        systemSessionManager = new SystemSessionManager(getActivity());
        if(systemSessionManager.checkLogin())
            getActivity().finish();
        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();

        email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);

        recyclerView = (RecyclerView) view.findViewById(R.id.faciListing);
        faciList = getBookmarks(user.getUserId(), 2);
        faciListingAdapter = new FaciListingAdapter(getActivity(), faciList,new FaciListingAdapter.OnItemClickListener() {
            @Override public void onItemClick(Marker item) {
                //Execute command here
                Intent intent = new Intent(getActivity(), com.example.owner.petbetter.activities.PointLocActivity.class);
                intent.putExtra("thisBookmark", new Gson().toJson(item));
                startActivity(intent);
            }
        });
        System.out.println("Size of faci list "+faciList.size());
        //faciListingAdapter.notifyDataSetChanged();
        //faciListingAdapter = new FaciListingAdapter(getActivity(), faciList);
        faciListingAdapter.notifyItemRangeChanged(0, faciListingAdapter.getItemCount());
        recyclerView.setAdapter(faciListingAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        System.out.println("We did this I guess");
        return view;
    }

    private void initializeDatabase() {

        petBetterDb = new DataAdapter(getActivity());

        try {
            petBetterDb.createDatabase();
        } catch (SQLException e) {
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


    public ArrayList<Marker> getBookmarks(long userId, int type){

        //modify this method in such a way that it only gets bookmarks tagged by user. Separate from facilities.
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Marker> result = petBetterDb.getBookmarks(userId, type);
        petBetterDb.closeDatabase();
        return result;
    }


    //create a list that will be getting stuff from data and follow the video on how it will pass the data to adapter.
    public void setNameTextView(String text) {
        nameTextView.setText(text);
    }
}
