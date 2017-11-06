package com.example.owner.petbetter.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.petbetter.R;

import java.util.ArrayList;


/**
 * Created by Kristian on 8/12/2017.
 */

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


    private ActionMenuView amvMenu;
    private SearchView searchView;
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_search);

        Toolbar searchToolbar = (Toolbar) findViewById(R.id.searchToolbar);
        setSupportActionBar(searchToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //getMenuInflater().inflate(R.menu.search_toolbar_menu, amvMenu.getMenu());
        MenuInflater inflater = getMenuInflater();

        //inflate search action from menu folder
        inflater.inflate(R.menu.options_menu,menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        //actions when making a query in search
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getBaseContext(),query,Toast.LENGTH_SHORT).show();


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        //get searchable info fro activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

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
  */      return true;
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
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.action_back){
            Intent intent = new Intent(this, com.example.owner.petbetter.activities.HomeActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void searchBackButtonClicked(View view){
        finish();
    }

    private void handleIntent(Intent intent){
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);

        }
    }
}
