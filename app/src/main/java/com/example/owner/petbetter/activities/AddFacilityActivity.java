package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kristian on 2/17/2018.
 */

public class AddFacilityActivity extends AppCompatActivity {

    private Button addFacilityButton;
    private EditText facilityName;
    private EditText phoneNum;
    private Spinner openTime;
    private Spinner closeTime;
    private SystemSessionManager systemSessionManager;

    HerokuService service;
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_add_new_facility);

        Toolbar toolbar = (Toolbar) findViewById(R.id.viewPostToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final TextView activityTitle = (TextView) findViewById(R.id.activity_title);
        activityTitle.setText("Add Facility");

        addFacilityButton = (Button) findViewById(R.id.addFacilityButton);
        facilityName = (EditText) findViewById(R.id.addFacilityName);
        phoneNum = (EditText) findViewById(R.id.addFacilityPhone);
        openTime = (Spinner) findViewById(R.id.addFacilityOpenTimeSpinner);
        closeTime = (Spinner) findViewById(R.id.addFacilityCloseTimeSpinner);
    }

    public void addFacility(View view){
        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        Bundle extras = new Bundle();
        extras.putString("bldg_name", facilityName.getText().toString());
        extras.putString("hours_open", openTime.getSelectedItem().toString());
        extras.putString("hours_close", closeTime.getSelectedItem().toString());
        extras.putString("phone_num", phoneNum.getText().toString());

        Intent intent = new Intent(
                AddFacilityActivity.this,
                com.example.owner.petbetter.activities.MapsActivity.class
        );
        intent.putExtras(extras);
        startActivity(intent);

    }
    public void viewPostBackButtonClicked(View view){
        Intent intent = new Intent(
                AddFacilityActivity.this,
                com.example.owner.petbetter.activities.VeterinarianHomeActivity.class
        );
        startActivity(intent);
    }
}
