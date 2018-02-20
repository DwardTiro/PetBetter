package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.example.owner.petbetter.fragments.FragmentBookmarkListing;
import com.example.owner.petbetter.fragments.FragmentFacilityListing;
import com.example.owner.petbetter.fragments.FragmentPetClinicListing;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;

import java.sql.SQLException;
import java.util.HashMap;

public class BookmarksActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DataAdapter petBetterDb;
    private ImageButton btnBookmarks;
    private ImageButton btnFaci;
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
        setContentView(R.layout.activity_bookmarks);

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
        btnBookmarks = (ImageButton) findViewById(R.id.bookmarkMapsButton);
        btnFaci = (ImageButton) findViewById(R.id.faciMapsButton);
        container = (FrameLayout) findViewById(R.id.bookmark_container);

        btnBookmarks.setBackgroundColor(Color.WHITE);
        btnBookmarks.setImageResource(R.mipmap.ic_bookmark_border_black_24dp);
        btnFaci.setBackgroundResource(R.color.medTurquoise);
        btnFaci.setImageResource(R.mipmap.ic_pets_white_24dp);
        FragmentBookmarkListing fragment = new FragmentBookmarkListing();
        getSupportFragmentManager().beginTransaction().add(R.id.bookmark_container,fragment).commit();

        btnBookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Fragment 1");
                btnBookmarks.setBackgroundColor(Color.WHITE);
                btnBookmarks.setImageResource(R.mipmap.ic_bookmark_border_black_24dp);
                btnFaci.setBackgroundResource(R.color.medTurquoise);
                btnFaci.setImageResource(R.mipmap.ic_pets_white_24dp);
                container.removeAllViews();
                FragmentBookmarkListing fragment1 = new FragmentBookmarkListing();
                getSupportFragmentManager().beginTransaction().add(R.id.bookmark_container,fragment1).commit();
            }
        });
        btnFaci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Fragment 2");
                btnFaci.setBackgroundColor(Color.WHITE);
                btnFaci.setImageResource(R.mipmap.ic_pets_black_24dp);
                btnBookmarks.setBackgroundResource(R.color.medTurquoise);
                btnBookmarks.setImageResource(R.mipmap.ic_bookmark_border_white_24dp);
                container.removeAllViews();
                FragmentFacilityListing fragment = new FragmentFacilityListing();
                getSupportFragmentManager().beginTransaction().add(R.id.bookmark_container,fragment).commit();
            }
        });
    }

    public void viewPostBackButtonClicked(View view){
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.home){
            Intent intent = new Intent(this, com.example.owner.petbetter.activities.CommActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.community){
            Intent intent = new Intent(this, com.example.owner.petbetter.activities.HomeActivity.class);
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
    /*
    public void bookmarkClicked(View v){
        System.out.println("We did this right I guess");
        FragmentBookmarkListing fragment = new FragmentBookmarkListing();
        getSupportFragmentManager().beginTransaction().add(R.id.bookmark_container,fragment).commit();


    }
    public void faciClicked(View v){
        FragmentFacilityListing fragment = new FragmentFacilityListing();

        getSupportFragmentManager().beginTransaction().add(R.id.bookmark_container,fragment).commit();
        //change frame_container
    }
    */
}
