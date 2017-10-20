package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by Kristian on 8/8/2017.
 */

public class VetProfileActivity extends AppCompatActivity {

    private ImageView profileBG;
    private TextView vetName;
    private TextView vetLandline;
    private TextView vetSpecialty;
    private TextView vetRating;
    private Button rateVetButton;


    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    private String email;
    private Veterinarian vetItem;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_vet_profile);


        profileBG = (ImageView) findViewById(R.id.profileImage);
        vetName = (TextView) findViewById(R.id.profileName);
        vetLandline = (TextView) findViewById(R.id.profileLandLine);
        vetSpecialty = (TextView) findViewById(R.id.vetSpecialty);
        vetRating = (TextView) findViewById(R.id.vetListRating);
        rateVetButton = (Button) findViewById(R.id.rateVetButton);


        systemSessionManager = new SystemSessionManager(this);
        if(systemSessionManager.checkLogin())
            finish();
        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();

        email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);

        final String jsonMyObject;
        Bundle extras = getIntent().getExtras();
        jsonMyObject = extras.getString("thisVet");

        vetItem = new Gson().fromJson(jsonMyObject,Veterinarian.class);

        vetName.setText(vetItem.getFirstName()+" "+vetItem.getLastName());
        vetLandline.setText(vetItem.getMobileNumber());
        vetSpecialty.setText(vetItem.getSpecialty());
        vetRating.setText(String.valueOf(vetItem.getRating()));

        rateVetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(),RateVetActivity.class);
                intent.putExtra("thisVet", jsonMyObject);
                startActivity(intent);

            }
        });


        //Toast.makeText(this, "Vet's Name: "+vetItem.getName() + ". Delete this toast. Just to help you see where vet variable is", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onResume(){
        super.onResume();

    }

    private void initializeDatabase() {

        petBetterDb = new DataAdapter(this);

        try {
            petBetterDb.createDatabase();
        } catch(SQLException e ){
            e.printStackTrace();
        }

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

    public void backClicked(View view){
       finish();

    }



    //Integrate to db to display stuff on page
}
