package com.example.owner.petbetter.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.activities.VetProfileActivity;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.Notifications;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.fragments.FragmentPetClinicListing;
import com.example.owner.petbetter.services.MyService;
import com.example.owner.petbetter.services.NotificationReceiver;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;

import com.example.owner.petbetter.fragments.FragmentVetListing;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import android.widget.Toast;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.owner.petbetter.ServiceGenerator.BASE_URL;


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
    private SwipeRefreshLayout swipeRefreshLayout;
    private int currFragment;
    private ImageView notifButton;
    private NotificationReceiver notifReceiver = new NotificationReceiver();

    private ArrayList<Veterinarian> vetList;
    HerokuService service;
    private ImageView imageViewDrawer;
    private Button addTopicButton;
    private Spinner spinnerFilter;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    /*
    @Override
    protected void onPause() {
        super.onPause();
        HomeActivity.this.unregisterReceiver(notifReceiver);
    }
*/
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
        //currFragment = 1;


        menuBar = (Toolbar) findViewById(R.id.menu_bar);
        vetButton = (Button) findViewById(R.id.vetButton);
        petCareButton = (Button) findViewById(R.id.petCareButton);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshHome);
        notifButton = (ImageView) findViewById(R.id.imageview_notifs);
        addTopicButton = (Button) findViewById(R.id.addTopicButton);
        /*
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.myrtle_green));
        //tabLayout.setSelectedTabIndicatorHeight((int) (3 * getResources().getDisplayMetrics().density));
        tabLayout.setTabTextColors(getResources().getColor(R.color.colorWhite), getResources().getColor(R.color.myrtle_green));
*/

        addTopicButton.setVisibility(View.GONE);

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

        imageViewDrawer = (ImageView) headerView.findViewById(R.id.imageViewDrawer);
        if(user.getUserPhoto()!=null){

            String newFileName = BASE_URL + user.getUserPhoto();
            System.out.println(newFileName);
            //String newFileName = "http://192.168.0.19/petbetter/"+thisMessageRep.getMessagePhoto();
            Glide.with(HomeActivity.this).load(newFileName).error(R.drawable.app_icon_yellow).into(imageViewDrawer);
            /*
            Picasso.with(inflater.getContext()).load("http://".concat(newFileName))
                    .error(R.drawable.back_button).into(holder.messageRepImage);*/
            //setImage(holder.messageRepImage, newFileName);

            imageViewDrawer.setVisibility(View.VISIBLE);
        }

        //userName = user.getName();

        System.out.println(user.getName());
        System.out.println(email);
        System.out.println(user.getUserId());
        textNavUser = (TextView) headerView.findViewById(R.id.textNavUser);
        textNavUser.setText(user.getName());

        //vetButtonClicked(this.navigationView);

        vetButtonClicked(this.findViewById(android.R.id.content));


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                syncVetChanges();
                syncClinicChanges();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        if(!getUnsyncedNotifications().isEmpty())
            notifButton.setImageResource(R.mipmap.ic_notifications_active_black_24dp);

        notifButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Redirect to notifications
                Intent intent = new Intent(HomeActivity.this, com.example.owner.petbetter.activities.NotificationActivity.class);
                startActivity(intent);
            }
        });

        hideItems();
    }
    @Override
    protected void onResume(){
        super.onResume();

        if(currFragment==1){
            vetButtonClicked(this.findViewById(android.R.id.content));
        }
        else{
            petCareButtonClicked(this.findViewById(android.R.id.content));
        }

        HomeActivity.this.registerReceiver(this.notifReceiver, new IntentFilter("com.example.ACTION_LOGOUT"));


        //initializeDatabase();
    }
    private void setUpViewPager(ViewPager viewPager){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        //vetList = getVeterinarians();
        viewPagerAdapter.addFragment(new FragmentVetListing(), "Veterinarians");
        viewPagerAdapter.addFragment(new FragmentPetClinicListing(), "Facilities");
        viewPager.setAdapter(viewPagerAdapter);
    }
    /*
    @Override
    protected void onRestart(){
        super.onRestart();
        recreate();
    }*/

    public void hideItems(){
        Menu menu = navigationView.getMenu();

        if(user.getUserType()==2){
            menu.findItem(R.id.community2).setVisible(false);
        }
        menu.findItem(R.id.search_drawer).setVisible(false);
    }

    public void syncClinicChanges(){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        System.out.println("WE HERE BOOIII");

        final Call<ArrayList<Facility>> call = service.getClinics(1);
        call.enqueue(new Callback<ArrayList<Facility>>() {
            @Override
            public void onResponse(Call<ArrayList<Facility>> call, Response<ArrayList<Facility>> response) {
                if(response.isSuccessful()){
                    setFacilities(response.body());

                    if(currFragment==1)
                        vetButtonClicked(findViewById(android.R.id.content));
                    else
                        petCareButtonClicked(findViewById(android.R.id.content));

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Facility>> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
    }

    public void syncVetChanges(){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        System.out.println("WE HERE BOOIII");

        final Call<ArrayList<Veterinarian>> call = service.getVeterinarians(1);
        call.enqueue(new Callback<ArrayList<Veterinarian>>() {
            @Override
            public void onResponse(Call<ArrayList<Veterinarian>> call, Response<ArrayList<Veterinarian>> response) {
                if(response.isSuccessful()){
                    setVeterinarians(response.body());

                    if(currFragment==1)
                        vetButtonClicked(findViewById(android.R.id.content));
                    else
                        petCareButtonClicked(findViewById(android.R.id.content));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Veterinarian>> call, Throwable t) {
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

    private ArrayList<Facility> getUnsyncedFacilities(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayList<Facility> result = petBetterDb.getUnsyncedFacilities();
        petBetterDb.closeDatabase();

        return result;
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

    private ArrayList<Veterinarian> getUnsyncedVets(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayList<Veterinarian> result = petBetterDb.getUnsyncedVets();
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

        currFragment = 1;
        vetButton.setBackgroundResource(R.color.main_White);
        vetButton.setTextColor(getResources().getColor(R.color.myrtle_green));
        petCareButton.setBackgroundResource(R.color.medTurquoise);
        petCareButton.setTextColor(getResources().getColor(R.color.colorWhite));
        //FragmentVetListing fragment = (FragmentVetListing) getSupportFragmentManager().findFragmentById(R.id.frame_container);
        vetList = getVeterinarians();
        vetButton.setPaintFlags(vetButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        petCareButton.setPaintFlags(petCareButton.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));

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
        currFragment = 2;
        petCareButton.setBackgroundResource(R.color.main_White);
        petCareButton.setTextColor(getResources().getColor(R.color.myrtle_green));
        vetButton.setBackgroundResource(R.color.medTurquoise);
        vetButton.setTextColor(getResources().getColor(R.color.colorWhite));
        FragmentPetClinicListing fragment = new FragmentPetClinicListing();
        petCareButton.setPaintFlags(vetButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        vetButton.setPaintFlags(petCareButton.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));

        getSupportFragmentManager().beginTransaction().add(R.id.frame_container,fragment).commit();
    }

    public void commButtonClicked(View view){
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


    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.search_drawer) {
            Intent intent = new Intent(this, com.example.owner.petbetter.activities.SearchActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.home){
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
            //stopService(new Intent(HomeActivity.this, MyService.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
