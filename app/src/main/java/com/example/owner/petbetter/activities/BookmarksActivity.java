package com.example.owner.petbetter.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.Notifications;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.fragments.FragmentBookmarkListing;
import com.example.owner.petbetter.fragments.FragmentFacilityListing;
import com.example.owner.petbetter.fragments.FragmentPetClinicListing;
import com.example.owner.petbetter.services.MyService;
import com.example.owner.petbetter.services.NotificationReceiver;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.owner.petbetter.ServiceGenerator.BASE_URL;

public class BookmarksActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DataAdapter petBetterDb;
    private Button faciBookmarks;
    private Button postBookmarks;
    private FrameLayout container;
    private NavigationView navigationView;
    private SystemSessionManager systemSessionManager;
    private TextView textNavEmail, textNavUser;
    private User user;
    private ImageView notifButton;
    private NotificationReceiver notifReceiver = new NotificationReceiver();
    private ImageView imageViewDrawer;

    HerokuService service;

    /*
    @Override
    protected void onResume() {
        super.onResume();
        BookmarksActivity.this.registerReceiver(this.notifReceiver, new IntentFilter("com.example.ACTION_LOGOUT"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        BookmarksActivity.this.unregisterReceiver(notifReceiver);
    }
    */
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
        imageViewDrawer = (ImageView) headerView.findViewById(R.id.imageViewDrawer);
        if(user.getUserPhoto()!=null){

            String newFileName = BASE_URL + user.getUserPhoto();
            System.out.println(newFileName);
            //String newFileName = "http://192.168.0.19/petbetter/"+thisMessageRep.getMessagePhoto();
            Glide.with(BookmarksActivity.this).load(newFileName).error(R.drawable.back_button).into(imageViewDrawer);
            /*
            Picasso.with(inflater.getContext()).load("http://".concat(newFileName))
                    .error(R.drawable.back_button).into(holder.messageRepImage);*/
            //setImage(holder.messageRepImage, newFileName);

            imageViewDrawer.setVisibility(View.VISIBLE);
        }

        notifButton = (ImageView) findViewById(R.id.imageview_notifs);

        if(!getUnsyncedNotifications().isEmpty())
            notifButton.setImageResource(R.mipmap.ic_notifications_active_black_24dp);

        textNavUser = (TextView) headerView.findViewById(R.id.textNavUser);
        textNavUser.setText(user.getName());
        faciBookmarks = (Button) findViewById(R.id.facilityBookmarkButton);
        postBookmarks = (Button) findViewById(R.id.postBookmarkButton);
        container = (FrameLayout) findViewById(R.id.bookmark_container);

        faciBookmarks.setBackgroundResource(R.color.colorWhite);
        faciBookmarks.setTextColor(getResources().getColor(R.color.myrtle_green));
        postBookmarks.setBackgroundResource(R.color.medTurquoise);
        postBookmarks.setTextColor(getResources().getColor(R.color.colorWhite));
        FragmentPetClinicListing fragment1 = new FragmentPetClinicListing();
        getSupportFragmentManager().beginTransaction().add(R.id.bookmark_container,fragment1).commitAllowingStateLoss();



        faciBookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Fragment 1");
                faciBookmarks.setBackgroundResource(R.color.colorWhite);
                faciBookmarks.setTextColor(getResources().getColor(R.color.myrtle_green));
                postBookmarks.setBackgroundResource(R.color.medTurquoise);
                postBookmarks.setTextColor(getResources().getColor(R.color.colorWhite));
                container.removeAllViews();
                FragmentPetClinicListing fragment1 = new FragmentPetClinicListing();
                getSupportFragmentManager().beginTransaction().replace(R.id.bookmark_container,fragment1).commit();
            }
        });
        postBookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Fragment 2");
                postBookmarks.setBackgroundResource(R.color.colorWhite);
                postBookmarks.setTextColor(getResources().getColor(R.color.myrtle_green));
                faciBookmarks.setBackgroundResource(R.color.medTurquoise);
                faciBookmarks.setTextColor(getResources().getColor(R.color.colorWhite));
                container.removeAllViews();
                FragmentFacilityListing fragment = new FragmentFacilityListing();
                getSupportFragmentManager().beginTransaction().replace(R.id.bookmark_container,fragment).commit();
            }
        });

        notifButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Redirect to notifications
                Intent intent = new Intent(BookmarksActivity.this, com.example.owner.petbetter.activities.NotificationActivity.class);
                startActivity(intent);
            }
        });

        hideItems();
    }

    public void hideItems(){
        Menu menu = navigationView.getMenu();
        if(user.getUserType()==1){
            menu.findItem(R.id.bookmarks).setVisible(false);
            menu.findItem(R.id.community).setVisible(false);
        }
        if(user.getUserType()==2){
            menu.findItem(R.id.community2).setVisible(false);
        }
    }

    public void viewPostBackButtonClicked(View view){
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.search_drawer) {
            Intent intent = new Intent(this, com.example.owner.petbetter.activities.SearchActivity.class);
            startActivity(intent);
        }

        else if(id == R.id.home){
            if(user.getUserType()==1){
                Intent intent = new Intent(this, com.example.owner.petbetter.activities.VeterinarianHomeActivity.class);
                startActivity(intent);
            }
            else{
                Intent intent = new Intent(this, com.example.owner.petbetter.activities.CommActivity.class);
                startActivity(intent);
            }
        }
        else if(id == R.id.community2){
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
            //stopService(new Intent(BookmarksActivity.this, MyService.class));
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
