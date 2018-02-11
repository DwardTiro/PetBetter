package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.owner.petbetter.activities.MainActivity;
import com.example.owner.petbetter.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Kristian on 8/5/2017.
 */

public class SignUpActivity extends AppCompatActivity {

    private EditText emailAdd;
    private EditText pWord;
    private EditText confirmPWord;

    private Button signUpBack;
    private Button signUpNext;

    @Override
    protected void onCreate(Bundle savedInstance){

        super.onCreate(savedInstance);
        setContentView(R.layout.activity_sign_up);
        emailAdd = (EditText) findViewById(R.id.signUpEmail);
        pWord = (EditText) findViewById(R.id.signUpPassword);
        confirmPWord = (EditText) findViewById(R.id.signUpConfirmPassword);



    }
    public void backClicked(View view){
        Intent intent = new Intent(this, com.example.owner.petbetter.activities.MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void nextClicked(View view){
        Intent intent = new Intent(this, com.example.owner.petbetter.activities.SignUpFinalActivity.class);
        Bundle extras = new Bundle();
        if(checkEmailValidity(emailAdd.getText().toString()) ){
            System.out.println("valid email");
        }
        //try emailAdd.getText().toString() if it doesn't work.
        extras.putString("EMAILADD", emailAdd.getText().toString());
        extras.putString("PASSWORD", pWord.getText().toString());
        extras.putString("CONFIRMPASSWORD", confirmPWord.getText().toString());
        intent.putExtras(extras);
        startActivity(intent);
    }
    private boolean checkEmailValidity(String email){

        String regEx = "^[\\w\\.-]+@([\\w\\-]+\\.)+[a-zA-Z]{2,4}$";

        Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();


    }

}
