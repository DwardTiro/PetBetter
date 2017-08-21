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
        vetLandline = (TextView) view.findViewById(R.id.profileLandLine);

    }

    public ImageView getProfileBG(){
        return profileBG;
    }
    public TextView getVetName(){
        return vetName;
    }

    public TextView getVetLandline(){
        return vetLandline;
    }
    public void setProfileBG(){

    }
    public void setVetName(String str){
        vetName.setText(str);
    }
    public void setVetLandline(String str){
        vetLandline.setText(str);
    }

    //Integrate to db to display stuff on page
}
