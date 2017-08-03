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
<<<<<<< HEAD
=======

import com.example.owner.petbetter.SectionsPageAdapter;
import com.example.owner.petbetter.Tab1Fragment;
import com.example.owner.petbetter.Tab2Fragment;
import com.example.owner.petbetter.Tab3Fragment;
import com.example.owner.petbetter.Tab4Fragment;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;


>>>>>>> bf49bd227a5faea6fb792a418c98bac19ae36fb7
import android.view.View;
import android.widget.Button;
<<<<<<< HEAD
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.example.owner.petbetter.R;
=======
import android.widget.TextView;

import java.sql.SQLException;
import java.util.HashMap;
>>>>>>> bf49bd227a5faea6fb792a418c98bac19ae36fb7

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "HomeActivity";


    private DrawerLayout nDrawerLayout;
    private ActionBarDrawerToggle nToggle;
<<<<<<< HEAD
    private HorizontalScrollView menuBar;
    private Button vetButton;
    private Button petCareButton;
    private Button commButton;
=======
    private TextView textNavEmail, textNavUser;

    private String userName;
    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
>>>>>>> bf49bd227a5faea6fb792a418c98bac19ae36fb7

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
<<<<<<< HEAD

        menuBar = (HorizontalScrollView) findViewById(R.id.menu_bar);
        vetButton = (Button) findViewById(R.id.vetButton);
        petCareButton = (Button) findViewById(R.id.petCareButton);
        commButton = (Button) findViewById(R.id.commButton);
=======
        View headerView = navigationView.getHeaderView(0);

        systemSessionManager = new SystemSessionManager(this);
        if(systemSessionManager.checkLogin())
            finish();



        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();

        String email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        textNavEmail = (TextView) headerView.findViewById(R.id.textNavEmail);
        textNavEmail.setText(email);

        user = getUser(email);


        //userName = user.getName();

        System.out.println(user.getName());
        System.out.println(email);
        textNavUser = (TextView) headerView.findViewById(R.id.textNavUser);
        textNavUser.setText(user.getName());

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

    private String getUserName(String email){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        String result = petBetterDb.getUserName(email);
        petBetterDb.closeDatabase();

        return result;
>>>>>>> bf49bd227a5faea6fb792a418c98bac19ae36fb7
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
