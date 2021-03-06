package com.example.owner.petbetter.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.LocationMarker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.w3c.dom.Text;

/**
 * Created by Kristian on 3/3/2018.
 */

public class ShowLocationActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMyLocationButtonClickListener{
    private GoogleMap mMap;
    private LocationMarker locationMarker;

    String bldgName;
    Double latitude;
    Double longitude;
    CameraPosition cameraPosition;


    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_show_location);
        TextView toolbarTitle = (TextView) findViewById(R.id.activity_title);
        toolbarTitle.setVisibility(View.GONE);


        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.showLocationMap);
        supportMapFragment.getMapAsync(this);



    }


    @Override
    public void onLocationChanged(android.location.Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void viewPostBackButtonClicked(View view){
        finish();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        String jsonMyObject;
        Bundle extras = getIntent().getExtras();
        bldgName = extras.getString("bldg_name");
        latitude = extras.getDouble("latitude");
        longitude = extras.getDouble("longitude");


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            //mMap.setOnMyLocationButtonClickListener(this);

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }

        LatLng position = new LatLng(latitude,longitude);

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        //mMap.animateCamera(CameraUpdateFactory.zoomBy(10));


        //LatLng position = new LatLng(latitude, longitude);
        System.out.println("location latitude: "+latitude);
        System.out.println("location longitude: "+longitude);
        mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(bldgName)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        final CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position)      // Sets the center of the map to Mountain View
                .zoom(10)                   // Sets the zoom
                .build();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 10));
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                return true;
            }
        });
        /*

        mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(bldgName)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(15));
        */
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        LatLng position = new LatLng(latitude,longitude);

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        //mMap.animateCamera(CameraUpdateFactory.zoomBy(10));


        //LatLng position = new LatLng(latitude, longitude);
        System.out.println("location latitude: "+latitude);
        System.out.println("location longitude: "+longitude);
        mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(bldgName)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        cameraPosition = new CameraPosition.Builder()
                .target(position)      // Sets the center of the map to Mountain View
                .zoom(10)                   // Sets the zoom
                .build();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 10));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        /*
        switch (requestCode) {
            case 200: {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    LatLng position = new LatLng(latitude,longitude);

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                    mMap.animateCamera(CameraUpdateFactory.zoomBy(15));

                    System.out.println("In show location bldg_name: "+bldgName);
                    System.out.println("In show location latitude: "+latitude);
                    System.out.println("In show location longitude: "+longitude);
                }
            }
        }*/
    }

    @Override
    public boolean onMyLocationButtonClick() {
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        return false;
    }
}
