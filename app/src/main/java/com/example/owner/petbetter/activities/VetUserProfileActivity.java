package com.example.owner.petbetter.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.Pending;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.services.NotificationReceiver;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;

import org.w3c.dom.Text;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.owner.petbetter.ServiceGenerator.BASE_URL;

/**
 * Created by Kristian on 3/26/2018.
 */

public class VetUserProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView vetName;
    private TextView vetDetails;
    private TextView vetSpecialization;
    private TextView vetContactInformation;
    private TextView vetPage;
    private TextView vetWebsite;
    private TextView vetEmail;
    private TextView vetRating;
    private TextView textNavEmail, textNavUser;
    private ImageView vetProfileImage;
    private Button editProfileButton;
    private User user;
    private Veterinarian vet;
    private DataAdapter petBetterDb;
    private NavigationView navigationView;
    private SystemSessionManager systemSessionManager;

    private ImageView imageViewDrawer;
    private Button addTopicButton;
    private ImageView notifButton;
    private ImageView verifyLicense;
    private ImageView verifySpecialty;
    private int licenseCheck;
    private NotificationReceiver notifReceiver = new NotificationReceiver();
    private Spinner spinnerFilter;
    private Button goToUserButton;
    private TextView education;
    private TextView education2;
    private RelativeLayout educationLayout;

    HerokuService service;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_vet_user_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout nDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle nToggle = new ActionBarDrawerToggle(this, nDrawerLayout, toolbar, R.string.open, R.string.close);
        nDrawerLayout.setDrawerListener(nToggle);
        nToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        vetProfileImage = (ImageView) findViewById(R.id.profileImage);
        vetName = (TextView) findViewById(R.id.profileName);
        vetRating = (TextView) findViewById(R.id.vetListRating);
        vetDetails = (TextView) findViewById(R.id.vetDescriptionTextField);
        vetSpecialization = (TextView) findViewById(R.id.specialtyTextField);
        vetContactInformation = (TextView) findViewById(R.id.phoneNumTextField);
        vetPage = (TextView) findViewById(R.id.faciPageTextField);
        vetWebsite = (TextView) findViewById(R.id.faciWebsiteTextField);
        vetEmail = (TextView) findViewById(R.id.faciEmailTextField);
        editProfileButton = (Button) findViewById(R.id.editVetProfileButton);
        verifyLicense = (ImageView) findViewById(R.id.vetVerified);
        verifySpecialty = (ImageView) findViewById(R.id.verifiedSpecialtyIndicator);
        education = (TextView) findViewById(R.id.vetEducationTextField);
        education2 = (TextView) findViewById(R.id.vetEducation2TextField);
        educationLayout = (RelativeLayout) findViewById(R.id.educationLayout);

        verifyLicense.setVisibility(View.INVISIBLE);
        verifySpecialty.setVisibility(View.INVISIBLE);

        notifButton = (ImageView) findViewById(R.id.imageview_notifs);
        addTopicButton = (Button) findViewById(R.id.addTopicButton);
        goToUserButton = (Button) findViewById(R.id.goToUserProfileButton);


        addTopicButton.setVisibility(View.GONE);

        systemSessionManager = new SystemSessionManager(this);
        if (systemSessionManager.checkLogin())
            finish();

        HashMap<String, String> userIn = systemSessionManager.getUserDetails();
        initializeDatabase();
        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        String email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);
        vet = getVetWithUserID(user.getUserId());
        textNavEmail = (TextView) headerView.findViewById(R.id.textNavEmail);
        textNavEmail.setText(email);
        textNavUser = (TextView) headerView.findViewById(R.id.textNavUser);
        textNavUser.setText(user.getName());
        imageViewDrawer = (ImageView) headerView.findViewById(R.id.imageViewDrawer);
        if (user.getUserPhoto() != null) {

            String newFileName = BASE_URL + user.getUserPhoto();
            System.out.println(newFileName);
            //String newFileName = "http://192.168.0.19/petbetter/"+thisMessageRep.getMessagePhoto();
            Glide.with(VetUserProfileActivity.this).load(newFileName).error(R.drawable.petbetter_logo_final_final).into(imageViewDrawer);
            /*
            Picasso.with(inflater.getContext()).load("http://".concat(newFileName))
                    .error(R.drawable.back_button).into(holder.messageRepImage);*/
            //setImage(holder.messageRepImage, newFileName);

            imageViewDrawer.setVisibility(View.VISIBLE);
        }


        vetName.setText(user.getName() + ",");
        vetSpecialization.setText(vet.getSpecialty());
        vetRating.setText(String.valueOf(vet.getRating()));
        vetDetails.setText(vet.getProfileDesc());

        if(!vet.getEducation().equals(",")){
            String[] educationArray = vet.getEducation().split(",", -1);
            if(educationArray.length>0&&educationArray[0].length()>0){
                education.setText("Undergraduate University: "+educationArray[0]);
            }
            if(educationArray.length>1&&educationArray[1].length()>0){
                education2.setText("Graduate University: "+educationArray[1]);
            }
        }
        else{
            educationLayout.setVisibility(View.GONE);
        }

        vetContactInformation.setText("Phone number: "+user.getMobileNumber());
        vetPage.setVisibility(View.GONE);
        vetWebsite.setVisibility(View.GONE);
        vetEmail.setText("Email: "+user.getEmail());
        if (user.getUserPhoto() != null) {

            String newFileName = BASE_URL + user.getUserPhoto();
            System.out.println(newFileName);
            Glide.with(VetUserProfileActivity.this).load(newFileName).error(R.drawable.petbetter_logo_final_final).into(vetProfileImage);
            /*
            Picasso.with(inflater.getContext()).load("http://".concat(newFileName))
                    .error(R.drawable.back_button).into(holder.messageRepImage);*/
            //setImage(holder.messageRepImage, newFileName);

            vetProfileImage.setVisibility(View.VISIBLE);
            vetProfileImage.setAdjustViewBounds(true);
        }


        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Bundle extras = new Bundle();

                extras.putString("first_name", user.getFirstName());
                extras.putString("last_name", user.getLastName());
                extras.putString("contact_num", user.getMobileNumber());
                extras.putString("description", vet.getProfileDesc());
                extras.putString("specialty", vet.getSpecialty());
                */
                Intent intent = new Intent(VetUserProfileActivity.this, EditVetProfileActivity.class);
                startActivity(intent);
            }
        });

        goToUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VetUserProfileActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });

        syncPendingChanges();

    }

    public void syncPendingChanges() {

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);


        final Call<ArrayList<Pending>> call2 = service.getPendingByUser(user.getUserId());
        call2.enqueue(new Callback<ArrayList<Pending>>() {
            @Override
            public void onResponse(Call<ArrayList<Pending>> call, Response<ArrayList<Pending>> response) {
                if (response.isSuccessful()) {
                    ArrayList<Pending> pendingList = response.body();
                    for (Pending pending : pendingList) {
                        if ((pending.getType() == 1 && pending.getIsApproved() == 1) ||
                                (pending.getType() == 2 && pending.getIsApproved() == 1)) {
                            licenseCheck += 1;
                            if (licenseCheck == 2) {
                                verifyLicense.setVisibility(View.VISIBLE);
                            }
                        }
                        if (pending.getType() == 4 && pending.getIsApproved() == 1) {
                            verifySpecialty.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Pending>> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        VetUserProfileActivity.this.registerReceiver(this.notifReceiver, new IntentFilter("com.example.ACTION_LOGOUT"));

        systemSessionManager = new SystemSessionManager(this);
        if (systemSessionManager.checkLogin())
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

        vetProfileImage = (ImageView) findViewById(R.id.profileImage);
        vetName = (TextView) findViewById(R.id.profileName);
        vetRating = (TextView) findViewById(R.id.vetListRating);
        vetDetails = (TextView) findViewById(R.id.vetDescriptionTextField);
        vetSpecialization = (TextView) findViewById(R.id.specialtyTextField);
        vetContactInformation = (TextView) findViewById(R.id.phoneNumTextField);

        vetName.setText(user.getName() + ",");
        vetSpecialization.setText(vet.getSpecialty());
        vetRating.setText(String.valueOf(vet.getRating()));
        vetDetails.setText(vet.getProfileDesc());

        if (user.getUserPhoto() != null) {

            String newFileName = BASE_URL + user.getUserPhoto();
            System.out.println(newFileName);
            Glide.with(VetUserProfileActivity.this).load(newFileName).error(R.drawable.petbetter_logo_final_final).into(vetProfileImage);
            /*
            Picasso.with(inflater.getContext()).load("http://".concat(newFileName))
                    .error(R.drawable.back_button).into(holder.messageRepImage);*/
            //setImage(holder.messageRepImage, newFileName);

            vetProfileImage.setVisibility(View.VISIBLE);
            vetProfileImage.setAdjustViewBounds(true);
        }

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

    private Veterinarian getVetWithUserID(long user_id) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Veterinarian result = petBetterDb.getVeterinarianWithId(user_id);
        petBetterDb.closeDatabase();

        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.nav_bar,menu);
        hideItems();
        return true;
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

        if (id == R.id.home) {
            if (user.getUserType() == 1) {
                Intent intent = new Intent(this, com.example.owner.petbetter.activities.VeterinarianHomeActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, com.example.owner.petbetter.activities.CommActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.community2) {
            Intent intent = new Intent(this, com.example.owner.petbetter.activities.CommActivity.class);
            startActivity(intent);
        } else if (id == R.id.community) {
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
            SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();

            Intent intentLogout = new Intent().setAction("com.package.ACTION_LOGOUT");
            notifReceiver.onReceive(this, intentLogout);
            sendBroadcast(intentLogout);
            startActivity(intent);


        }
        return true;
    }
}

