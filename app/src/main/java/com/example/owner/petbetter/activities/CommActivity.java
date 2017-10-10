package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.fragments.FragmentCommunity;
import com.example.owner.petbetter.fragments.FragmentHome;
import com.example.owner.petbetter.fragments.FragmentMessages;
import com.example.owner.petbetter.fragments.FragmentMessagesHome;
import com.example.owner.petbetter.fragments.FragmentUserProfile;

public class CommActivity extends AppCompatActivity {

    private ImageButton btnHome;
    private ImageButton btnCommunity;
    private ImageButton btnMessage;
    private ImageButton btnProfile;
    private FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm);

        btnHome = (ImageButton) findViewById(R.id.btnHome);
        btnCommunity = (ImageButton) findViewById(R.id.btnCommunity);
        btnMessage = (ImageButton) findViewById(R.id.btnMessage);
        btnProfile = (ImageButton) findViewById(R.id.btnProfile);
        container = (FrameLayout) findViewById(R.id.comm_container);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Fragment 1");
                btnHome.setBackgroundColor(Color.WHITE);
                btnCommunity.setBackgroundResource(R.color.main_Color);
                btnCommunity.setImageResource(R.mipmap.ic_people_white_24dp);
                btnMessage.setBackgroundResource(R.color.main_Color);
                btnMessage.setImageResource(R.mipmap.ic_mail_outline_white_24dp);
                btnProfile.setBackgroundResource(R.color.main_Color);
                btnProfile.setImageResource(R.mipmap.ic_account_circle_white_24dp);
                container.removeAllViews();
                FragmentHome fragment3 = new FragmentHome();
                getSupportFragmentManager().beginTransaction().add(R.id.comm_container,fragment3).commit();
            }
        });
        btnCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Fragment 2");
                btnCommunity.setImageResource(R.mipmap.ic_people_black_24dp);
                btnCommunity.setBackgroundColor(Color.WHITE);
                btnHome.setBackgroundResource(R.color.main_Color);
                btnMessage.setBackgroundResource(R.color.main_Color);
                btnMessage.setImageResource(R.mipmap.ic_mail_outline_white_24dp);
                btnProfile.setBackgroundResource(R.color.main_Color);
                btnProfile.setImageResource(R.mipmap.ic_account_circle_white_24dp);
                container.removeAllViews();
                FragmentCommunity fragment2 = new FragmentCommunity();
                getSupportFragmentManager().beginTransaction().add(R.id.comm_container,fragment2).commit();
            }
        });
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Fragment 3");
                btnMessage.setImageResource(R.mipmap.ic_mail_outline_black_24dp);
                btnMessage.setBackgroundColor(Color.WHITE);
                btnHome.setBackgroundResource(R.color.main_Color);
                btnCommunity.setBackgroundResource(R.color.main_Color);
                btnCommunity.setImageResource(R.mipmap.ic_people_white_24dp);
                btnProfile.setBackgroundResource(R.color.main_Color);
                btnProfile.setImageResource(R.mipmap.ic_account_circle_white_24dp);
                container.removeAllViews();
                //attach new fragment here
                FragmentMessagesHome fragment = new FragmentMessagesHome();
                getSupportFragmentManager().beginTransaction().add(R.id.comm_container,fragment).commit();
            }
        });
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Fragment 4");
                btnProfile.setImageResource(R.mipmap.ic_account_circle_black_24dp);
                btnProfile.setBackgroundColor(Color.WHITE);
                btnHome.setBackgroundResource(R.color.main_Color);
                btnCommunity.setBackgroundResource(R.color.main_Color);
                btnCommunity.setImageResource(R.mipmap.ic_people_white_24dp);
                btnMessage.setBackgroundResource(R.color.main_Color);
                btnMessage.setImageResource(R.mipmap.ic_mail_outline_white_24dp);
                container.removeAllViews();
                FragmentUserProfile fragment1 = new FragmentUserProfile();
                getSupportFragmentManager().beginTransaction().add(R.id.comm_container,fragment1).commit();
            }
        });
    }

}
