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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    public void onCreate(Bundle savedInstance) {
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
        if (systemSessionManager.checkLogin())
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
        vetEducation.setEnabled(false);
        vetSpecialization.setSelection(getIndex(vetSpecialization, vet.getSpecialty()));

        if (user.getUserPhoto() != null) {
            String newFileName = BASE_URL + user.getUserPhoto();
            //String newFileName = "http://192.168.0.19/petbetter/"+user.getUserPhoto();
            System.out.println("USER PHOTO " + user.getUserPhoto());
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
                editProfile(user.getUserId(),
                        vetFirstName.getText().toString(),
                        vetLastName.getText().toString(),
                        vetEmailAddress.getText().toString(),
                        vetContactInfo.getText().toString(),
                        vetContactInfo.getText().toString(),
                        image,
                        user.getPassword());

                service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
                final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
                final HerokuService service3 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
                final HerokuService service4 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);


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
                                if (response.isSuccessful()) {
                                    dataSynced(12);
                                    final Call<User> call3 = service2.checkLogin(user.getEmail(), user.getPassword());
                                    call3.enqueue(new Callback<User>() {
                                        @Override
                                        public void onResponse(Call<User> call, Response<User> response) {
                                            if(response.isSuccessful()) {
                                                editProfile(response.body().getUserId(), response.body().getFirstName(),
                                                        response.body().getLastName(), response.body().getEmail(),
                                                        response.body().getMobileNumber(), response.body().getPhoneNumber(),
                                                        response.body().getUserPhoto(), response.body().getPassword());
                                                editVeterinarian(response.body().getUserId(),
                                                        vetSpecialization.getSelectedItem().toString(),
                                                        vetEducation.getText().toString(), vetDescription.getText().toString());

                                                final Call<Void> call3 = service3.editVeterinarian(vetSpecialization.getSelectedItem().toString(),
                                                        vetEducation.getText().toString(), vetDescription.getText().toString(), response.body().getUserId());
                                                call3.enqueue(new Callback<Void>() {
                                                    @Override
                                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                                        dataSynced(1);
                                                        System.out.println("Edit Vet Profile Success");
                                                        Intent intent = new Intent(EditVetProfileActivity.this, com.example.owner.petbetter.activities.VetUserProfileActivity.class);
                                                        startActivity(intent);
                                                        Toast.makeText(EditVetProfileActivity.this, "Edit Profile Success", Toast.LENGTH_SHORT).show();

                                                        if(vetSpecialization.getSelectedItem().toString()!=vet.getSpecialty()){
                                                            System.out.println("What's the problem par? "+vetSpecialization.getSelectedItem().toString());
                                                            Call<Void> call4 = service4.editPending(vet.getUserId(), vetSpecialization.getSelectedItem().toString());
                                                            call4.enqueue(new Callback<Void>() {
                                                                @Override
                                                                public void onResponse(Call<Void> call, Response<Void> response) {
                                                                    System.out.println("Successfully edited pending.");
                                                                }

                                                                @Override
                                                                public void onFailure(Call<Void> call, Throwable t) {

                                                                }
                                                            });
                                                        }


                                                    }

                                                    @Override
                                                    public void onFailure(Call<Void> call, Throwable t) {

                                                    }
                                                });
                                            }

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
                        Toast.makeText(EditVetProfileActivity.this, "Unable to update user on server", Toast.LENGTH_LONG);
                    }
                });

            }
        });


    }

    private void editProfile(long _id, String firstName, String lastName, String emailAddress, String mobileNum,
                             String landline, String image, String password) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        petBetterDb.editProfile(_id, firstName, lastName, emailAddress, mobileNum, landline, image, password);
        petBetterDb.closeDatabase();
    }

    public void editVeterinarian(long user_id, String specialty, String education, String profile_desc) {
        try{
            petBetterDb.openDatabase();
        }catch (SQLException e){
            e.printStackTrace();
        }
        petBetterDb.editVeterinarian(user_id, specialty, education, profile_desc);
        petBetterDb.closeDatabase();
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

    private void dataSynced(int n) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        petBetterDb.dataSynced(n);
        petBetterDb.closeDatabase();
    }

    public void editBackClicked(View view) {
        finish();
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

    private Veterinarian getVetWithUserID(long user_id) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Veterinarian result = petBetterDb.getVeterinarianWithId(user_id);
        petBetterDb.closeDatabase();

        return result;
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
        ActivityCompat.requestPermissions(EditVetProfileActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

    }

    private String imageToString() {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] imgByte = byteArrayOutputStream.toByteArray();

            return Base64.encodeToString(imgByte, Base64.DEFAULT);
        } catch (NullPointerException npe) {
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {


                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                if (bitmap.getHeight() > 150 || bitmap.getWidth() > 150) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
                }

                vetProfileImage.setImageBitmap(bitmap);
                vetProfileImage.setAdjustViewBounds(true);

            } catch (IOException e) {
                e.printStackTrace();
            }
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
}
