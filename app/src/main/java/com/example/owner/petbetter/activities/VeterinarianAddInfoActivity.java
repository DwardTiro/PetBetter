package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.SQLException;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kristian on 1/29/2018.
 */

public class VeterinarianAddInfoActivity extends AppCompatActivity {

    private String firstName;
    private String lastName;
    private String phoneNum;
    private String specialty;
    private String emailAddress;
    private String password;
    private int userType;

    private TextView phoneNumTextView;
    private Spinner vetSpecialtySpinner;
    private DataAdapter petBetterDb;
    HerokuService service;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_veterinarian_add_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.signUpToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final TextView activityTitle = (TextView) findViewById(R.id.activity_title);
        activityTitle.setText("Sign Up");

        vetSpecialtySpinner = (Spinner) findViewById(R.id.vetSpecialtySpinner);
        phoneNumTextView = (TextView) findViewById(R.id.signUpVetTextPhoneNum);
        initializeDatabase();
    }

    public void signUpBackButtonClicked(View view) {
        Intent intent = new Intent(
                VeterinarianAddInfoActivity.this,
                com.example.owner.petbetter.activities.SignUpUserTypeActivity.class
        );

        startActivity(intent);
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

    public void signUpVeterinarian(View view) {
        Bundle extras = getIntent().getExtras();
        firstName = extras.getString("first_name");
        lastName = extras.getString("last_name");
        emailAddress = extras.getString("email_address");
        password = extras.getString("password");
        userType = extras.getInt("user_type");
        specialty = vetSpecialtySpinner.getSelectedItem().toString();
        phoneNum = phoneNumTextView.getText().toString();
        uploadUsertoDB();
        uploadVetToDB();


    }



    private void uploadUsertoDB(){
        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);


        User user = new User(firstName, lastName, emailAddress, password, 1);

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(user);
        System.out.println(jsonArray);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());

        Call<Void> call = service.addUser(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("User added to server successfully");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("FAILED TO ADD USER TO SERVER");
            }
        });
    }

    public void uploadVetToDB(){
        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        User user = getUser(emailAddress);

        Veterinarian vet = new Veterinarian(99,
                user.getUserId(),
                lastName,
                firstName,
                phoneNum,
                phoneNum,
                emailAddress,
                password,
                0,
                userType,
                null,
                specialty,
                0);

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(vet);
        System.out.println(jsonArray);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());

        Call<Void> call = service.addVet(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("User added to server successfully");
                Intent intent = new Intent(VeterinarianAddInfoActivity.this, com.example.owner.petbetter.activities.MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("FAILED TO ADD USER TO SERVER");
            }
        });
    }
}
