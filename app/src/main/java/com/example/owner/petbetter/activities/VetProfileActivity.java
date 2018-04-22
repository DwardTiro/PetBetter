package com.example.owner.petbetter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.Message;
import com.example.owner.petbetter.classes.Pending;
import com.example.owner.petbetter.classes.Rating;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
import com.google.android.gms.vision.text.Text;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.owner.petbetter.ServiceGenerator.BASE_URL;

/**
 * Created by Kristian on 8/8/2017.
 */

public class VetProfileActivity extends AppCompatActivity {

    private ImageView profileBG;
    private TextView vetName;
    private TextView vetLandline;
    private TextView vetPage;
    private TextView vetWebsite;
    private TextView vetEmail;
    private TextView vetSpecialty;
    private TextView vetRating;
    private Button rateVetButton;
    private Button messageVetButton;


    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    private String email;
    private Veterinarian vetItem;
    private Message messageItem;
    private long mId;
    private boolean messageExist = true;

    private ImageView verifyLicense;
    private ImageView verifySpecialty;
    private int licenseCheck = 0;
    private TextView education;
    private TextView education2;
    private RelativeLayout educationLayout;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_vet_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView profileName = (TextView) findViewById(R.id.profileToolbarName);



        profileBG = (ImageView) findViewById(R.id.profileImage);
        vetName = (TextView) findViewById(R.id.profileName);
        vetLandline = (TextView) findViewById(R.id.phoneNumTextField);
        vetPage = (TextView) findViewById(R.id.faciPageTextField);
        vetWebsite = (TextView) findViewById(R.id.faciWebsiteTextField);
        vetEmail = (TextView) findViewById(R.id.faciEmailTextField);
        vetSpecialty = (TextView) findViewById(R.id.specialtyTextField);
        vetRating = (TextView) findViewById(R.id.vetListRating);
        rateVetButton = (Button) findViewById(R.id.rateVetButton);
        messageVetButton = (Button) findViewById(R.id.messageVetButton);
        verifyLicense = (ImageView) findViewById(R.id.verifiedLicenseIndicator);
        verifySpecialty = (ImageView) findViewById(R.id.verifiedSpecialtyIndicator);
        education = (TextView) findViewById(R.id.vetEducationTextField);
        education2 = (TextView) findViewById(R.id.vetEducation2TextField);
        educationLayout = (RelativeLayout) findViewById(R.id.educationLayout);

        verifyLicense.setVisibility(View.GONE);
        verifySpecialty.setVisibility(View.INVISIBLE);

        systemSessionManager = new SystemSessionManager(this);
        if(systemSessionManager.checkLogin())
            finish();
        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();

