package com.example.owner.petbetter.activities;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.owner.petbetter.R;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * Created by Kristian on 3/3/2018.
 */

public class ShowLocationActivity extends FragmentActivity
        implements OnMapReadyCallback,
        LocationListener,
        GoogleMap.OnMyLocationButtonClickListener
{

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        //setContentView(R.layout.activity_show_location);

    }


    @Override
    public void onLocationChanged(Location location) {

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

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
