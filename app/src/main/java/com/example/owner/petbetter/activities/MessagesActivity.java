package com.example.owner.petbetter.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.adapters.MessageAdapter;
import com.example.owner.petbetter.classes.Message;
import com.example.owner.petbetter.classes.MessageRep;
import com.example.owner.petbetter.classes.Notifications;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.fragments.FragmentMessages;
import com.example.owner.petbetter.fragments.FragmentVetListing;
import com.example.owner.petbetter.interfaces.CheckUpdates;
import com.example.owner.petbetter.services.MyService;
import com.example.owner.petbetter.services.NotificationReceiver;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;
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
 * Created by Kristian on 2/22/2018.
 */


public class MessagesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, CheckUpdates {

    private ArrayList<Message> messageList;
    private DataAdapter petBetterDb;
    private FloatingActionButton messagesFab;
    private MessageAdapter messageAdapter;
    private NavigationView navigationView;
    private RecyclerView messagesRecyclerView;
    private SystemSessionManager systemSessionManager;
    private TextView textNavEmail, textNavUser;
    private Toolbar menuBar;
    private User user;
    private FragmentMessages fragment1;
    private NotificationReceiver notifReceiver = new NotificationReceiver((CheckUpdates) this);
    //private NotificationReceiver notifReceiver2 = new NotificationReceiver();
    private ImageView notifButton;
    private AutoCompleteTextView actvMessage;
    private ImageView imageViewDrawer;
    private int currFragment = 1;
    private Button messagesButton;
    private Button messageReqButton;
    private Toolbar toolbar;
    private ArrayList<Message> pendingMessages;
    private SwipeRefreshLayout refreshMessages;
    private int refChecker = 0;
    private Button addTopicButton;
    private Spinner spinnerFilter;
    private FloatingActionButton fab;

    HerokuService service;

    @Override
    protected void onResume() {
        super.onResume();
        MessagesActivity.this.registerReceiver(this.notifReceiver, new IntentFilter(Intent.ACTION_ATTACH_DATA));
        onResult();
    }


