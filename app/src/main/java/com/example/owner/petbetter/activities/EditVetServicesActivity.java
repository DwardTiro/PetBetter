package com.example.owner.petbetter.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.adapters.ServiceAdapter;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.Services;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.SQLException;
import java.util.ArrayList;

import okhttp3.RequestBody;
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
    private TextView editServicesTitle;
    private ImageButton newTopic;
    private Button saveChangesButton;
    private ArrayList<Long> serviceIds;

    private LinearLayout currentServices;

    HerokuService service;
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_add_services);

        Toolbar toolbar = (Toolbar) findViewById(R.id.viewPostToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final TextView activityTitle = (TextView) findViewById(R.id.activity_title);
        activityTitle.setText("Edit Facility Services");

        currentServices = (LinearLayout) findViewById(R.id.serviceContainer);
        Button addField = (Button) findViewById(R.id.addFieldButton);
        newTopic = (ImageButton) findViewById(R.id.topicNewPost);
        //editServicesTitle = (TextView) findViewById(R.id.labelActivity);
        saveChangesButton = (Button) findViewById(R.id.addFacilityButton);
        newTopic.setVisibility(View.GONE);
        addField.setVisibility(View.GONE);

        saveChangesButton.setText("Save Changes");

        //editServicesTitle.setVisibility(View.GONE);

        serviceList = new ArrayList<>();
        serviceIds = new ArrayList<>();

        String jsonMyObject;
        Bundle extras = getIntent().getExtras();
        jsonMyObject = extras.getString("thisClinic");
        faciItem = new Gson().fromJson(jsonMyObject, Facility.class);
        initializeDatabase();

        getServiceList();

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < serviceIds.size(); i++){
                    //editService(serviceIds.get(i), );
                    EditText serviceField;
                    EditText priceField;
                    priceField = (EditText) (currentServices.getChildAt(i).findViewById(R.id.servicePriceField));
                    serviceField = (EditText) (currentServices.getChildAt(i).findViewById(R.id.serviceNameField));

                    editService(serviceIds.get(i), serviceField.getText().toString(), Float.parseFloat(priceField.getText().toString()));
                }
                serviceList = getServicesWithFaciId(faciItem.getId());
                System.out.println("Services ID in list is "+ serviceList.get(0).getId() );

                Gson gson = new GsonBuilder().serializeNulls().create();
                String jsonArray = gson.toJson(serviceList);
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray);

                final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
                final Call<Void> call = service.editFacilityService(body);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            finish();
                            Toast.makeText(EditVetServicesActivity.this, "Facilities edited successfully", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });

            }
        });



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

            EditText serviceName = (EditText) findViewById(R.id.serviceNameField);
            EditText servicePrice = (EditText) findViewById(R.id.servicePriceField);

            serviceName.setText(serviceList.get(i).getServiceName());
            servicePrice.setText(Float.toString(serviceList.get(i).getServicePrice()));
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
                    for(int i = 0; i < serviceList.size(); i++){
                        System.out.println("Service ID is: " + serviceList.get(i).getId());
                        serviceIds.add(serviceList.get(i).getId());
                    }

                    createNewEditText();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Services>> call, Throwable t) {
                System.out.println("Something went wrong");
            }
        });

    }

    public void deleteRow(View view){
        currentServices.removeView((View) view.getParent());
        System.out.println("ID of removed service" + ((View) view.getParent()).getId());

    }
    private void initializeDatabase() {

        petBetterDb = new DataAdapter(this);

        try {
            petBetterDb.createDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void editService(long service_id, String service_name, float service_price){

        try{
            petBetterDb.openDatabase();
        }catch (SQLException e){
            e.printStackTrace();
        }

        petBetterDb.editService(service_id, service_name, service_price);
        petBetterDb.closeDatabase();


    }

    private void dataSynced(int n) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        petBetterDb.dataSynced(n);
        petBetterDb.closeDatabase();

    }

    private ArrayList<Services> getServicesWithFaciId(long faci_id){
        try{
            petBetterDb.openDatabase();
        }catch (SQLException e){
            e.printStackTrace();
        }

        ArrayList<Services> result = petBetterDb.getServicesWithId(faci_id);
        petBetterDb.closeDatabase();

        return  result;
    }
    public void viewPostBackButtonClicked(View view){
        finish();
    }
}
