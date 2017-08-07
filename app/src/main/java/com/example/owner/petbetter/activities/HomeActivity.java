package com.example.owner.petbetter;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.owner.petbetter.MapsActivity;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;

import com.example.owner.petbetter.fragments.fragment_vet_listing;

import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.example.owner.petbetter.R;

import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.HashMap;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{



    private HorizontalScrollView menuBar;
    private Button vetButton;
    private Button petCareButton;
    private Button commButton;
    private TextView textNavEmail, textNavUser;

    private String userName;
    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout nDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle nToggle = new ActionBarDrawerToggle(this, nDrawerLayout,toolbar, R.string.open, R.string.close);
        nDrawerLayout.setDrawerListener(nToggle);
        nToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        menuBar = (HorizontalScrollView) findViewById(R.id.menu_bar);
        vetButton = (Button) findViewById(R.id.vetButton);
        petCareButton = (Button) findViewById(R.id.petCareButton);
        commButton = (Button) findViewById(R.id.commButton);

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
        hideItems();


        //userName = user.getName();

        System.out.println(user.getName());
        System.out.println(email);
        textNavUser = (TextView) headerView.findViewById(R.id.textNavUser);
        textNavUser.setText(user.getName());

        fragment_vet_listing fragment = new fragment_vet_listing();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_container,fragment).commit();
    }

    private void hideItems(){
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.settings).setVisible(false);
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
    }

    public void vetButtonClicked(View view){

        Toast.makeText(this,"Vet",Toast.LENGTH_SHORT).show();
    }

    public void vet_listing_clicked(View view){
        Toast.makeText(this,"Clicked",Toast.LENGTH_SHORT).show();
    }

    public void petCareButtonClicked(View view){
        Toast.makeText(this,"Pet",Toast.LENGTH_SHORT).show();

    }
    public void commButtonClicked(View view){

        Toast.makeText(this,"Comm",Toast.LENGTH_SHORT).show();
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
        else if(id == R.id.community){
            System.out.println("NYEEAAAMM");
        }
        else if(id == R.id.bookmarks){
            System.out.println("BOOKMARKS BACK!");
        }
        else if(id==R.id.add_location){
            //Toast.makeText(this,"Location",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, com.example.owner.petbetter.MapsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.log_out){
            Intent intent = new Intent(this, com.example.owner.petbetter.MainActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
