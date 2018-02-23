package com.example.owner.petbetter.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.adapters.SearchListingAdapter;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.Marker;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.Topic;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.fragments.FragmentCommunity;
import com.example.owner.petbetter.fragments.FragmentFacilityListing;
import com.example.owner.petbetter.fragments.FragmentPetClinicListing;
import com.example.owner.petbetter.fragments.FragmentPosts;
import com.example.owner.petbetter.fragments.FragmentVetListing;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    HerokuService service;


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

        vetSearchClicked(this.findViewById(android.R.id.content));

        //  recyclerView = (RecyclerView) findViewById(R.id.recyclerViewSearch);
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    public void vetSearchClicked(View v){

        System.out.println("SEARCH RESULTS FOR VETS~");
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

                FragmentVetListing fragment1 = new FragmentVetListing(vetList);
                getSupportFragmentManager().beginTransaction().add(R.id.frame_search,fragment1).commit();
                //ArrayAdapter<Veterinarian> adapter = new ArrayAdapter<Veterinarian>(this,R.layout.,vetList);

            }

            @Override
            public void onFailure(Call<ArrayList<Veterinarian>> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
                Toast.makeText(SearchActivity.this, "Unable to get vets from server", Toast.LENGTH_LONG);
            }
        });

        /*
        * PROCEDURE:
        * 1. Find a way to be able to feed search suggestions via json (from server)
        * 2. Modify get functions from server to display new data.
        * */
    }

    public void petSearchClicked(View v){

        vetSearchButton.setBackgroundResource(R.color.medTurquoise);
        petSearchButton.setBackgroundResource(R.color.myrtle_green);
        topicSearchButton.setBackgroundResource(R.color.medTurquoise);
        postSearchButton.setBackgroundResource(R.color.medTurquoise);

        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);


        final Call<ArrayList<Facility>> call = service.getClinics();
        call.enqueue(new Callback<ArrayList<Facility>>() {
            @Override
            public void onResponse(Call<ArrayList<Facility>> call, Response<ArrayList<Facility>> response) {
                ArrayList<Facility> faciList = response.body();

                FragmentPetClinicListing fragment1 = new FragmentPetClinicListing(faciList);
                getSupportFragmentManager().beginTransaction().add(R.id.frame_search,fragment1).commit();
                //ArrayAdapter<Veterinarian> adapter = new ArrayAdapter<Veterinarian>(this,R.layout.,vetList);

            }

            @Override
            public void onFailure(Call<ArrayList<Facility>> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
                Toast.makeText(SearchActivity.this, "Unable to get vets from server", Toast.LENGTH_LONG);
            }
        });
    }

    public void topicSearchClicked(View v){

        vetSearchButton.setBackgroundResource(R.color.medTurquoise);
        petSearchButton.setBackgroundResource(R.color.medTurquoise);
        topicSearchButton.setBackgroundResource(R.color.myrtle_green);
        postSearchButton.setBackgroundResource(R.color.medTurquoise);

        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);


        final Call<ArrayList<Topic>> call = service.getTopics();
        call.enqueue(new Callback<ArrayList<Topic>>() {
            @Override
            public void onResponse(Call<ArrayList<Topic>> call, Response<ArrayList<Topic>> response) {
                ArrayList<Topic> topicList = response.body();

                FragmentCommunity fragment1 = new FragmentCommunity(topicList);
                getSupportFragmentManager().beginTransaction().add(R.id.frame_search,fragment1).commit();
                //ArrayAdapter<Veterinarian> adapter = new ArrayAdapter<Veterinarian>(this,R.layout.,vetList);

            }

            @Override
            public void onFailure(Call<ArrayList<Topic>> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
                Toast.makeText(SearchActivity.this, "Unable to get vets from server", Toast.LENGTH_LONG);
            }
        });
    }

    public void postSearchClicked(View v){
        vetSearchButton.setBackgroundResource(R.color.medTurquoise);
        petSearchButton.setBackgroundResource(R.color.medTurquoise);
        topicSearchButton.setBackgroundResource(R.color.medTurquoise);
        postSearchButton.setBackgroundResource(R.color.myrtle_green);

        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);


        final Call<ArrayList<Post>> call = service.getPosts();
        call.enqueue(new Callback<ArrayList<Post>>() {
            @Override
            public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
                ArrayList<Post> postList = response.body();

                FragmentPosts fragment1 = new FragmentPosts(postList);
                getSupportFragmentManager().beginTransaction().add(R.id.frame_search,fragment1).commit();
                //ArrayAdapter<Veterinarian> adapter = new ArrayAdapter<Veterinarian>(this,R.layout.,vetList);

            }

            @Override
            public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
                Toast.makeText(SearchActivity.this, "Unable to get vets from server", Toast.LENGTH_LONG);
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
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
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

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

        }
    }

    private void filterData(String query) {

    }
}
