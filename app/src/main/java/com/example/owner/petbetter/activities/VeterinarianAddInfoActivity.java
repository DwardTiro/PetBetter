package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.RequestBody;

/**
 * Created by Kristian on 1/29/2018.
 */

public class VeterinarianAddInfoActivity extends AppCompatActivity {

    private Button signUpButton;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextEmail;
    private TextInputEditText editTextPassword;
    private Spinner vetSpecialtySpinner;
    HerokuService service;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_veterinarian_add_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.signUpToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final TextView activityTitle = (TextView) findViewById(R.id.activity_title);
        activityTitle.setText("Sign Up");

        vetSpecialtySpinner = (Spinner) findViewById(R.id.vetSpecialtySpinner);
    }

    public void signUpBackButtonClicked(View view) {
        Intent intent = new Intent(
                VeterinarianAddInfoActivity.this,
                com.example.owner.petbetter.activities.SignUpUserTypeActivity.class
        );

        startActivity(intent);

    }

    public void signUpVeterinarian(View view){
        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        User user = new User(editTextFirstName.getText().toString(), editTextLastName.getText().toString(),
                editTextEmail.getText().toString(), editTextPassword.getText().toString(), 1);

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(user);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());


        Toast.makeText(VeterinarianAddInfoActivity.this, vetSpecialtySpinner.getSelectedItem().toString(),
                Toast.LENGTH_SHORT);
        Bundle extras = getIntent().getExtras();
        int usertype = extras.getInt("USERTYPE");
        System.out.println(vetSpecialtySpinner.getSelectedItem().toString() + usertype);

    }
}
