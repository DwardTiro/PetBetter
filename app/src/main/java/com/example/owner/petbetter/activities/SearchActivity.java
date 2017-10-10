package com.example.owner.petbetter.activities;

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

    private Toolbar searchToolbar;
    private ActionMenuView amvMenu;
    private SearchView searchView;
    //private Spinner searchSpinner;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_search);
        searchToolbar = (Toolbar) findViewById(R.id.toolbarSearch);

        amvMenu = (ActionMenuView) searchToolbar.findViewById(R.id.amvMenu);

        amvMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onOptionsItemSelected(item);
            }
        });
        setSupportActionBar(searchToolbar);

        searchView = (SearchView) findViewById(R.id.searchView);

        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            public boolean onQueryTextSubmit(String query) {
                //When user presses enter

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //if you want to get something while text is changing
                return false;
            }
        });


        //searchSpinner = (Spinner) findViewById(R.id.spinnerCategory);



/*
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.searchSpinner));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        searchSpinner.setAdapter(adapter);
        searchSpinner.setOnItemSelectedListener(SearchActivity.this);
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.search_toolbar_menu, amvMenu.getMenu());


        Spinner searchSpinner = (Spinner) amvMenu.getMenu().findItem(R.id.spinner_category).getActionView();

        if(searchSpinner==null){
            Toast.makeText(this,"HUHU SADBOYS",Toast.LENGTH_SHORT).show();
        }
        //ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.searchSpinner, android.R.layout.simple_spinner_item);
        //searchSpinner.setAdapter(adapter);

        searchSpinner.setOnItemSelectedListener(this);
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
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.action_back){
            Intent intent = new Intent(this, com.example.owner.petbetter.activities.HomeActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
