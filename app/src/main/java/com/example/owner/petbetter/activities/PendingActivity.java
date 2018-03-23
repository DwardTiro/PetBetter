package com.example.owner.petbetter.activities;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.owner.petbetter.R;

public class PendingActivity extends AppCompatActivity {

    private Button pendingEducButton;
    private Button pendingLicenseButton;
    private Button pendingServicesButton;
    private Button pendingAdminButton;
    private int currFragment = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);

        pendingEducButton = (Button) findViewById(R.id.pendingEducButton);
        pendingLicenseButton = (Button) findViewById(R.id.pendingLicenseButton);
        pendingServicesButton = (Button) findViewById(R.id.pendingServicesButton);
        pendingAdminButton = (Button) findViewById(R.id.pendingAdminButton);

        pendingEducClicked(this.findViewById(android.R.id.content));
    }

    public void pendingEducClicked(View view){
        currFragment = 1;
        pendingEducButton.setBackgroundResource(R.color.main_White);
        pendingEducButton.setTextColor(getResources().getColor(R.color.myrtle_green));
        pendingLicenseButton.setBackgroundResource(R.color.medTurquoise);
        pendingLicenseButton.setTextColor(getResources().getColor(R.color.colorWhite));
        pendingServicesButton.setBackgroundResource(R.color.medTurquoise);
        pendingServicesButton.setTextColor(getResources().getColor(R.color.colorWhite));
        pendingAdminButton.setBackgroundResource(R.color.medTurquoise);
        pendingAdminButton.setTextColor(getResources().getColor(R.color.colorWhite));
        pendingEducButton.setPaintFlags(pendingEducButton.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        pendingLicenseButton.setPaintFlags(pendingLicenseButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
        pendingServicesButton.setPaintFlags(pendingServicesButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
        pendingAdminButton.setPaintFlags(pendingAdminButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
    }

    public void setPendingLicenseButton(View view){
        currFragment = 2;
        pendingLicenseButton.setBackgroundResource(R.color.main_White);
        pendingLicenseButton.setTextColor(getResources().getColor(R.color.myrtle_green));
        pendingEducButton.setBackgroundResource(R.color.medTurquoise);
        pendingEducButton.setTextColor(getResources().getColor(R.color.colorWhite));
        pendingServicesButton.setBackgroundResource(R.color.medTurquoise);
        pendingServicesButton.setTextColor(getResources().getColor(R.color.colorWhite));
        pendingAdminButton.setBackgroundResource(R.color.medTurquoise);
        pendingAdminButton.setTextColor(getResources().getColor(R.color.colorWhite));
        pendingLicenseButton.setPaintFlags(pendingLicenseButton.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        pendingEducButton.setPaintFlags(pendingEducButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
        pendingServicesButton.setPaintFlags(pendingServicesButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
        pendingAdminButton.setPaintFlags(pendingAdminButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
    }

    public void pendingServicesClicked(View view){
        currFragment = 3;
        pendingServicesButton.setBackgroundResource(R.color.main_White);
        pendingServicesButton.setTextColor(getResources().getColor(R.color.myrtle_green));
        pendingLicenseButton.setBackgroundResource(R.color.medTurquoise);
        pendingLicenseButton.setTextColor(getResources().getColor(R.color.colorWhite));
        pendingEducButton.setBackgroundResource(R.color.medTurquoise);
        pendingEducButton.setTextColor(getResources().getColor(R.color.colorWhite));
        pendingAdminButton.setBackgroundResource(R.color.medTurquoise);
        pendingAdminButton.setTextColor(getResources().getColor(R.color.colorWhite));
        pendingServicesButton.setPaintFlags(pendingServicesButton.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        pendingLicenseButton.setPaintFlags(pendingLicenseButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
        pendingEducButton.setPaintFlags(pendingEducButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
        pendingAdminButton.setPaintFlags(pendingAdminButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
    }

    public void pendingAdminClicked(View view){
        currFragment = 4;
        pendingAdminButton.setBackgroundResource(R.color.main_White);
        pendingAdminButton.setTextColor(getResources().getColor(R.color.myrtle_green));
        pendingLicenseButton.setBackgroundResource(R.color.medTurquoise);
        pendingLicenseButton.setTextColor(getResources().getColor(R.color.colorWhite));
        pendingServicesButton.setBackgroundResource(R.color.medTurquoise);
        pendingServicesButton.setTextColor(getResources().getColor(R.color.colorWhite));
        pendingEducButton.setBackgroundResource(R.color.medTurquoise);
        pendingEducButton.setTextColor(getResources().getColor(R.color.colorWhite));
        pendingAdminButton.setPaintFlags(pendingAdminButton.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        pendingLicenseButton.setPaintFlags(pendingLicenseButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
        pendingServicesButton.setPaintFlags(pendingServicesButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
        pendingEducButton.setPaintFlags(pendingEducButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
    }
}
