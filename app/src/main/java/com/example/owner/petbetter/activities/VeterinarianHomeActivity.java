package com.example.owner.petbetter.activities;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.FacilityMembership;
import com.example.owner.petbetter.classes.LocationMarker;
import com.example.owner.petbetter.classes.Notifications;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.fragments.FragmentNotifs;
import com.example.owner.petbetter.fragments.FragmentPetClinicListing;
import com.example.owner.petbetter.services.MyService;
import com.example.owner.petbetter.services.NotificationReceiver;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.owner.petbetter.ServiceGenerator.BASE_URL;

/**
 * Created by Kristian on 2/24/2018.
 */

public class VeterinarianHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DataAdapter petBetterDb;
    private NavigationView navigationView;
    private RecyclerView messagesRecyclerView;
    private SystemSessionManager systemSessionManager;
    private TextView textNavEmail, textNavUser;
    private User user;
    private MyService notifService;
    private ImageView notifButton;
    private NotificationReceiver notifReceiver = new NotificationReceiver();
    private ImageView imageViewDrawer;
    private ArrayList<Facility> faciList;
    private Veterinarian thisVet;
    private Button addTopicButton;
    private Spinner filterSpinner;
    private FloatingActionButton fab;
    private SwipeRefreshLayout refreshVetHome;

    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    HerokuService service;


    @Override
    protected void onResume() {
        super.onResume();
        VeterinarianHomeActivity.this.registerReceiver(this.notifReceiver, new IntentFilter("com.example.ACTION_LOGOUT"));
        getVetChanges();
        getClinicChanges();
        getMembershipChanges();
    }

    @Override
    protected void onPause() {
        super.onPause();
        VeterinarianHomeActivity.this.unregisterReceiver(notifReceiver);

    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_veterinarian_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout nDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle nToggle = new ActionBarDrawerToggle(this, nDrawerLayout, toolbar, R.string.open, R.string.close);
        nDrawerLayout.setDrawerListener(nToggle);
        nToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        systemSessionManager = new SystemSessionManager(this);
        if (systemSessionManager.checkLogin())
            finish();

        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();
        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        String email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        textNavEmail = (TextView) headerView.findViewById(R.id.textNavEmail);
        textNavEmail.setText(email);

        user = getUser(email);
        imageViewDrawer = (ImageView) headerView.findViewById(R.id.imageViewDrawer);
        addTopicButton = (Button) findViewById(R.id.addTopicButton);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        VeterinarianHomeActivity.this,
                        com.example.owner.petbetter.activities.AddFacilityActivity.class
                );
                startActivity(intent);
            }
        });

        addTopicButton.setVisibility(View.GONE);
        if(user.getUserPhoto()!=null){

            String newFileName = BASE_URL + user.getUserPhoto();
            System.out.println(newFileName);
            //String newFileName = "http://192.168.0.19/petbetter/"+thisMessageRep.getMessagePhoto();
            Glide.with(VeterinarianHomeActivity.this).load(newFileName).error(R.drawable.petbetter_logo_final_final).into(imageViewDrawer);
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


        Intent notifAlarm = new Intent(VeterinarianHomeActivity.this, NotificationReceiver.class);
        if(PendingIntent.getBroadcast(this,0,notifAlarm, PendingIntent.FLAG_NO_CREATE)==null){
            Intent intentStartBroadcast = new Intent().setAction("START_BROADCAST");
            sendBroadcast(intentStartBroadcast);
        }

        /*
        notifAlarm = new Intent(VeterinarianHomeActivity.this, NotificationReceiver.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(VeterinarianHomeActivity.this,0,notifAlarm, PendingIntent.FLAG_NO_CREATE)!=null);
        if(alarmRunning==false){
            pendingIntent = PendingIntent.getBroadcast(VeterinarianHomeActivity.this, 0, notifAlarm, 0);
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 10000, pendingIntent);
            //alarmManager.cancel(pendingIntent);
            //1800000
        }*/

        notifButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Redirect to notifications
                Intent intent = new Intent(VeterinarianHomeActivity.this, com.example.owner.petbetter.activities.NotificationActivity.class);
                startActivity(intent);

            }
        });

        hideItems();

        if(user.getUserType()==1){

            thisVet = getVeterinarianWithId(user.getUserId());


            /*
            if(thisVet!=null){
                faciList = getFacilitiesByVetId(thisVet.getId());
                if(faciList.size()>0){
                    FragmentPetClinicListing fragment = new FragmentPetClinicListing(faciList);
                    getSupportFragmentManager().beginTransaction().replace(R.id.vethome_container,fragment).commitAllowingStateLoss();
                }
            }*/
        }

        getVetChanges();
        getClinicChanges();
        getMembershipChanges();

        refreshVetHome = (SwipeRefreshLayout) findViewById(R.id.refreshVetHome);

        refreshVetHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshVetHome.setRefreshing(true);
                getVetChanges();
                getClinicChanges();
                getMembershipChanges();
                refreshVetHome.setRefreshing(false);
            }
        });

    }

    public void getMembershipChanges(){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        final Call<ArrayList<FacilityMembership>> call = service.getFacilityMembers();
        call.enqueue(new Callback<ArrayList<FacilityMembership>>() {
            @Override
            public void onResponse(Call<ArrayList<FacilityMembership>> call, Response<ArrayList<FacilityMembership>> response) {
                if(response.isSuccessful()){
                    setMembers(response.body());
                    dataSynced(18);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FacilityMembership>> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
    }

    public void getClinicChanges(){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        final Call<ArrayList<Facility>> call = service.getClinics(1);
        call.enqueue(new Callback<ArrayList<Facility>>() {
            @Override
            public void onResponse(Call<ArrayList<Facility>> call, Response<ArrayList<Facility>> response) {
                if(response.isSuccessful()){
                    setFacilities(response.body());
                    dataSynced(2);

                    if(user!=null){
                        faciList = getFacilitiesByVetId(user.getUserId());
                        if(faciList.size()>0){
                            FragmentPetClinicListing fragment = new FragmentPetClinicListing(faciList);
                            getSupportFragmentManager().beginTransaction().replace(R.id.vethome_container,fragment).commitAllowingStateLoss();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Facility>> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.nav_bar,menu);
        hideItems();
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

    public void getVetChanges(){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        final Call<ArrayList<Veterinarian>> call = service.getVeterinarians(1);
        call.enqueue(new Callback<ArrayList<Veterinarian>>() {
            @Override
            public void onResponse(Call<ArrayList<Veterinarian>> call, Response<ArrayList<Veterinarian>> response) {
                if(response.isSuccessful()){
                    setVeterinarians(response.body());
                    dataSynced(1);
                    if(thisVet==null){
                        thisVet = getVeterinarianWithId(user.getUserId());
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Veterinarian>> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
    }

    private void dataSynced(int n){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        petBetterDb.dataSynced(n);
        petBetterDb.closeDatabase();

    }

    public long setVeterinarians(ArrayList<Veterinarian> vetList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setVeterinarians(vetList);
        petBetterDb.closeDatabase();

        return result;
    }

    public long setMembers(ArrayList<FacilityMembership> fmList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setMembers(fmList);
        petBetterDb.closeDatabase();

        return result;
    }

    public long setFacilities(ArrayList<Facility> faciList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setFacilities(faciList);
        petBetterDb.closeDatabase();

        return result;
    }

    public void hideItems(){
        Menu menu = navigationView.getMenu();

        if(user.getUserType()==2){
            menu.findItem(R.id.community2).setVisible(false);
        }
        menu.findItem(R.id.search_drawer).setVisible(false);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.search_drawer) {
            Intent intent = new Intent(this, com.example.owner.petbetter.activities.SearchActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.home) {
            if(user.getUserType()==1||user.getUserType()==4){
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
        else if (id == R.id.community) {
            Intent intent = new Intent(this, com.example.owner.petbetter.activities.HomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.messages) {
            Intent intent = new Intent(this, com.example.owner.petbetter.activities.MessagesActivity.class);
            startActivity(intent);
        } else if (id == R.id.user_profile) {
            Intent intent = new Intent(this, com.example.owner.petbetter.activities.UserProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.bookmarks) {
            Intent intent = new Intent(this, com.example.owner.petbetter.activities.BookmarksActivity.class);
            startActivity(intent);
        } else if (id == R.id.log_out) {
            Intent intent = new Intent(this, com.example.owner.petbetter.activities.MainActivity.class);
            SharedPreferences preferences =getSharedPreferences("prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();

            Intent intentLogout = new Intent().setAction("com.package.ACTION_LOGOUT");
            notifReceiver.onReceive(this, intentLogout);
            sendBroadcast(intentLogout);
            startActivity(intent);


            //stopService(new Intent(VeterinarianHomeActivity.this, MyService.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void killBackground(){
        //Intent notifAlarm = new Intent(VeterinarianHomeActivity.this, NotificationReceiver.class);
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(VeterinarianHomeActivity.this, 0, notifAlarm, 0);
        alarmManager.cancel(pendingIntent);
    }

    private void initializeDatabase() {

        petBetterDb = new DataAdapter(this);

        try {
            petBetterDb.createDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private User getUser(String email) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        User result = petBetterDb.getUser(email);
        petBetterDb.closeDatabase();

        return result;
    }

    private Veterinarian getVeterinarianWithId(long userId) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Veterinarian result = petBetterDb.getVeterinarianWithId(userId);
        petBetterDb.closeDatabase();

        return result;
    }


    private ArrayList<Facility> getFacilitiesByVetId(long vetId) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Facility> result = petBetterDb.getFacilitiesByVetId(vetId);
        petBetterDb.closeDatabase();

        return result;
    }


    public void vetAddFacilityOnClick(View view){
        Intent intent = new Intent(
                VeterinarianHomeActivity.this,
                com.example.owner.petbetter.activities.AddFacilityActivity.class
                );
        startActivity(intent);
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
}
