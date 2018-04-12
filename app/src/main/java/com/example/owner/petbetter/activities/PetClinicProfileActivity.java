package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.adapters.HoursAdapter;
import com.example.owner.petbetter.adapters.ServiceAdapter;
import com.example.owner.petbetter.adapters.VetListingAdapter;
import com.example.owner.petbetter.adapters.VetRowAdapter;
import com.example.owner.petbetter.classes.Bookmark;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.LocationMarker;
import com.example.owner.petbetter.classes.Pending;
import com.example.owner.petbetter.classes.Rating;
import com.example.owner.petbetter.classes.Services;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.classes.WorkHours;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.owner.petbetter.ServiceGenerator.BASE_URL;


public class PetClinicProfileActivity extends AppCompatActivity {


    private TextView petClinicName;
    private TextView petClinicAddress;
    private TextView petClinicLandline;
    private TextView faciPage;
    private TextView faciWebsite;
    private TextView faciEmail;
    private TextView petClinicRating;
    private ImageView clinicProfileImage;

    private Button petClinicRateButton;
    private Button bookMarkButton;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    private String email;
    private Facility faciItem;
    private int mId;
    private double longitude, latitude;
    private Toolbar toolbar;
    private boolean isBookmarked = false;
    private RecyclerView serviceRecyclerView;
    private TextView noServicesTextView;
    private TextView ambulanceTextView;
    private TextView confinementTextView;
    private TextView homeServiceTextView;
    private TextView surgeryTextView;
    private ImageView verifiedServices;
    private ArrayList<Services> serviceList;
    private int pSize;
    private int pendingCtr;
    private boolean isVerified = true;
    private ArrayList<Veterinarian> vetList;
    private RecyclerView vetRecyclerView;
    private VetListingAdapter vetListingAdapter;
    private ArrayList<WorkHours> hoursList;
    private RecyclerView hoursRecyclerView;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_petclinic_profile);

        petClinicName = (TextView) findViewById(R.id.clinicName);
        petClinicAddress = (TextView) findViewById(R.id.addressTextField);
        petClinicLandline = (TextView) findViewById(R.id.phoneNumTextField);
        faciPage = (TextView) findViewById(R.id.faciPageTextField);
        faciWebsite = (TextView) findViewById(R.id.faciWebsiteTextField);
        faciEmail = (TextView) findViewById(R.id.faciEmailTextField);

        petClinicRating = (TextView) findViewById(R.id.clinicRatingNumerator);
        clinicProfileImage = (ImageView) findViewById(R.id.clinicProfileImage);
        bookMarkButton = (Button) findViewById(R.id.bookmarkClinicButton);
        ambulanceTextView = (TextView) findViewById(R.id.ambulanceTextView);
        confinementTextView = (TextView) findViewById(R.id.confinementTextView);
        homeServiceTextView = (TextView) findViewById(R.id.homeServiceTextView);
        surgeryTextView = (TextView) findViewById(R.id.surgeryTextView);
        noServicesTextView = (TextView) findViewById(R.id.noServicesTextView);
        serviceRecyclerView = (RecyclerView) findViewById(R.id.servicesRecyclerView);
        verifiedServices = (ImageView) findViewById(R.id.verifiedServices);
        verifiedServices.setVisibility(View.VISIBLE);

        vetRecyclerView = (RecyclerView) findViewById(R.id.vetRecyclerView);

        hoursRecyclerView = (RecyclerView) findViewById(R.id.hoursRecyclerView);

        petClinicRateButton = (Button) findViewById(R.id.rateClinicButton);


        //petClinicBookmarkButton.setVisibility(View.GONE);

        toolbar = (Toolbar) findViewById(R.id.viewPostToolbar);
        setSupportActionBar(toolbar);
        final TextView activityTitle = (TextView) findViewById(R.id.activity_title);
        activityTitle.setText("View Pet Facility Profile");

        getSupportActionBar().setDisplayShowTitleEnabled(false);


        systemSessionManager = new SystemSessionManager(this);
        if (systemSessionManager.checkLogin())
            finish();
        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();

        email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);

        final String jsonMyObject;
        Bundle extras = getIntent().getExtras();
        jsonMyObject = extras.getString("thisClinic");

        faciItem = new Gson().fromJson(jsonMyObject, Facility.class);

        petClinicName.setText(faciItem.getFaciName());
        petClinicAddress.setText(faciItem.getLocation());
        petClinicRating.setText(String.valueOf(faciItem.getRating()));

        String[] contactArray = faciItem.getContactInfo().split(",", -1);

        if(contactArray.length>0&&contactArray[0].length()>0){
            petClinicLandline.setText("Phone Number: "+contactArray[0]);
        }
        if(contactArray.length>1&&contactArray[1].length()>0){
            faciPage.setText("Facebook Page: "+contactArray[1]);
        }
        if(contactArray.length>2&&contactArray[2].length()>0){
            faciWebsite.setText("Website: "+contactArray[2]);
        }
        if(contactArray.length>3&&contactArray[3].length()>0){
            faciEmail.setText("Email: "+contactArray[3]);
        }

        if (checkIfRated(user.getUserId(), faciItem.getId())) {
            petClinicRateButton.setBackgroundResource(R.color.myrtle_green);
            petClinicRateButton.setText("Rated");
            petClinicRateButton.setEnabled(false);
        }


        petClinicRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RateFacilityActivity.class);
                intent.putExtra("thisClinic", jsonMyObject);
                startActivity(intent);
            }
        });

        if (checkIfBookmark((int) faciItem.getId(), (int) user.getUserId())) {
            bookMarkButton.setBackgroundResource(R.color.myrtle_green);
            bookMarkButton.setText("Bookmarked");
            isBookmarked = true;
        } else {
            bookMarkButton.setBackgroundResource(R.color.amazonite);
            bookMarkButton.setText("Bookmark");
        }

        bookMarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isBookmarked) {
                    int id = generateBookmarkIds();

                    bookMarkButton.setEnabled(false);

                    addFacilityBookmarkToDB(id, faciItem.getId(), user.getUserId());
                    final HerokuService bookMarkService = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
                    Bookmark bookmark = new Bookmark(1, faciItem.getId(), 1, user.getUserId());
                    Gson gson = new GsonBuilder().serializeNulls().create();
                    String jsonArray = gson.toJson(bookmark);
                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray);

                    Call<Void> call = bookMarkService.addBookmark(body);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            System.out.println("New bookmark added");
                            if (response.isSuccessful()) {
                                bookMarkButton.setText("Bookmarked");
                                bookMarkButton.setBackgroundResource(R.color.myrtle_green);
                                dataSync(16);
                                isBookmarked = true;
                                syncBookmarkChanges();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            System.out.println("Bookmark not added");
                        }
                    });

                } else {
                    bookMarkButton.setBackgroundResource(R.color.amazonite);
                    bookMarkButton.setText("Bookmark");
                    deleteFacilityBookmark(faciItem.getId(), user.getUserId());
                    removeBookmark(faciItem.getId(), user.getUserId());
                }
            }
        });

        if (faciItem.getFaciPhoto() != null) {
            String newFileName = BASE_URL + faciItem.getFaciPhoto();
            System.out.println("FACI PHOTO " + faciItem.getFaciPhoto());
            //String newFileName = "http://192.168.0.19/petbetter/"+thisMessageRep.getMessagePhoto();
            Glide.with(PetClinicProfileActivity.this).load(newFileName).error(R.drawable.app_icon_yellow).into(clinicProfileImage);
        }

        getWorkhoursList();
        getServiceList();
        getVetList();
        syncRatingChanges();

        //Toast.makeText(this, "Facility's Name: "+faciItem.getFaciName() + ". Delete this toast. Just to help you see where vet variable is", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        View item = toolbar.findViewById(R.id.topicNewPost);
        item.setVisibility(View.GONE);
        item.setEnabled(false);
        return super.onCreateOptionsMenu(menu);
    }

    public void getVetList() {

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final Call<ArrayList<Veterinarian>> call = service.getVetsByFacility(faciItem.getId());

        call.enqueue(new Callback<ArrayList<Veterinarian>>() {
            @Override
            public void onResponse(Call<ArrayList<Veterinarian>> call, Response<ArrayList<Veterinarian>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        System.out.println("hi");
                        vetList = response.body();
                        vetRecyclerView.setVisibility(View.VISIBLE);
                        vetListingAdapter = new VetListingAdapter(PetClinicProfileActivity.this, vetList, new VetListingAdapter.OnItemClickListener() {
                            @Override public void onItemClick(Veterinarian item) {

                                Intent intent = new Intent(PetClinicProfileActivity.this, com.example.owner.petbetter.activities.VetProfileActivity.class);
                                intent.putExtra("thisVet", new Gson().toJson(item));
                                startActivity(intent);
                            }
                        });
                        vetRecyclerView.setAdapter(vetListingAdapter);
                        /*
                        vetRecyclerView.setAdapter(new VetRowAdapter(PetClinicProfileActivity.this,
                                getLayoutInflater(), vetList, new VetRowAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Veterinarian item) {
                                Intent intent = new Intent(PetClinicProfileActivity.this, com.example.owner.petbetter.activities.VetProfileActivity.class);
                                intent.putExtra("thisVet", new Gson().toJson(item));
                                startActivity(intent);
                            }
                        }));*/

                        vetRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        vetRecyclerView.setHasFixedSize(true);
                        vetRecyclerView.setLayoutManager(new LinearLayoutManager(PetClinicProfileActivity.this));



                    } else {
                        /*
                        System.out.println("Went here inside else bruh");
                        serviceRecyclerView.setVisibility(View.GONE);
                        noServicesTextView.setVisibility(View.VISIBLE);
                        */
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Veterinarian>> call, Throwable t) {

            }
        });

    }

    public void getServiceList() {

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final Call<ArrayList<Services>> call = service.getServicesWithFaciID(faciItem.getId());
        pendingCtr = 0;

        call.enqueue(new Callback<ArrayList<Services>>() {
            @Override
            public void onResponse(Call<ArrayList<Services>> call, Response<ArrayList<Services>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        serviceList = response.body();
                        serviceRecyclerView.setVisibility(View.VISIBLE);
                        noServicesTextView.setVisibility(View.GONE);
                        serviceRecyclerView.setAdapter(new ServiceAdapter(PetClinicProfileActivity.this, getLayoutInflater(), response.body()));
                        if(serviceRecyclerView.getAdapter() == null){
                            System.out.println("No adapter bruh");
                        }
                        serviceRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        serviceRecyclerView.setHasFixedSize(true);
                        serviceRecyclerView.setLayoutManager(new LinearLayoutManager(PetClinicProfileActivity.this));


                        for(Services services:serviceList){
                            System.out.println("service id "+services.getId());
                            final Call<ArrayList<Pending>> call2 = service.getPendingFacility(services.getId(), 3);
                            call2.enqueue(new Callback<ArrayList<Pending>>() {
                                @Override
                                public void onResponse(Call<ArrayList<Pending>> call, Response<ArrayList<Pending>> response) {

                                }

                                @Override
                                public void onFailure(Call<ArrayList<Pending>> call, Throwable t) {

                                    verifiedServices.setVisibility(View.INVISIBLE);
                                }
                            });
                        }


                    } else {
                        System.out.println("Went here inside else bruh");
                        serviceRecyclerView.setVisibility(View.GONE);
                        noServicesTextView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Services>> call, Throwable t) {

            }
        });

    }

    public void getWorkhoursList() {

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final Call<ArrayList<WorkHours>> call = service.getWorkhoursWithFaciID(faciItem.getId());
        //pendingCtr = 0;

        call.enqueue(new Callback<ArrayList<WorkHours>>() {
            @Override
            public void onResponse(Call<ArrayList<WorkHours>> call, Response<ArrayList<WorkHours>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        hoursList = response.body();
                        System.out.println("NUMBER OF WORK HOURS: "+hoursList.size());
                        hoursRecyclerView.setVisibility(View.VISIBLE);
                        hoursRecyclerView.setAdapter(new HoursAdapter(PetClinicProfileActivity.this,
                                getLayoutInflater(), hoursList));
                        hoursRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        hoursRecyclerView.setHasFixedSize(true);
                        hoursRecyclerView.setLayoutManager(new LinearLayoutManager(PetClinicProfileActivity.this));
                        /*
                        serviceList = response.body();
                        serviceRecyclerView.setVisibility(View.VISIBLE);
                        noServicesTextView.setVisibility(View.GONE);
                        serviceRecyclerView.setAdapter(new ServiceAdapter(PetClinicProfileActivity.this, getLayoutInflater(), response.body()));
                        if(serviceRecyclerView.getAdapter() == null){
                            System.out.println("No adapter bruh");
                        }
                        serviceRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        serviceRecyclerView.setHasFixedSize(true);
                        serviceRecyclerView.setLayoutManager(new LinearLayoutManager(PetClinicProfileActivity.this));
                        */
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<WorkHours>> call, Throwable t) {
                Log.d("onFailure HOURS ", t.getLocalizedMessage());
            }
        });

    }

    public void syncBookmarkChanges() {
        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        ArrayList<Bookmark> unsyncedBookmarks = getUnsyncedBookmarks();
        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(unsyncedBookmarks);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray);

        final Call<Void> call = service.addBookmarks(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                dataSync(16);
                bookMarkButton.setEnabled(true);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private void removeBookmark(long item_id, long user_id) {
        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final Call<Void> call = service.deleteBookmark(user_id, item_id, 1);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    //syncBookmarkChanges();
                    isBookmarked = false;
                    bookMarkButton.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void syncRatingChanges() {

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service3 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        ArrayList<Rating> unsyncedRatings = getUnsyncedRatings();

        System.out.println("how many ratings? " + unsyncedRatings.size());

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(unsyncedRatings);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addRatings(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    System.out.println("RATINGS ADDED YEY");
                    dataSynced(14);

                    final Call<ArrayList<Rating>> call2 = service2.getRatings();
                    call2.enqueue(new Callback<ArrayList<Rating>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Rating>> call, Response<ArrayList<Rating>> response) {
                            if (response.isSuccessful()) {
                                setRatings(response.body());
                                ArrayList<Float> faciRatings = getFacilityRatings(faciItem.getId());
                                float n = 0;
                                if (faciRatings != null) {
                                    if (faciRatings.size() > 0) {
                                        for (int i = 0; i < faciRatings.size(); i++) {
                                            n = n + faciRatings.get(i);
                                        }
                                        n = n / (float) faciRatings.size();
                                        petClinicRating.setText(String.valueOf(n));
                                    } else {
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

    public long setRatings(ArrayList<Rating> rateList) {
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setRatings(rateList);
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Rating> getUnsyncedRatings() {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Rating> result = petBetterDb.getUnsyncedRatings();
        petBetterDb.closeDatabase();

        return result;
    }

    private void dataSynced(int n) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        petBetterDb.dataSynced(n);
        petBetterDb.closeDatabase();

    }

    private void deleteFacilityBookmark(long item_id, long user_id) {
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        petBetterDb.deleteFacilityBookmark(item_id, user_id);
        petBetterDb.closeDatabase();
    }

    private boolean checkIfBookmark(int item_id, int user_id) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        boolean result = petBetterDb.checkIfBookMark(item_id, user_id, 1);
        petBetterDb.closeDatabase();

        return result;
    }

    private boolean checkIfRated(long rater_id, long rated_id) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        boolean result = petBetterDb.checkIfRated(rater_id, rated_id, 2);
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Float> getFacilityRatings(long faciId) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Float> result = petBetterDb.getFacilityRatings(faciId);
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Bookmark> getUnsyncedBookmarks() {
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Bookmark> result = petBetterDb.getUnsyncedBookmarks();
        petBetterDb.closeDatabase();

        return result;
    }


    private void initializeDatabase() {

        petBetterDb = new DataAdapter(this);

        try {
            petBetterDb.createDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void dataSync(int n) {
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        petBetterDb.dataSynced(n);
        petBetterDb.closeDatabase();

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

    public int generateNewMarkerId() {
        ArrayList<Integer> storedIds;
        int markerId = 1;

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        storedIds = petBetterDb.getMarkerIds();
        petBetterDb.closeDatabase();


        if (storedIds.isEmpty()) {
            return markerId;
        } else {
            while (storedIds.contains(markerId)) {
                markerId += 1;
            }

            return markerId;
        }
    }

    //mId, faciItem.getFaciName(), faciItem.getLocation(), user.getUserId(), 1
    public long convertFaciToBookmark(int mId, String bldgName, double longitude, double latitude, String location, long userId, int type) {
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        long result = petBetterDb.convertFaciToBookmark(mId, bldgName, longitude, latitude, location, userId, type);
        petBetterDb.closeDatabase();

        return result;
    }

    public int generateBookmarkIds() {
        int newId;
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayList<Integer> ids = petBetterDb.generateBookmarkIds();
        if (ids.size() != 0) {
            newId = ids.get(ids.size() - 1);
            newId += 1;
        } else
            newId = 1;

        petBetterDb.closeDatabase();

        return newId;
    }

    public long addFacilityBookmarkToDB(long _id, long item_id, long user_id) {
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.addFacilityBookmark(_id, item_id, user_id);

        petBetterDb.closeDatabase();
        return result;
    }

    public LocationMarker getMarker(String bldgName) {
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        LocationMarker result = petBetterDb.getMarker(bldgName);
        petBetterDb.closeDatabase();

        return result;
    }

    private LocationMarker getLocationMarker(int faciId) {
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        LocationMarker result = petBetterDb.getMarkerWithId(faciId);
        petBetterDb.closeDatabase();
        return result;
    }

    //get methods
    public TextView getPetClinicName() {
        return petClinicName;
    }

    public TextView getPetClinicAddress() {
        return petClinicAddress;
    }

    public TextView getPetClinicLandline() {
        return petClinicLandline;
    }

    public TextView getPetClinicRating() {
        return petClinicRating;
    }

    //set methods
    public void setPetClinicName(String str) {
        petClinicName.setText(str);
    }

    public void setPetClinicAddress(String str) {
        petClinicAddress.setText(str);
    }

    public void setPetClinicLandline(String str) {
        petClinicLandline.setText(str);
    }

    public void setPetClinicOpenTime(String str) {

    }

    public void setPetClinicCloseTime(String str) {

    }

    public void setPetClinicRating(String str) {
        petClinicRating.setText(str);
    }


    public void viewPostBackButtonClicked(View view) {
        finish();
    }

    public void onFacilityLocationClicked(View view) {
        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        final Call<LocationMarker> call = service.getMarkerWithFaciId(faciItem.getId());
        call.enqueue(new Callback<LocationMarker>() {
            @Override
            public void onResponse(Call<LocationMarker> call, Response<LocationMarker> response) {
                if(response.isSuccessful()){
                    LocationMarker location = response.body();
                    Bundle extras = new Bundle();
                    extras.putString("bldg_name", location.getBldgName());
                    extras.putDouble("latitude", location.getLatitude());
                    extras.putDouble("longitude", location.getLongitude());

                    Intent intent = new Intent(PetClinicProfileActivity.this, com.example.owner.petbetter.activities.ShowLocationActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<LocationMarker> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
    }
}
