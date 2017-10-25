package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.example.owner.petbetter.classes.Marker;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddMarkerActivity extends AppCompatActivity {

    private EditText editBldgName;
    private Spinner locTypeSpinner;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    private String email;
    private Address tempAddress = null;
    private Facility faciItem;
    private int markerId, type, fId;

    public static final int USE_ADDRESS_NAME = 1;
    public static final int USE_ADDRESS_LOCATION = 2;

    int fetchType = USE_ADDRESS_LOCATION;
    //LatLng point;
    double longitude, latitude;
    String location;

    private static final String TAG = "ADD_MARKER_ACTIVITY_ASYNC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_marker);

<<<<<<< HEAD
        Button saveButton = (Button) findViewById(R.id.addMarkerSaveButton);
        Toolbar toolbar = (Toolbar) findViewById(R.id.addMarkerToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

=======
>>>>>>> f5a51e1a45a2927d17740d83d60256630a16fb54



        systemSessionManager = new SystemSessionManager(this);
        if(systemSessionManager.checkLogin())
            finish();
        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();

        email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);
        locTypeSpinner = (Spinner) findViewById(R.id.spinnerLocType);
        if(user.getUserType()==2){
            locTypeSpinner.setVisibility(View.INVISIBLE);
            locTypeSpinner.setEnabled(false);
        }

        Bundle extras = getIntent().getExtras();
        markerId = extras.getInt("MARKERID");
        location = extras.getString("LOCATION");
        longitude = extras.getDouble("LONGITUDE");
        latitude = extras.getDouble("LATITUDE");
        //Intent mIntent = getIntent();
        //markerId = mIntent.getIntExtra("intVariableName", 0);
        //markerId = getLocation();//should return longlat


        editBldgName = (EditText) findViewById(R.id.editBldgName);
        editBldgName.requestFocusFromTouch();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(locTypeSpinner.getSelectedItem().toString()=="Register as Clinic"){
                    type = 2;
                }
                else{
                    type= 1;
                }

                if(editBldgName.getText().toString().matches("")){
                    Toast.makeText(view.getContext(),"Give a name to the location",Toast.LENGTH_SHORT).show();
                }
                else{

                    bldgName = editBldgName.getText().toString();
                    if(type==1||type==2){
                        touchMarker(bldgName, longitude, latitude, location);

                        Intent intent = new Intent(view.getContext(), com.example.owner.petbetter.activities.MapsActivity.class);
                        startActivity(intent);
                    }
            /*
            if(type==2){

                faciId = generateFaciId();

            /*
        markerId = extras.getInt("MARKERID");
        location = extras.getString("LOCATION");
        longitude = extras.getDouble("LONGITUDE");
        latitude = extras.getDouble("LATITUDE");

                Facility faciItem = new Facility(faciId, bldgName, location, hoursOpen, hoursClose,contactInfo, vetId, rating);
                //sync to db
            }*/
                }
            }
        });


    }
<<<<<<< HEAD
    public void addMarkerBackClicked(View view){
        finish();
    }
=======

>>>>>>> f5a51e1a45a2927d17740d83d60256630a16fb54
    private void initializeDatabase() {

        petBetterDb = new DataAdapter(this);

        try {
            petBetterDb.createDatabase();
        } catch(SQLException e ){
            e.printStackTrace();
        }

    }

    public void okClicked(View view){

        if(locTypeSpinner.getSelectedItem().toString()=="Save in Bookmarks"){
            type= 1;
        }
        else{
            type = 2;
        }

        if(editBldgName.getText().toString().matches("")){
            Toast.makeText(this,"Give a name to the location",Toast.LENGTH_SHORT).show();
        }
        else{
            String bldgName = editBldgName.getText().toString();
            touchMarker(bldgName, longitude, latitude, location);

            if(type==2){
                fId = generateFaciId();
                convertBookmarkToFaci(fId, bldgName, location, user.getUserId(), 0);
                faciItem = getFacility(fId);
            }


            Intent intent = new Intent(this, com.example.owner.petbetter.activities.MapsActivity.class);
            startActivity(intent);
        }
    }

    public void cancelClicked(View view){
        Intent intent = new Intent(this, com.example.owner.petbetter.activities.MapsActivity.class);
        startActivity(intent);
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

    private void touchMarker(String bldgName, double longitude, double latitude, String location){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        petBetterDb.touchMarker(markerId, bldgName, longitude, latitude, location, user.getUserId(), type);
        petBetterDb.closeDatabase();

    }

    public int generateFaciId(){
        ArrayList<Integer> storedIds;
        int markerId = 1;

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        storedIds = petBetterDb.generateFaciIds();
        petBetterDb.closeDatabase();


        if(storedIds.isEmpty()) {
            return markerId;
        } else {
            while (storedIds.contains(markerId)){
                markerId += 1;
            }

            return markerId;
        }
    }


    public long convertBookmarkToFaci(int fId, String faciName, String location, long userId, int rating){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        long result = petBetterDb.convertBookmarkToFaci(fId, faciName, location, userId, rating);
        petBetterDb.closeDatabase();

        return result;
    }

    private Facility getFacility(int id){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        Facility result = petBetterDb.getFacility(id);
        petBetterDb.closeDatabase();

        return result;
    }
}