        email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);

        final String jsonMyObject;
        Bundle extras = getIntent().getExtras();
        jsonMyObject = extras.getString("thisVet");

        /*
        messageItem.setFromName(user.getName());
        messageItem.setFromId(user.getUserId());
        messageItem.setUserId(vetItem.getUserId());
        */


        vetItem = new Gson().fromJson(jsonMyObject,Veterinarian.class);

        profileName.setText(vetItem.getFirstName()+" " +vetItem.getLastName());
        vetName.setText(vetItem.getFirstName()+" "+vetItem.getLastName());
        vetLandline.setText("Phone number: "+vetItem.getMobileNumber());
        vetPage.setVisibility(View.GONE);
        vetWebsite.setVisibility(View.GONE);
        vetEmail.setText("Email: "+vetItem.getEmail());

        if(!vetItem.getEducation().equals(",")){
            String[] educationArray = vetItem.getEducation().split(",", -1);
            if(educationArray.length>0&&educationArray[0].length()>0){
                education.setText("Undergraduate University: "+educationArray[0]);
            }
            if(educationArray.length>1&&educationArray[1].length()>0){
                education2.setText("Graduate University: "+educationArray[1]);
            }
        }
        else{
            educationLayout.setVisibility(View.GONE);
        }

        //vetSpecialty.setText(vetItem.getSpecialty());

        if(vetItem.getUserPhoto()!=null){
            String newFileName = BASE_URL + vetItem.getUserPhoto();
            //String newFileName = "http://192.168.0.19/petbetter/"+vetItem.getUserPhoto();
            System.out.println("USER PHOTO "+user.getUserPhoto());
            Glide.with(VetProfileActivity.this).load(newFileName).error(R.drawable.petbetter_logo_final_final).into(profileBG);
        }
        syncRatingChanges();
        syncPendingChanges();

        if(checkIfRated(user.getUserId(), vetItem.getId())){
            rateVetButton.setBackgroundResource(R.color.myrtle_green);
            rateVetButton.setText("Rated");
            rateVetButton.setEnabled(false);
        }

        syncMessageChanges(user.getUserId());

        //mId = getMessageId(user.getUserId(), vetItem.getUserId());

        rateVetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(),RateVetActivity.class);
                intent.putExtra("thisVet", jsonMyObject);
                startActivity(intent);

            }
        });

        messageVetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                messageItem = getMessage(mId);
                Intent intent = new Intent(view.getContext(),NewMessageActivity.class);
                intent.putExtra("thisVet", vetItem.getEmail());
                //intent.putExtra("thisVet", new Gson().toJson(vetItem.getEmail()));
                startActivity(intent);

            }
        });


        //Toast.makeText(this, "Vet's Name: "+vetItem.getName() + ". Delete this toast. Just to help you see where vet variable is", Toast.LENGTH_LONG).show();
    }

    public void syncPendingChanges(){

        System.out.println("PENDING WOOO");
        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);


        final Call<ArrayList<Pending>> call2 = service.getPendingByUser(user.getUserId());
        call2.enqueue(new Callback<ArrayList<Pending>>() {
            @Override
            public void onResponse(Call<ArrayList<Pending>> call, Response<ArrayList<Pending>> response) {
                if(response.isSuccessful()){
                    ArrayList<Pending> pendingList = response.body();
                    System.out.println("PENDING SIZE "+response.body().size());
                    for(Pending pending:pendingList){
                        if((pending.getType()==1 && pending.getIsApproved()==1)||
                                (pending.getType()==2 && pending.getIsApproved()==1)){
                            licenseCheck += 1;
                            if(licenseCheck==2){
                                verifyLicense.setVisibility(View.VISIBLE);
                            }
                        }
                        if(pending.getType()==4&&pending.getIsApproved()==1){
                            verifySpecialty.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Pending>> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });

    }


    private ArrayList<Pending> getUnsyncedPending(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Pending> result = petBetterDb.getUnsyncedPending();
        petBetterDb.closeDatabase();

        return result;
    }

    public long setPending(ArrayList<Pending> pendingList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setPending(pendingList);
        petBetterDb.closeDatabase();

        return result;
    }

    public void syncRatingChanges(){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service3 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        ArrayList<Rating> unsyncedRatings = getUnsyncedRatings();

        System.out.println("how many ratings? "+unsyncedRatings.size());

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(unsyncedRatings);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addRatings(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    System.out.println("RATINGS ADDED YEY");
                    dataSynced(14);

                    final Call<ArrayList<Rating>> call2 = service2.getRatings();
                    call2.enqueue(new Callback<ArrayList<Rating>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Rating>> call, Response<ArrayList<Rating>> response) {
                            if(response.isSuccessful()){
                                setRatings(response.body());
                                ArrayList<Float> vetRatings = getVeterinarianRatings(vetItem.getId());
                                float n = 0;
                                if(vetRatings!=null){
                                    if(vetRatings.size()>0){
                                        for(int i = 0; i<vetRatings.size();i++){
                                            n = n + vetRatings.get(i);
                                        }
                                        n = n/(float) vetRatings.size();
                                        vetRating.setText(String.valueOf(n));
                                    }
                                    else{
                                        vetRating.setText("0.0");
                                    }
                                }

                                final Call<Void> call3 = service3.updateRating(vetItem.getId(), n, 1);
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

    public void syncMessageChanges(final long userId){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        ArrayList<Message> unsyncedMessages = getUnsyncedMessages();
        System.out.println("UNSYNCED MESSAGES: "+unsyncedMessages.size());
        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(unsyncedMessages);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addMessages(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    System.out.println("MESSAGES ADDED YEY");
                    dataSynced(5);

                    final Call<ArrayList<Message>> call2 = service2.getMessages(userId);
                    call2.enqueue(new Callback<ArrayList<Message>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
                            if(response.isSuccessful()){
                                setMessages(response.body());
                                mId = getMessageId(user.getUserId(), vetItem.getUserId());
                                if(messageExist==false){
                                    messageItem = getMessage(mId);
                                    Intent intent = new Intent(VetProfileActivity.this,MessageActivity.class);
                                    intent.putExtra("thisMessage", new Gson().toJson(messageItem));
                                    startActivity(intent);
                                    messageExist = true;
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Message>> call, Throwable t) {
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


    public long setMessages(ArrayList<Message> messageList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setMessages(messageList);
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Message> getUnsyncedMessages(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Message> result = petBetterDb.getUnsyncedMessages();
        petBetterDb.closeDatabase();

        return result;
    }

    public long setRatings(ArrayList<Rating> rateList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setRatings(rateList);
        petBetterDb.closeDatabase();

        return result;
    }


    private boolean checkIfRated(long rater_id, long rated_id) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        boolean result = petBetterDb.checkIfRated(rater_id, rated_id, 1);
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Float> getVeterinarianRatings(long vetId){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Float> result = petBetterDb.getVeterinarianRatings(vetId);
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Rating> getUnsyncedRatings(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Rating> result = petBetterDb.getUnsyncedRatings();
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


    @Override
    protected void onResume(){
        super.onResume();

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

    //back function found in toolbar profile for Image Button profileToolbarBack
    public void profileBackClicked(View view){
       finish();
    }

    private long getMessageId(long fromId, long toId){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        long result = petBetterDb.getMessageId(fromId, toId);
        petBetterDb.closeDatabase();

        return result;
    }

    private Message getMessage(long messageId){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        Message result = petBetterDb.getMessage(messageId);
        petBetterDb.closeDatabase();

        return result;
    }

    public int generateMessageId(){
        ArrayList<Integer> storedIds;
        int messageRepId = 1;

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        storedIds = petBetterDb.getMessageIds();
        petBetterDb.closeDatabase();


        if(storedIds.isEmpty()) {
            return messageRepId;
        } else {
            while (storedIds.contains(messageRepId)){
                messageRepId += 1;
            }
            return messageRepId;
        }
    }

    private long createMessage(int messageId, long userId, long toId){
        long  result;

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        result = petBetterDb.createMessage(messageId, userId, toId);
        petBetterDb.closeDatabase();



        return result;
    }
    //Integrate to db to display stuff on page

}
