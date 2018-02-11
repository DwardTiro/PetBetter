package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.owner.petbetter.R;

/**
 * Created by Kristian on 1/29/2018.
 */

public class SignUpPetOwnerActivity extends AppCompatActivity {

    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstance) {

        super.onCreate(savedInstance);
        setContentView(R.layout.activity_sign_up_pet_owner);

        Toolbar toolbar = (Toolbar) findViewById(R.id.signUpToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final TextView activityTitle = (TextView) findViewById(R.id.activity_title);
        activityTitle.setText("Sign Up");
        //backButton = (ImageButton) findViewById(R.id.signUpPetOwnerBackButton);

        Bundle extras = getIntent().getExtras();
        String jsonMyObject;
        //jsonMyObject = extras.getString("thisSignUp");


    }

    public void signUpBackButtonClicked(View view) {
        Intent intent = new Intent(
                SignUpPetOwnerActivity.this,
                com.example.owner.petbetter.activities.SignUpUserTypeActivity.class
        );
        startActivity(intent);
    }

}
