package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.activities.MainActivity;

/**
 * Created by Kristian on 8/16/2017.
 */

public class SignUpFinalActivity extends AppCompatActivity {

    private EditText emailAdd;
    private EditText pWord;
    private EditText confirmPWord;

    private Button signUpBack;
    private Button signUpNext;

    @Override
    protected void onCreate(Bundle savedInstance){

        super.onCreate(savedInstance);
        setContentView(R.layout.activity_sign_up_final);

    }
    public void backButtonClicked(View view){
        Intent intent = new Intent(this, com.example.owner.petbetter.activities.SignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void nextButtonClicked(View view){

    }
}
