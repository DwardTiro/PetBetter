package com.example.owner.petbetter.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.owner.petbetter.ServiceGenerator.BASE_URL;

/**
 * Created by Kristian on 10/23/2017.
 */

public class EditProfileActivity extends AppCompatActivity {

    private EditText firstNameEdit;
    private EditText lastNameEdit;
    private EditText emailEdit;
    private EditText mobileEdit;
    private EditText phoneEdit;
    private Button saveButton;
    private ImageButton editImage;
    private static final int IMG_REQUEST = 777;
    private Bitmap bitmap;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    private HerokuService service;


    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        setContentView(R.layout.activity_edit_profile);

        firstNameEdit = (EditText) findViewById(R.id.editUserFirstName);
        lastNameEdit = (EditText) findViewById(R.id.editUserLastName);
        emailEdit = (EditText) findViewById(R.id.editUserEmail);
        mobileEdit = (EditText) findViewById(R.id.editUserMobileNum);
        phoneEdit = (EditText) findViewById(R.id.editUserLandline);
        saveButton = (Button) findViewById(R.id.saveButton);
        editImage = (ImageButton) findViewById(R.id.editImage);

        systemSessionManager = new SystemSessionManager(this);
        if (systemSessionManager.checkLogin())
            finish();


        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();


        String email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        System.out.println(email);
        user = getUser(email);

        firstNameEdit.setText(user.getFirstName());
        lastNameEdit.setText(user.getLastName());
        emailEdit.setText(user.getEmail());
        mobileEdit.setText(user.getMobileNumber());
        phoneEdit.setText(user.getPhoneNumber());

        if(user.getUserPhoto()!=null){
            String newFileName = BASE_URL + user.getUserPhoto();
            //String newFileName = "http://192.168.0.19/petbetter/"+user.getUserPhoto();
            System.out.println("USER PHOTO "+user.getUserPhoto());
            Glide.with(EditProfileActivity.this).load(newFileName).error(R.drawable.app_icon_yellow).into(editImage);
        }


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String image = imageToString();
                //user.setUserPhoto(image);
                editProfile(user.getUserId(), firstNameEdit.getText().toString(), lastNameEdit.getText().toString(),
                        emailEdit.getText().toString(), mobileEdit.getText().toString(), phoneEdit.getText().toString(),
                        image);
                service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
                final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

                final Call<User> call = service.checkLogin(user.getEmail(), user.getPassword());
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        //User thisUser = response.body();
                        final User thisUser = getUserWithId((int) user.getUserId());
                        thisUser.setUserId(response.body().getUserId());


                        Gson gson = new GsonBuilder().serializeNulls().create();
                        String jsonArray = gson.toJson(thisUser);
                        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
                        //RequestBody doesn't contain message_photo.
                        final Call<Void> call2 = service2.editProfile(body);
                        call2.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if(response.isSuccessful()){
                                    dataSynced(12);
                                    final Call<User> call3 = service2.checkLogin(thisUser.getEmail(), thisUser.getPassword());
                                    call3.enqueue(new Callback<User>() {
                                        @Override
                                        public void onResponse(Call<User> call, Response<User> response) {
                                            editProfile(response.body().getUserId(), response.body().getFirstName(),
                                                    response.body().getLastName(), response.body().getEmail(),
                                                    response.body().getMobileNumber(), response.body().getPhoneNumber(),
                                                    response.body().getUserPhoto());
                                        }

                                        @Override
                                        public void onFailure(Call<User> call, Throwable t) {

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

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d("onFailure", t.getLocalizedMessage());
                        Toast.makeText(EditProfileActivity.this, "Unable to update user on server", Toast.LENGTH_LONG);
                    }
                });
                finish();

            }
        });

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do stuff here
                selectImage();

            }
        });

    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
        ActivityCompat.requestPermissions(EditProfileActivity.this,
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

                editImage.setImageBitmap(bitmap);
                editImage.setAdjustViewBounds(true);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    private User getUserWithId(int id) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        User result = petBetterDb.getUserWithId(id);
        petBetterDb.closeDatabase();

        return result;
    }

    private void editProfile(long _id, String firstName, String lastName, String emailAddress, String mobileNum,
                             String landline, String image) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        petBetterDb.editProfile(_id, firstName, lastName, emailAddress, mobileNum, landline, image);
        petBetterDb.closeDatabase();
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

    public void editBackClicked(View view){
        finish();
    }

}
