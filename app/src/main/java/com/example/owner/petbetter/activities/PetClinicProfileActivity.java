package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.owner.petbetter.R;


public class PetClinicProfileActivity extends AppCompatActivity {


    private TextView petClinicName;
    private TextView petClinicAddress;
    private TextView petClinicLandline;
    private TextView petClinicOpenTime;
    private TextView petClinicCloseTime;
    private TextView petClinicRating;

    private ImageView petClinicImage;
    private ImageView petClinicServiceOne;
    private ImageView petClinicServiceTwo;
    private ImageView petClinicServiceThree;

    private Button petClinicMessageButton;
    private Button petClinicRateButton;
    private Button petClinicBookmarkButton;

    private LinearLayout serviceContainer;
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_petclinic_profile);

        petClinicName = (TextView) findViewById(R.id.clinicName);
        petClinicAddress = (TextView) findViewById(R.id.clinicAddress);
        petClinicLandline = (TextView) findViewById(R.id.clinicLandLine);
        petClinicOpenTime = (TextView) findViewById(R.id.clinicOpenTime);
        petClinicCloseTime = (TextView) findViewById(R.id.clinicCloseTime);
        petClinicRating = (TextView) findViewById(R.id.clinicRatingNumerator);

        petClinicImage = (ImageView) findViewById(R.id.clinicProfileImage);
        petClinicServiceOne = (ImageView) findViewById(R.id.imageServiceOne);
        petClinicServiceTwo = (ImageView) findViewById(R.id.imageServiceTwo);
        petClinicServiceThree = (ImageView) findViewById(R.id.imageServiceThree);

        serviceContainer = (LinearLayout) findViewById(R.id.serviceContainer);
    }

    //get methods
    public TextView getPetClinicName(){
        return petClinicName;
    }
    public TextView getPetClinicAddress(){
        return petClinicAddress;
    }
    public TextView getPetClinicLandline(){
        return petClinicLandline;
    }
    public TextView getPetClinicOpenTime(){
        return petClinicOpenTime;
    }
    public TextView getPetClinicCloseTime(){
        return petClinicCloseTime;
    }
    public TextView getPetClinicRating(){
        return petClinicRating;
    }
    public ImageView getPetClinicImage(){
        return petClinicImage;
    }
    public ImageView getPetClinicServiceOne(){
        return petClinicServiceOne;
    }
    public ImageView getPetClinicServiceTwo(){
        return petClinicServiceTwo;
    }
    public ImageView getPetClinicServiceThree(){
        return petClinicServiceThree;
    }
    public LinearLayout getServiceContainer(){
        return serviceContainer;
    }

    //set methods
    public void setPetClinicName(String str){
        petClinicName.setText(str);
    }
    public void setPetClinicAddress(String str){
        petClinicAddress.setText(str);
    }
    public void setPetClinicLandline(String str){
        petClinicLandline.setText(str);
    }
    public void setPetClinicOpenTime(String str){
        petClinicOpenTime.setText(str);
    }
    public void setPetClinicCloseTime(String str){
        petClinicCloseTime.setText(str);
    }
    public void setPetClinicRating(String str){
        petClinicRating.setText(str);
    }





    public void clinicBackClicked(View view){
        Intent intent = new Intent(this, com.example.owner.petbetter.activities.HomeActivity.class);
        startActivity(intent);
    }
}
