package com.example.owner.petbetter.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.LocationMarker;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.sql.SQLException;
import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity
        implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMyLocationButtonClickListener {

    private GoogleMap mMap;
    private GoogleApiClient client;
    private FusedLocationProviderClient mFusedLocationClient;
    private Geocoder geocoder;
    private String strAdd;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 10;
    private boolean hasLocationPermission = false;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    public static final int REQUEST_LOCATION_CODE = 99;
    private User user;
    private String email;
    private Location lastLocation;
    private LocationRequest locationRequest;
    double longitude, latitude;
    LatLng pointTemp;
    int markerId;
    String location;
    private Marker newMarker;

    HerokuService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkLocationPermission();
        }
        getLocationPermissions();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;

        System.out.println("went here");

        if (ContextCompat.checkSelfPermission(
                this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);
        } else {
            System.out.println("Error in permission bruh");
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title("New LocationMarker");
                markerOptions.position(latLng);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                mMap.clear();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                newMarker = mMap.addMarker(markerOptions);
            }
        });


    }


    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);

    }


    protected synchronized void buildGoogleApiClient() {
        client = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        client.connect();
    }

    public int generateMarkerId() {
        ArrayList<Integer> storedIds;
        int markerId = 1;

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        storedIds = petBetterDb.getMarkerIds();
        petBetterDb.closeDatabase();


        if (storedIds.isEmpty()) {
            return markerId;
        } else {
            while (storedIds.contains(markerId)) {
                markerId += 1;
            }

            return markerId;
        }
    }

    /*
    private void loadMarkers(long userId) {
        LatLng resultLatLng;
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<LocationMarker> result = petBetterDb.loadMarkers(userId);
        System.out.println("MARKERS RESULT SIZE: " + result.size());
        petBetterDb.closeDatabase();

        for (int i = 0; i < result.size(); i++) {
            resultLatLng = new LatLng(result.get(i).getLatitude(), result.get(i).getLongitude());
            mMap.addMarker(new MarkerOptions().position(resultLatLng).title(result.get(i).getBldgName()));
            goToLocationZoom(result.get(i).getLatitude(), result.get(i).getLongitude(), 13);
        }

    }
*/


    private void goToLocationZoom(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mMap.moveCamera(update);
    }

    private User getUser(String email) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        User result = petBetterDb.getUser(email);
        petBetterDb.closeDatabase();

        return result;
    }

    private void initializeDatabase() {

        petBetterDb = new DataAdapter(this);

        try {
            petBetterDb.createDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void mapBackButtonClicked(View view) {
        finish();
        Intent intent = new Intent(view.getContext(), com.example.owner.petbetter.activities.AddFacilityActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            }
            return false;

        } else
            return true;
    }


    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(MapsActivity.this, "Checked your location", Toast.LENGTH_SHORT).show();
        return false;
    }


    private void getLocationPermissions() {
        String[] locationPermissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            System.out.println("Fine location permission okay");
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                initMap();
                System.out.println("Coarse location permission okay");
                hasLocationPermission = true;
            } else {
                System.out.println("Coarse location permission not okay");
                ActivityCompat.requestPermissions(
                        this,
                        locationPermissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            System.out.println("Fine location permission not okay");
            ActivityCompat.requestPermissions(
                    this,
                    locationPermissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        hasLocationPermission = false;

        switch (requestCode) {
            case REQUEST_LOCATION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        if(client == null){
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else{
                    System.out.println("Denied");
                }
            }
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        lastLocation = location;

        LatLng position = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(position);
        markerOptions.title("My LocationMarker");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        newMarker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(15));

        if(client != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
        }
    }

    public void onAddFacilityClicked(View view){
        System.out.println("went here");
        System.out.println(newMarker.getPosition().latitude + " " + newMarker.getPosition().longitude);
        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        Bundle extras = getIntent().getExtras();
        String facilityName = extras.getString("bldg_name");
        String openTime = extras.getString("hours_open");
        String closeTime = extras.getString("hours_close");
        String phoneNum = extras.getString("phone_num");

        Facility facility = new Facility(
                4,
                facilityName,
                "Here",
                openTime,
                closeTime,
                phoneNum,
                4,
                0
        );
        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(facility);
        System.out.println(jsonArray);
        RequestBody body = RequestBody.create(
                okhttp3.MediaType.parse("application/json; charset=utf-8"),
                jsonArray.toString()
        );

        LocationMarker locationMarker = new LocationMarker(1,
                facilityName,
                newMarker.getPosition().latitude,
                newMarker.getPosition().longitude,
                facilityName,
                4,
                1
                );
        String locationJson = gson.toJson(locationMarker);
        RequestBody locationBody = RequestBody.create(
                okhttp3.MediaType.parse("application/json; charset=utf-8"),
                locationJson.toString()
        );

        Call<Void> call = service.addFacility(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("Facility added to server successfully");

            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("FAILED TO ADD FACILITY TO SERVER");
            }
        });
        Call<Void> locationCall = service.addLocation(locationBody);
        locationCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> locationCall, Response<Void> response) {
                System.out.println("Location added to server successfully");
                Intent intent = new Intent(MapsActivity.this, com.example.owner.petbetter.activities.MapsActivity.class);
                startActivity(intent);

            }
            @Override
            public void onFailure(Call<Void> locationCall, Throwable t) {
                System.out.println("Location TO ADD FACILITY TO SERVER");
            }
        });
    }
}

