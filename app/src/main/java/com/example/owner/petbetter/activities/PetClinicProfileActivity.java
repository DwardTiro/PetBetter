package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.LocationMarker;
import com.example.owner.petbetter.classes.Rating;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
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


public class PetClinicProfileActivity extends AppCompatActivity {


    private TextView petClinicName;
    private TextView petClinicAddress;
    private TextView petClinicLandline;
    private TextView petClinicOpenTime;
    private TextView petClinicCloseTime;
    private TextView petClinicRating;

    private Button petClinicRateButton;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    private String email;
    private Facility faciItem;
    private int mId;
    private double longitude, latitude;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_petclinic_profile);

        petClinicName = (TextView) findViewById(R.id.clinicName);
        petClinicAddress = (TextView) findViewById(R.id.addressTextField);
        petClinicLandline = (TextView) findViewById(R.id.phoneNumTextField);
        petClinicOpenTime = (TextView) findViewById(R.id.openTimeTextField);
        petClinicCloseTime = (TextView) findViewById(R.id.closeTimeTextField);
        petClinicRating = (TextView) findViewById(R.id.clinicRatingNumerator);


        petClinicRateButton = (Button) findViewById(R.id.rateClinicButton);

        //petClinicBookmarkButton.setVisibility(View.GONE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.viewPostToolbar);
        setSupportActionBar(toolbar);
        final TextView activityTitle = (TextView) findViewById(R.id.activity_title);
        activityTitle.setText("View Pet Facility Profile");

        getSupportActionBar().setDisplayShowTitleEnabled(false);


        systemSessionManager = new SystemSessionManager(this);
        if(systemSessionManager.checkLogin())
            finish();
        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();

        email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);

        final String jsonMyObject;
        Bundle extras = getIntent().getExtras();
        jsonMyObject = extras.getString("thisClinic");

        faciItem = new Gson().fromJson(jsonMyObject,Facility.class);

        petClinicName.setText(faciItem.getFaciName());
        petClinicAddress.setText(faciItem.getLocation());
        petClinicRating.setText(String.valueOf(faciItem.getRating()));
        petClinicLandline.setText(faciItem.getContactInfo());
        petClinicOpenTime.setText(faciItem.getHoursOpen());
        petClinicCloseTime.setText(faciItem.getHoursClose());

        petClinicRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),RateFacilityActivity.class);
                intent.putExtra("thisClinic",jsonMyObject);
                startActivity(intent);
            }
        });

        syncRatingChanges();

        //Toast.makeText(this, "Facility's Name: "+faciItem.getFaciName() + ". Delete this toast. Just to help you see where vet variable is", Toast.LENGTH_LONG).show();
    }

    public void syncRatingChanges(){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service3 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        ArrayList<Rating> unsyncedRatings = getUnsyncedRatings();

        System.out.println("how many ratings? "+unsyncedRatings.size());

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(unsyncedRatings);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addRatings(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    System.out.println("RATINGS ADDED YEY");
                    dataSynced(14);

                    final Call<ArrayList<Rating>> call2 = service2.getRatings();
                    call2.enqueue(new Callback<ArrayList<Rating>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Rating>> call, Response<ArrayList<Rating>> response) {
                            if(response.isSuccessful()){
                                setRatings(response.body());
                                ArrayList<Float> faciRatings = getFacilityRatings(faciItem.getId());
                                float n = 0;
                                if(faciRatings!=null){
                                    if(faciRatings.size()>0){
                                        for(int i = 0; i<faciRatings.size();i++){
                                            n = n + faciRatings.get(i);
                                        }
                                        n = n/(float) faciRatings.size();
                                        petClinicRating.setText(String.valueOf(n));
                                    }
                                    else{
                                        petClinicRating.setText("0.0");
                                    }
                                }

                                final Call<Void> call3 = service3.updateRating(faciItem.getId(), n, 2);
                                call3.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {

                                    }
                                });

                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Rating>> call, Throwable t) {
                            Log.d("onFailure", t.getLocalizedMessage());

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
    }

    public long setRatings(ArrayList<Rating> rateList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setRatings(rateList);
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Rating> getUnsyncedRatings(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Rating> result = petBetterDb.getUnsyncedRatings();
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

    private ArrayList<Float> getFacilityRatings(long faciId){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Float> result = petBetterDb.getFacilityRatings(faciId);
        petBetterDb.closeDatabase();

        return result;
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

    //mId, faciItem.getFaciName(), faciItem.getLocation(), user.getUserId(), 1
    public long convertFaciToBookmark(int mId, String bldgName,double longitude, double latitude, String location, long userId, int type){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        long result = petBetterDb.convertFaciToBookmark(mId, bldgName, longitude, latitude, location, userId, type);
        petBetterDb.closeDatabase();

        return result;
    }

    public LocationMarker getMarker(String bldgName){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        LocationMarker result = petBetterDb.getMarker(bldgName);
        petBetterDb.closeDatabase();

        return result;
    }

    //get methods
    public TextView getPetClinicName(){
        return petClinicName;
    }
    public TextView getPetClinicAddress(){
        return petClinicAddress;
    }
    public TextView getPetClinicLandline(){
        return petClinicLandline;
    }
    public TextView getPetClinicOpenTime(){
        return petClinicOpenTime;
    }
    public TextView getPetClinicCloseTime(){
        return petClinicCloseTime;
    }
    public TextView getPetClinicRating(){
        return petClinicRating;
    }

    //set methods
    public void setPetClinicName(String str){
        petClinicName.setText(str);
    }
    public void setPetClinicAddress(String str){
        petClinicAddress.setText(str);
    }
    public void setPetClinicLandline(String str){
        petClinicLandline.setText(str);
    }
    public void setPetClinicOpenTime(String str){
        petClinicOpenTime.setText(str);
    }
    public void setPetClinicCloseTime(String str){
        petClinicCloseTime.setText(str);
    }
    public void setPetClinicRating(String str){
        petClinicRating.setText(str);
    }
  




    public void viewPostBackButtonClicked(View view){

        Intent intent = new Intent(this, com.example.owner.petbetter.activities.HomeActivity.class);
        startActivity(intent);
    }
}
