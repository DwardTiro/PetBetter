package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kristian on 10/23/2017.
 */

public class EditProfileActivity extends AppCompatActivity {

    private EditText firstNameEdit;
    private EditText lastNameEdit;
    private EditText emailEdit;
    private EditText mobileEdit;
    private EditText phoneEdit;
    private Button saveButton;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    private HerokuService service;


    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        setContentView(R.layout.activity_edit_profile);

        firstNameEdit = (EditText) findViewById(R.id.editUserFirstName);
        lastNameEdit = (EditText) findViewById(R.id.editUserLastName);
        emailEdit = (EditText) findViewById(R.id.editUserEmail);
        mobileEdit = (EditText) findViewById(R.id.editUserMobileNum);
        phoneEdit = (EditText) findViewById(R.id.editUserLandline);
        saveButton = (Button) findViewById(R.id.saveButton);

        systemSessionManager = new SystemSessionManager(this);
        if (systemSessionManager.checkLogin())
            finish();


        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();


        String email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        System.out.println(email);
        user = getUser(email);

        firstNameEdit.setText(user.getFirstName());
        lastNameEdit.setText(user.getLastName());
        emailEdit.setText(user.getEmail());
        mobileEdit.setText(user.getMobileNumber());
        phoneEdit.setText(user.getPhoneNumber());


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile(user.getUserId(), firstNameEdit.getText().toString(), lastNameEdit.getText().toString(),
                        emailEdit.getText().toString(), mobileEdit.getText().toString(), phoneEdit.getText().toString());
                service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
                final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

                final Call<User> call = service.getUser(user.getEmail());
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        //User thisUser = response.body();
                        User thisUser = getUserWithId((int) user.getUserId());
                        thisUser.setUserId(response.body().getUserId());

                        Gson gson = new GsonBuilder().serializeNulls().create();
                        String jsonArray = gson.toJson(thisUser);
                        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
                        //RequestBody doesn't contain message_photo.
                        final Call<Void> call2 = service2.editProfile(body);
                        call2.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if(response.isSuccessful()){
                                    dataSynced(12);
                                    //successfully updated remote db

                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.d("onFailure", t.getLocalizedMessage());

                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d("onFailure", t.getLocalizedMessage());
                        Toast.makeText(EditProfileActivity.this, "Unable to update user on server", Toast.LENGTH_LONG);
                    }
                });
                finish();

            }
        });

    }


    private void initializeDatabase() {

        petBetterDb = new DataAdapter(this);

        try {
            petBetterDb.createDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private User getUser(String email) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        User result = petBetterDb.getUser(email);
        petBetterDb.closeDatabase();

        return result;
    }

    private User getUserWithId(int id) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        User result = petBetterDb.getUserWithId(id);
        petBetterDb.closeDatabase();

        return result;
    }

    private void editProfile(long _id, String firstName, String lastName, String emailAddress, String mobileNum, String landline) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        petBetterDb.editProfile(_id, firstName, lastName, emailAddress, mobileNum, landline);
        petBetterDb.closeDatabase();
    }

    private void dataSynced(int n) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        petBetterDb.dataSynced(n);
        petBetterDb.closeDatabase();
    }

    public void editBackClicked(View view){
        finish();
    }

}
