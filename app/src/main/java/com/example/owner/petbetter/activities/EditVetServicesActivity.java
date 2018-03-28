package com.example.owner.petbetter.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.adapters.ServiceAdapter;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.Services;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kristian on 3/29/2018.
 */

public class EditVetServicesActivity extends AppCompatActivity {

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private Facility faciItem;
    private ArrayList<Services> serviceList;

    private LinearLayout currentServices;

    HerokuService service;
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_add_services);

        currentServices = (LinearLayout) findViewById(R.id.serviceContainer);
        Button addField = (Button) findViewById(R.id.addFieldButton);
        addField.setVisibility(View.GONE);

        serviceList = new ArrayList<>();

        String jsonMyObject;
        Bundle extras = getIntent().getExtras();
        jsonMyObject = extras.getString("thisClinic");
        faciItem = new Gson().fromJson(jsonMyObject, Facility.class);

        getServiceList();
        createNewEditText();


    }

    private void createNewEditText() {
        /*
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
        final EditText editText = new EditText(this);
        editText.setLayoutParams(lparams);
        return editText;
        */
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View serviceView = inflater.inflate(R.layout.fragment_new_service_field, null);

        System.out.println("Service List size: "+ serviceList.size());
        for(int i =0; i<serviceList.size();i++){
            currentServices.addView(serviceView, currentServices.getChildCount());
        }
    }

    public void getServiceList() {

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final Call<ArrayList<Services>> call = service.getServicesWithFaciID(faciItem.getId());
        call.enqueue(new Callback<ArrayList<Services>>() {
            @Override
            public void onResponse(Call<ArrayList<Services>> call, Response<ArrayList<Services>> response) {
                if (response.isSuccessful()) {
                    serviceList.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Services>> call, Throwable t) {
                System.out.println("Something went wrong");
            }
        });

    }
}
