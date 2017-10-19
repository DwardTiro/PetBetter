package com.example.owner.petbetter.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Kristian on 10/19/2017.
 */

public class RateFacilityActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText reviewText;
    private TextView rateTitle;
    private Button rateButton;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    private Facility faciItem;


    private int pId, uId, rId;
    private String timeStamp;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        setContentView(R.layout.activity_rate);

        Toolbar toolbar = (Toolbar) findViewById(R.id.rateToolbar);
        setSupportActionBar(toolbar);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        reviewText = (EditText) findViewById(R.id.reviewEditText);
        rateTitle = (TextView) findViewById(R.id.rate_title);
        rateButton = (Button) findViewById(R.id.rateButton);


        systemSessionManager = new SystemSessionManager(this);

        if (systemSessionManager.checkLogin())
            finish();

        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();

        String email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);

        final String jsonMyObject;
        Bundle extras = getIntent().getExtras();
        jsonMyObject = extras.getString("thisClinic");
        faciItem = new Gson().fromJson(jsonMyObject, Facility.class);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rateButton.setEnabled(true);

            }
        });
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                timeStamp = sdf.format(new Date());
                pId = generateFacilityRatingId();
                System.out.println(ratingBar.getRating());
                createFacilityRating(pId, user.getUserId(), faciItem.getId(), ratingBar.getRating(), reviewText.getText().toString(), timeStamp, 0);

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

    private long createFacilityRating(int pId, long userId, long facility_id, float rating, String comment, String timeStamp, int isDeleted) {
        long result;

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        result = petBetterDb.createFacilityRating(pId, userId, facility_id, rating, comment, timeStamp, isDeleted);
        petBetterDb.closeDatabase();


        return result;
    }

    public int generateFacilityRatingId() {
        ArrayList<Integer> storedIds;
        int markerId = 1;

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        storedIds = petBetterDb.getFacilityRatingIds();
        petBetterDb.closeDatabase();


        if (storedIds.isEmpty()) {
            return markerId;
        } else {
            while (storedIds.contains(markerId)) {
                markerId += 1;
            }

            return markerId;
        }
    }

    public void rateBackButtonClicked(View view) {
        finish();
    }


}
