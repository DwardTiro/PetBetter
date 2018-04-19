package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.Rating;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kristian on 10/19/2017.
 */

public class RateVetActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText reviewText;
    private TextView rateTitle;
    private Button rateButton;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    private Veterinarian vetItem;
    HerokuService service;


    private int pId, uId, rId;
    private String timeStamp;
    private TextView tvRating;
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_rate);


        Toolbar toolbar = (Toolbar) findViewById(R.id.rateToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        reviewText = (EditText) findViewById(R.id.reviewEditText);
        rateTitle = (TextView) findViewById(R.id.rate_title);
        rateButton = (Button) findViewById(R.id.rateButton);
        tvRating = (TextView) findViewById(R.id.tvRating);


        systemSessionManager = new SystemSessionManager(this);

        if(systemSessionManager.checkLogin())
            finish();

        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();

        String email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);

        final String jsonMyObject;
        Bundle extras = getIntent().getExtras();
        jsonMyObject = extras.getString("thisVet");
        vetItem = new Gson().fromJson(jsonMyObject,Veterinarian.class);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rateButton.setEnabled(true);
                tvRating.setText(Float.toString(v));
            }
        });
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                timeStamp = sdf.format(new Date());
                pId = generateVetRatingId();
                System.out.println(ratingBar.getRating());

                float newRating = calculateRating(ratingBar.getRating(),vetItem.getId());
                System.out.println("New rating is "+newRating);
                createVetRating(pId, user.getUserId(),vetItem.getId(),ratingBar.getRating(),reviewText.getText().toString(),timeStamp,0);

                setNewRating(newRating, vetItem.getId());

                addRatings(getUnsyncedRatings());
                //update vet rating

                System.out.println("Recorded new rating is " + vetItem.getRating());
                System.out.println("New Specialty is " + vetItem.getSpecialty());

                finish();
                Intent intent = new Intent(view.getContext(),com.example.owner.petbetter.activities.HomeActivity.class);
                startActivity(intent);

            }
        });

    }
    private void initializeDatabase() {

        petBetterDb = new DataAdapter(this);

        try {
            petBetterDb.createDatabase();
        } catch(SQLException e ){
            e.printStackTrace();
        }

    }

    private void addRatings(ArrayList<Rating> ratings){
        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        System.out.println("HOW MANY RATINGS? "+ratings.size());

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(ratings);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addRatings(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                dataSynced(14);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
    }

    private User getUser(String email){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        User result = petBetterDb.getUser(email);
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Rating> getUnsyncedRatings(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Rating> result = petBetterDb.getUnsyncedRatings();
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

    private long createVetRating(int pId,long userId, long vet_id, float rating, String comment, String timeStamp, int isDeleted){
        long  result;

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        result = petBetterDb.createVetRating(pId,userId, vet_id, rating, comment, timeStamp, isDeleted);
        petBetterDb.closeDatabase();


        return result;
    }

    public int generateVetRatingId(){
        ArrayList<Integer> storedIds;
        int markerId = 1;

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        storedIds = petBetterDb.getRatingIds();
        petBetterDb.closeDatabase();


        if(storedIds.isEmpty()) {
            return markerId;
        } else {
            while (storedIds.contains(markerId)){
                markerId += 1;
            }

            return markerId;
        }
    }
    private float calculateRating(float rating, long vet_id){
        ArrayList<Float> ratings;


        float sumRating = 0,newRating;

        try{
            petBetterDb.openDatabase();
        } catch(SQLException e){
            e.printStackTrace();
        }
        ratings = petBetterDb.getVeterinarianRatings(vet_id);
        petBetterDb.closeDatabase();
        if(ratings.isEmpty()){

            newRating = rating ;
        }
        else{
            for(int i =0; i<ratings.size();i++){
                sumRating += ratings.get(i);
                System.out.println("New sum = "+sumRating);
            }
            sumRating += rating;
            System.out.println("New sum = "+sumRating);
            newRating = sumRating/(ratings.size()+1);
        }
        return newRating;

    }
    private void setNewRating(float newRating, long vet_id){

        try{
            petBetterDb.openDatabase();
        } catch(SQLException e){
            e.printStackTrace();
        }

        petBetterDb.setNewVetRating(newRating,vet_id);
        petBetterDb.closeDatabase();
    }
    public void rateBackButtonClicked(View view){
        finish();

    }


}
