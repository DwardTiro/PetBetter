package com.example.owner.petbetter.activities;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.Marker;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private Geocoder geocoder;
    private String strAdd;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    private String email;
    double touchLat;
    double touchLong;
    LatLng pointTemp;
    String bldgName="House";
    int markerId;
    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        systemSessionManager = new SystemSessionManager(this);
        if(systemSessionManager.checkLogin())
            finish();
        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();
        email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);
        System.out.println("What's the problem?");
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
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();

                // Setting the position of the marker
                options.position(point);
                pointTemp = point;
                //Get LatLng from touched point;

                ///here is reverse GeoCoding which helps in getting address from latlng
                //Toast.makeText(MapsActivity.this, pointTemp.longitude+" "+pointTemp.latitude, Toast.LENGTH_LONG).show();
                try {
                    Geocoder geo = new Geocoder(MapsActivity.this.getApplicationContext(), Locale.getDefault());
                    //List<Address> addresses = geo.getFromLocation(touchLat,touchLong, 1);

                    List<Address> addresses = geo.getFromLocation(pointTemp.latitude,pointTemp.longitude, 1);

                    for(int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++) {
                        location += " " + addresses.get(0).getAddressLine(i);
                    }

                    // draws the marker at the currently touched location
                    //touchMarker(pointTemp.longitude, pointTemp.latitude);
                    Intent myIntent = new Intent(MapsActivity.this, com.example.owner.petbetter.activities.AddMarkerActivity.class);
                    Bundle extras = new Bundle();
                    markerId = generateMarkerId();
                    extras.putInt("MARKERID", markerId);
                    extras.putString("LOCATION", location);
                    extras.putDouble("LONGITUDE", pointTemp.longitude);
                    extras.putDouble("LATITUDE", pointTemp.latitude);
                    myIntent.putExtras(extras);
                    startActivity(myIntent);
                    location = "";
                    System.out.println("INTENT SUCCESSFULLY DONE");

                }
                catch (Exception e) {
                    e.printStackTrace(); // getFromLocation() may sometimes fail
                    //Toast.makeText(MapsActivity.this, "Service is not available. Google Play Services not up to date", Toast.LENGTH_LONG).show();
                }
            }
        });
        loadMarkers(user.getUserId());
        System.out.println("WHY ARE YOU SO LAGGY?");
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

        ArrayList<Marker> result = petBetterDb.loadMarkers(userId);
        System.out.println("MARKERS RESULT SIZE: "+result.size());
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
    public void mapBackButtonClicked(View view){
        finish();
        Intent intent = new Intent(view.getContext(),com.example.owner.petbetter.activities.HomeActivity.class);
        startActivity(intent);
    }
    //Find a way to add markers with location or longlat.
}
