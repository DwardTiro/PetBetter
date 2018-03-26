package com.example.owner.petbetter.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import static com.example.owner.petbetter.ServiceGenerator.BASE_URL;

/**
 * Created by Kristian on 3/26/2018.
 */

public class EditVetProfileActivity extends AppCompatActivity {

    private EditText vetFirstName;
    private EditText vetLastName;
    private EditText vetEducation;
    private EditText vetContactInfo;
    private EditText vetEmailAddress;
    private EditText vetDescription;
    private Spinner vetSpecialization;
    private ImageView vetProfileImage;
    private Bitmap bitmap;
    private static final int IMG_REQUEST = 777;
    private Button saveButton;

    private User user;
    private Veterinarian vet;
    private DataAdapter petBetterDb;
    private NavigationView navigationView;
    private SystemSessionManager systemSessionManager;

    HerokuService service;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_edit_vet_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);

        vetFirstName = (EditText) findViewById(R.id.editVetFirstName);
        vetLastName = (EditText) findViewById(R.id.editVetLastName);
        vetEducation = (EditText) findViewById(R.id.editVetEducation);
        vetContactInfo = (EditText) findViewById(R.id.editVetContactNum);
        vetEmailAddress = (EditText) findViewById(R.id.editVetEmail);
        vetDescription = (EditText) findViewById(R.id.editVetAboutMe);
        vetSpecialization = (Spinner) findViewById(R.id.editVetSpecialty);
        vetProfileImage = (ImageView) findViewById(R.id.editVetImage);
        saveButton = (Button) findViewById(R.id.saveButton);

        systemSessionManager = new SystemSessionManager(this);
        if(systemSessionManager.checkLogin())
            finish();

        HashMap<String, String> userIn = systemSessionManager.getUserDetails();
        initializeDatabase();
        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        String email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);
        vet = getVetWithUserID(user.getUserId());

        vetFirstName.setText(user.getFirstName());
        vetLastName.setText(user.getLastName());
        vetContactInfo.setText(user.getMobileNumber());
        vetEmailAddress.setText(user.getEmail());
        vetDescription.setText(vet.getProfileDesc());
        vetEducation.setText(vet.getEducation());
        vetSpecialization.setSelection(getIndex(vetSpecialization, vet.getSpecialty()));

        if(user.getUserPhoto()!=null){
            String newFileName = BASE_URL + user.getUserPhoto();
            //String newFileName = "http://192.168.0.19/petbetter/"+user.getUserPhoto();
            System.out.println("USER PHOTO "+user.getUserPhoto());
            Glide.with(EditVetProfileActivity.this).load(newFileName).error(R.drawable.app_icon_yellow).into(vetProfileImage);
        }
        vetProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String image = imageToString();
                
            }
        });



    }

    public void editBackClicked(View view) {
        finish();
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
    private Veterinarian getVetWithUserID(long user_id){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        Veterinarian result = petBetterDb.getVeterinarianWithId(user_id);
        petBetterDb.closeDatabase();

        return result;
    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
        ActivityCompat.requestPermissions(EditVetProfileActivity.this,
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
                if(bitmap.getHeight()>150||bitmap.getWidth()>150){
                    bitmap = Bitmap.createScaledBitmap(bitmap,150,150,false);
                }

                vetProfileImage.setImageBitmap(bitmap);
                vetProfileImage.setAdjustViewBounds(true);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int getIndex(Spinner spinner, String value){
        int itemIndex = 0;
        for(int i = 0; i < spinner.getCount(); i++){
            if(spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)){
                itemIndex = i;
                break;
            }
        }
        return  itemIndex;
    }
}
