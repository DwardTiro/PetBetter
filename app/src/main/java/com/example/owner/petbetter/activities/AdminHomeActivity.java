package com.example.owner.petbetter.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.owner.petbetter.R;

public class AdminHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        System.out.println("ADMIN PAGE");
    }
}
