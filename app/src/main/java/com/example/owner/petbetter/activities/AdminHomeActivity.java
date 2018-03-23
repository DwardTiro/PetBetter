package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminHomeActivity extends AppCompatActivity {
    private Button pendingInfoButton;
    private Button userListButton;
    private Button topicListButton;
    private Button postListButton;
    private int currFragment = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

    }

    public void pendingClicked(View v){
        Intent intent = new Intent(this, com.example.owner.petbetter.activities.PendingActivity.class);
        startActivity(intent);
    }

    public void monitorVetsClicked(View v){
        Intent intent = new Intent(this, com.example.owner.petbetter.activities.MonitorVetsActivity.class);
        startActivity(intent);
    }


}
