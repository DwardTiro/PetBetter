package com.example.owner.petbetter.activities;

import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.LocationMarker;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class PointLocActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private Geocoder geocoder;
    private String strAdd;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    private String email;
    private LocationMarker locationMarkerItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        System.out.println("Eyy");

        systemSessionManager = new SystemSessionManager(this);
        if(systemSessionManager.checkLogin())
            finish();
        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();
        email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);

        //Intents that receive the contents
        String jsonMyObject;
        Bundle extras = getIntent().getExtras();
        jsonMyObject = extras.getString("thisBookmark");
        locationMarkerItem = new Gson().fromJson(jsonMyObject,LocationMarker.class);

        //Toast.makeText(this, "Longitude: "+locationMarkerItem.getLongitude()+"Latitude: "+locationMarkerItem.getLatitude(), Toast.LENGTH_LONG).show();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        //geocoder = new Geocoder(this, Locale.getDefault());

        //Use the marker data as parameter


        LatLng resultLatLng = new LatLng(locationMarkerItem.getLatitude(), locationMarkerItem.getLongitude());
        mMap.addMarker(new MarkerOptions().position(resultLatLng).title(locationMarkerItem.getBldgName()));
        goToLocationZoom(locationMarkerItem.getLatitude(), locationMarkerItem.getLongitude(), 13);
        //loadMarker(user.getUserId());
    }

    public int generateMarkerId(){
        ArrayList<Integer> storedIds;
        int markerId = 1;

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        storedIds = petBetterDb.getMarkerIds();
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

    private void loadMarkers(long userId){
        LatLng resultLatLng;
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<LocationMarker> result = petBetterDb.loadMarkers(userId);
        petBetterDb.closeDatabase();

        for(int i = 0;i<result.size();i++){
            resultLatLng = new LatLng(result.get(i).getLatitude(), result.get(i).getLongitude());
            mMap.addMarker(new MarkerOptions().position(resultLatLng).title(result.get(i).getBldgName()));
            goToLocationZoom(result.get(i).getLatitude(), result.get(i).getLongitude(), 13);
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

    private void goToLocationZoom(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mMap.moveCamera(update);
    }

    private void initializeDatabase() {

        petBetterDb = new DataAdapter(this);

        try {
            petBetterDb.createDatabase();
        } catch(SQLException e ){
            e.printStackTrace();
        }

    }
    //Find a way to add markers with location or longlat.
}
