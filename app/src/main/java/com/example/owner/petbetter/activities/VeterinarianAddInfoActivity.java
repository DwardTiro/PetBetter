package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.SQLException;
import java.util.ArrayList;

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
    private int userId;

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

        Bundle extras = getIntent().getExtras();
        firstName = extras.getString("first_name");
        lastName = extras.getString("last_name");
        emailAddress = extras.getString("email_address");
        password = extras.getString("password");
        userType = extras.getInt("user_type");
        specialty = vetSpecialtySpinner.getSelectedItem().toString();
        phoneNum = phoneNumTextView.getText().toString();
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

    private int generateNewUserId(){
        int newId;
        try{
            petBetterDb.openDatabase();
        } catch (SQLException e){
            e.printStackTrace();
        }
        ArrayList<Integer> ids = petBetterDb.generateUserIds();
        if(ids.size() != 0){
            newId = ids.get(ids.size() - 1);
            newId += 1;
        }
        else
            newId = 1;

        petBetterDb.closeDatabase();

        return newId;
    }

    private long addUsertoDB(
            int userId,
            String firstName,
            String lastName,
            String emailAddress,
            String password,
            int userType
    ) {

        try{
            petBetterDb.openDatabase();
        } catch (SQLException e){
            e.printStackTrace();
        }

        long result = petBetterDb.addUser(userId,firstName,lastName,emailAddress,password,userType);
        petBetterDb.closeDatabase();

        return result;
    }

    private void dataSync(int n) {
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        petBetterDb.dataSynced(n);
        petBetterDb.closeDatabase();

    }

    private ArrayList<User> getUnsyncedUsers(){
        try{
            petBetterDb.openDatabase();
        }catch (SQLException e){
            e.printStackTrace();
        }
        ArrayList<User> result = petBetterDb.getUnsyncedUsers();

        petBetterDb.closeDatabase();

        return result;
    }

    public long setUsers(ArrayList<User> userList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setUsers(userList);
        petBetterDb.closeDatabase();

        return result;
    }

    private User getNewUser(String email) {
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        User result = petBetterDb.getNewUser(email);

        petBetterDb.closeDatabase();

        return result;
    }

    private void syncUserChanges(){
        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        System.out.println("WE HERE BOOIII");
        ArrayList<User> unsyncedUsers = getUnsyncedUsers();
        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(unsyncedUsers);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addUsers(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    System.out.println("Users ADDED YEY");
                    dataSync(12);

                    final Call<ArrayList<User>> call2 = service2.getUsers();
                    call2.enqueue(new Callback<ArrayList<User>>() {
                        @Override
                        public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                            if(response.isSuccessful()){
                                System.out.println("Number of clinics from server: "+response.body().size());
                                setUsers(response.body());
                                userId = (int)getNewUser(emailAddress).getUserId();
                                System.out.println("User id is: " + userId);
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                            Log.d("onFailure", t.getLocalizedMessage());

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
    }


    public void signUpVeterinarian(View view) {

        uploadUsertoDB();
    }



    private void uploadUsertoDB(){
        int newId = generateNewUserId();
        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);


        User user = new User(firstName, lastName, emailAddress, password, 1);

        //addUsertoDB(newId,firstName,lastName,emailAddress,password, 1);

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(user);
        System.out.println(jsonArray);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());

        Call<Void> call = service.addUser(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("User added to server successfully");
                getUserFromDB();

                //dataSync(12);

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("FAILED TO ADD USER TO SERVER");
            }
        });
    }

    public void getUserFromDB(){
        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        //final User user;
        Call<User> call = service.checkLogin(emailAddress, password);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                System.out.println("User retrieved from server successfully");
                final User user = response.body();
                userId = (int)user.getUserId();
                uploadVetToDB();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println("Failed to retrieve user from server.");
            }
        });
        //return user;
    }


    public void uploadVetToDB(){
        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        //System.out.println("Got user with ID: "+user.getUserId());
        //User user = getUser(emailAddress);

        /*
        Veterinarian vet = new Veterinarian(99,
                userId,
                lastName,
                firstName,
                null,
                phoneNum,
                emailAddress,
                password,
                0,
                userType,
                null,
                specialty,
                0);
        */
        Gson gson = new GsonBuilder().serializeNulls().create();
        //String jsonArray = gson.toJson(vet);
        //System.out.println(jsonArray);
        //RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());

        Call<Void> call = service.addVet(userId, specialty, 0, "09567761376");
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
