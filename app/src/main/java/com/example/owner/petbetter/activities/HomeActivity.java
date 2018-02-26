package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.activities.VetProfileActivity;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.fragments.FragmentPetClinicListing;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;

import com.example.owner.petbetter.fragments.FragmentVetListing;

import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{



    private Toolbar menuBar;
    private Button vetButton;
    private Button petCareButton;
    private TextView textNavEmail, textNavUser;

    private String userName;
    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    private NavigationView navigationView;

    private ArrayList<Veterinarian> vetList;
    HerokuService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout nDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle nToggle = new ActionBarDrawerToggle(this, nDrawerLayout,toolbar, R.string.open, R.string.close);
        nDrawerLayout.setDrawerListener(nToggle);
        nToggle.syncState();



        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        menuBar = (Toolbar) findViewById(R.id.menu_bar);
        vetButton = (Button) findViewById(R.id.vetButton);
        petCareButton = (Button) findViewById(R.id.petCareButton);

        View headerView = navigationView.getHeaderView(0);

        systemSessionManager = new SystemSessionManager(this);
        if(systemSessionManager.checkLogin())
            finish();



        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();
        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        //instance
        System.out.println("PLS HOW MANY "+getVeterinarians().size());

        String email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        textNavEmail = (TextView) headerView.findViewById(R.id.textNavEmail);
        textNavEmail.setText(email);

        user = getUser(email);

        //userName = user.getName();

        System.out.println(user.getName());
        System.out.println(email);
        System.out.println(user.getUserId());
        textNavUser = (TextView) headerView.findViewById(R.id.textNavUser);
        textNavUser.setText(user.getName());

        //vetButtonClicked(this.navigationView);

        vetButtonClicked(this.findViewById(android.R.id.content));

    }
    @Override
    protected void onResume(){
        super.onResume();

        systemSessionManager = new SystemSessionManager(this);
        if(systemSessionManager.checkLogin())
            finish();

        initializeDatabase();
        vetButtonClicked(this.findViewById(android.R.id.content));

    }
    @Override
    protected void onRestart(){
        super.onRestart();
        recreate();
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

    private ArrayList<Veterinarian> getVeterinarians(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Veterinarian> result = petBetterDb.getVeterinarians();
        System.out.println("The number of veterinarians is: "+result.size());
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Facility> getClinics(Veterinarian veterinarian){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Facility> result = petBetterDb.getClinics();
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


        vetButton.setBackgroundResource(R.color.myrtle_green);
        petCareButton.setBackgroundResource(R.color.medTurquoise);
        //FragmentVetListing fragment = (FragmentVetListing) getSupportFragmentManager().findFragmentById(R.id.frame_container);
        vetList = getVeterinarians();

        FragmentVetListing fragment1 = new FragmentVetListing();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_container,fragment1).commit();
       // FragmentManager fm = getSupportFragmentManager();
       // FragmentTransaction ft = fm.beginTransaction();
        // vetList = getVeterinarians();

        System.out.println("VET LIST SIZE: " + vetList.size());

    }
/*
    public void vetListingClicked(View view){


        Intent intent = new Intent(this, com.example.owner.petbetter.activities.VetProfileActivity.class);

        Bundle extras = new Bundle();



        startActivity(intent);

        Intent intent = new Intent(this, VetProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
       //setContentView(R.layout.activity_vet_profile);
    }
    */

/*
    public void petCareListingClicked(View view){

        Intent intent = new Intent(this, com.example.owner.petbetter.activities.PetClinicProfileActivity.class);
        startActivity(intent);
    }
*/
    public void petCareButtonClicked(View view){
        petCareButton.setBackgroundResource(R.color.myrtle_green);
        vetButton.setBackgroundResource(R.color.medTurquoise);
        FragmentPetClinicListing fragment = new FragmentPetClinicListing();

        getSupportFragmentManager().beginTransaction().add(R.id.frame_container,fragment).commit();
    }

    public void commButtonClicked(View view){
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.nav_bar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.action_search){
            Intent intent = new Intent(this, com.example.owner.petbetter.activities.SearchActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
}
