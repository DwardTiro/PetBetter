package com.example.owner.petbetter.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kristian on 2/17/2018.
 */

public class AddFacilityActivity extends AppCompatActivity {

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

    HerokuService service;
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_add_new_facility);

        Toolbar toolbar = (Toolbar) findViewById(R.id.viewPostToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final TextView activityTitle = (TextView) findViewById(R.id.activity_title);
        activityTitle.setText("Add Facility");

        addFacilityButton = (Button) findViewById(R.id.addFacilityButton);
        facilityName = (EditText) findViewById(R.id.addFacilityName);
        phoneNum = (EditText) findViewById(R.id.addFacilityPhone);
        openTime = (Spinner) findViewById(R.id.addFacilityOpenTimeSpinner);
        closeTime = (Spinner) findViewById(R.id.addFacilityCloseTimeSpinner);
        facilityAddress = (EditText) findViewById(R.id.addFacilityAddress);
        editImage = (ImageButton) findViewById(R.id.clinicEditImage);

        facilityName.addTextChangedListener(formWatcher);
        phoneNum.addTextChangedListener(formWatcher);
        facilityAddress.addTextChangedListener(formWatcher);

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


    }

    private final TextWatcher formWatcher = new TextWatcher(){

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(facilityName.getText().toString().length() == 0 ||
                    phoneNum.getText().toString().length() == 0 ||
                    facilityAddress.getText().toString().length() == 0){
                addFacilityButton.setBackgroundColor(getResources().getColor(R.color.medTurquoise));
                addFacilityButton.setEnabled(false);
            }
            else {
                addFacilityButton.setEnabled(true);
                addFacilityButton.setBackgroundColor(getResources().getColor(R.color.myrtle_green));
            }
        }
    };
    public void addFacility(View view){
        if(facilityAddress.getText().toString()!=""){
            service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
            String image = imageToString();

            Bundle extras = new Bundle();
            extras.putString("bldg_name", facilityName.getText().toString());
            extras.putString("hours_open", openTime.getSelectedItem().toString());
            extras.putString("hours_close", closeTime.getSelectedItem().toString());
            extras.putString("phone_num", phoneNum.getText().toString());
            extras.putString("location", facilityAddress.getText().toString());
            extras.putString("image", image);

            Intent intent = new Intent(
                    AddFacilityActivity.this,
                    com.example.owner.petbetter.activities.MapsActivity.class
            );
            intent.putExtras(extras);
            startActivity(intent);
        }
        else{
            Toast.makeText(AddFacilityActivity.this, "Please input a location", Toast.LENGTH_SHORT);
        }
    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
        ActivityCompat.requestPermissions(AddFacilityActivity.this,
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
                if(bitmap.getHeight()>200||bitmap.getWidth()>240){
                    bitmap = Bitmap.createScaledBitmap(bitmap,200,240,false);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void viewPostBackButtonClicked(View view){
        Intent intent = new Intent(
                AddFacilityActivity.this,
                com.example.owner.petbetter.activities.VeterinarianHomeActivity.class
        );
        startActivity(intent);
    }
}
