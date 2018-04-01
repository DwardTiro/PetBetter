package com.example.owner.petbetter.activities;

import android.Manifest;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    private Spinner openTime;
    private Spinner closeTime;
    private ImageButton editImage;
    private SystemSessionManager systemSessionManager;
    private static final int IMG_REQUEST = 777;
    private Bitmap bitmap;
    private TextView textViewAddress;
    private Facility faciItem;
    private DataAdapter petBetterDb;
    private ImageButton topicNewPost;

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
        openTime = (Spinner) findViewById(R.id.addFacilityOpenTimeSpinner);
        closeTime = (Spinner) findViewById(R.id.addFacilityCloseTimeSpinner);
        facilityAddress = (EditText) findViewById(R.id.addFacilityAddress);
        facilityAddress.setEnabled(false);
        facilityAddress.setVisibility(View.GONE);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        textViewAddress.setVisibility(View.GONE);
        topicNewPost = (ImageButton) findViewById(R.id.topicNewPost);
        topicNewPost.setVisibility(View.GONE);

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
        phoneNum.setText(faciItem.getContactInfo());

        openTime.setSelection(getIndex(openTime,faciItem.getHoursOpen()));
        closeTime.setSelection(getIndex(closeTime,faciItem.getHoursClose()));
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });



    }


    //function to edit facility info
    public void addFacility(View view){
        if(facilityAddress.getText().toString()!=""){
            service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
            String image = imageToString();


            editFacility(faciItem.getId(), facilityName.getText().toString(), faciItem.getLocation(),
                    openTime.getSelectedItem().toString(), closeTime.getSelectedItem().toString(), phoneNum.getText().toString(),
                    faciItem.getRating(), image);

            uploadFacilityChanges();
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

    private void editFacility(long _id, String faciName, String location, String hoursOpen,String hoursClose,
                              String contactInfo, float rating, String faciPhoto) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        petBetterDb.editFacility(_id, faciName, location, hoursOpen, hoursClose, contactInfo, rating, faciPhoto);
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

    private int getIndex(Spinner spinner, String value) {
        int itemIndex = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                itemIndex = i;
                break;
            }
        }
        return itemIndex;
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
