package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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
    private ImageButton exitButton;

    HerokuService service;

    @Override
    protected void onCreate(Bundle savedInstance) {

        super.onCreate(savedInstance);
        setContentView(R.layout.activity_sign_up_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.signUpToolbar);
        toolbar.setBackgroundColor(getResources().getColor((R.color.main_White)));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final TextView activityTitle = (TextView) findViewById(R.id.activity_title);
        activityTitle.setText("Sign Up");
        activityTitle.setTextColor(getResources().getColor(R.color.myrtle_green));
        exitButton = (ImageButton) findViewById(R.id.viewPostToolbarBack);
        exitButton.setBackgroundColor(getResources().getColor(R.color.main_White));
        //backButton = (ImageButton) findViewById(R.id.signUpPetOwnerBackButton);

        //jsonMyObject = extras.getString("thisSignUp");

        signupFirstName = (EditText) findViewById(R.id.signUpTextFirstName);
        signupLastName = (EditText) findViewById(R.id.signUpTextLastName);
        signupEmail = (EditText) findViewById(R.id.signUpTextEmailAddress);
        signupPassword = (TextInputLayout) findViewById(R.id.textInputLayoutSignUp);
        nextButton = (Button) findViewById(R.id.signUpNextButton);

        signupFirstName.addTextChangedListener(formWatcher);
        signupLastName.addTextChangedListener(formWatcher);
        signupEmail.addTextChangedListener(formWatcher);
        signupPassword.getEditText().addTextChangedListener(formWatcher);

    }

    private final TextWatcher formWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (signupFirstName.getText().toString().length() == 0 ||
                    signupLastName.getText().toString().length() == 0 ||
                    signupEmail.getText().toString().length() == 0 ||
                    signupPassword.getEditText().getText().length() == 0) {
                nextButton.setEnabled(false);
            } else {
                nextButton.setEnabled(true);

            }
        }
    };

    public void signUpNext(View v) {
        Bundle extras = getIntent().getExtras();
        final int userType = extras.getInt("USERTYPE");
        //still fixing function wont go here yet

        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);


        User user = new User(signupFirstName.getText().toString(), signupLastName.getText().toString(),
                signupEmail.getText().toString(), signupPassword.getEditText().getText().toString(), userType, 0);

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(user);
        System.out.println(jsonArray);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());

        Call<ResponseBody> call = service.addUser(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        String strResponse = response.body().string();
                        System.out.println("Server response: "+strResponse);

                        if(strResponse.equals("User registered")){
                            if(userType==1){
                                Intent intent = new Intent(SignUpUserActivity.this,
                                        com.example.owner.petbetter.activities.VeterinarianAddInfoActivity.class);
                                Bundle vetExtras = new Bundle();
                                vetExtras.putInt("user_type", userType);
                                vetExtras.putString("first_name", signupFirstName.getText().toString());
                                vetExtras.putString("last_name", signupLastName.getText().toString());
                                vetExtras.putString("email_address", signupEmail.getText().toString());
                                vetExtras.putString("password", signupPassword.getEditText().getText().toString());
                                intent.putExtras(vetExtras);
                                startActivity(intent);
                            }
                            if(userType==2||userType==4){
                                Intent intent = new Intent(
                                        SignUpUserActivity.this,
                                        com.example.owner.petbetter.activities.MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(SignUpUserActivity.this, "Thank you for registering. Welcome to PetBetter", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                            Toast.makeText(SignUpUserActivity.this, "Email is already taken", Toast.LENGTH_SHORT ).show();
                            //strResponse.equals("");
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(SignUpUserActivity.this, "Email is already taken", Toast.LENGTH_SHORT ).show();
                    }

                }
                else{
                    Toast.makeText(SignUpUserActivity.this, "Email is already taken", Toast.LENGTH_SHORT ).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("FAILED TO ADD USER TO SERVER");
                Toast.makeText(SignUpUserActivity.this, "Email is already taken", Toast.LENGTH_SHORT ).show();
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
