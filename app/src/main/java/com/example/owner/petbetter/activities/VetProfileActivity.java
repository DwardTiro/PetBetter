package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.owner.petbetter.R;
import com.google.android.gms.vision.text.Text;

/**
 * Created by Kristian on 8/8/2017.
 */

public class VetProfileActivity extends AppCompatActivity {

    private ImageView profileBG;
    private TextView vetName;
    private TextView vetClinicAddress;
    private TextView vetTime;
    private TextView vetLandline;
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_vet_profile);
    }
    public void backClicked(View view){
        Intent intent = new Intent(this, com.example.owner.petbetter.activities.HomeActivity.class);
        startActivity(intent);

        profileBG = (ImageView) view.findViewById(R.id.profileImage);
        vetName = (TextView) view.findViewById(R.id.vetListName);
        vetClinicAddress = (TextView) view.findViewById(R.id.vetListAddress);
        vetTime = (TextView) view.findViewById(R.id.profileOperatingTime);
        vetLandline = (TextView) view.findViewById(R.id.profileLandLine);

    }

    public ImageView getProfileBG(){
        return profileBG;
    }
    public TextView getVetName(){
        return vetName;
    }
    public TextView getVetClinicAddress(){
        return vetClinicAddress;
    }
    public TextView getVetTime(){
        return vetTime;
    }
    public TextView getVetLandline(){
        return vetLandline;
    }
    public void setProfileBG(){

    }
    public void setVetName(String str){
        vetName.setText(str);
    }
    public void setVetClinicAddress(String str){
        vetClinicAddress.setText(str);
    }
    public void setVetTime(String str){
        vetTime.setText(str);
    }
    public void setVetLandline(String str){
        vetLandline.setText(str);
    }
}
