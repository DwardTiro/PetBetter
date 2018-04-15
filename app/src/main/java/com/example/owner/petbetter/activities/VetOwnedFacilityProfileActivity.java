package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.adapters.HoursAdapter;
import com.example.owner.petbetter.adapters.ServiceAdapter;
import com.example.owner.petbetter.adapters.VetListingAdapter;
import com.example.owner.petbetter.adapters.VetRowAdapter;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.LocationMarker;
import com.example.owner.petbetter.classes.Pending;
import com.example.owner.petbetter.classes.Services;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.classes.WorkHours;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.owner.petbetter.ServiceGenerator.BASE_URL;

/**
 * Created by Kristian on 3/28/2018.
 */

public class VetOwnedFacilityProfileActivity extends AppCompatActivity{

    private ImageButton addNewTopic;
    private TextView facilityName;
    private TextView facilityRating;
    private TextView facilityAddress;
    private TextView facilityLandline;
    private TextView faciPage;
    private TextView faciWebsite;
    private TextView faciEmail;
    private TextView noServicesTextView;
    private ImageView facilityImage;
    private RecyclerView serviceRecyclerView;
    private Button editFacilityProfileButton;
    private Button editServicesButton;
    private Button addServicesButton;
    private Facility faciItem;
    private ArrayList<Services> serviceList;
    private ImageView verifiedServices;
    private ArrayList<Veterinarian> vetList;
    private RecyclerView vetRecyclerView;
    private VetListingAdapter vetListingAdapter;
    private ArrayList<WorkHours> hoursList;
    private RecyclerView hoursRecyclerView;


    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_vet_owned_facility);

        Bundle extras = getIntent().getExtras();
        String jsonMyObject = extras.getString("thisClinic");
        faciItem = new Gson().fromJson(jsonMyObject, Facility.class);

        //set up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.viewPostToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final TextView toolbarTitle = (TextView) findViewById(R.id.activity_title);
        toolbarTitle.setText(faciItem.getFaciName());

        addNewTopic = (ImageButton) findViewById(R.id.topicNewPost);
        addNewTopic.setVisibility(View.GONE);

        //set up content
        facilityName = (TextView) findViewById(R.id.clinicName);
        facilityRating = (TextView) findViewById(R.id.clinicRatingNumerator);
        facilityAddress = (TextView) findViewById(R.id.addressTextField);
        facilityLandline = (TextView) findViewById(R.id.phoneNumTextField);
        faciPage = (TextView) findViewById(R.id.faciPageTextField);
        faciWebsite = (TextView) findViewById(R.id.faciWebsiteTextField);
        faciEmail = (TextView) findViewById(R.id.faciEmailTextField);

        facilityImage = (ImageView) findViewById(R.id.clinicProfileImage);

        facilityAddress.setText(faciItem.getLocation());
        noServicesTextView = (TextView) findViewById(R.id.noServicesTextView);
        serviceRecyclerView = (RecyclerView) findViewById(R.id.servicesRecyclerView);
        hoursRecyclerView = (RecyclerView) findViewById(R.id.hoursRecyclerView);
        vetRecyclerView = (RecyclerView) findViewById(R.id.vetRecyclerView);
        editFacilityProfileButton = (Button) findViewById(R.id.editFacilityButton);
        editServicesButton = (Button) findViewById(R.id.editServicesButton);
        addServicesButton = (Button) findViewById(R.id.addServicesButton);
        verifiedServices = (ImageView) findViewById(R.id.verifiedServices);

        verifiedServices.setVisibility(View.VISIBLE);
        editServicesButton.setVisibility(View.GONE);

        if (faciItem.getFaciPhoto() != null) {
            String newFileName = BASE_URL + faciItem.getFaciPhoto();
            System.out.println("FACI PHOTO " + faciItem.getFaciPhoto());
            //String newFileName = "http://192.168.0.19/petbetter/"+thisMessageRep.getMessagePhoto();
            Glide.with(VetOwnedFacilityProfileActivity.this).load(newFileName).error(R.drawable.petbetter_logo_final_final).into(facilityImage);
        }

        editFacilityProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VetOwnedFacilityProfileActivity.this, com.example.owner.petbetter.activities.EditFacilityActivity.class);
                intent.putExtra("thisClinic", new Gson().toJson(faciItem));
                startActivity(intent);
            }
        });

        editServicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VetOwnedFacilityProfileActivity.this, com.example.owner.petbetter.activities.EditVetServicesActivity.class);
                intent.putExtra("thisClinic", new Gson().toJson(faciItem));
                startActivity(intent);
            }
        });
        addServicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VetOwnedFacilityProfileActivity.this, com.example.owner.petbetter.activities.AddServicesActivity.class);
                intent.putExtra("thisClinic", new Gson().toJson(faciItem));
                startActivity(intent);
            }
        });

        facilityName.setText(faciItem.getFaciName());
        facilityRating.setText(Float.toString(faciItem.getRating()));

        getWorkhoursList();
        getServiceList();
        getVetList();
    }

    @Override
    public void onResume(){
        super.onResume();
        //serviceRecyclerView = (RecyclerView) findViewById(R.id.servicesRecyclerView);
        //hoursRecyclerView = (RecyclerView) findViewById(R.id.hoursRecyclerView);

        getWorkhoursList();
        getServiceList();
        getVetList();

        facilityName = (TextView) findViewById(R.id.clinicName);
        facilityRating = (TextView) findViewById(R.id.clinicRatingNumerator);
        facilityAddress = (TextView) findViewById(R.id.addressTextField);


        String[] contactArray = faciItem.getContactInfo().split(",", -1);


        facilityAddress.setText(faciItem.getLocation());
        if(contactArray.length>0&&contactArray[0].length()>0){
            facilityLandline.setText("Phone Number: "+contactArray[0]);
        }
        if(contactArray.length>1&&contactArray[1].length()>0){
            faciPage.setText("Facebook Page: "+contactArray[1]);
        }
        if(contactArray.length>2&&contactArray[2].length()>0){
            faciWebsite.setText("Website: "+contactArray[2]);
        }
        if(contactArray.length>3&&contactArray[3].length()>0){
            faciEmail.setText("Email: "+contactArray[3]);
        }

    }


    public void getWorkhoursList() {

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final Call<ArrayList<WorkHours>> call = service.getWorkhoursWithFaciID(faciItem.getId());
        //pendingCtr = 0;

        call.enqueue(new Callback<ArrayList<WorkHours>>() {
            @Override
            public void onResponse(Call<ArrayList<WorkHours>> call, Response<ArrayList<WorkHours>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        hoursList = response.body();
                        System.out.println("NUMBER OF WORK HOURS: "+hoursList.size());
                        hoursRecyclerView.setVisibility(View.VISIBLE);
                        hoursRecyclerView.setAdapter(new HoursAdapter(VetOwnedFacilityProfileActivity.this,
                                getLayoutInflater(), hoursList));
                        hoursRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        hoursRecyclerView.setHasFixedSize(true);
                        hoursRecyclerView.setLayoutManager(new LinearLayoutManager(VetOwnedFacilityProfileActivity.this));
                        /*
                        serviceList = response.body();
                        serviceRecyclerView.setVisibility(View.VISIBLE);
                        noServicesTextView.setVisibility(View.GONE);
                        serviceRecyclerView.setAdapter(new ServiceAdapter(PetClinicProfileActivity.this, getLayoutInflater(), response.body()));
                        if(serviceRecyclerView.getAdapter() == null){
                            System.out.println("No adapter bruh");
                        }
                        serviceRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        serviceRecyclerView.setHasFixedSize(true);
                        serviceRecyclerView.setLayoutManager(new LinearLayoutManager(PetClinicProfileActivity.this));
                        */
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<WorkHours>> call, Throwable t) {
                Log.d("onFailure HOURS ", t.getLocalizedMessage());
            }
        });

    }

    public void onFacilityLocationClicked(View view) {
        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        final Call<LocationMarker> call = service.getMarkerWithFaciId(faciItem.getId());
        call.enqueue(new Callback<LocationMarker>() {
            @Override
            public void onResponse(Call<LocationMarker> call, Response<LocationMarker> response) {
                if(response.isSuccessful()){
                    LocationMarker location = response.body();
                    Bundle extras = new Bundle();
                    extras.putString("bldg_name", location.getBldgName());
                    extras.putDouble("latitude", location.getLatitude());
                    extras.putDouble("longitude", location.getLongitude());

                    Intent intent = new Intent(VetOwnedFacilityProfileActivity.this, com.example.owner.petbetter.activities.ShowLocationActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<LocationMarker> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
    }

    public void getVetList() {

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final Call<ArrayList<Veterinarian>> call = service.getVetsByFacility(faciItem.getId());

        call.enqueue(new Callback<ArrayList<Veterinarian>>() {
            @Override
            public void onResponse(Call<ArrayList<Veterinarian>> call, Response<ArrayList<Veterinarian>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        System.out.println("hi");
                        vetList = response.body();
                        vetRecyclerView.setVisibility(View.VISIBLE);

                        vetListingAdapter = new VetListingAdapter(VetOwnedFacilityProfileActivity.this, vetList, new VetListingAdapter.OnItemClickListener() {
                            @Override public void onItemClick(Veterinarian item) {

                                Intent intent = new Intent(VetOwnedFacilityProfileActivity.this, com.example.owner.petbetter.activities.VetProfileActivity.class);
                                intent.putExtra("thisVet", new Gson().toJson(item));
                                startActivity(intent);
                            }
                        });
                        vetRecyclerView.setAdapter(vetListingAdapter);
                        /*
                        vetRecyclerView.setAdapter(new VetRowAdapter(VetOwnedFacilityProfileActivity.this,
                                getLayoutInflater(), vetList, new VetRowAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Veterinarian item) {
                                Intent intent = new Intent(VetOwnedFacilityProfileActivity.this, com.example.owner.petbetter.activities.VetProfileActivity.class);
                                intent.putExtra("thisVet", new Gson().toJson(item));
                                startActivity(intent);
                            }
                        }));*/

                        vetRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        vetRecyclerView.setHasFixedSize(true);
                        vetRecyclerView.setLayoutManager(new LinearLayoutManager(VetOwnedFacilityProfileActivity.this));



                    } else {
                        /*
                        System.out.println("Went here inside else bruh");
                        serviceRecyclerView.setVisibility(View.GONE);
                        noServicesTextView.setVisibility(View.VISIBLE);
                        */
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Veterinarian>> call, Throwable t) {

            }
        });

    }


    public void getServiceList() {

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final Call<ArrayList<Services>> call = service.getServicesWithFaciID(faciItem.getId());

        call.enqueue(new Callback<ArrayList<Services>>() {
            @Override
            public void onResponse(Call<ArrayList<Services>> call, Response<ArrayList<Services>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        serviceList = response.body();
                        editServicesButton.setVisibility(View.VISIBLE);
                        serviceRecyclerView.setVisibility(View.VISIBLE);
                        noServicesTextView.setVisibility(View.GONE);
                        serviceRecyclerView.setAdapter(new ServiceAdapter(VetOwnedFacilityProfileActivity.this, getLayoutInflater(), response.body()));
                        if(serviceRecyclerView.getAdapter() == null){
                            System.out.println("No adapter bruh");
                        }
                        serviceRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        serviceRecyclerView.setHasFixedSize(true);
                        serviceRecyclerView.setLayoutManager(new LinearLayoutManager(VetOwnedFacilityProfileActivity.this));


                        for(Services services:serviceList){
                            System.out.println("service id "+services.getId());
                            final Call<ArrayList<Pending>> call2 = service.getPendingFacility(services.getId(), 3);
                            call2.enqueue(new Callback<ArrayList<Pending>>() {
                                @Override
                                public void onResponse(Call<ArrayList<Pending>> call, Response<ArrayList<Pending>> response) {

                                }

                                @Override
                                public void onFailure(Call<ArrayList<Pending>> call, Throwable t) {

                                    verifiedServices.setVisibility(View.INVISIBLE);
                                }
                            });
                        }


                    } else {
                        System.out.println("Went here inside else bruh");
                        serviceRecyclerView.setVisibility(View.GONE);
                        noServicesTextView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Services>> call, Throwable t) {

            }
        });

    }

    public void viewPostBackButtonClicked(View view){ finish(); }
}