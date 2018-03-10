package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.adapters.SearchListingAdapter;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.Topic;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.fragments.FragmentCommunity;
import com.example.owner.petbetter.fragments.FragmentPetClinicListing;
import com.example.owner.petbetter.fragments.FragmentPosts;
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


/**
 * Created by Kristian on 8/12/2017.
 */

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private ActionMenuView amvMenu;
    private SearchView searchView;

    //needed for database initialization
    private DataAdapter petBetterDb;
    //user data
    private User user;
    //get current session info etc
    private SystemSessionManager systemSessionManager;

    private RecyclerView recyclerView;
    private SearchListingAdapter searchListingAdapter;
    private ArrayList<Topic> topicList;
    private ArrayList<Facility> clinicList;
    private ArrayList<Post> postList;

    private Button vetSearchButton;
    private Button petSearchButton;
    private Button topicSearchButton;
    private Button postSearchButton;
    private AutoCompleteTextView actvSearch;
    private RelativeLayout relativeLayout;
    private int currFragment = 1;
    HerokuService service;
    HerokuService service2;
    HerokuService service3;
    HerokuService service4;
    private FragmentVetListing fragment1;
    private FragmentPetClinicListing fragment2;

    private String VET_TAG = "Veterinarians";
    private String FACI_TAG = "Facilities";
    private String TOPIC_TAG = "Topics";
    private String POST_TAG = "Posts";


    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_search);

        Toolbar searchToolbar = (Toolbar) findViewById(R.id.searchToolbar);
        setSupportActionBar(searchToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        systemSessionManager = new SystemSessionManager(this);
        if (systemSessionManager.checkLogin())
            finish();
        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();
        String email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);

        vetSearchButton = (Button) findViewById(R.id.vetSearchButton);
        petSearchButton = (Button) findViewById(R.id.petSearchButton);
        topicSearchButton = (Button) findViewById(R.id.topicSearchButton);
        postSearchButton = (Button) findViewById(R.id.postSearchButton);
        actvSearch = (AutoCompleteTextView) findViewById(R.id.actvSearch);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeSearch);
        relativeLayout.setClickable(true);

        vetSearchClicked(this.findViewById(android.R.id.content));

        //  recyclerView = (RecyclerView) findViewById(R.id.recyclerViewSearch);
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    public void vetSearchClicked(View v){

        currFragment = 1;
        vetSearchButton.setBackgroundResource(R.color.myrtle_green);
        petSearchButton.setBackgroundResource(R.color.medTurquoise);
        topicSearchButton.setBackgroundResource(R.color.medTurquoise);
        postSearchButton.setBackgroundResource(R.color.medTurquoise);

        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);


        final Call<ArrayList<Veterinarian>> call = service.getVeterinarians();
        call.enqueue(new Callback<ArrayList<Veterinarian>>() {
            @Override
            public void onResponse(Call<ArrayList<Veterinarian>> call, Response<ArrayList<Veterinarian>> response) {
                ArrayList<Veterinarian> vetList = response.body();

                fragment1 = new FragmentVetListing(vetList);

                getSupportFragmentManager().beginTransaction().replace(R.id.frame_search,fragment1).
                        addToBackStack(null).commitAllowingStateLoss();
                // getSupportFragmentManager().beginTransaction().add(R.id.frame_search,fragment1).commitAllowingStateLoss();
                //ArrayAdapter<Veterinarian> adapter = new ArrayAdapter<Veterinarian>(this,R.layout.,vetList);

            }

            @Override
            public void onFailure(Call<ArrayList<Veterinarian>> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
                Toast.makeText(SearchActivity.this, "Unable to get vets from server", Toast.LENGTH_LONG);
            }
        });


        actvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(currFragment==1){
                    service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

                    //query the substring to server data

                    Gson gson = new GsonBuilder().serializeNulls().create();
                    String jsonArray = gson.toJson(actvSearch.getText().toString());

                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());

                    final Call<ArrayList<Veterinarian>> call = service.queryVeterinarians(body);
                    call.enqueue(new Callback<ArrayList<Veterinarian>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Veterinarian>> call, Response<ArrayList<Veterinarian>> response) {
                            ArrayList<Veterinarian> vetList = response.body();

                            FragmentVetListing fragment1 = new FragmentVetListing(vetList);
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame_search,fragment1).
                                    addToBackStack(null).commitAllowingStateLoss();
                            //ArrayAdapter<Veterinarian> adapter = new ArrayAdapter<Veterinarian>(this,R.layout.,vetList);

                        }

                        @Override
                        public void onFailure(Call<ArrayList<Veterinarian>> call, Throwable t) {
                            Log.d("onFailure", t.getLocalizedMessage());
                            Toast.makeText(SearchActivity.this, "Unable to get vets from server", Toast.LENGTH_LONG);
                        }
                    });
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        /*
        * PROCEDURE:
        * 1. Find a way to be able to feed search suggestions via json (from server)
        * 2. Modify get functions from server to display new data.
        * */
    }

    public void petSearchClicked(View v){
        currFragment = 2;
        vetSearchButton.setBackgroundResource(R.color.medTurquoise);
        petSearchButton.setBackgroundResource(R.color.myrtle_green);
        topicSearchButton.setBackgroundResource(R.color.medTurquoise);
        postSearchButton.setBackgroundResource(R.color.medTurquoise);

        service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);


        final Call<ArrayList<Facility>> call = service2.getClinics();
        call.enqueue(new Callback<ArrayList<Facility>>() {
            @Override
            public void onResponse(Call<ArrayList<Facility>> call, Response<ArrayList<Facility>> response) {
                ArrayList<Facility> faciList = response.body();

                FragmentPetClinicListing fragment1 = new FragmentPetClinicListing(faciList);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_search,fragment1).
                        addToBackStack(null).commitAllowingStateLoss();
                //ArrayAdapter<Veterinarian> adapter = new ArrayAdapter<Veterinarian>(this,R.layout.,vetList);

            }

            @Override
            public void onFailure(Call<ArrayList<Facility>> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
                Toast.makeText(SearchActivity.this, "Unable to get vets from server", Toast.LENGTH_LONG);
            }
        });

        actvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(currFragment==2){
                    service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

                    //query the substring to server data

                    Gson gson = new GsonBuilder().serializeNulls().create();
                    String jsonArray = gson.toJson(actvSearch.getText().toString());

                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());

                    final Call<ArrayList<Facility>> call = service2.queryFacilities(body);
                    call.enqueue(new Callback<ArrayList<Facility>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Facility>> call, Response<ArrayList<Facility>> response) {
                            ArrayList<Facility> faciList = response.body();

                            fragment2 = new FragmentPetClinicListing(faciList);
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame_search,fragment2).
                                    addToBackStack(null).commitAllowingStateLoss();
                            //ArrayAdapter<Veterinarian> adapter = new ArrayAdapter<Veterinarian>(this,R.layout.,vetList);

                        }

                        @Override
                        public void onFailure(Call<ArrayList<Facility>> call, Throwable t) {
                            Log.d("onFailure", t.getLocalizedMessage());
                            Toast.makeText(SearchActivity.this, "Unable to get vets from server", Toast.LENGTH_LONG);
                        }
                    });
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void topicSearchClicked(View v){
        currFragment = 3;
        vetSearchButton.setBackgroundResource(R.color.medTurquoise);
        petSearchButton.setBackgroundResource(R.color.medTurquoise);
        topicSearchButton.setBackgroundResource(R.color.myrtle_green);
        postSearchButton.setBackgroundResource(R.color.medTurquoise);

        service3 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);


        final Call<ArrayList<Topic>> call = service3.getTopics();
        call.enqueue(new Callback<ArrayList<Topic>>() {
            @Override
            public void onResponse(Call<ArrayList<Topic>> call, Response<ArrayList<Topic>> response) {
                ArrayList<Topic> topicList = response.body();

                FragmentCommunity fragment1 = new FragmentCommunity(topicList);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_search,fragment1).
                        addToBackStack(null).commitAllowingStateLoss();
                //ArrayAdapter<Veterinarian> adapter = new ArrayAdapter<Veterinarian>(this,R.layout.,vetList);

            }

            @Override
            public void onFailure(Call<ArrayList<Topic>> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
                Toast.makeText(SearchActivity.this, "Unable to get vets from server", Toast.LENGTH_LONG);
            }
        });

        actvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(currFragment==3){
                    service3 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

                    //query the substring to server data

                    Gson gson = new GsonBuilder().serializeNulls().create();
                    String jsonArray = gson.toJson(actvSearch.getText().toString());

                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());

                    final Call<ArrayList<Topic>> call = service3.queryTopics(body);
                    call.enqueue(new Callback<ArrayList<Topic>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Topic>> call, Response<ArrayList<Topic>> response) {
                            ArrayList<Topic> topicList = response.body();

                            FragmentCommunity fragment1 = new FragmentCommunity(topicList);
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame_search,fragment1).
                                    addToBackStack(null).commitAllowingStateLoss();
                            //ArrayAdapter<Veterinarian> adapter = new ArrayAdapter<Veterinarian>(this,R.layout.,vetList);

                        }

                        @Override
                        public void onFailure(Call<ArrayList<Topic>> call, Throwable t) {
                            Log.d("onFailure", t.getLocalizedMessage());
                            Toast.makeText(SearchActivity.this, "Unable to get vets from server", Toast.LENGTH_LONG);
                        }
                    });
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void postSearchClicked(View v){
        currFragment = 4;
        vetSearchButton.setBackgroundResource(R.color.medTurquoise);
        petSearchButton.setBackgroundResource(R.color.medTurquoise);
        topicSearchButton.setBackgroundResource(R.color.medTurquoise);
        postSearchButton.setBackgroundResource(R.color.myrtle_green);

        service4 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);


        final Call<ArrayList<Post>> call = service4.getPosts();
        call.enqueue(new Callback<ArrayList<Post>>() {
            @Override
            public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
                ArrayList<Post> postList = response.body();

                FragmentPosts fragment1 = new FragmentPosts(postList);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_search,fragment1).
                        addToBackStack(null).commitAllowingStateLoss();
                //ArrayAdapter<Veterinarian> adapter = new ArrayAdapter<Veterinarian>(this,R.layout.,vetList);

            }

            @Override
            public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
                Toast.makeText(SearchActivity.this, "Unable to get vets from server", Toast.LENGTH_LONG);
            }
        });

        actvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(currFragment==4){
                    service4 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

                    //query the substring to server data

                    Gson gson = new GsonBuilder().serializeNulls().create();
                    String jsonArray = gson.toJson(actvSearch.getText().toString());

                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());

                    final Call<ArrayList<Post>> call = service4.queryPosts(body);
                    call.enqueue(new Callback<ArrayList<Post>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
                            ArrayList<Post> postList = response.body();

                            FragmentPosts fragment1 = new FragmentPosts(postList);
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame_search,fragment1).
                                    addToBackStack(null).commitAllowingStateLoss();
                            //ArrayAdapter<Veterinarian> adapter = new ArrayAdapter<Veterinarian>(this,R.layout.,vetList);

                        }

                        @Override
                        public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
                            Log.d("onFailure", t.getLocalizedMessage());
                            Toast.makeText(SearchActivity.this, "Unable to get vets from server", Toast.LENGTH_LONG);
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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

    private ArrayList<Veterinarian> getVeterinarians() {
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Veterinarian> result = petBetterDb.getVeterinarians();
        petBetterDb.closeDatabase();
        return result;
    }

    private ArrayList<Facility> getClinics() {

        //modify this method in such a way that it only gets bookmarks tagged by user. Separate from facilities.
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Facility> result = petBetterDb.getClinics();
        petBetterDb.closeDatabase();
        return result;
    }

    private ArrayList<Topic> getTopics() {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Topic> result = petBetterDb.getTopics();
        petBetterDb.closeDatabase();

        return result;
    }

    private int getFollowerCount(int topicId) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int result = petBetterDb.getFollowerCount(topicId);
        petBetterDb.closeDatabase();

        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.search_toolbar_menu, amvMenu.getMenu());

        MenuInflater inflater = getMenuInflater();

        //inflate search action from menu folder
        //inflater.inflate(R.menu.options_menu, menu);
        //MenuItem searchItem = menu.findItem(R.id.action_search);
        //AutoCompleteTextView actv = (AutoCompleteTextView) MenuItemCompat.getActionView(searchItem);



        //actions when making a query in search

        //
        // Associate searchable configuration with the SearchView
        //Spinner searchSpinner = (Spinner) amvMenu.getMenu().findItem(R.id.spinner_category).getActionView();
/*
        if(searchSpinner==null){
            Toast.makeText(this,"HUHU SADBOYS",Toast.LENGTH_SHORT).show();
        }
        //ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.searchSpinner, android.R.layout.simple_spinner_item);
        //searchSpinner.setAdapter(adapter);

        searchSpinner.setOnItemSelectedListener(this);
  */
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        try{
            if(currFragment==1){
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_search,fragment1).
                        addToBackStack(null).commitAllowingStateLoss();
            }
            if(currFragment==2){
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_search,fragment2).
                        addToBackStack(null).commitAllowingStateLoss();
            }
        }catch(NullPointerException npe){
            if(vetSearchButton.getBackground().equals("R.color.myrtle_green")){
                currFragment = 1;
            }
            if(petSearchButton.getBackground().equals("R.color.myrtle_green")){
                currFragment = 2;
            }
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView myText = (TextView) view;
        //Toast.makeText(this, "You selected " + myText.getText(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_back) {
            Intent intent = new Intent(this, com.example.owner.petbetter.activities.HomeActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void searchBackButtonClicked(View view) {
        finish();
    }


    private void filterData(String query) {

    }
}
