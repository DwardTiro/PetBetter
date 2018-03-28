package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.adapters.ServiceAdapter;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.Services;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kristian on 3/28/2018.
 */

public class VetOwnedFacilityProfileActivity extends AppCompatActivity{

    private ImageButton addNewTopic;
    private TextView facilityName;
    private TextView facilityRating;
    private TextView facilityAddress;
    private TextView facilityLandline;
    private TextView facilityOpenTime;
    private TextView facilityCloseTime;
    private TextView noServicesTextView;
    private RecyclerView serviceRecyclerView;
    private Button editFacilityProfileButton;
    private Button editServicesButton;
    private Facility faciItem;


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
        facilityOpenTime = (TextView) findViewById(R.id.openTimeTextField);
        facilityCloseTime = (TextView) findViewById(R.id.closeTimeTextField);

        facilityAddress.setText(faciItem.getLocation());
        facilityLandline.setText(faciItem.getContactInfo());
        facilityOpenTime.setText(faciItem.getHoursOpen());
        facilityCloseTime.setText(faciItem.getHoursClose());
        noServicesTextView = (TextView) findViewById(R.id.noServicesTextView);
        serviceRecyclerView = (RecyclerView) findViewById(R.id.servicesRecyclerView);
        editFacilityProfileButton = (Button) findViewById(R.id.editFacilityButton);
        editServicesButton = (Button) findViewById(R.id.editServicesButton);

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

        facilityName.setText(faciItem.getFaciName());
        facilityRating.setText(Float.toString(faciItem.getRating()));

        getServiceList();
    }

    public void getServiceList() {

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final Call<ArrayList<Services>> call = service.getServicesWithFaciID(faciItem.getId());
        call.enqueue(new Callback<ArrayList<Services>>() {
            @Override
            public void onResponse(Call<ArrayList<Services>> call, Response<ArrayList<Services>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {

                        serviceRecyclerView.setVisibility(View.VISIBLE);
                        noServicesTextView.setVisibility(View.GONE);
                        serviceRecyclerView.setAdapter(new ServiceAdapter(VetOwnedFacilityProfileActivity.this, getLayoutInflater(), response.body()));
                        if(serviceRecyclerView.getAdapter() == null){
                            System.out.println("No adapter bruh");
                        }
                        serviceRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        serviceRecyclerView.setHasFixedSize(true);
                        serviceRecyclerView.setLayoutManager(new LinearLayoutManager(VetOwnedFacilityProfileActivity.this));
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