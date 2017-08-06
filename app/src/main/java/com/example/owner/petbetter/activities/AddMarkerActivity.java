package com.example.owner.petbetter;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.example.owner.petbetter.classes.Marker;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddMarkerActivity extends AppCompatActivity {

    private EditText numEdit, streetEdit,bldgEdit, provinceEdit, cityEdit;;
    private ProgressBar progressBar;
    private TextView infoText;
    private CheckBox checkBox;
    private Button btnAdd;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    private String email;
    private Address tempAddress = null;
    private int markerId;

    public static final int USE_ADDRESS_NAME = 1;
    public static final int USE_ADDRESS_LOCATION = 2;

    int fetchType = USE_ADDRESS_LOCATION;

    private static final String TAG = "ADD_MARKER_ACTIVITY_ASYNC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_marker);

        numEdit = (EditText) findViewById(R.id.numEdit);
        streetEdit = (EditText) findViewById(R.id.streetEdit);
        bldgEdit = (EditText) findViewById(R.id.bldgEdit);
        cityEdit = (EditText) findViewById(R.id.cityEdit);
        provinceEdit = (EditText) findViewById(R.id.provinceEdit);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        infoText = (TextView) findViewById(R.id.infoText);
        checkBox = (CheckBox) findViewById(R.id.checkbox);

        systemSessionManager = new SystemSessionManager(this);
        if(systemSessionManager.checkLogin())
            finish();
        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();

        email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);

    }

    private void initializeDatabase() {

        petBetterDb = new DataAdapter(this);

        try {
            petBetterDb.createDatabase();
        } catch(SQLException e ){
            e.printStackTrace();
        }

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

    private void addMarker(int rowId, String bldgNum, String street, String bldgName, String city, String province,
                              double longitude, double latitude, long userId){


        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("ROWID IS "+rowId);
        petBetterDb.addMarker(rowId, bldgNum, street, bldgName, city, province, longitude, latitude, userId);
        petBetterDb.closeDatabase();

    }

    public int generateMarkerId(){
        ArrayList<Integer> storedIds;
        int markerId = 1;

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        storedIds = petBetterDb.getMarkerIds();
        petBetterDb.closeDatabase();


        if(storedIds.isEmpty()) {
            return markerId;
        } else {
            while (storedIds.contains(markerId)){
                markerId += 1;
            }

            return markerId;
        }
    }

    public void onButtonClicked(View view) {
        new GeocodeAsyncTask().execute();
    }

    class GeocodeAsyncTask extends AsyncTask<Void, Void, Address> {

        String errorMessage = "";

        @Override
        protected void onPreExecute() {
            infoText.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Address doInBackground(Void ... none) {
            Geocoder geocoder = new Geocoder(AddMarkerActivity.this, Locale.getDefault());
            List<Address> addresses = null;


            String name = numEdit.getText().toString()+" "+
            streetEdit.getText().toString()+" "+
            bldgEdit.getText().toString()+" "+
            cityEdit.getText().toString()+" "+
            provinceEdit.getText().toString();
            try {
                addresses = geocoder.getFromLocationName(name, 1);
            } catch (IOException e) {
                errorMessage = "Service not available";
                Log.e(TAG, errorMessage, e);
            }

            if(addresses != null && addresses.size() > 0)
                return addresses.get(0);

            return null;
        }

        protected void onPostExecute(Address address) {
            if(address == null) {
                progressBar.setVisibility(View.INVISIBLE);
                infoText.setVisibility(View.VISIBLE);
                infoText.setText(errorMessage);
            }
            else {
                String addressName = "";
                for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    addressName += " --- " + address.getAddressLine(i);
                }
                progressBar.setVisibility(View.INVISIBLE);
                infoText.setVisibility(View.VISIBLE);
                infoText.setText("Latitude: " + address.getLatitude() + "\n" +
                        "Longitude: " + address.getLongitude() + "\n" +
                        "Address: " + addressName);
                tempAddress = address;
            }
            user = getUser(email);

        }
    }

    public void addToDb(View v){
        if(tempAddress==null)
            Toast.makeText(this,"Please tap Fetch before adding to database",Toast.LENGTH_SHORT).show();
        else{
            markerId = generateMarkerId();
            addMarker(markerId,numEdit.getText().toString(),
                    streetEdit.getText().toString(),
                    bldgEdit.getText().toString(),
                    cityEdit.getText().toString(),
                    provinceEdit.getText().toString(),
                    tempAddress.getLongitude(),
                    tempAddress.getLatitude(),
                    user.getUserId());
        }
    }
}
