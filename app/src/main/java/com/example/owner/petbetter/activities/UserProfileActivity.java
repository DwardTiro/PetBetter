package com.example.owner.petbetter.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.Notifications;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.interfaces.CheckLogout;
import com.example.owner.petbetter.services.MyService;
import com.example.owner.petbetter.services.NotificationReceiver;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kristian on 2/20/2018.
 */


public class UserProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, CheckLogout{

    private TextView userProfileName;
    private Button editProfileButton;
    private DataAdapter petBetterDb;
    private NavigationView navigationView;
    private SystemSessionManager systemSessionManager;
    private TextView textNavEmail, textNavUser;
    private Toolbar menuBar;
    private User user;
    private ImageView notifButton;
    private NotificationReceiver notifReceiver = new NotificationReceiver(this);

    HerokuService service;

    @Override
    protected void onPause() {
        super.onPause();
        UserProfileActivity.this.unregisterReceiver(notifReceiver);
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_user_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout nDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle nToggle = new ActionBarDrawerToggle(this, nDrawerLayout,toolbar, R.string.open, R.string.close);
        nDrawerLayout.setDrawerListener(nToggle);
        nToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        editProfileButton = (Button) findViewById(R.id.editProfileButton);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), com.example.owner.petbetter.activities.EditProfileActivity.class);
                startActivity(intent);
            }
        });
        systemSessionManager = new SystemSessionManager(this);
        if(systemSessionManager.checkLogin())
            finish();

        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();
        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        String email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        textNavEmail = (TextView) headerView.findViewById(R.id.textNavEmail);
        textNavEmail.setText(email);
        notifButton = (ImageView) findViewById(R.id.imageview_notifs);

        if(!getUnsyncedNotifications().isEmpty())
            notifButton.setImageResource(R.mipmap.ic_notifications_active_black_24dp);

        user = getUser(email);

        textNavUser = (TextView) headerView.findViewById(R.id.textNavUser);
        textNavUser.setText(user.getName());

        userProfileName = (TextView) findViewById(R.id.userProfileName);
        String userName = user.getFirstName() + " " +user.getLastName();
        userProfileName.setText(userName);

        notifButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Redirect to notifications
                Intent intent = new Intent(UserProfileActivity.this, com.example.owner.petbetter.activities.NotificationActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onResume(){
        super.onResume();

        UserProfileActivity.this.registerReceiver(this.notifReceiver, new IntentFilter("com.example.ACTION_LOGOUT"));

        systemSessionManager = new SystemSessionManager(this);
        if(systemSessionManager.checkLogin())
            finish();
        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();
        View headerView = navigationView.getHeaderView(0);

        String email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        textNavEmail = (TextView) headerView.findViewById(R.id.textNavEmail);
        textNavEmail.setText(email);

        user = getUser(email);

        textNavUser = (TextView) headerView.findViewById(R.id.textNavUser);
        textNavUser.setText(user.getName());

        userProfileName = (TextView) findViewById(R.id.userProfileName);
        String userName = user.getFirstName() + " " +user.getLastName();
        userProfileName.setText(userName);
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

            SharedPreferences preferences =getSharedPreferences("prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();

            Intent intentLogout = new Intent().setAction("com.package.ACTION_LOGOUT");
            notifReceiver.onReceive(this, intentLogout);
            sendBroadcast(intentLogout);
            startActivity(intent);
            //stopService(new Intent(UserProfileActivity.this, MyService.class));
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

    private ArrayList<Notifications> getUnsyncedNotifications(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Notifications> result = petBetterDb.getUnsyncedNotifications();
        petBetterDb.closeDatabase();

        return result;
    }

    @Override
    public void onLogout() {

    }
}
