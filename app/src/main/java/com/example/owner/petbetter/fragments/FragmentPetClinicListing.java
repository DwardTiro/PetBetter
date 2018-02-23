package com.example.owner.petbetter.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.adapters.ClinicListingAdapter;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Kristian on 8/8/2017.
 */

public class FragmentPetClinicListing extends Fragment {

    private ClinicListingAdapter clinicListingAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Facility> faciList;
    private TextView nameTextView;


    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;

    public FragmentPetClinicListing() {
    }

    @SuppressLint("ValidFragment")
    public FragmentPetClinicListing(ArrayList<Facility> faciList) {
        this.faciList = faciList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view =  inflater.inflate(R.layout.fragment_petcare_listing,container, false);

        initializeDatabase();
        recyclerView = (RecyclerView) view.findViewById(R.id.petCareListing);

        if(faciList==null){
            faciList = getClinics();
        }




        //System.out.println("Rating is "+vetList.get(0).getRating());
        clinicListingAdapter = new ClinicListingAdapter(getActivity(), faciList, new ClinicListingAdapter.OnItemClickListener() {
            @Override public void onItemClick(Facility item) {

                Intent intent = new Intent(getActivity(), com.example.owner.petbetter.activities.PetClinicProfileActivity.class);
                intent.putExtra("thisClinic", new Gson().toJson(item));
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(clinicListingAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


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

    public ArrayList<Facility> getClinics(){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Facility> result = petBetterDb.getClinics();
        petBetterDb.closeDatabase();
        return result;
    }

}