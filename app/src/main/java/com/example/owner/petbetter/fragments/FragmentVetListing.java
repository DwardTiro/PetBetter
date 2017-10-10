package com.example.owner.petbetter.fragments;

import android.app.Activity;
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
import android.widget.Toast;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.activities.VetProfileActivity;
import com.example.owner.petbetter.adapters.VetListingAdapter;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.android.gms.vision.text.Text;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Kristian on 8/4/2017.
 */

public class FragmentVetListing extends Fragment {

    private VetListingAdapter vetListingAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Veterinarian> vetList;
    private TextView nameTextView;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;

    private VetProfileActivity vetProfileActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_vet_listings,container, false);
        //If code above doesn't work inflate homeactivity instead.

        initializeDatabase();
        recyclerView = (RecyclerView) view.findViewById(R.id.vetListing);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        vetList = getVeterinarians();
        System.out.println("Rating is "+vetList.get(0).getRating());

        vetListingAdapter = new VetListingAdapter(getActivity(), vetList, new VetListingAdapter.OnItemClickListener() {
            @Override public void onItemClick(Veterinarian item) {

                Intent intent = new Intent(getActivity(), com.example.owner.petbetter.activities.VetProfileActivity.class);
                intent.putExtra("thisVet", new Gson().toJson(item));
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(vetListingAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        nameTextView = (TextView) view.findViewById(R.id.vetListName);

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


    public ArrayList<Veterinarian> getVeterinarians(){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Veterinarian> result = petBetterDb.getVeterinarians();
        petBetterDb.closeDatabase();
        return result;
    }


    //create a list that will be getting stuff from data and follow the video on how it will pass the data to adapter.
    public void setNameTextView(String text){
        nameTextView.setText(text);
    }

}

