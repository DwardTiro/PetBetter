package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.Pending;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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
    private String education;
    private int isLicensed = 1;
    private String profileDesc;

    private EditText editEducation;
    private SwitchCompat switchLicense;
    private EditText editProfileDesc;
    private EditText phoneNumTextView;
    private Spinner vetSpecialtySpinner;
    private DataAdapter petBetterDb;
    HerokuService service;
    HerokuService service2;
    private ImageButton exitButton;
    private EditText editEducation2;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_veterinarian_add_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.signUpToolbar);
        toolbar.setBackgroundColor(getResources().getColor((R.color.main_White)));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final TextView activityTitle = (TextView) findViewById(R.id.activity_title);
        activityTitle.setText("Sign Up");
        activityTitle.setTextColor(getResources().getColor(R.color.myrtle_green));
        exitButton = (ImageButton) findViewById(R.id.viewPostToolbarBack);
        exitButton.setBackgroundColor(getResources().getColor(R.color.main_White));

        vetSpecialtySpinner = (Spinner) findViewById(R.id.vetSpecialtySpinner);
        phoneNumTextView = (EditText) findViewById(R.id.signUpVetTextPhoneNum);
        editEducation = (EditText) findViewById(R.id.signUpVetTextEducation);
        editEducation2 = (EditText) findViewById(R.id.signUpVetTextEducation2);

        switchLicense = (SwitchCompat) findViewById(R.id.switchLicense);
        editProfileDesc = (EditText) findViewById(R.id.editProfileDesc);
        switchLicense.setVisibility(View.GONE);
        initializeDatabase();



        Bundle extras = getIntent().getExtras();
        firstName = extras.getString("first_name");
        lastName = extras.getString("last_name");
        emailAddress = extras.getString("email_address");
        password = extras.getString("password");
        userType = extras.getInt("user_type");
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

    private long addPending(int id, int foreignId, int type, int isApproved) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        long result = petBetterDb.addPending(id, foreignId, type, isApproved);
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

    private int generateNewPendingId(){
        int newId;
        try{
            petBetterDb.openDatabase();
        } catch (SQLException e){
            e.printStackTrace();
        }
        ArrayList<Integer> ids = petBetterDb.generatePendingIds();
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

        specialty = vetSpecialtySpinner.getSelectedItem().toString();
        phoneNum = phoneNumTextView.getText().toString();
        education = editEducation.getText().toString()+","+editEducation2.getText().toString();
        profileDesc = editProfileDesc.getText().toString();
        uploadUsertoDB();
    }



    private void uploadUsertoDB(){
        int newId = generateNewUserId();
        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);


        User user = new User(firstName, lastName, emailAddress, password, 1, 0);

        //addUsertoDB(newId,firstName,lastName,emailAddress,password, 1);

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(user);
        System.out.println(jsonArray);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());

        Call<ResponseBody> call = service.addUser(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    String strResponse = response.body().string();
                    System.out.println("Server response: "+strResponse);

                    if(strResponse.equals("User registered")){
                        System.out.println("User added to server successfully");
                        getUserFromDB();
                    }
                    else{
                        Toast.makeText(VeterinarianAddInfoActivity.this, "Email is already taken", Toast.LENGTH_SHORT ).show();
                        //strResponse.equals("");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(VeterinarianAddInfoActivity.this, "Email is already taken", Toast.LENGTH_SHORT ).show();
                } catch(NullPointerException npe){
                    System.out.println("User added to server successfully");
                    getUserFromDB();
                }



                //dataSync(12);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
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

        //id, foreignid, type, isapproved
        int id;
        if(education!=""){
            id = generateNewPendingId();
            addPending(id, userId, 1, 0);
        }

        if(isLicensed==1){
            id = generateNewPendingId();
            addPending(id, userId, 2, 0);
        }

        if(specialty!=""){
            id = generateNewPendingId();
            addPending(id, userId, 4, 0);
        }



        Call<Integer> call = service.addVet(userId, specialty, 0, education, isLicensed, profileDesc);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                System.out.println("User added to server successfully");

                syncPendingChanges();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                String timeStamp = sdf.format(new Date());
                ArrayList<Post> posts = new ArrayList<Post>();
                Post thisPost = new Post(0, 20, "Dr. "+firstName+" "+lastName+", DVM review", "",49,timeStamp, null, response.body(), 1, 0);
                //back here
                posts.add(thisPost);
                uploadPost(posts);
                Intent intent = new Intent(VeterinarianAddInfoActivity.this, com.example.owner.petbetter.activities.MainActivity.class);
                startActivity(intent);
                Toast.makeText(VeterinarianAddInfoActivity.this, "Thank you for registering as a veterinarian. Please check your email." +
                        "We've sent you some verification instructions for your new facility.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                System.out.println("FAILED TO ADD USER TO SERVER");
            }
        });
    }

    private void uploadPost(ArrayList<Post> posts){
        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        System.out.println("HOW MANY POSTS? "+posts.size());

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(posts);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addPosts(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                dataSynced(9);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
                Toast.makeText(VeterinarianAddInfoActivity.this, "Unable to upload posts on server", Toast.LENGTH_LONG);
            }
        });
    }

    public void syncPendingChanges(){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        System.out.println("WE HERE BOOIII");
        ArrayList<Pending> unsyncedPending = getUnsyncedPending();
        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(unsyncedPending);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addPending(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    System.out.println("PENDING ADDED YEY");
                    dataSynced(17);

                    final Call<ArrayList<Pending>> call2 = service2.getPending();
                    call2.enqueue(new Callback<ArrayList<Pending>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Pending>> call, Response<ArrayList<Pending>> response) {
                            if(response.isSuccessful()){
                                System.out.println("Number of pending from server: "+response.body().size());
                                setPending(response.body());
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Pending>> call, Throwable t) {
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

    private ArrayList<Pending> getUnsyncedPending(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Pending> result = petBetterDb.getUnsyncedPending();
        petBetterDb.closeDatabase();

        return result;
    }

    private void dataSynced(int n){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        petBetterDb.dataSynced(n);
        petBetterDb.closeDatabase();

    }

    public long setPending(ArrayList<Pending> pendingList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setPending(pendingList);
        petBetterDb.closeDatabase();

        return result;
    }
}
