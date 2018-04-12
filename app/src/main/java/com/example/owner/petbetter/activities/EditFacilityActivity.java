package com.example.owner.petbetter.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.Services;
import com.example.owner.petbetter.classes.WorkHours;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.owner.petbetter.ServiceGenerator.BASE_URL;

public class EditFacilityActivity extends AppCompatActivity {

    private Button addFacilityButton;
    private EditText facilityName;
    private EditText facilityAddress;
    private EditText phoneNum;
    private EditText faciPage;
    private EditText faciWebsite;
    private EditText faciEmail;
    private ImageButton editImage;
    private SystemSessionManager systemSessionManager;
    private static final int IMG_REQUEST = 777;
    private Bitmap bitmap;
    private TextView textViewAddress;
    private Facility faciItem;
    private DataAdapter petBetterDb;
    private ImageButton topicNewPost;
    private LinearLayout hoursContainer;
    private Button addHours;
    private ArrayList<WorkHours> hoursList;

    HerokuService service;
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_add_new_facility);

        Toolbar toolbar = (Toolbar) findViewById(R.id.viewPostToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final TextView activityTitle = (TextView) findViewById(R.id.activity_title);
        activityTitle.setText("Edit Facility");

        addFacilityButton = (Button) findViewById(R.id.addFacilityButton);
        addFacilityButton.setText("Save Changes");
        addFacilityButton.setBackgroundColor(getResources().getColor(R.color.medTurquoise));
        addFacilityButton.setEnabled(true);
        facilityName = (EditText) findViewById(R.id.addFacilityName);
        phoneNum = (EditText) findViewById(R.id.addFacilityPhone);
        faciPage = (EditText) findViewById(R.id.addFaciPage);
        faciWebsite = (EditText) findViewById(R.id.addFaciWebsite);
        faciEmail = (EditText) findViewById(R.id.addFaciEmail);
        facilityAddress = (EditText) findViewById(R.id.addFacilityAddress);
        facilityAddress.setEnabled(false);
        facilityAddress.setVisibility(View.GONE);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        textViewAddress.setVisibility(View.GONE);
        topicNewPost = (ImageButton) findViewById(R.id.topicNewPost);
        topicNewPost.setVisibility(View.GONE);

        hoursContainer = (LinearLayout) findViewById(R.id.hoursContainer);
        addHours = (Button) findViewById(R.id.addTimeButton);

        initializeDatabase();

        editImage = (ImageButton) findViewById(R.id.clinicEditImage);


        String jsonMyObject;
        Bundle extras = getIntent().getExtras();
        jsonMyObject = extras.getString("thisClinic");
        faciItem = new Gson().fromJson(jsonMyObject, Facility.class);

        if(faciItem.getFaciPhoto()!=null){
            String newFileName = BASE_URL + faciItem.getFaciPhoto();
            //String newFileName = "http://192.168.0.19/petbetter/"+user.getUserPhoto();
            System.out.println("USER PHOTO "+faciItem.getFaciPhoto());
            Glide.with(EditFacilityActivity.this).load(newFileName).error(R.drawable.app_icon_yellow).into(editImage);
            editImage.setAdjustViewBounds(true);
        }

        facilityName.setText(faciItem.getFaciName());
        String[] contactArray = faciItem.getContactInfo().split(",", -1);
        phoneNum.setText(contactArray[0]);
        faciPage.setText(contactArray[1]);
        faciWebsite.setText(contactArray[2]);
        faciEmail.setText(contactArray[3]);


        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        createNewEditText();
        addHours.setOnClickListener(new View.OnClickListener() {
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
        final View serviceView = inflater.inflate(R.layout.fragment_new_hours_field, null);
        if (hoursContainer.getChildCount() > 0)
            hoursContainer.addView(serviceView, hoursContainer.getChildCount());
        else if (hoursContainer.getChildCount() == 1) {
            hoursContainer.addView(serviceView, 1);
        } else
            hoursContainer.addView(serviceView, 0);
    }

    public void deleteRow(View view) {
        hoursContainer.removeView((View) view.getParent());

    }

    //function to edit facility info
    public void addFacility(View view){
        if(facilityAddress.getText().toString()!=""){


            ArrayList<WorkHours> hoursList = new ArrayList<>();
            for(int i=0;i<hoursContainer.getChildCount();i++){
                EditText editText = (EditText) (hoursContainer.getChildAt(i).findViewById(R.id.dayField));
                Spinner openSpinner = (Spinner) (hoursContainer.getChildAt(i).findViewById(R.id.addFacilityOpenTimeSpinner));
                Spinner closeSpinner = (Spinner) (hoursContainer.getChildAt(i).findViewById(R.id.addFacilityCloseTimeSpinner));

                WorkHours workHours = new WorkHours(0, (int) faciItem.getId(), editText.getText().toString(),
                        openSpinner.getSelectedItem().toString(), closeSpinner.getSelectedItem().toString(), 0);
                hoursList.add(workHours);
            }

            Gson gson = new GsonBuilder().serializeNulls().create();
            String jsonHours = gson.toJson(hoursList);

            service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
            String image = imageToString();


            String contactInfo = phoneNum.getText().toString()+","+faciPage.getText().toString()+","+
                    faciWebsite.getText().toString()+","+faciEmail.getText().toString();

            editFacility(faciItem.getId(), facilityName.getText().toString(), faciItem.getLocation(), contactInfo,
                    faciItem.getRating(), image);

            uploadFacilityChanges();
            addWorkHours(jsonHours);
            Intent intent = new Intent(EditFacilityActivity.this, com.example.owner.petbetter.activities.VeterinarianHomeActivity.class);

            startActivity(intent);
        }
        else{
            Toast.makeText(EditFacilityActivity.this, "Please input a location", Toast.LENGTH_SHORT);
        }
    }

    public void uploadFacilityChanges(){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        System.out.println("WE HERE BOOIII");
        Facility unsyncedFacility = getFacility((int) faciItem.getId());

        System.out.println("IMAGE STRING BRO "+unsyncedFacility.getFaciPhoto());
        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(unsyncedFacility);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.editFacilities(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    System.out.println("FACILITIES ADDED YEY");
                    dataSynced(2);

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
    }

    public void addWorkHours(String hoursList) {

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        /*
        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(hoursList);*/
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), hoursList.toString());
        final Call<Void> call = service.addWorkhours(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    dataSynced(18);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
    }

    private Facility getFacility(int id){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        Facility result = petBetterDb.getFacility(id);
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

    private void initializeDatabase() {

        petBetterDb = new DataAdapter(this);

        try {
            petBetterDb.createDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void editFacility(long _id, String faciName, String location, String contactInfo, float rating, String faciPhoto) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        petBetterDb.editFacility(_id, faciName, location, contactInfo, rating, faciPhoto);
        petBetterDb.closeDatabase();

    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
        ActivityCompat.requestPermissions(EditFacilityActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

    }

    private String imageToString(){
        try{
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            byte[] imgByte = byteArrayOutputStream.toByteArray();

            return Base64.encodeToString(imgByte,Base64.DEFAULT);
        }catch(NullPointerException npe){
            return null;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMG_REQUEST && resultCode == RESULT_OK && data!=null){
            Uri path = data.getData();
            try {


                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                if(bitmap.getHeight()>230||bitmap.getWidth()>415){
                    bitmap = Bitmap.createScaledBitmap(bitmap,415,230,false);
                }
                editImage.setImageBitmap(bitmap);
                editImage.setAdjustViewBounds(true);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void viewPostBackButtonClicked(View view){
        finish();
    }
}
