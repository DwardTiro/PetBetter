package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.activities.SignUpActivity;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.example.owner.petbetter.activities.HomeActivity;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText editEmail;
    private EditText editPassword;
    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private TextView textInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        systemSessionManager = new SystemSessionManager(getApplicationContext());

        loginButton = (Button) findViewById(R.id.loginButton);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);
        textInfo = (TextView) findViewById(R.id.textInfo);
        textInfo.setVisibility(View.INVISIBLE);
        initializeDatabase();
    }

    public void userLogin(View v){
        System.out.println("Im here");
        login(v);
        //Intent intent = new Intent(this, com.example.owner.petbetter.HomeActivity.class);
        //startActivity(intent);
    }
    private void initializeDatabase() {

        petBetterDb = new DataAdapter(this);

        try {
            petBetterDb.createDatabase();
        } catch(SQLException e ){
            e.printStackTrace();
        }

    }

    private void login(View v) {

        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        View focusView = null;

        if (email.trim().length() > 0 && password.trim().length() > 0) {

            if (!checkEmailValidity(email)||email.equals("")) {
                //System.out.println("Invalid email");
                textInfo.setText("Invalid email or password");
                textInfo.setVisibility(View.VISIBLE);
            } else {

                if (checkLogin(email, password)) {
                    systemSessionManager.createUserSession(email);

                    Intent intent = new Intent(this, com.example.owner.petbetter.activities.HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    finish();


                } else {
                    //System.out.println("Invalid email or password");
                    textInfo.setText("Invalid email or password");
                    textInfo.setVisibility(View.VISIBLE);
                }

            }

        } else {

            if (email.trim().length() == 0) {
                //System.out.println("Email Required");
                textInfo.setText("Email Required");
                textInfo.setVisibility(View.VISIBLE);
            } else if (password.trim().length() == 0) {

                //System.out.println("Password Required");
                textInfo.setText("Password Required");
                textInfo.setVisibility(View.VISIBLE);
            }
        }

    }

    private boolean checkLogin(String email, String password) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        boolean result = petBetterDb.checkLogin(email, password);
        petBetterDb.closeDatabase();


        return result;
    }
    public void signUp(View view){

        Intent intent = new Intent(this, SignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
    private boolean checkEmailValidity(String email){

        String regEx = "^[\\w\\.-]+@([\\w\\-]+\\.)+[a-zA-Z]{2,4}$";

        Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();


    }


}
