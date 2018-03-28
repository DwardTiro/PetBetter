package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.activities.MainActivity;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Kristian on 8/16/2017.
 */

public class SignUpFinalActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private String emailAdd;
    private String pWord;
    private String confirmPWord;

    private EditText fName;
    private EditText lName;
    private EditText userName;
    private Spinner userType;
    private int userType2;

    private Button signUpBack;
    private Button signUpNext;


    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;


    @Override
    protected void onCreate(Bundle savedInstance){

        super.onCreate(savedInstance);
        setContentView(R.layout.activity_sign_up_final);

        Bundle extras = getIntent().getExtras();
        emailAdd = extras.getString("EMAILADD");
        pWord = extras.getString("PASSWORD");
        confirmPWord = extras.getString("CONFIRMPASSWORD");

        fName = (EditText) findViewById(R.id.signUpFName);
        lName = (EditText) findViewById(R.id.signUpLName);
        userName = (EditText) findViewById(R.id.signUpUsername);

        userType = (Spinner) findViewById(R.id.spinnerUserType);
        userType.setOnItemSelectedListener(this);

        ArrayList<String> categories = new ArrayList<String>();
        categories.add("Pet Owner");
        categories.add("Veterinarian");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        userType.setAdapter(dataAdapter);



        initializeDatabase();

    }

    public void backButtonClicked(View view){
        Intent intent = new Intent(this, com.example.owner.petbetter.activities.SignUpActivity.class);

        startActivity(intent);
    }

    public void nextButtonClicked(View view){

        int userId = generateUserId();
        //addUser(userId, fName.getText().toString(), lName.getText().toString(), emailAdd, pWord, userType2);

        Intent intent = new Intent(this, com.example.owner.petbetter.activities.MainActivity.class);
        startActivity(intent);
    }
    /*

    private void addUser(int userId, String fName, String lName, String emailAdd, String pWord, int userType2){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        petBetterDb.addUser(userId, fName, lName, emailAdd, pWord, userType2);
        petBetterDb.closeDatabase();

        if(userType2==1){
            int vetId = generateVetId();
            addVet(vetId, userId, 0);
        }

    }*/

    private long addVet(int vetId, int userId, int rating){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.addVet(vetId, userId, rating);
        petBetterDb.closeDatabase();

        return result;
    }

    private void initializeDatabase() {

        petBetterDb = new DataAdapter(this);

        try {
            petBetterDb.createDatabase();
        } catch(SQLException e ){
            e.printStackTrace();
        }
    }

    public int generateUserId(){
        ArrayList<Integer> storedIds;
        int userId = 1;

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        storedIds = petBetterDb.getUserIds();
        petBetterDb.closeDatabase();


        if(storedIds.isEmpty()) {
            return userId;
        } else {
            while (storedIds.contains(userId)){
                userId += 1;
            }
            return userId;
        }
    }

    public int generateVetId(){
        ArrayList<Integer> storedIds;
        int userId = 1;

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        storedIds = petBetterDb.generateVetIds();
        petBetterDb.closeDatabase();


        if(storedIds.isEmpty()) {
            return userId;
        } else {
            while (storedIds.contains(userId)){
                userId += 1;
            }
            return userId;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        userType2 = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