    @Override
    protected void onPause() {
        super.onPause();
        MessagesActivity.this.unregisterReceiver(notifReceiver);
        //MessagesActivity.this.unregisterReceiver(notifReceiver2);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        try {
            //on click notification
            Bundle extras = getIntent().getExtras();
            String jsonMyObject;
            jsonMyObject = extras.getString("notifMessage");

            Notifications notifItem = new Gson().fromJson(jsonMyObject, Notifications.class);
            notifRead(notifItem.getId());
        } catch (Exception e) {
            Log.e("onclick", "Exception onclick" + e);
        }
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_messages);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout nDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle nToggle = new ActionBarDrawerToggle(this, nDrawerLayout, toolbar, R.string.open, R.string.close);
        nDrawerLayout.setDrawerListener(nToggle);
        nToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        systemSessionManager = new SystemSessionManager(this);
        if (systemSessionManager.checkLogin())
            finish();

        final HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();
        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        notifButton = (ImageView) findViewById(R.id.imageview_notifs);
        actvMessage = (AutoCompleteTextView) findViewById(R.id.actvMesaage);
        messagesButton = (Button) findViewById(R.id.messagesButton);
        messageReqButton = (Button) findViewById(R.id.messageReqButton);
        refreshMessages = (SwipeRefreshLayout) findViewById(R.id.refreshMessages);
        addTopicButton = (Button) findViewById(R.id.addTopicButton);
        //fab = (FloatingActionButton) findViewById(R.id.fab);

        addTopicButton.setVisibility(View.GONE);

        if(!getUnsyncedNotifications().isEmpty())
            notifButton.setImageResource(R.mipmap.ic_notifications_active_black_24dp);

        String email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        textNavEmail = (TextView) headerView.findViewById(R.id.textNavEmail);
        textNavEmail.setText(email);

        user = getUser(email);
        imageViewDrawer = (ImageView) headerView.findViewById(R.id.imageViewDrawer);
        if(user.getUserPhoto()!=null){

            String newFileName = BASE_URL + user.getUserPhoto();
            System.out.println(newFileName);
            //String newFileName = "http://192.168.0.19/petbetter/"+thisMessageRep.getMessagePhoto();
            Glide.with(MessagesActivity.this).load(newFileName).error(R.drawable.app_icon_yellow).into(imageViewDrawer);
            /*
            Picasso.with(inflater.getContext()).load("http://".concat(newFileName))
                    .error(R.drawable.back_button).into(holder.messageRepImage);*/
            //setImage(holder.messageRepImage, newFileName);

            imageViewDrawer.setVisibility(View.VISIBLE);
        }

        textNavUser = (TextView) headerView.findViewById(R.id.textNavUser);
        textNavUser.setText(user.getName());


        messagesButtonClicked(this.findViewById(android.R.id.content));
        /*
        messagesRecyclerView = (RecyclerView) findViewById(R.id.messagesRecyclerView);
        messageList = getMessages(user.getUserId());
        System.out.println("Size of message list in this thing "+messageList.size());
        messageAdapter = new MessageAdapter(this, messageList,new MessageAdapter.OnItemClickListener() {
            @Override public void onItemClick(Message item) {
                Intent intent = new Intent(MessagesActivity.this, com.example.owner.petbetter.activities.MessageActivity.class);
                intent.putExtra("thisMessage", new Gson().toJson(item));
                startActivity(intent);
            }
        });

        messageAdapter.notifyItemRangeChanged(0, messageAdapter.getItemCount());
        messagesRecyclerView.setAdapter(messageAdapter);
        messagesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        messagesRecyclerView.setHasFixedSize(true);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        */


        notifButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Redirect to notifications
                Intent intent = new Intent(MessagesActivity.this, com.example.owner.petbetter.activities.NotificationActivity.class);
                startActivity(intent);
            }
        });

        hideItems();
        actvMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(actvMessage.getText().toString().equals("")){
                    Intent intent = new Intent(MessagesActivity.this, com.example.owner.petbetter.activities.MessagesActivity.class);
                    startActivity(intent);
                }
                else{
                    if(currFragment == 1){
                        service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

                        //query the substring to server data
                        final Call<ArrayList<Message>> call = service.queryMessages(actvMessage.getText().toString(), user.getUserId());
                        call.enqueue(new Callback<ArrayList<Message>>() {
                            @Override
                            public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
                                ArrayList<Message> messageList = response.body();

                                for(Message message : messageList){
                                    User mUser;
                                    if(getUserWithId(message.getFromId()).getUserId()==user.getUserId()){
                                        mUser = getUserWithId(message.getUserId());
                                    }
                                    else{
                                        mUser = getUserWithId(message.getFromId());
                                    }

                                    message.setFromName(mUser.getName());
                                }
                                fragment1 = new FragmentMessages(messageList, 1);
                                //replace frame_se
                                System.out.println("DO WE GET HERE MESSAGES PAR");

                                getSupportFragmentManager().beginTransaction().replace(R.id.messages_container,fragment1).
                                        addToBackStack(null).commitAllowingStateLoss();
                                //ArrayAdapter<Veterinarian> adapter = new ArrayAdapter<Veterinarian>(this,R.layout.,vetList);

                            }

                            @Override
                            public void onFailure(Call<ArrayList<Message>> call, Throwable t) {
                                Log.d("onFailure", t.getLocalizedMessage());
                                Toast.makeText(MessagesActivity.this, "Unable to get vets from server", Toast.LENGTH_LONG);
                            }
                        });
                    }
                    if(currFragment == 2){

                    }
                }

            }



            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        refreshMessages.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshMessages.setRefreshing(true);
                syncMessageChanges(user.getUserId());
                syncMessageRepChanges();
                refreshMessages.setRefreshing(false);
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
                                System.out.println("response size messages "+response.body().size());
                                setMessages(response.body());
                                if(currFragment==1){
                                    messagesButtonClicked(findViewById(android.R.id.content));
                                }
                                else{
                                    messageReqButtonClicked(findViewById(android.R.id.content));
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

    private void dataSynced(int n){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        petBetterDb.dataSynced(n);
        petBetterDb.closeDatabase();

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

    public void syncMessageRepChanges(){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        ArrayList<MessageRep> unsyncedMessages = getUnsyncedMessageReps();
        System.out.println("UNSYNCED MESSAGEREPS: "+unsyncedMessages.size());
        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(unsyncedMessages);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.addMessageReps(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    System.out.println("MESSAGEREPS ADDED YEY");
                    dataSynced(6);

                    final Call<ArrayList<MessageRep>> call2 = service2.getMessageReps();
                    call2.enqueue(new Callback<ArrayList<MessageRep>>() {
                        @Override
                        public void onResponse(Call<ArrayList<MessageRep>> call, Response<ArrayList<MessageRep>> response) {
                            if(response.isSuccessful()){
                                System.out.println("response size messagereps "+response.body().size());
                                setMessageReps(response.body());


                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<MessageRep>> call, Throwable t) {
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

    private ArrayList<MessageRep> getUnsyncedMessageReps(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<MessageRep> result = petBetterDb.getUnsyncedMessageReps();
        petBetterDb.closeDatabase();

        return result;
    }

    public long setMessageReps(ArrayList<MessageRep> messageRepList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setMessageReps(messageRepList);
        petBetterDb.closeDatabase();

        return result;
    }

    public void messagesButtonClicked(View view){

        currFragment = 1;
        messageReqButton.setBackgroundResource(R.color.medTurquoise);
        messageReqButton.setTextColor(getResources().getColor(R.color.colorWhite));
        messagesButton.setBackgroundResource(R.color.main_White);
        messagesButton.setTextColor(getResources().getColor(R.color.myrtle_green));
        messagesButton.setPaintFlags( messagesButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        messageReqButton.setPaintFlags( messageReqButton.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));


        //messageList = getMessages(user.getUserId());
        fragment1 = new FragmentMessages();

        getSupportFragmentManager().beginTransaction().replace(R.id.messages_container,fragment1).
                addToBackStack(null).commitAllowingStateLoss();
        /*
        FragmentPetClinicListing fragment = new FragmentPetClinicListing();

        getSupportFragmentManager().beginTransaction().add(R.id.frame_container,fragment).commit();*/
    }

    public void messageReqButtonClicked(View view){

        currFragment = 2;
        messageReqButton.setBackgroundResource(R.color.main_White);
        messageReqButton.setTextColor(getResources().getColor(R.color.myrtle_green));
        messagesButton.setBackgroundResource(R.color.medTurquoise);
        messagesButton.setTextColor(getResources().getColor(R.color.colorWhite));
        messageReqButton.setPaintFlags( messageReqButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        messagesButton.setPaintFlags( messagesButton.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));

        /*
        actvMessage.setText("");
        actvMessage.setEnabled(false);*/

        pendingMessages = getPendingMessages(user.getUserId());
        FragmentMessages fragment2 = new FragmentMessages(pendingMessages, 2);
        getSupportFragmentManager().beginTransaction().replace(R.id.messages_container,fragment2).
                addToBackStack(null).commitAllowingStateLoss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //getMenuInflater().inflate(R.menu.options_menu,menu);

        //hideItems();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        /*
        if(id == R.id.search_option){
            //change appearance of toolbar
            Intent intent = new Intent(this, com.example.owner.petbetter.activities.SearchActivity.class);
            startActivity(intent);
            return true;
        }*/



        return super.onOptionsItemSelected(item);
    }

    public void hideItems(){
        Menu menu = navigationView.getMenu();

        if(user.getUserType()==2){
            menu.findItem(R.id.community2).setVisible(false);
        }
        menu.findItem(R.id.search_drawer).setVisible(false);
        //menu.findItem(R.id.new_message_option).setVisible(false);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.search_drawer) {
            Intent intent = new Intent(this, com.example.owner.petbetter.activities.SearchActivity.class);
            startActivity(intent);
        }

        else if (id == R.id.home) {
            if(user.getUserType()==1){
                Intent intent = new Intent(this, com.example.owner.petbetter.activities.VeterinarianHomeActivity.class);
                startActivity(intent);
            }
            else{
                Intent intent = new Intent(this, com.example.owner.petbetter.activities.CommActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.community) {
            Intent intent = new Intent(this, com.example.owner.petbetter.activities.HomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.messages) {
            Intent intent = new Intent(this, com.example.owner.petbetter.activities.MessagesActivity.class);
            startActivity(intent);
        } else if (id == R.id.user_profile) {
            Intent intent;
            if(user.getUserType() == 1){
                intent = new Intent(this, com.example.owner.petbetter.activities.VetUserProfileActivity.class);
            }else
                intent = new Intent(this, com.example.owner.petbetter.activities.UserProfileActivity.class);

            startActivity(intent);
        } else if (id == R.id.bookmarks) {
            Intent intent = new Intent(this, com.example.owner.petbetter.activities.BookmarksActivity.class);
            startActivity(intent);
        } else if (id == R.id.log_out) {
            Intent intent = new Intent(this, com.example.owner.petbetter.activities.MainActivity.class);
            SharedPreferences preferences =getSharedPreferences("prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
            Intent intentLogout = new Intent().setAction("com.package.ACTION_LOGOUT");
            //notifReceiver.onReceive(this, intentLogout);
            sendBroadcast(intentLogout);
            startActivity(intent);
            //stopService(new Intent(MessagesActivity.this, MyService.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initializeDatabase() {

        petBetterDb = new DataAdapter(this);

        try {
            petBetterDb.createDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void notifRead(long notifId){

        //modify this method in such a way that it only gets bookmarks tagged by user. Separate from facilities.
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        petBetterDb.notifRead(notifId);
        petBetterDb.closeDatabase();
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
    public ArrayList<Message> getMessages(long userId){

        //modify this method in such a way that it only gets bookmarks tagged by user. Separate from facilities.
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Message> result = petBetterDb.getMessages(userId);
        petBetterDb.closeDatabase();
        User tempUser;

        for(int i=0;i<result.size();i++){
            tempUser = getUserWithId(result.get(i).getUserId());
            if(tempUser.getUserId()==user.getUserId()){
                tempUser = getUserWithId(result.get(i).getFromId());
                result.get(i).setFromName(tempUser.getName());
            }
            else{
                tempUser = getUserWithId(result.get(i).getUserId());
                result.get(i).setFromName(tempUser.getName());
            }
        }

        return result;
    }

    private User getUserWithId(long id){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        User result = petBetterDb.getUserWithId((int) id);
        petBetterDb.closeDatabase();

        return result;
    }


    public void onResult() {
        if(actvMessage.getText().toString()==""){
            messageList = getMessages(user.getUserId());
            messageAdapter.updateList(messageList);
            //messageAdapter.notifyDataSetChanged();
            System.out.println("ONRESULT MESSAGES");
        }

    }

    private ArrayList<Notifications> getUnsyncedNotifications(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Notifications> result = petBetterDb.getUnsyncedNotifications();
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Message> getPendingMessages(long userId){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Message> result = petBetterDb.getPendingMessages(userId);
        petBetterDb.closeDatabase();

        return result;
    }
}
