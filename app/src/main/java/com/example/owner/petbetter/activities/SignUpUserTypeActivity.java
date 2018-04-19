package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.owner.petbetter.R;

/**
 * Created by Kristian on 8/5/2017.
 */

public class SignUpUserTypeActivity extends AppCompatActivity {

    private Button signUpBack;

    @Override
    protected void onCreate(Bundle savedInstance) {

        super.onCreate(savedInstance);
        setContentView(R.layout.activity_sign_up_select_user_type);

        Toolbar toolbar = (Toolbar) findViewById(R.id.signUpToolbar);
        toolbar.setBackgroundColor(getResources().getColor((R.color.colorWhite)));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final TextView activityTitle = (TextView) findViewById(R.id.activity_title);
        activityTitle.setText("");

    }

    public void signUpBackButtonClicked(View view) {
        Intent intent = new Intent(this, com.example.owner.petbetter.activities.MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void petOwnerTypeButtonClicked(View view) {
        Bundle extras = new Bundle();
        extras.putInt("USERTYPE", 2);
        Intent intent = new Intent(
                this, SignUpUserActivity.class
        );
        intent.putExtras(extras);
        startActivity(intent);
    }

    public void veterinarianTypeButtonClicked(View view) {
        Bundle extras = new Bundle();
        extras.putInt("USERTYPE", 1);
        Intent intent = new Intent(
                this, SignUpUserActivity.class
        );
        intent.putExtras(extras);
        startActivity(intent);
    }

    public void businessTypeButtonClicked(View view) {
        Bundle extras = new Bundle();
        extras.putInt("USERTYPE", 4);
        Intent intent = new Intent(
                this, SignUpUserActivity.class
        );
        intent.putExtras(extras);
        startActivity(intent);
    }

    //code if pet owner is selected

    //code if veterinarian is selected


}
