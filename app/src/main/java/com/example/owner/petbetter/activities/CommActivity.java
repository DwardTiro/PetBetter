package com.example.owner.petbetter.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.Notifications;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.Topic;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.fragments.FragmentCommunity;
import com.example.owner.petbetter.fragments.FragmentHome;
import com.example.owner.petbetter.fragments.FragmentMessages;
import com.example.owner.petbetter.fragments.FragmentMessagesHome;
import com.example.owner.petbetter.fragments.FragmentUserProfile;
import com.example.owner.petbetter.interfaces.CheckLogout;
import com.example.owner.petbetter.interfaces.CheckUpdates;
import com.example.owner.petbetter.services.MyService;
import com.example.owner.petbetter.services.NotificationReceiver;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, CheckUpdates, CheckLogout {

    private DataAdapter petBetterDb;
    private ImageButton btnHome;
    private ImageButton btnCommunity;
    private FrameLayout container;
    private NavigationView navigationView;
    private SystemSessionManager systemSessionManager;
    private TextView textNavEmail, textNavUser;
    private User user;
    private SwipeRefreshLayout refreshCommunity;
    private int currFragment;
    private ImageView notifButton;

    private NotificationReceiver notifReceiver = new NotificationReceiver((CheckUpdates) this);
    private NotificationReceiver notifReceiver2 = new NotificationReceiver((CheckLogout) this);
    HerokuService service;

    @Override
    protected void onResume() {
        super.onResume();
        CommActivity.this.registerReceiver(this.notifReceiver, new IntentFilter(Intent.ACTION_ATTACH_DATA));
        CommActivity.this.registerReceiver(this.notifReceiver2, new IntentFilter("com.example.ACTION_LOGOUT"));
        onResult();
    }

    @Override
    protected void onPause() {
        super.onPause();
        CommActivity.this.unregisterReceiver(notifReceiver);
        CommActivity.this.unregisterReceiver(notifReceiver2);
    }

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
        notifButton = (ImageView) findViewById(R.id.imageview_notifs);

        if(!getUnsyncedNotifications().isEmpty())
            notifButton.setImageResource(R.mipmap.ic_notifications_active_black_24dp);


        textNavUser = (TextView) headerView.findViewById(R.id.textNavUser);
        textNavUser.setText(user.getName());

        btnHome = (ImageButton) findViewById(R.id.btnHome);
        btnCommunity = (ImageButton) findViewById(R.id.btnCommunity);
        container = (FrameLayout) findViewById(R.id.comm_container);
        refreshCommunity = (SwipeRefreshLayout) findViewById(R.id.refreshCommunity);



        refreshCommunity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshCommunity.setRefreshing(true);
                syncTopicChanges();
                syncPostChanges();
                refreshCommunity.setRefreshing(false);
            }
        });

        currFragment = 2;
        btnHome.setBackgroundColor(Color.WHITE);
        btnCommunity.setBackgroundResource(R.color.medTurquoise);
        btnCommunity.setImageResource(R.mipmap.ic_people_white_24dp);
        container.removeAllViews();
        FragmentHome fragment3 = new FragmentHome();
        getSupportFragmentManager().beginTransaction().replace(R.id.comm_container,fragment3).commitAllowingStateLoss();

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Fragment 1");
                btnHome.setBackgroundColor(Color.WHITE);
                btnCommunity.setBackgroundResource(R.color.medTurquoise);
                btnCommunity.setImageResource(R.mipmap.ic_people_white_24dp);
                container.removeAllViews();
                FragmentHome fragment3 = new FragmentHome();
                getSupportFragmentManager().beginTransaction().replace(R.id.comm_container,fragment3).commitAllowingStateLoss();
                currFragment = 2;
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
                getSupportFragmentManager().beginTransaction().replace(R.id.comm_container,fragment2).commitAllowingStateLoss();
                currFragment = 1;
            }
        });

        notifButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Redirect to notifications
                Intent intent = new Intent(CommActivity.this, com.example.owner.petbetter.activities.NotificationActivity.class);
                startActivity(intent);
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
        getSupportFragmentManager().beginTransaction().replace(R.id.comm_container,fragment3).commitAllowingStateLoss();
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
            SharedPreferences preferences =getSharedPreferences("prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
            Intent intentLogout = new Intent().setAction("com.package.ACTION_LOGOUT");
            notifReceiver2.onReceive(this, intentLogout);
            sendBroadcast(intentLogout);
            startActivity(intent);
            startActivity(intent);
            //stopService(new Intent(CommActivity.this, MyService.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void syncTopicChanges(){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        System.out.println("WE HERE BOOIII");

        final Call<ArrayList<Topic>> call = service.getTopics();
        call.enqueue(new Callback<ArrayList<Topic>>() {
            @Override
            public void onResponse(Call<ArrayList<Topic>> call, Response<ArrayList<Topic>> response) {
                if(response.isSuccessful()){
                    setTopics(response.body());

                    if(currFragment==1){
                        btnCommunity.setImageResource(R.mipmap.ic_people_black_24dp);
                        btnCommunity.setBackgroundColor(Color.WHITE);
                        btnHome.setBackgroundResource(R.color.medTurquoise);
                        container.removeAllViews();
                        FragmentCommunity fragment2 = new FragmentCommunity();
                        getSupportFragmentManager().beginTransaction().replace(R.id.comm_container,fragment2).commitAllowingStateLoss();
                    }
                    else{
                        btnHome.setBackgroundColor(Color.WHITE);
                        btnCommunity.setBackgroundResource(R.color.medTurquoise);
                        btnCommunity.setImageResource(R.mipmap.ic_people_white_24dp);
                        container.removeAllViews();
                        FragmentHome fragment3 = new FragmentHome();
                        getSupportFragmentManager().beginTransaction().replace(R.id.comm_container,fragment3).commitAllowingStateLoss();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Topic>> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
    }

    public void syncPostChanges(){
        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        System.out.println("WE HERE BOOIII");

        final Call<ArrayList<Post>> call = service.getPosts();
        call.enqueue(new Callback<ArrayList<Post>>() {
            @Override
            public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
                if(response.isSuccessful()){
                    setPosts(response.body());

                    if(currFragment==1){
                        btnCommunity.setImageResource(R.mipmap.ic_people_black_24dp);
                        btnCommunity.setBackgroundColor(Color.WHITE);
                        btnHome.setBackgroundResource(R.color.medTurquoise);
                        container.removeAllViews();
                        FragmentCommunity fragment2 = new FragmentCommunity();
                        getSupportFragmentManager().beginTransaction().replace(R.id.comm_container,fragment2).commit();
                    }
                    else{
                        btnHome.setBackgroundColor(Color.WHITE);
                        btnCommunity.setBackgroundResource(R.color.medTurquoise);
                        btnCommunity.setImageResource(R.mipmap.ic_people_white_24dp);
                        container.removeAllViews();
                        FragmentHome fragment3 = new FragmentHome();
                        getSupportFragmentManager().beginTransaction().replace(R.id.comm_container,fragment3).commit();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
    }

    private void initializeDatabase() {

        petBetterDb = new DataAdapter(this);

        try {
            petBetterDb.createDatabase();
        } catch(SQLException e ){
            e.printStackTrace();
        }

    }

    public long setTopics(ArrayList<Topic> topicList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setTopics(topicList);
        petBetterDb.closeDatabase();

        return result;
    }

    public long setPosts(ArrayList<Post> postList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setPosts(postList);
        petBetterDb.closeDatabase();

        return result;
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
    public void onResult() {
        //maybe you could show a button that says "more results" and it updates the fragments when you tap it.

        /*
        if(currFragment==1){
            btnCommunity.setImageResource(R.mipmap.ic_people_black_24dp);
            btnCommunity.setBackgroundColor(Color.WHITE);
            btnHome.setBackgroundResource(R.color.medTurquoise);
            container.removeAllViews();
            FragmentCommunity fragment2 = new FragmentCommunity();
            getSupportFragmentManager().beginTransaction().replace(R.id.comm_container,fragment2).commit();
        }
        else{
            btnHome.setBackgroundColor(Color.WHITE);
            btnCommunity.setBackgroundResource(R.color.medTurquoise);
            btnCommunity.setImageResource(R.mipmap.ic_people_white_24dp);
            container.removeAllViews();
            FragmentHome fragment3 = new FragmentHome();
            getSupportFragmentManager().beginTransaction().replace(R.id.comm_container,fragment3).commit();
        }*/

        /*
        if(topicList.size()!=getMessageReps(messageId).size()){
            messageRepList = getMessageReps(messageId);
            messageRepAdapter.updateList(messageRepList);

        }

        if(messageRepList.size()!=getMessageReps(messageId).size()){
            messageRepList = getMessageReps(messageId);
            messageRepAdapter.updateList(messageRepList);

        }
        */
    }

    @Override
    public void onLogout() {

    }
}
