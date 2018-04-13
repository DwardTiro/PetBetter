package com.example.owner.petbetter.activities;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.Topic;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.fragments.FragmentCommunity;
import com.example.owner.petbetter.fragments.FragmentHome;
import com.example.owner.petbetter.fragments.FragmentPetClinicListing;
import com.example.owner.petbetter.fragments.FragmentUser;
import com.example.owner.petbetter.fragments.FragmentVetListing;
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

public class MonitorVetsActivity extends AppCompatActivity {

    private Button monitorVetsButton;
    private Button monitorFaciButton;
    private Button monitorTopicsButton;
    private Button monitorPostsButton;
    private AutoCompleteTextView actvMonitor;
    private int currFragment = 1;
    private HerokuService service;
    private HerokuService service2;
    private HerokuService service3;
    private HerokuService service4;
    private FragmentUser fragment1;
    private FragmentPetClinicListing fragment2;

    private DataAdapter petBetterDb;
    private User user;
    private SystemSessionManager systemSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_vets);

        monitorVetsButton = (Button) findViewById(R.id.monitorVetsButton);
        monitorFaciButton = (Button) findViewById(R.id.monitorFaciButton);
        monitorTopicsButton = (Button) findViewById(R.id.monitorTopicsButton);
        monitorPostsButton = (Button) findViewById(R.id.monitorPostsButton);
        actvMonitor = (AutoCompleteTextView) findViewById(R.id.actvSearch);

        monitorTopicsButton.setVisibility(View.GONE);
        monitorPostsButton.setVisibility(View.GONE);

        systemSessionManager = new SystemSessionManager(this);
        if (systemSessionManager.checkLogin())
            finish();
        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();
        String email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);

        monitorVetsClicked(this.findViewById(android.R.id.content));
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

    public void searchBackButtonClicked(View view) {
        finish();
    }

    public void monitorVetsClicked(View view){
        currFragment = 1;
        monitorVetsButton.setBackgroundResource(R.color.main_White);
        monitorVetsButton.setTextColor(getResources().getColor(R.color.myrtle_green));
        monitorFaciButton.setBackgroundResource(R.color.medTurquoise);
        monitorFaciButton.setTextColor(getResources().getColor(R.color.colorWhite));
        monitorTopicsButton.setBackgroundResource(R.color.medTurquoise);
        monitorTopicsButton.setTextColor(getResources().getColor(R.color.colorWhite));
        monitorPostsButton.setBackgroundResource(R.color.medTurquoise);
        monitorPostsButton.setTextColor(getResources().getColor(R.color.colorWhite));
        monitorVetsButton.setPaintFlags(monitorVetsButton.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        monitorFaciButton.setPaintFlags(monitorFaciButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
        monitorTopicsButton.setPaintFlags(monitorTopicsButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
        monitorPostsButton.setPaintFlags(monitorPostsButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));

        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        final Call<ArrayList<User>> call = service.getUsers();
        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                ArrayList<User> userList = response.body();

                fragment1 = new FragmentUser(userList);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_monitoritems,fragment1).
                        addToBackStack(null).commitAllowingStateLoss();


                // getSupportFragmentManager().beginTransaction().add(R.id.frame_search,fragment1).commitAllowingStateLoss();
                //ArrayAdapter<Veterinarian> adapter = new ArrayAdapter<Veterinarian>(this,R.layout.,vetList);

            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
                Toast.makeText(MonitorVetsActivity.this, "Unable to get vets from server", Toast.LENGTH_LONG);
            }
        });


        actvMonitor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(currFragment==1){
                    service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

                    //query the substring to server data

                    Gson gson = new GsonBuilder().serializeNulls().create();
                    String jsonArray = gson.toJson(actvMonitor.getText().toString());

                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());

                    final Call<ArrayList<User>> call = service.queryUsers(body);
                    call.enqueue(new Callback<ArrayList<User>>() {
                        @Override
                        public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                            ArrayList<User> userList = response.body();


                            FragmentUser fragment1 = new FragmentUser(userList);
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame_monitoritems,fragment1).
                                    addToBackStack(null).commitAllowingStateLoss();



                            //ArrayAdapter<Veterinarian> adapter = new ArrayAdapter<Veterinarian>(this,R.layout.,vetList);

                        }

                        @Override
                        public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                            Log.d("onFailure", t.getLocalizedMessage());
                            Toast.makeText(MonitorVetsActivity.this, "Unable to get vets from server", Toast.LENGTH_LONG);
                        }
                    });
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void monitorFaciClicked(View view){
        currFragment = 2;
        monitorFaciButton.setBackgroundResource(R.color.main_White);
        monitorFaciButton.setTextColor(getResources().getColor(R.color.myrtle_green));
        monitorVetsButton.setBackgroundResource(R.color.medTurquoise);
        monitorVetsButton.setTextColor(getResources().getColor(R.color.colorWhite));
        monitorTopicsButton.setBackgroundResource(R.color.medTurquoise);
        monitorTopicsButton.setTextColor(getResources().getColor(R.color.colorWhite));
        monitorPostsButton.setBackgroundResource(R.color.medTurquoise);
        monitorPostsButton.setTextColor(getResources().getColor(R.color.colorWhite));
        monitorFaciButton.setPaintFlags(monitorFaciButton.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        monitorVetsButton.setPaintFlags(monitorVetsButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
        monitorTopicsButton.setPaintFlags(monitorTopicsButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
        monitorPostsButton.setPaintFlags(monitorPostsButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));

        service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);


        final Call<ArrayList<Facility>> call = service2.getClinics(0);
        call.enqueue(new Callback<ArrayList<Facility>>() {
            @Override
            public void onResponse(Call<ArrayList<Facility>> call, Response<ArrayList<Facility>> response) {
                ArrayList<Facility> faciList = response.body();


                FragmentPetClinicListing fragment2 = new FragmentPetClinicListing(faciList);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_monitoritems,fragment2).
                        addToBackStack(null).commitAllowingStateLoss();

                //ArrayAdapter<Veterinarian> adapter = new ArrayAdapter<Veterinarian>(this,R.layout.,vetList);

            }

            @Override
            public void onFailure(Call<ArrayList<Facility>> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
                Toast.makeText(MonitorVetsActivity.this, "Unable to get vets from server", Toast.LENGTH_LONG);
            }
        });

        actvMonitor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(currFragment==2){
                    service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

                    //query the substring to server data

                    Gson gson = new GsonBuilder().serializeNulls().create();
                    String jsonArray = gson.toJson(actvMonitor.getText().toString());

                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());

                    final Call<ArrayList<Facility>> call = service2.queryFacilities(user.getUserId(), actvMonitor.getText().toString());
                    call.enqueue(new Callback<ArrayList<Facility>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Facility>> call, Response<ArrayList<Facility>> response) {
                            ArrayList<Facility> faciList = response.body();


                            fragment2 = new FragmentPetClinicListing(faciList);
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame_monitoritems,fragment2).
                                    addToBackStack(null).commitAllowingStateLoss();

                            //ArrayAdapter<Veterinarian> adapter = new ArrayAdapter<Veterinarian>(this,R.layout.,vetList);

                        }

                        @Override
                        public void onFailure(Call<ArrayList<Facility>> call, Throwable t) {
                            Log.d("onFailure", t.getLocalizedMessage());
                            Toast.makeText(MonitorVetsActivity.this, "Unable to get vets from server", Toast.LENGTH_LONG);
                        }
                    });
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void monitorTopicsClicked(View view){
        currFragment = 3;
        monitorTopicsButton.setBackgroundResource(R.color.main_White);
        monitorTopicsButton.setTextColor(getResources().getColor(R.color.myrtle_green));
        monitorFaciButton.setBackgroundResource(R.color.medTurquoise);
        monitorFaciButton.setTextColor(getResources().getColor(R.color.colorWhite));
        monitorVetsButton.setBackgroundResource(R.color.medTurquoise);
        monitorVetsButton.setTextColor(getResources().getColor(R.color.colorWhite));
        monitorPostsButton.setBackgroundResource(R.color.medTurquoise);
        monitorPostsButton.setTextColor(getResources().getColor(R.color.colorWhite));
        monitorTopicsButton.setPaintFlags(monitorTopicsButton.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        monitorFaciButton.setPaintFlags(monitorFaciButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
        monitorVetsButton.setPaintFlags(monitorVetsButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
        monitorPostsButton.setPaintFlags(monitorPostsButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));

        service3 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);


        final Call<ArrayList<Topic>> call = service3.getTopics();
        call.enqueue(new Callback<ArrayList<Topic>>() {
            @Override
            public void onResponse(Call<ArrayList<Topic>> call, Response<ArrayList<Topic>> response) {
                ArrayList<Topic> topicList = response.body();


                FragmentCommunity fragment1 = new FragmentCommunity(topicList);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_monitoritems,fragment1).
                        addToBackStack(null).commitAllowingStateLoss();


                //ArrayAdapter<Veterinarian> adapter = new ArrayAdapter<Veterinarian>(this,R.layout.,vetList);

            }

            @Override
            public void onFailure(Call<ArrayList<Topic>> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
                Toast.makeText(MonitorVetsActivity.this, "Unable to get vets from server", Toast.LENGTH_LONG);
            }
        });

        actvMonitor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(currFragment==3){
                    service3 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

                    //query the substring to server data

                    Gson gson = new GsonBuilder().serializeNulls().create();
                    String jsonArray = gson.toJson(actvMonitor.getText().toString());

                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());

                    final Call<ArrayList<Topic>> call = service3.queryTopics(user.getUserId(), actvMonitor.getText().toString());
                    call.enqueue(new Callback<ArrayList<Topic>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Topic>> call, Response<ArrayList<Topic>> response) {
                            ArrayList<Topic> topicList = response.body();



                            FragmentCommunity fragment1 = new FragmentCommunity(topicList);
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame_monitoritems,fragment1).
                                    addToBackStack(null).commitAllowingStateLoss();

                            //ArrayAdapter<Veterinarian> adapter = new ArrayAdapter<Veterinarian>(this,R.layout.,vetList);

                        }

                        @Override
                        public void onFailure(Call<ArrayList<Topic>> call, Throwable t) {
                            Log.d("onFailure", t.getLocalizedMessage());
                            Toast.makeText(MonitorVetsActivity.this, "Unable to get vets from server", Toast.LENGTH_LONG);
                        }
                    });
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void monitorPostsClicked(View view){
        currFragment = 4;
        monitorPostsButton.setBackgroundResource(R.color.main_White);
        monitorPostsButton.setTextColor(getResources().getColor(R.color.myrtle_green));
        monitorFaciButton.setBackgroundResource(R.color.medTurquoise);
        monitorFaciButton.setTextColor(getResources().getColor(R.color.colorWhite));
        monitorTopicsButton.setBackgroundResource(R.color.medTurquoise);
        monitorTopicsButton.setTextColor(getResources().getColor(R.color.colorWhite));
        monitorVetsButton.setBackgroundResource(R.color.medTurquoise);
        monitorVetsButton.setTextColor(getResources().getColor(R.color.colorWhite));
        monitorPostsButton.setPaintFlags(monitorPostsButton.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        monitorFaciButton.setPaintFlags(monitorFaciButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
        monitorTopicsButton.setPaintFlags(monitorTopicsButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
        monitorVetsButton.setPaintFlags(monitorVetsButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));

        service4 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);


        final Call<ArrayList<Post>> call = service4.getPosts();
        call.enqueue(new Callback<ArrayList<Post>>() {
            @Override
            public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
                ArrayList<Post> postList = response.body();

                FragmentHome fragment1 = new FragmentHome(postList);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_monitoritems,fragment1).
                        addToBackStack(null).commitAllowingStateLoss();


                //ArrayAdapter<Veterinarian> adapter = new ArrayAdapter<Veterinarian>(this,R.layout.,vetList);

            }

            @Override
            public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
                Toast.makeText(MonitorVetsActivity.this, "Unable to get vets from server", Toast.LENGTH_LONG);
            }
        });

        actvMonitor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(currFragment==4){
                    service4 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

                    //query the substring to server data

                    Gson gson = new GsonBuilder().serializeNulls().create();
                    String jsonArray = gson.toJson(actvMonitor.getText().toString());

                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());

                    final Call<ArrayList<Post>> call = service4.queryPosts(user.getUserId(), actvMonitor.getText().toString());
                    call.enqueue(new Callback<ArrayList<Post>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
                            ArrayList<Post> postList = response.body();


                            FragmentHome fragment1 = new FragmentHome(postList);
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame_monitoritems,fragment1).
                                    addToBackStack(null).commitAllowingStateLoss();

                            //ArrayAdapter<Veterinarian> adapter = new ArrayAdapter<Veterinarian>(this,R.layout.,vetList);

                        }

                        @Override
                        public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
                            Log.d("onFailure", t.getLocalizedMessage());
                            Toast.makeText(MonitorVetsActivity.this, "Unable to get vets from server", Toast.LENGTH_LONG);
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

}
