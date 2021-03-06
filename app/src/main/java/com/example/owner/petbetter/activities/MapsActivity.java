package com.example.owner.petbetter.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.FacilityMembership;
import com.example.owner.petbetter.classes.LocationMarker;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.classes.WorkHours;
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
import com.google.gson.reflect.TypeToken;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
    private int vetId;

    private String faciName;
    private String phoneNum;
    private String address;
    private String image;
    private long faciId;
    private long locationId;
    private RequestQueue requestQueue;
    private ImageButton topicNewPost;

    private Button addLocationButton;
    private ArrayList<WorkHours> hoursList;


    HerokuService service;
    private String jsonHours;
    private boolean fromMain = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        addLocationButton = (Button) findViewById(R.id.addLocationButton);
        addLocationButton.setEnabled(true);
        requestQueue = Volley.newRequestQueue(this);
        //topicNewPost = (ImageButton) findViewById(R.id.topicNewPost);
        //topicNewPost.setVisibility(View.GONE);
        Bundle extras = getIntent().getExtras();
        faciName = extras.getString("bldg_name");
        phoneNum = extras.getString("phone_num");
        //address = extras.getString("location");
        jsonHours = extras.getString("workhours");
        /*
        Type type = new TypeToken<ArrayList<WorkHours>>(){}.getType();
        hoursList = new Gson().fromJson(jsonMyObject, type);*/
        image = extras.getString("image");
        fromMain = extras.getBoolean("fromMain");

        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        systemSessionManager = new SystemSessionManager(this);
        if (systemSessionManager.checkLogin())
            finish();

        HashMap<String, String> userIn = systemSessionManager.getUserDetails();
        initializeDatabase();

        String email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);
        //vetId = getVeterinarian((int) user.getUserId()).getId();
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
            onMyLocationButtonClick();
        } else {
            System.out.println("Error in permission bruh");
        }


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {


                addLocationButton.setBackgroundColor(getResources().getColor(R.color.myrtle_green));
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(faciName);
                markerOptions.position(latLng);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                latitude = latLng.latitude;
                longitude = latLng.longitude;

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

    private Veterinarian getVeterinarian(int user_id) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Veterinarian result = petBetterDb.getVeterinarianWithId(user_id);
        ArrayList<Integer> ids = petBetterDb.generateFaciIds();

        for (int i = 0; i < ids.size(); i++) {
            System.out.print(ids.get(i) + " ");
        }

        petBetterDb.closeDatabase();

        return result;
    }

    private Facility getNewFacility(int vet_id) {
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Facility result = petBetterDb.getNewFacilityWithId(vet_id);

        petBetterDb.closeDatabase();

        return result;
    }

    private void dataSync(int n) {
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        petBetterDb.dataSynced(n);
        petBetterDb.closeDatabase();

    }

    private void initializeDatabase() {

        petBetterDb = new DataAdapter(this);

        try {
            petBetterDb.createDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private long addFacilitytoDB(int faci_id, String faci_name, String location, String contact_info, String faciPhoto) {

        try{
            petBetterDb.openDatabase();
        } catch (SQLException e){
            e.printStackTrace();
        }

        long result = petBetterDb.addFacility(faci_id,faci_name,location,contact_info, faciPhoto);
        petBetterDb.closeDatabase();

        return result;
    }

    private long addLocationMarkertoDB(
            int marker_id,
            String bldg_name,
            double latitude,
            double longitude,
            String location,
            long userId,
            int type,
            long faciId
            ){

        try{
            petBetterDb.openDatabase();
        } catch (SQLException e){
            e.printStackTrace();
        }
        long result = petBetterDb.addMarker(marker_id, bldg_name, latitude, longitude, location, userId, type, faciId);
        petBetterDb.closeDatabase();

        return result;

    }

    private ArrayList<Facility> getUnsyncedFacilities() {
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Facility> result = petBetterDb.getUnsyncedFacilities();


        petBetterDb.closeDatabase();

        return result;
    }

    private void syncFacilityChanges(){
        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        System.out.println("WE HERE BOOIII");
        ArrayList<Facility> unsyncedFacilities = getUnsyncedFacilities();
        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(unsyncedFacilities);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addFacilities(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    System.out.println("FACILITIES ADDED YEY");
                    dataSync(2);

                    final Call<ArrayList<Facility>> call2 = service2.getClinics(1);
                    call2.enqueue(new Callback<ArrayList<Facility>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Facility>> call, Response<ArrayList<Facility>> response) {
                            if(response.isSuccessful()){
                                System.out.println("Number of clinics from server: "+response.body().size());
                                setFacilities(response.body());
                                faciId = response.body().get(response.body().size()-1).getId();
                                int id = generateNewMemberID();
                                addFacilityMember(id, faciId, user.getUserId());
                                syncFacilityMemberChanges();
                                addFacilityLocation();

                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Facility>> call, Throwable t) {
                            Log.d("onFailure", t.getLocalizedMessage());

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
    }


    public void syncLocationChanges(){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        System.out.println("WE HERE BOOIII");
        ArrayList<LocationMarker> unsyncedLocations = getUnsyncedMarkers();

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(unsyncedLocations);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addLocations(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    System.out.println("SERVICES ADDED YEY");
                    dataSync(4);

                    final Call<ArrayList<LocationMarker>> call2 = service2.loadLocations();
                    call2.enqueue(new Callback<ArrayList<LocationMarker>>() {
                        @Override
                        public void onResponse(Call<ArrayList<LocationMarker>> call, Response<ArrayList<LocationMarker>> response) {
                            if(response.isSuccessful()){
                                setLocationMarkers(response.body());
                                Intent intent = new Intent(MapsActivity.this, com.example.owner.petbetter.activities.VeterinarianHomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<LocationMarker>> call, Throwable t) {
                            Log.d("onFailure", t.getLocalizedMessage());

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
    }

    private ArrayList<LocationMarker> getUnsyncedMarkers(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<LocationMarker> result = petBetterDb.getUnsyncedMarkers();
        petBetterDb.closeDatabase();

        return result;
    }

    public long setLocationMarkers(ArrayList<LocationMarker> locationMarkerList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setLocationMarkers(locationMarkerList);
        petBetterDb.closeDatabase();

        return result;
    }

    public long addFacilityMember(int id, long faciId, long userId){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.addFacilityMember(id, faciId, userId);
        petBetterDb.closeDatabase();

        return result;
    }

    public long setFacilities(ArrayList<Facility> faciList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setFacilities(faciList);
        petBetterDb.closeDatabase();

        return result;
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
        //Toast.makeText(MapsActivity.this, "Checked your location", Toast.LENGTH_SHORT).show();
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
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (client == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
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
        markerOptions.title(faciName);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        newMarker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(15));

        if (client != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
        }
    }

    public void onAddFacilityClicked(View view) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        System.out.println("MARKER PAR "+newMarker.getPosition().latitude+", "+newMarker.getPosition().longitude);
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latng=" + newMarker.getPosition().latitude + "," + newMarker.getPosition().longitude + "&key=AIzaSyBETjxiY1bwpzb7Wq6OleNwDtQ5jEBVEKQ";
        List<Address> addresses;
        try{
            addresses = geocoder.getFromLocation(newMarker.getPosition().latitude, newMarker.getPosition().longitude, 1);
            System.out.println("NUMBER OF ADDRESSES: "+addresses);
            address = addresses.get(0).getAddressLine(0);
            /*
            for(int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++) {
                address += ", " + addresses.get(0).getAddressLine(0);
            }*/


        } catch (IOException e) {
            e.printStackTrace();
        }
        //AIzaSyBETjxiY1bwpzb7Wq6OleNwDtQ5jEBVEKQ

        System.out.println("ADDRESS PAR: "+address);
        System.out.println(newMarker.getPosition().latitude + " " + newMarker.getPosition().longitude);

        //addFacility();

        //Gson gson = new GsonBuilder().serializeNulls().create();
        //String jsonHours = gson.toJson(hoursList);

        Intent intent = new Intent(MapsActivity.this, com.example.owner.petbetter.activities.AddServicesActivity.class);
        Bundle extras = new Bundle();
        extras.putString("bldg_name", faciName);
        extras.putString("phone_num", phoneNum);
        extras.putString("workhours", jsonHours);
        //address = extras.getString("location");
        extras.putString("image", image);
        extras.putDouble("longitude", longitude);
        extras.putDouble("latitude", latitude);
        extras.putString("address", address);
        extras.putBoolean("isNew", true);
        extras.putBoolean("fromMain", fromMain);
        intent.putExtras(extras);
        startActivity(intent);


    }

    private int generateNewFacilityID(){
        ArrayList<Integer> faciIDs;
        int newId;
        try{
            petBetterDb.openDatabase();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        faciIDs = petBetterDb.generateFaciIds();

        if(faciIDs.size() != 0){
            newId = faciIDs.get(faciIDs.size() - 1);
            newId += 1;
        }
        else
            newId = 1;

        petBetterDb.closeDatabase();

        return newId;
    }

    private int generateNewMemberID(){
        ArrayList<Integer> faciIDs;
        int newId;
        try{
            petBetterDb.openDatabase();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        faciIDs = petBetterDb.generateMembershipIds();

        if(faciIDs.size() != 0){
            newId = faciIDs.get(faciIDs.size() - 1);
            newId += 1;
        }
        else
            newId = 1;

        petBetterDb.closeDatabase();

        return newId;
    }

    public void addFacility() {

        faciId = generateNewFacilityID();

        /*
        Facility facility = new Facility(
                (int)faciId,
                faciName,
                address,
                openTime,
                closeTime,
                phoneNum,
                0,
                image,
                0
        );*/

        addFacilitytoDB((int)faciId,faciName,address,phoneNum, image);
        syncFacilityChanges();

    }

    public void syncFacilityMemberChanges(){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);


        ArrayList<FacilityMembership> unsyncedMembers = getUnsyncedMembers();

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(unsyncedMembers);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addMembers(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    dataSynced(18);

                    final Call<ArrayList<FacilityMembership>> call2 = service2.getFacilityMembers();
                    call2.enqueue(new Callback<ArrayList<FacilityMembership>>() {
                        @Override
                        public void onResponse(Call<ArrayList<FacilityMembership>> call, Response<ArrayList<FacilityMembership>> response) {
                            if(response.isSuccessful()){
                                setMembers(response.body());

                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<FacilityMembership>> call, Throwable t) {
                            Log.d("onFailure", t.getLocalizedMessage());

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
    }

    private void dataSynced(int n){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        petBetterDb.dataSynced(n);
        petBetterDb.closeDatabase();

    }

    private ArrayList<FacilityMembership> getUnsyncedMembers(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<FacilityMembership> result = petBetterDb.getUnsyncedMembers();
        petBetterDb.closeDatabase();

        return result;
    }

    public long setMembers(ArrayList<FacilityMembership> fmList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setMembers(fmList);
        petBetterDb.closeDatabase();

        return result;
    }

    private int generateNewLocationMarkerId(){
        int newId;
        try{
            petBetterDb.openDatabase();
        } catch (SQLException e){
            e.printStackTrace();
        }
        ArrayList<Integer> ids = petBetterDb.generateLocationMarkerIds();
        if(ids.size() != 0){
            newId = ids.get(ids.size() - 1);
            newId += 1;
        }
        else
            newId = 1;

        petBetterDb.closeDatabase();

        return newId;
    }

    public void addFacilityLocation() {
        System.out.println("Start adding new location");

        locationId = generateNewLocationMarkerId();
        System.out.println("Generated Location Id is: " + locationId);


        LocationMarker locationMarker = new LocationMarker(
                locationId,
                faciName,
                newMarker.getPosition().latitude,
                newMarker.getPosition().longitude,
                address,
                user.getUserId(),
                1,
                faciId
        );

        addLocationMarkertoDB(
                (int) locationId,
                faciName,
                newMarker.getPosition().latitude,
                newMarker.getPosition().longitude,
                address,
                user.getUserId(),
                1,
                (int)faciId);
        syncLocationChanges();

    }
}

