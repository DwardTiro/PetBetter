package com.example.owner.petbetter.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.WorkHours;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

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
    private EditText faciPage;
    private EditText faciWebsite;
    private EditText faciEmail;
    private ImageButton editImage;
    private TextView textViewAddress;
    private SystemSessionManager systemSessionManager;
    private static final int IMG_REQUEST = 777;
    private Bitmap bitmap;
    private ImageButton topicNewPost;
    private LinearLayout hoursContainer;
    private Button addHours;

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
        faciPage = (EditText) findViewById(R.id.addFaciPage);
        faciWebsite = (EditText) findViewById(R.id.addFaciWebsite);
        faciEmail = (EditText) findViewById(R.id.addFaciEmail);
        facilityAddress = (EditText) findViewById(R.id.addFacilityAddress);
        editImage = (ImageButton) findViewById(R.id.clinicEditImage);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        topicNewPost = (ImageButton) findViewById(R.id.topicNewPost);
        hoursContainer = (LinearLayout) findViewById(R.id.hoursContainer);
        addHours = (Button) findViewById(R.id.addTimeButton);
        topicNewPost.setVisibility(View.GONE);

        textViewAddress.setVisibility(View.GONE);
        facilityAddress.setVisibility(View.GONE);

        facilityName.addTextChangedListener(formWatcher);
        phoneNum.addTextChangedListener(formWatcher);
        //facilityAddress.addTextChangedListener(formWatcher);

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

    public void deleteRow(View view) {
        hoursContainer.removeView((View) view.getParent());

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
            if(facilityName.getText().toString().length() == 0){
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

        ArrayList<WorkHours> hoursList = new ArrayList<>();
        for(int i=0;i<hoursContainer.getChildCount();i++){
            EditText editText = (EditText) hoursContainer.getChildAt(i).findViewById(R.id.dayField);
            Spinner openSpinner = (Spinner) hoursContainer.getChildAt(i).findViewById(R.id.addFacilityOpenTimeSpinner);
            Spinner closeSpinner = (Spinner) hoursContainer.getChildAt(i).findViewById(R.id.addFacilityCloseTimeSpinner);

            WorkHours workHours = new WorkHours(0, 0, editText.getText().toString(), openSpinner.getSelectedItem().toString(),
                    closeSpinner.getSelectedItem().toString(), 0);
            hoursList.add(workHours);
        }

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonHours = gson.toJson(hoursList);

        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        String image = imageToString();

        String contactInfo = phoneNum.getText().toString()+","+faciPage.getText().toString()+","+
                faciWebsite.getText().toString()+","+faciEmail.getText().toString();


        Bundle extras = new Bundle();
        extras.putString("bldg_name", facilityName.getText().toString());
        extras.putString("phone_num", contactInfo);
        extras.putString("workhours", jsonHours);
        extras.putString("location", facilityAddress.getText().toString());
        extras.putString("image", image);

        Intent intent = new Intent(
                AddFacilityActivity.this,
                com.example.owner.petbetter.activities.MapsActivity.class
        );
        intent.putExtras(extras);
        startActivity(intent);
    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
        ActivityCompat.requestPermissions(AddFacilityActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

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
        Intent intent = new Intent(
                AddFacilityActivity.this,
                com.example.owner.petbetter.activities.VeterinarianHomeActivity.class
        );
        startActivity(intent);
    }
}
