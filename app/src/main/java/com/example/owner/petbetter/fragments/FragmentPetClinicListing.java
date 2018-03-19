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

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.activities.BookmarksActivity;
import com.example.owner.petbetter.activities.SearchActivity;
import com.example.owner.petbetter.activities.VeterinarianHomeActivity;
import com.example.owner.petbetter.adapters.ClinicListingAdapter;
import com.example.owner.petbetter.classes.Bookmark;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private String email;
    private ArrayList<Bookmark> bookMarkList;
    private boolean isLinked = false;
    private ArrayList<Long> bookMarkIds;

    public FragmentPetClinicListing() {
    }

    @SuppressLint("ValidFragment")
    public FragmentPetClinicListing(ArrayList<Facility> faciList) {
        this.faciList = faciList;
    }

    @SuppressLint("ValidFragment")
    public FragmentPetClinicListing(ArrayList<Facility> faciList, boolean isLinked) {
        this.faciList = faciList;
        this.isLinked = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view =  inflater.inflate(R.layout.fragment_petcare_listing,container, false);

        systemSessionManager = new SystemSessionManager(getActivity());
        if(systemSessionManager.checkLogin())
            getActivity();
        HashMap<String,String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();
        email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);
        recyclerView = (RecyclerView) view.findViewById(R.id.petCareListing);

        if(faciList==null ){
            faciList = getClinics();
        }

        if(isLinked&&(getActivity() instanceof SearchActivity)){
            clinicListingAdapter = new ClinicListingAdapter(getActivity(), faciList, new ClinicListingAdapter.OnItemClickListener() {
                @Override public void onItemClick(Facility item) {
                    Intent intent = new Intent(getActivity(), com.example.owner.petbetter.activities.NewPostActivity.class);
                    //System.out.println("REQUEST CODE ID BEFORE PASSING "+item.getId());
                    //intent.putExtra("faciId", new Gson().toJson(item.getId()));
                    intent.putExtra("faciId", item.getId());
                    getActivity().setResult(getActivity().RESULT_OK, intent);
                    getActivity().finish();
                    //startActivity(intent);
                }
            });
        }
        //System.out.println("Rating is "+vetList.get(0).getRating());
        else if(getActivity() instanceof  VeterinarianHomeActivity){
            clinicListingAdapter = new ClinicListingAdapter(getActivity(), faciList, new ClinicListingAdapter.OnItemClickListener() {
                @Override public void onItemClick(Facility item) {
                    Intent intent = new Intent(getActivity(), com.example.owner.petbetter.activities.EditFacilityActivity.class);
                    intent.putExtra("thisClinic", new Gson().toJson(item));
                    startActivity(intent);
                }
            });
        }
        else if(getActivity() instanceof BookmarksActivity){
            final HerokuService bookmarkService = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
            Call<ArrayList<Facility>> call = bookmarkService.getFacilityBookmarks(user.getUserId());
            call.enqueue(new Callback<ArrayList<Facility>>() {
                @Override
                public void onResponse(Call<ArrayList<Facility>> call, Response<ArrayList<Facility>> response) {
                    if(response.isSuccessful() && !(response.body() == null))
                    {
                        ArrayList<Facility> bookMarkList = response.body();
                        //faciList = response.body();
                        System.out.println("Facilist size is" + bookMarkList.size());
                        clinicListingAdapter = new ClinicListingAdapter(getActivity(), bookMarkList, new ClinicListingAdapter.OnItemClickListener() {
                            @Override public void onItemClick(Facility item) {
                                Intent intent = new Intent(getActivity(), com.example.owner.petbetter.activities.PetClinicProfileActivity.class);
                                intent.putExtra("thisClinic", new Gson().toJson(item));
                                startActivity(intent);
                            }
                        });
                        recyclerView.setAdapter(clinicListingAdapter);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Facility>> call, Throwable t) {

                }
            });


        }
        else if(isLinked==false){
            clinicListingAdapter = new ClinicListingAdapter(getActivity(), faciList, new ClinicListingAdapter.OnItemClickListener() {
                @Override public void onItemClick(Facility item) {

                    Intent intent = new Intent(getActivity(), com.example.owner.petbetter.activities.PetClinicProfileActivity.class);
                    intent.putExtra("thisClinic", new Gson().toJson(item));
                    startActivity(intent);
                }
            });
        }

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

}