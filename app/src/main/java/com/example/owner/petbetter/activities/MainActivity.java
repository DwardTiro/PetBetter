package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.TypefaceUtil;
import com.example.owner.petbetter.activities.SignUpActivity;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.classes.VeterinarianList;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.example.owner.petbetter.activities.HomeActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText editEmail;
    private EditText editPassword;
    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private TextView textInfo;
    HerokuService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceUtil.overrideFont(getApplicationContext(),"SERIF","fonts/Roboto-Regular.ttf");

        setContentView(R.layout.activity_main);

        systemSessionManager = new SystemSessionManager(getApplicationContext());

        loginButton = (Button) findViewById(R.id.loginButton);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);
        textInfo = (TextView) findViewById(R.id.textInfo);
        textInfo.setVisibility(View.INVISIBLE);
        initializeDatabase();

        editPassword.setTypeface(Typeface.DEFAULT);
        editPassword.setTransformationMethod(new PasswordTransformationMethod());

        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);


    }

    public void userLogin(View v){
        System.out.println("Im here");
        login(v);
        //Intent intent = new Intent(this, com.example.owner.petbetter.HomeActivity.class);
        //startActivity(intent);
    }
    private void initializeDatabase() {

        petBetterDb = new DataAdapter(this);

        try {
            petBetterDb.createDatabase();
        } catch(SQLException e ){
            e.printStackTrace();
        }

    }

    private void login(View v) {

        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        View focusView = null;


        if (checkEmailValidity(email)&& password.trim().length() > 0) {

            Call<User> call = service.checkLogin(email, password);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    System.out.println("fuck this weird shit");
                    if(response.isSuccessful()){



                        ArrayList<Veterinarian> vetList = getVeterinarians();
                        System.out.println("VET LIST HERE: "+vetList.size());
                        syncVetChanges(vetList);
                        System.out.println("vet changes synced");
                        //watch youtube tutorial first before touching this

                        User user = response.body();
                        System.out.println("From server wew: "+response.body().toString());
                        systemSessionManager.createUserSession(user.getEmail());
                        Intent intent = new Intent(MainActivity.this, com.example.owner.petbetter.activities.HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);


                        finish();

                    }
                    else{
                        textInfo.setText("Invalid Email or Password");
                        textInfo.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    textInfo.setText("Wrong username/password");
                    textInfo.setVisibility(View.VISIBLE);
                    Log.d("onFailure", t.getLocalizedMessage());
                }
            });
/*
            if (!checkEmailValidity(email)||email.equals("")) {
  //System.out.println("Invalid email");
                textInfo.setText("Invalid email or password");
                textInfo.setVisibility(View.VISIBLE);
            } else {

                try{
                    if (checkLogin(email, password)) {
                        systemSessionManager.createUserSession(email);
                        Intent intent = new Intent(MainActivity.this, com.example.owner.petbetter.activities.HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        finish();
                    } else {
                        //System.out.println("Invalid email or password");
                        textInfo.setText("Invalid Email or Password");
                        textInfo.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e){
                    textInfo.setText("Invalid Email or Password");
                    textInfo.setVisibility(View.VISIBLE);
                }


            }
*/
        } else {

            if (email.trim().length() == 0) {
                //System.out.println("Email Required");
                textInfo.setText("Email Required");
                textInfo.setVisibility(View.VISIBLE);
            } else if (password.trim().length() == 0) {

                //System.out.println("Password Required");
                textInfo.setText("Password Required");
                textInfo.setVisibility(View.VISIBLE);
            }
        }

    }

    public void syncVetChanges(final ArrayList<Veterinarian> vets){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        System.out.println("WE HERE BOOIII");
        final Call<ArrayList<Veterinarian>> call = service.getVeterinarians();
        System.out.println("WAT HAPPEN");
        call.enqueue(new Callback<ArrayList<Veterinarian>>() {
            @Override
            public void onResponse(Call<ArrayList<Veterinarian>> call, Response<ArrayList<Veterinarian>> response) {
                if(response.isSuccessful()){
                    ArrayList<Veterinarian> vetList = response.body();
                    if(!vets.equals(vetList)){
                        final ArrayList<Veterinarian> unsyncedVets = getUnsyncedVets();
                        System.out.println("UNSYNCED VETS: "+unsyncedVets.size());
                        if(!unsyncedVets.isEmpty()){
                            Call<Void> call2 = service2.addVets(unsyncedVets);
                            System.out.println("WE HERE BOI? "+call2.toString());
                            call2.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if(response.isSuccessful()){
                                        System.out.println("VETS ADDED YEY");
                                        dataSynced(1);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Log.d("onFailure", t.getLocalizedMessage());
                                }
                            });
                            //make addVet.php able to receive arraylists
                        }
                        if(vets.size()<vetList.size()){
                            int n = vets.size();
                            while(n<vetList.size()){
                                petBetterDb.addVet(vetList.get(n).getId(), (int) vetList.get(n).getUserId(), (int) vetList.get(n).getRating());
                                n+=1;
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Veterinarian>> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });

    }

    private boolean checkLogin(String email, String password) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        boolean result = petBetterDb.checkLogin(email, password);
        petBetterDb.closeDatabase();


        return result;
    }

    private ArrayList<Veterinarian> getVeterinarians(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Veterinarian> result = petBetterDb.getVeterinarians();
        System.out.println("The number of veterinarians is: "+result.size());
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Veterinarian> getUnsyncedVets(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Veterinarian> result = petBetterDb.getUnsyncedVets();
        System.out.println("The number of veterinarians is: "+result.size());
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

    public void signUp(View view){

        Intent intent = new Intent(this, SignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
    private boolean checkEmailValidity(String email){

        String regEx = "^[\\w\\.-]+@([\\w\\-]+\\.)+[a-zA-Z]{2,4}$";

        Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();


    }


}
