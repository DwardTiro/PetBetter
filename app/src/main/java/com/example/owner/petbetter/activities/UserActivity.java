package com.example.owner.petbetter.activities;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.Facility;
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
import com.example.owner.petbetter.fragments.FragmentNoResults;
import com.example.owner.petbetter.fragments.FragmentUserProfile;
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

import static com.example.owner.petbetter.ServiceGenerator.BASE_URL;

public class UserActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, CheckUpdates{

    private DataAdapter petBetterDb;
    private Button btnHome;
    private Button btnCommunity;
    private FrameLayout container;
    private NavigationView navigationView;
    private SystemSessionManager systemSessionManager;
    private TextView textNavEmail, textNavUser;
    private User user;
    private SwipeRefreshLayout refreshCommunity;
    private int currFragment;
    private ImageView notifButton;
    private ImageView imageViewDrawer;
    private Button addTopicButton;
    private Spinner spinnerFilter;
    private ArrayList<Topic> userTopics;


    private NotificationReceiver notifReceiver = new NotificationReceiver(this);
    private NotificationReceiver notifReceiver2 = new NotificationReceiver();
    HerokuService service;

    @Override
    protected void onResume() {
        super.onResume();
        if(currFragment==1){
            btnHome.performClick();
        }
        if(currFragment==2){
            btnCommunity.performClick();
        }

        UserActivity.this.registerReceiver(this.notifReceiver, new IntentFilter(Intent.ACTION_ATTACH_DATA));
        UserActivity.this.registerReceiver(this.notifReceiver2, new IntentFilter("com.example.ACTION_LOGOUT"));
        onResult();
    }

