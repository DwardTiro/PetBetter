package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.fragments.FragmentCommunity;
import com.example.owner.petbetter.fragments.FragmentHome;
import com.example.owner.petbetter.fragments.FragmentMessages;
import com.example.owner.petbetter.fragments.FragmentMessagesHome;
import com.example.owner.petbetter.fragments.FragmentUserProfile;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;

import java.sql.SQLException;
import java.util.HashMap;

public class CommActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DataAdapter petBetterDb;
    private ImageButton btnHome;
    private ImageButton btnCommunity;
    private FrameLayout container;
    private NavigationView navigationView;
    private SystemSessionManager systemSessionManager;
    private TextView textNavEmail, textNavUser;
    private User user;

    HerokuService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_comm);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout nDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle nToggle = new ActionBarDrawerToggle(this, nDrawerLayout,toolbar, R.string.open, R.string.close);
        nDrawerLayout.setDrawerListener(nToggle);
        nToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        systemSessionManager = new SystemSessionManager(this);
        if(systemSessionManager.checkLogin())
            finish();

        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();
        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        String email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        textNavEmail = (TextView) headerView.findViewById(R.id.textNavEmail);
        textNavEmail.setText(email);

        user = getUser(email);


        textNavUser = (TextView) headerView.findViewById(R.id.textNavUser);
        textNavUser.setText(user.getName());

        btnHome = (ImageButton) findViewById(R.id.btnHome);
        btnCommunity = (ImageButton) findViewById(R.id.btnCommunity);
        container = (FrameLayout) findViewById(R.id.comm_container);

        FragmentHome fragment3 = new FragmentHome();
        getSupportFragmentManager().beginTransaction().add(R.id.comm_container,fragment3).commit();
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Fragment 1");
                btnHome.setBackgroundColor(Color.WHITE);
                btnCommunity.setBackgroundResource(R.color.medTurquoise);
                btnCommunity.setImageResource(R.mipmap.ic_people_white_24dp);
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
                btnHome.setBackgroundResource(R.color.medTurquoise);
                container.removeAllViews();
                FragmentCommunity fragment2 = new FragmentCommunity();
                getSupportFragmentManager().beginTransaction().add(R.id.comm_container,fragment2).commit();
            }
        });

    }
    @Override
    public void onRestart(){
        super.onRestart();
        //recreate();
        FragmentHome fragment3 = new FragmentHome();
        btnHome.setBackgroundColor(Color.WHITE);
        btnCommunity.setBackgroundResource(R.color.medTurquoise);
        btnCommunity.setImageResource(R.mipmap.ic_people_white_24dp);
        container.removeAllViews();
        getSupportFragmentManager().beginTransaction().add(R.id.comm_container,fragment3).commitAllowingStateLoss();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.home){
            Intent intent = new Intent(this, com.example.owner.petbetter.activities.CommActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.community){
            Intent intent = new Intent(this, com.example.owner.petbetter.activities.HomeActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.messages){
            Intent intent = new Intent(this, com.example.owner.petbetter.activities.MessagesActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.user_profile){
            Intent intent = new Intent(this, com.example.owner.petbetter.activities.UserProfileActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.bookmarks){
            Intent intent = new Intent(this, com.example.owner.petbetter.activities.BookmarksActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.log_out){
            Intent intent = new Intent(this, com.example.owner.petbetter.activities.MainActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
}
