package com.example.owner.petbetter.activities;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.Pending;
import com.example.owner.petbetter.fragments.FragmentPending;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingActivity extends AppCompatActivity {

    private Button pendingEducButton;
    private Button pendingLicenseButton;
    private Button pendingServicesButton;
    private Button pendingSpecialtyButton;
    private int currFragment = 1;
    private HerokuService service;
    private AutoCompleteTextView actvPending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);

        pendingEducButton = (Button) findViewById(R.id.pendingEducButton);
        pendingLicenseButton = (Button) findViewById(R.id.pendingLicenseButton);
        pendingServicesButton = (Button) findViewById(R.id.pendingServicesButton);
        pendingSpecialtyButton = (Button) findViewById(R.id.pendingSpecialtyButton);
        actvPending = (AutoCompleteTextView) findViewById(R.id.actvSearch);

        pendingEducClicked(this.findViewById(android.R.id.content));
    }

    public void searchBackButtonClicked(View view) {
        finish();
    }

    public void pendingEducClicked(View view){
        currFragment = 1;
        pendingEducButton.setBackgroundResource(R.color.main_White);
        pendingEducButton.setTextColor(getResources().getColor(R.color.myrtle_green));
        pendingLicenseButton.setBackgroundResource(R.color.medTurquoise);
        pendingLicenseButton.setTextColor(getResources().getColor(R.color.colorWhite));
        pendingServicesButton.setBackgroundResource(R.color.medTurquoise);
        pendingServicesButton.setTextColor(getResources().getColor(R.color.colorWhite));
        pendingSpecialtyButton.setBackgroundResource(R.color.medTurquoise);
        pendingSpecialtyButton.setTextColor(getResources().getColor(R.color.colorWhite));
        pendingEducButton.setPaintFlags(pendingEducButton.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        pendingLicenseButton.setPaintFlags(pendingLicenseButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
        pendingServicesButton.setPaintFlags(pendingServicesButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
        pendingSpecialtyButton.setPaintFlags(pendingSpecialtyButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));

        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        final Call<ArrayList<Pending>> call = service.getPending();
        call.enqueue(new Callback<ArrayList<Pending>>() {
            @Override
            public void onResponse(Call<ArrayList<Pending>> call, Response<ArrayList<Pending>> response) {
                ArrayList<Pending> pendingList = response.body();

                FragmentPending fragment1 = new FragmentPending(pendingList, 1);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_pending,fragment1).
                        addToBackStack(null).commitAllowingStateLoss();


                // getSupportFragmentManager().beginTransaction().add(R.id.frame_search,fragment1).commitAllowingStateLoss();
                //ArrayAdapter<Veterinarian> adapter = new ArrayAdapter<Veterinarian>(this,R.layout.,vetList);

            }

            @Override
            public void onFailure(Call<ArrayList<Pending>> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
                Toast.makeText(PendingActivity.this, "Unable to get vets from server", Toast.LENGTH_LONG);
            }
        });


        actvPending.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(currFragment==1){
                    service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

                    //query the substring to server data


                    final Call<ArrayList<Pending>> call = service.queryPending(actvPending.getText().toString(), 1);
                    call.enqueue(new Callback<ArrayList<Pending>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Pending>> call, Response<ArrayList<Pending>> response) {
                            ArrayList<Pending> pendingList = response.body();


                            FragmentPending fragment1 = new FragmentPending(pendingList, 1);
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame_pending,fragment1).
                                    addToBackStack(null).commitAllowingStateLoss();
                            /*
                            FragmentUser fragment1 = new FragmentUser(userList);
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame_pending,fragment1).
                                    addToBackStack(null).commitAllowingStateLoss();
                            */


                            //ArrayAdapter<Veterinarian> adapter = new ArrayAdapter<Veterinarian>(this,R.layout.,vetList);

                        }

                        @Override
                        public void onFailure(Call<ArrayList<Pending>> call, Throwable t) {
                            Log.d("onFailure", t.getLocalizedMessage());
                            Toast.makeText(PendingActivity.this, "Unable to get vets from server", Toast.LENGTH_LONG);
                        }
                    });
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void pendingLicenseClicked(View view){
        currFragment = 2;
        pendingLicenseButton.setBackgroundResource(R.color.main_White);
        pendingLicenseButton.setTextColor(getResources().getColor(R.color.myrtle_green));
        pendingEducButton.setBackgroundResource(R.color.medTurquoise);
        pendingEducButton.setTextColor(getResources().getColor(R.color.colorWhite));
        pendingServicesButton.setBackgroundResource(R.color.medTurquoise);
        pendingServicesButton.setTextColor(getResources().getColor(R.color.colorWhite));
        pendingSpecialtyButton.setBackgroundResource(R.color.medTurquoise);
        pendingSpecialtyButton.setTextColor(getResources().getColor(R.color.colorWhite));
        pendingLicenseButton.setPaintFlags(pendingLicenseButton.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        pendingEducButton.setPaintFlags(pendingEducButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
        pendingServicesButton.setPaintFlags(pendingServicesButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
        pendingSpecialtyButton.setPaintFlags(pendingSpecialtyButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
    }

    public void pendingServicesClicked(View view){
        currFragment = 3;
        pendingServicesButton.setBackgroundResource(R.color.main_White);
        pendingServicesButton.setTextColor(getResources().getColor(R.color.myrtle_green));
        pendingLicenseButton.setBackgroundResource(R.color.medTurquoise);
        pendingLicenseButton.setTextColor(getResources().getColor(R.color.colorWhite));
        pendingEducButton.setBackgroundResource(R.color.medTurquoise);
        pendingEducButton.setTextColor(getResources().getColor(R.color.colorWhite));
        pendingSpecialtyButton.setBackgroundResource(R.color.medTurquoise);
        pendingSpecialtyButton.setTextColor(getResources().getColor(R.color.colorWhite));
        pendingServicesButton.setPaintFlags(pendingServicesButton.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        pendingLicenseButton.setPaintFlags(pendingLicenseButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
        pendingEducButton.setPaintFlags(pendingEducButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
        pendingSpecialtyButton.setPaintFlags(pendingSpecialtyButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
    }

    public void pendingSpecialtyClicked(View view){
        currFragment = 4;
        pendingSpecialtyButton.setBackgroundResource(R.color.main_White);
        pendingSpecialtyButton.setTextColor(getResources().getColor(R.color.myrtle_green));
        pendingLicenseButton.setBackgroundResource(R.color.medTurquoise);
        pendingLicenseButton.setTextColor(getResources().getColor(R.color.colorWhite));
        pendingServicesButton.setBackgroundResource(R.color.medTurquoise);
        pendingServicesButton.setTextColor(getResources().getColor(R.color.colorWhite));
        pendingEducButton.setBackgroundResource(R.color.medTurquoise);
        pendingEducButton.setTextColor(getResources().getColor(R.color.colorWhite));
        pendingSpecialtyButton.setPaintFlags(pendingSpecialtyButton.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        pendingLicenseButton.setPaintFlags(pendingLicenseButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
        pendingServicesButton.setPaintFlags(pendingServicesButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
        pendingEducButton.setPaintFlags(pendingEducButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
    }
}