    @Override
    protected void onPause() {
        super.onPause();
        UserActivity.this.unregisterReceiver(notifReceiver);
        UserActivity.this.unregisterReceiver(notifReceiver2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout nDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle nToggle = new ActionBarDrawerToggle(this, nDrawerLayout,toolbar, R.string.open, R.string.close);
        nDrawerLayout.setDrawerListener(nToggle);
        nToggle.syncState();

        spinnerFilter = (Spinner) findViewById(R.id.spinnerFilter);
        String[] filterItems = new String[]{"Most recent", "Most upvotes"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, filterItems);
        spinnerFilter.setAdapter(adapterSpinner);



        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        systemSessionManager = new SystemSessionManager(this);
        if(systemSessionManager.checkLogin())
            finish();

        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();
        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        Intent notifAlarm = new Intent(UserActivity.this, NotificationReceiver.class);
        if(PendingIntent.getBroadcast(this,0,notifAlarm, PendingIntent.FLAG_NO_CREATE)==null){
            System.out.println("Pre-broadcast");
            Intent intentStartBroadcast = new Intent().setAction("START_BROADCAST");
            sendBroadcast(intentStartBroadcast);
        }

        String email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        textNavEmail = (TextView) headerView.findViewById(R.id.textNavEmail);
        imageViewDrawer = (ImageView) headerView.findViewById(R.id.imageViewDrawer);

        textNavEmail.setText(email);


        user = getUser(email);

        if(user.getUserPhoto()!=null){

            String newFileName = BASE_URL + user.getUserPhoto();
            System.out.println(newFileName);
            //String newFileName = "http://192.168.0.19/petbetter/"+thisMessageRep.getMessagePhoto();
            Glide.with(UserActivity.this).load(newFileName).error(R.drawable.app_icon_yellow).into(imageViewDrawer);
            /*
            Picasso.with(inflater.getContext()).load("http://".concat(newFileName))
                    .error(R.drawable.back_button).into(holder.messageRepImage);*/
            //setImage(holder.messageRepImage, newFileName);

            imageViewDrawer.setVisibility(View.VISIBLE);
        }
        notifButton = (ImageView) findViewById(R.id.imageview_notifs);
        addTopicButton = (Button) findViewById(R.id.addTopicButton);


        if(!getUnsyncedNotifications().isEmpty())
            notifButton.setImageResource(R.mipmap.ic_notifications_active_black_24dp);


        textNavUser = (TextView) headerView.findViewById(R.id.textNavUser);
        textNavUser.setText(user.getName());

        btnHome = (Button) findViewById(R.id.allPostsButton);
        btnCommunity = (Button) findViewById(R.id.allTopicsButton);
        container = (FrameLayout) findViewById(R.id.comm_container);
        refreshCommunity = (SwipeRefreshLayout) findViewById(R.id.refreshCommunity);


        currFragment = 1;
        btnHome.setBackgroundResource(R.color.main_White);
        btnHome.setTextColor(getResources().getColor(R.color.myrtle_green));
        btnCommunity.setBackgroundResource(R.color.medTurquoise);
        btnCommunity.setTextColor(getResources().getColor(R.color.colorWhite));
        btnHome.setPaintFlags(btnHome.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnCommunity.setPaintFlags(btnCommunity.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));

        container.removeAllViews();

        if(spinnerFilter.getSelectedItem().toString()=="Most recent"){
            filterPosts(1);
        }
        if(spinnerFilter.getSelectedItem().toString()=="Most upvotes"){
            filterPosts(2);
        }
        /*
        FragmentHome fragment3 = new FragmentHome();
        getSupportFragmentManager().beginTransaction().replace(R.id.comm_container,fragment3).commitAllowingStateLoss();
        */


        refreshCommunity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshCommunity.setRefreshing(true);
                if(currFragment == 2){
                    syncTopicChanges();
                }
                else{
                    if(spinnerFilter.getSelectedItem().toString()=="Most recent"){
                        filterPosts(1);
                    }
                    else{
                        filterPosts(2);
                    }
                }

                refreshCommunity.setRefreshing(false);
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Fragment 1");
                btnCommunity.setBackgroundResource(R.color.medTurquoise);
                btnCommunity.setTextColor(getResources().getColor(R.color.colorWhite));
                btnHome.setBackgroundResource(R.color.main_White);
                btnHome.setTextColor(getResources().getColor(R.color.myrtle_green));
                btnHome.setPaintFlags(btnHome.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                btnCommunity.setPaintFlags(btnCommunity.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                container.removeAllViews();
                if(spinnerFilter.getSelectedItem().toString()=="Most recent"){
                    filterPosts(1);
                }
                if(spinnerFilter.getSelectedItem().toString()=="Most upvotes"){
                    filterPosts(2);
                }
                currFragment = 1;
            }
        });
        btnCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Fragment 2");
                btnCommunity.setBackgroundResource(R.color.main_White);
                btnCommunity.setTextColor(getResources().getColor(R.color.myrtle_green));
                btnHome.setBackgroundResource(R.color.medTurquoise);
                btnHome.setTextColor(getResources().getColor(R.color.colorWhite));
                btnCommunity.setPaintFlags(btnCommunity.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                btnHome.setPaintFlags(btnHome.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                container.removeAllViews();

                syncTopicChanges();
                /*
                FragmentCommunity fragment2 = new FragmentCommunity();
                getSupportFragmentManager().beginTransaction().replace(R.id.comm_container,fragment2).commitAllowingStateLoss();
                */
                currFragment = 2;
            }
        });

        notifButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Redirect to notifications
                Intent intent = new Intent(UserActivity.this, com.example.owner.petbetter.activities.NotificationActivity.class);
                startActivity(intent);
            }
        });

        addTopicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, com.example.owner.petbetter.activities.NewTopicActivity.class);
                startActivity(intent);
            }
        });

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(currFragment==1){
                    if(spinnerFilter.getSelectedItem().toString()=="Most recent"){
                        filterPosts(1);
                    }
                    if(spinnerFilter.getSelectedItem().toString()=="Most upvotes"){
                        filterPosts(2);

                    }
                }
                else{
                    syncTopicChanges();
                    /*
                    FragmentCommunity fragment2 = new FragmentCommunity();
                    getSupportFragmentManager().beginTransaction().replace(R.id.comm_container,fragment2).commitAllowingStateLoss();*/
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        hideItems();
    }

    public void hideItems(){
        Menu menu = navigationView.getMenu();

        if(user.getUserType()==2){
            menu.findItem(R.id.community2).setVisible(false);
        }
    }

    @Override
    public void onRestart(){
        super.onRestart();
        //recreate();
        FragmentHome fragment3 = new FragmentHome();
        btnCommunity.setBackgroundResource(R.color.main_White);
        btnCommunity.setTextColor(getResources().getColor(R.color.myrtle_green));
        btnHome.setBackgroundResource(R.color.medTurquoise);
        btnHome.setTextColor(getResources().getColor(R.color.colorWhite));
        btnCommunity.setPaintFlags(btnCommunity.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnHome.setPaintFlags(btnHome.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
        container.removeAllViews();
        getSupportFragmentManager().beginTransaction().replace(R.id.comm_container,fragment3).commitAllowingStateLoss();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item){
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
            notifReceiver2.onReceive(this, intentLogout);
            sendBroadcast(intentLogout);
            startActivity(intent);
            //stopService(new Intent(CommActivity.this, MyService.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void filterPosts(int order){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final Call<ArrayList<Post>> call = service.getFilteredUserPosts(order, user.getUserId());
        call.enqueue(new Callback<ArrayList<Post>>() {
            @Override
            public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
                if(response.isSuccessful()){
                    System.out.println("SIZE OF USER POSTS "+response.body().size());
                    ArrayList postList = response.body();
                    if(response.body().size()>0){
                        FragmentHome fragment3 = new FragmentHome(postList);
                        getSupportFragmentManager().beginTransaction().replace(R.id.comm_container,fragment3).commitAllowingStateLoss();
                    }
                    else{
                        FragmentNoResults fragmentpar = new FragmentNoResults(1);
                        getSupportFragmentManager().beginTransaction().replace(R.id.comm_container,fragmentpar).commitAllowingStateLoss();
                    }

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
                FragmentNoResults fragmentpar = new FragmentNoResults(1);
                getSupportFragmentManager().beginTransaction().replace(R.id.comm_container,fragmentpar).commitAllowingStateLoss();
            }
        });
    }

    public void syncTopicChanges(){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        System.out.println("WE HERE BOOIII");

        final Call<ArrayList<Topic>> call = service.getUserTopics(user.getUserId());
        call.enqueue(new Callback<ArrayList<Topic>>() {
            @Override
            public void onResponse(Call<ArrayList<Topic>> call, Response<ArrayList<Topic>> response) {
                if(response.isSuccessful()){
                    userTopics = response.body();
                    if(response.body().size()>0){
                        container.removeAllViews();
                        FragmentCommunity fragment2 = new FragmentCommunity(userTopics);
                        getSupportFragmentManager().beginTransaction().replace(R.id.comm_container,fragment2).commitAllowingStateLoss();
                    }
                    else{
                        FragmentNoResults fragmentpar = new FragmentNoResults(2);
                        getSupportFragmentManager().beginTransaction().replace(R.id.comm_container,fragmentpar).commitAllowingStateLoss();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Topic>> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
                FragmentNoResults fragmentpar = new FragmentNoResults(2);
                getSupportFragmentManager().beginTransaction().replace(R.id.comm_container,fragmentpar).commitAllowingStateLoss();
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
                        btnCommunity.setBackgroundResource(R.color.medTurquoise);
                        btnCommunity.setTextColor(getResources().getColor(R.color.colorWhite));
                        btnHome.setBackgroundResource(R.color.main_White);
                        btnHome.setTextColor(getResources().getColor(R.color.myrtle_green));
                        btnHome.setPaintFlags(btnHome.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        btnCommunity.setPaintFlags(btnCommunity.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                        container.removeAllViews();
                        if(spinnerFilter.getSelectedItem().toString()=="Most recent"){
                            filterPosts(1);
                        }
                        if(spinnerFilter.getSelectedItem().toString()=="Most upvotes"){
                            filterPosts(2);
                        }

                        /*
                        FragmentHome fragment3 = new FragmentHome();
                        getSupportFragmentManager().beginTransaction().replace(R.id.comm_container,fragment3).commit();*/

                    }
                    else{
                        btnCommunity.setBackgroundResource(R.color.main_White);
                        btnCommunity.setTextColor(getResources().getColor(R.color.myrtle_green));
                        btnHome.setBackgroundResource(R.color.medTurquoise);
                        btnHome.setTextColor(getResources().getColor(R.color.colorWhite));
                        btnCommunity.setPaintFlags(btnCommunity.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        btnHome.setPaintFlags(btnHome.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                        container.removeAllViews();
                        syncTopicChanges();
                        /*
                        FragmentCommunity fragment2 = new FragmentCommunity();
                        getSupportFragmentManager().beginTransaction().replace(R.id.comm_container,fragment2).commit();*/
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
}
