package com.example.owner.petbetter.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.owner.petbetter.R;

/**
 * Created by Kristian on 10/16/2017.
 */

public class NewMessageActivity extends AppCompatActivity {



    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        Toolbar toolbar = (Toolbar) findViewById(R.id.newMsgToolbar);
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_new_message);

    }
}
