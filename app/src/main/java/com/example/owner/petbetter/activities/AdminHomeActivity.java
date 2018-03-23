package com.example.owner.petbetter.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.owner.petbetter.services.NotificationReceiver;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminHomeActivity extends AppCompatActivity {
    private NotificationReceiver notifReceiver = new NotificationReceiver();

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

    public void adminLogoutClicked(View v){
        Intent intent = new Intent(this, com.example.owner.petbetter.activities.MainActivity.class);
        SharedPreferences preferences =getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();

        Intent intentLogout = new Intent().setAction("com.package.ACTION_LOGOUT");
        notifReceiver.onReceive(this, intentLogout);
        sendBroadcast(intentLogout);
        startActivity(intent);
    }


}
