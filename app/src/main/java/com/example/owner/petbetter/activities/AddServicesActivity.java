package com.example.owner.petbetter.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.FacilityMembership;
import com.example.owner.petbetter.classes.LocationMarker;
import com.example.owner.petbetter.classes.Services;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;
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
 * Created by Kristian on 3/26/2018.
 */

public class AddServicesActivity extends AppCompatActivity {


    private String faciName;
    private String openTime;
    private String closeTime;
    private String phoneNum;
    private String address;
    private String image;
    private long faciId;
    private long locationId;
    double longitude, latitude;
    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    private int vetId;

    private LinearLayout newServices;
    private Button addField;

    HerokuService service;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_add_services);

        newServices = (LinearLayout) findViewById(R.id.serviceContainer);
        addField = (Button) findViewById(R.id.addFieldButton);

        Bundle extras = getIntent().getExtras();
        faciName = extras.getString("bldg_name");
        openTime = extras.getString("hours_open");
        closeTime = extras.getString("hours_close");
        phoneNum = extras.getString("phone_num");
        address = extras.getString("address");
        image = extras.getString("image");
        longitude = extras.getDouble("longitude");
        latitude = extras.getDouble("latitude");

        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        systemSessionManager = new SystemSessionManager(this);
        if (systemSessionManager.checkLogin())
            finish();

        HashMap<String, String> userIn = systemSessionManager.getUserDetails();
        initializeDatabase();

        String email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);
        vetId = getVeterinarian((int) user.getUserId()).getId();

        Toast.makeText(this, faciName, Toast.LENGTH_SHORT).show();

        createNewEditText();
        addField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewEditText();
            }
        });
    }

    private void createNewEditText() {
        /*
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
        final EditText editText = new EditText(this);
        editText.setLayoutParams(lparams);
        return editText;
        */
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View serviceView = inflater.inflate(R.layout.fragment_new_service_field, null);
        if(newServices.getChildCount() > 0)
            newServices.addView(serviceView, newServices.getChildCount());
        else if(newServices.getChildCount() == 1){
            newServices.addView(serviceView, 1);
        }
        else
            newServices.addView(serviceView, 0);
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

    public void deleteRow(View view) {
        newServices.removeView((View) view.getParent());

    }

    private Veterinarian getVeterinarian(int user_id) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Veterinarian result = petBetterDb.getVeterinarianWithId(user_id);
        ArrayList<Integer> ids = petBetterDb.generateFaciIds();

        for (int i = 0; i < ids.size(); i++) {
            System.out.print(ids.get(i) + " ");
        }

        petBetterDb.closeDatabase();

        return result;
    }


    public void addFacility(View view) {
        /*
        EditText serviceField;
        List<EditText> priceFields = new ArrayList<>();
        List<EditText> serviceFields = new ArrayList<>();

        for (int i = 0; i < newServices.getChildCount(); i++) {
            EditText priceField = (EditText) (newServices.getChildAt(i).findViewById(R.id.servicePriceField));
            serviceField = (EditText) (newServices.getChildAt(i).findViewById(R.id.serviceNameField));
            priceFields.add(priceField);
            serviceFields.add(serviceField);
        }
        for(int i = 0; i < priceFields.size(); i++){
            System.out.println("Value at edittext number: "+(i)+" "+ priceFields.get(i).getText().toString());
            System.out.println("Value at edittext number: "+(i)+" "+ serviceFields.get(i).getText().toString());
        }*/
        EditText serviceField;
        EditText priceField;
        List<EditText> priceFields = new ArrayList<>();
        List<EditText> serviceFields = new ArrayList<>();
        ArrayList<Services> servicesList = new ArrayList<>();


        faciId = generateNewFacilityID();
        addFacilitytoDB((int) faciId, faciName, address, openTime, closeTime, phoneNum, image);
        syncFacilityChanges();

        for (int i = 0; i < newServices.getChildCount(); i++) {
            priceField = (EditText) (newServices.getChildAt(i).findViewById(R.id.servicePriceField));
            serviceField = (EditText) (newServices.getChildAt(i).findViewById(R.id.serviceNameField));

            addService(0, serviceField.getText().toString(), priceField.getText().toString(), faciId);
            /*
            servicesList.add(new Services(0,
                    faciId,
                    serviceField.getText().toString(),
                    Float.parseFloat(priceField.getText().toString()),
                    0));
            */
        }
        syncServicesChanges();


    }

    private long addService(long _id, String service_name, String service_price, long faci_id) {
        long result;
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        result = petBetterDb.addServices(_id, service_name, service_price, faci_id);
        petBetterDb.closeDatabase();
        return result;
    }

    private int generateNewFacilityID() {
        ArrayList<Integer> faciIDs;
        int newId;
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        faciIDs = petBetterDb.generateFaciIds();

        if (faciIDs.size() != 0) {
            newId = faciIDs.get(faciIDs.size() - 1);
            newId += 1;
        } else
            newId = 1;

        petBetterDb.closeDatabase();

        return newId;
    }

    private int generateNewMemberID() {
        ArrayList<Integer> faciIDs;
        int newId;
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        faciIDs = petBetterDb.generateMembershipIds();

        if (faciIDs.size() != 0) {
            newId = faciIDs.get(faciIDs.size() - 1);
            newId += 1;
        } else
            newId = 1;

        petBetterDb.closeDatabase();

        return newId;
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

    private ArrayList<Facility> getUnsyncedFacilities() {
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Facility> result = petBetterDb.getUnsyncedFacilities();


        petBetterDb.closeDatabase();

        return result;
    }

    private long addFacilitytoDB(
            int faci_id,
            String faci_name,
            String location,
            String hours_open,
            String hours_close,
            String contact_info,
            String faciPhoto
    ) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        long result = petBetterDb.addFacility(faci_id, faci_name, location, hours_open, hours_close, contact_info, faciPhoto);
        petBetterDb.closeDatabase();

        return result;
    }

    public long setFacilities(ArrayList<Facility> faciList) {
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setFacilities(faciList);
        petBetterDb.closeDatabase();

        return result;
    }

    public long addFacilityMember(int id, long faciId, long vetId) {
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.addFacilityMember(id, faciId, vetId);
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<FacilityMembership> getUnsyncedMembers() {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<FacilityMembership> result = petBetterDb.getUnsyncedMembers();
        petBetterDb.closeDatabase();

        return result;
    }

    public long setMembers(ArrayList<FacilityMembership> fmList) {
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setMembers(fmList);
        petBetterDb.closeDatabase();

        return result;
    }

    public long setServices(ArrayList<Services> servicesList) {
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setServices(servicesList);
        petBetterDb.closeDatabase();

        return result;
    }

    private int generateNewLocationMarkerId() {
        int newId;
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayList<Integer> ids = petBetterDb.generateLocationMarkerIds();
        if (ids.size() != 0) {
            newId = ids.get(ids.size() - 1);
            newId += 1;
        } else
            newId = 1;

        petBetterDb.closeDatabase();

        return newId;
    }

    private long addLocationMarkertoDB(
            int marker_id,
            String bldg_name,
            double latitude,
            double longitude,
            String location,
            long userId,
            int type,
            long faciId
    ) {
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.addMarker(marker_id, bldg_name, latitude, longitude, location, userId, type, faciId);
        petBetterDb.closeDatabase();

        return result;

    }

    private ArrayList<LocationMarker> getUnsyncedMarkers() {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<LocationMarker> result = petBetterDb.getUnsyncedMarkers();
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Services> getUnsyncedServices() {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Services> result = petBetterDb.getUnsyncedServices();
        petBetterDb.closeDatabase();

        return result;
    }

    public long setLocationMarkers(ArrayList<LocationMarker> locationMarkerList) {
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setLocationMarkers(locationMarkerList);
        petBetterDb.closeDatabase();

        return result;
    }

    public void syncServicesChanges() {

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);


        ArrayList<Services> unsyncedServices = getUnsyncedServices();

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(unsyncedServices);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addServices(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    dataSynced(11);

                    final Call<ArrayList<Services>> call2 = service2.getServices();
                    call2.enqueue(new Callback<ArrayList<Services>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Services>> call, Response<ArrayList<Services>> response) {
                            if (response.isSuccessful()) {
                                setServices(response.body());
                                Intent intent = new Intent(AddServicesActivity.this, com.example.owner.petbetter.activities.VeterinarianHomeActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Services>> call, Throwable t) {
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

    public void syncFacilityMemberChanges() {

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);


        ArrayList<FacilityMembership> unsyncedMembers = getUnsyncedMembers();

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(unsyncedMembers);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addMembers(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    dataSynced(18);

                    final Call<ArrayList<FacilityMembership>> call2 = service2.getFacilityMembers();
                    call2.enqueue(new Callback<ArrayList<FacilityMembership>>() {
                        @Override
                        public void onResponse(Call<ArrayList<FacilityMembership>> call, Response<ArrayList<FacilityMembership>> response) {
                            if (response.isSuccessful()) {
                                setMembers(response.body());

                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<FacilityMembership>> call, Throwable t) {
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


    private void syncFacilityChanges() {
        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        System.out.println("WE HERE BOOIII");
        ArrayList<Facility> unsyncedFacilities = getUnsyncedFacilities();
        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(unsyncedFacilities);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addFacilities(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    System.out.println("FACILITIES ADDED YEY");
                    dataSynced(2);

                    final Call<ArrayList<Facility>> call2 = service2.getClinics(1);
                    call2.enqueue(new Callback<ArrayList<Facility>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Facility>> call, Response<ArrayList<Facility>> response) {
                            if (response.isSuccessful()) {
                                System.out.println("Number of clinics from server: " + response.body().size());
                                setFacilities(response.body());
                                faciId = response.body().get(response.body().size() - 1).getId();
                                int id = generateNewMemberID();
                                addFacilityMember(id, faciId, (long) vetId);
                                syncFacilityMemberChanges();
                                addFacilityLocation();

                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Facility>> call, Throwable t) {
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

    public void syncLocationChanges() {

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        System.out.println("WE HERE BOOIII");
        ArrayList<LocationMarker> unsyncedLocations = getUnsyncedMarkers();

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(unsyncedLocations);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addLocations(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    System.out.println("SERVICES ADDED YEY");
                    dataSynced(4);

                    final Call<ArrayList<LocationMarker>> call2 = service2.loadLocations();
                    call2.enqueue(new Callback<ArrayList<LocationMarker>>() {
                        @Override
                        public void onResponse(Call<ArrayList<LocationMarker>> call, Response<ArrayList<LocationMarker>> response) {
                            if (response.isSuccessful()) {
                                setLocationMarkers(response.body());

                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<LocationMarker>> call, Throwable t) {
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

    public void addFacilityLocation() {
        System.out.println("Start adding new location");

        locationId = generateNewLocationMarkerId();
        System.out.println("Generated Location Id is: " + locationId);


        LocationMarker locationMarker = new LocationMarker(
                locationId,
                faciName,
                latitude,
                longitude,
                address,
                user.getUserId(),
                1,
                faciId
        );

        addLocationMarkertoDB(
                (int) locationId,
                faciName,
                latitude,
                longitude,
                address,
                user.getUserId(),
                1,
                (int) faciId);
        syncLocationChanges();

    }


}
