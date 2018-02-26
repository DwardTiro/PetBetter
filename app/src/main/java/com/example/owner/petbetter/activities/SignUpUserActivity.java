package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kristian on 1/29/2018.
 */

public class SignUpUserActivity extends AppCompatActivity {

    private Button nextButton;

    private EditText signupFirstName;
    private EditText signupLastName;
    private EditText signupEmail;
    private TextInputLayout signupPassword;

    HerokuService service;

    @Override
    protected void onCreate(Bundle savedInstance) {

        super.onCreate(savedInstance);
        setContentView(R.layout.activity_sign_up_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.signUpToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final TextView activityTitle = (TextView) findViewById(R.id.activity_title);
        activityTitle.setText("Sign Up");
        //backButton = (ImageButton) findViewById(R.id.signUpPetOwnerBackButton);

        //jsonMyObject = extras.getString("thisSignUp");

        signupFirstName = (EditText) findViewById(R.id.signUpTextFirstName);
        signupLastName = (EditText) findViewById(R.id.signUpTextLastName);
        signupEmail = (EditText) findViewById(R.id.signUpTextEmailAddress);
        signupPassword = (TextInputLayout) findViewById(R.id.textInputLayoutSignUp);
        nextButton = (Button) findViewById(R.id.signUpNextButton);

    }

    public void signUpNext(View v){
        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        Bundle extras = getIntent().getExtras();
        int userType = extras.getInt("USERTYPE");
        User user = new User(signupFirstName.getText().toString(), signupLastName.getText().toString(),
                signupEmail.getText().toString(), signupPassword.getEditText().getText().toString(), userType);

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(user);
        System.out.println(jsonArray);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());

        Call<Void> call = service.addUser(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("User added to server successfully");
                Intent intent = new Intent(
                        SignUpUserActivity.this,
                        com.example.owner.petbetter.activities.SignUpUserTypeActivity.class
                );
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("FAILED TO ADD USER TO SERVER");
            }
        });
    }

    public void signUpBackButtonClicked(View view) {
        Intent intent = new Intent(
                SignUpUserActivity.this,
                com.example.owner.petbetter.activities.SignUpUserTypeActivity.class
        );

        startActivity(intent);

    }

}
