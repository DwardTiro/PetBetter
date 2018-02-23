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

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.adapters.SearchListingAdapter;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.Marker;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.Topic;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


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
    private ArrayList<Veterinarian> vetList;
    private ArrayList<Facility> clinicList;
    private ArrayList<Post> postList;

    private Button vetSearchButton;
    private Button petSearchButton;
    private Button topicSearchButton;
    private Button postSearchButton;


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
    }

    public void topicSearchClicked(View v){

        vetSearchButton.setBackgroundResource(R.color.medTurquoise);
        petSearchButton.setBackgroundResource(R.color.medTurquoise);
        topicSearchButton.setBackgroundResource(R.color.myrtle_green);
        postSearchButton.setBackgroundResource(R.color.medTurquoise);
    }

    public void postSearchClicked(View v){
        vetSearchButton.setBackgroundResource(R.color.medTurquoise);
        petSearchButton.setBackgroundResource(R.color.medTurquoise);
        topicSearchButton.setBackgroundResource(R.color.medTurquoise);
        postSearchButton.setBackgroundResource(R.color.myrtle_green);
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
        AutoCompleteTextView actv = (AutoCompleteTextView) MenuItemCompat.getActionView(searchItem);


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
