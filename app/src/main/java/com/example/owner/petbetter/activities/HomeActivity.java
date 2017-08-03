package com.example.owner.petbetter;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.example.owner.petbetter.R;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "HomeActivity";


    private DrawerLayout nDrawerLayout;
    private ActionBarDrawerToggle nToggle;
    private HorizontalScrollView menuBar;
    private Button vetButton;
    private Button petCareButton;
    private Button commButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        Log.d(TAG, "onCreate: Starting.");
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);


        nDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        nToggle = new ActionBarDrawerToggle(this, nDrawerLayout, R.string.open, R.string.close);

        nDrawerLayout.addDrawerListener(nToggle);
        nToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        menuBar = (HorizontalScrollView) findViewById(R.id.menu_bar);
        vetButton = (Button) findViewById(R.id.vetButton);
        petCareButton = (Button) findViewById(R.id.petCareButton);
        commButton = (Button) findViewById(R.id.commButton);
    }

    public void vetButtonClicked(View view){


    }
    public void petCareButtonClicked(View view){


    }
    public void commButtonClicked(View view){


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(nToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.profile){
            System.out.println("CAMBODJA");
        }
        else if(id == R.id.settings){
            System.out.println("NYEEAAAMM");
        }
        else if(id == R.id.bookmarks){
            System.out.println("NYEEAAAMM");
        }
        else if(id==R.id.add_location){
            Intent intent = new Intent(this, com.example.owner.petbetter.MapsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.log_out){
            Intent intent = new Intent(this, com.example.owner.petbetter.MainActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
