package com.example.owner.petbetter.database;

/**
 * Created by owner on 27/7/2017.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.Marker;
import com.example.owner.petbetter.classes.Message;
import com.example.owner.petbetter.classes.MessageRep;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.PostRep;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


public class DataAdapter {

    private static final String TAG = "DataAdapter";

    private int gOpenCounter;

    private SQLiteDatabase petBetterDb;
    private DatabaseHelper petBetterDatabaseHelper;

    private static final String USER_TABLE = "users";
    private static final String MARKER_TABLE = "markers";
    private static final String VET_TABLE = "veterinarians";
    private static final String FACI_TABLE = "facilities";
    private static final String POST_TABLE = "posts";
    private static final String MESSAGE_TABLE = "messages";
    private static final String POST_REP_TABLE = "postreps";
    private static final String MESSAGE_REP_TABLE = "messagereps";


    public DataAdapter(Context context) {
        Context myContext = context;
        petBetterDatabaseHelper = DatabaseHelper.getInstance(myContext);
    }

    public DataAdapter createDatabase() throws SQLException {

        try {
            petBetterDatabaseHelper.createDatabase();
        }catch (IOException ioe) {
            Log.e(TAG, ioe.toString() + "UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }

        return this;
    }

    public synchronized DataAdapter openDatabase() throws SQLException {

        gOpenCounter++;

        if(gOpenCounter == 1) {

            try {
                petBetterDatabaseHelper.openDatabase();
                petBetterDatabaseHelper.close();
                petBetterDb = petBetterDatabaseHelper.getWritableDatabase();
            }catch (SQLException sqle) {
                Log.e(TAG, "open >>" +sqle.toString());
                throw sqle;
            }

        }

        return this;
    }

    public synchronized void closeDatabase() {

        gOpenCounter--;

        if(gOpenCounter == 0) {

            petBetterDb.close();
        }

        Log.d("database open", gOpenCounter + "");
    }

    public boolean checkLogin (String username, String password) {

        String sql = "SELECT * FROM users WHERE email = '" + username + "' AND password = '" + password + "'";

        Cursor c = petBetterDb.rawQuery(sql, null);

        if(c.getCount() > 0) {
            c.close();
            return true;
        } else {
            c.close();
            return false;
        }
    }

    public int getUserId(String username) {

        int result;
        String sql = "SELECT _id FROM users WHERE email = '" + username + "'";
        Cursor c = petBetterDb.rawQuery(sql, null);

        c.moveToFirst();
        result = c.getInt(c.getColumnIndexOrThrow("_id"));

        c.close();
        return result;
    }

    public String getUserName (int userId) {

        String result;
        String sql = "SELECT first_name, last_name FROM " + USER_TABLE + " WHERE _id = " + userId;
        Cursor c = petBetterDb.rawQuery(sql, null);

        Log.e("cursor", c.getCount() + "");

        c.moveToFirst();

        result = c.getString(c.getColumnIndexOrThrow("first_name")) + " " + c.getString(c.getColumnIndexOrThrow("last_name"));
        c.close();

        return result;
    }

    public String getUserName (String email) {

        String result;
        String sql = "SELECT first_name, last_name FROM " + USER_TABLE + " WHERE email = '" + email + "'";
        Cursor c = petBetterDb.rawQuery(sql, null);

        c.moveToFirst();
        result = c.getString(c.getColumnIndexOrThrow("first_name")) + " " + c.getString(c.getColumnIndexOrThrow("last_name"));

        c.close();

        return result;
    }

    public User getUser(String email){

        String sql = "SELECT * FROM " + USER_TABLE + " WHERE email = '" + email + "'";
        Cursor c = petBetterDb.rawQuery(sql, null);

        Log.e("cursor", c.getCount() + "");

        c.moveToFirst();

        User result = new User(c.getLong(c.getColumnIndexOrThrow("_id")),
                c.getString(c.getColumnIndexOrThrow("first_name")),
                c.getString(c.getColumnIndexOrThrow("last_name")),
                c.getString(c.getColumnIndexOrThrow("mobile_num")),
                c.getString(c.getColumnIndexOrThrow("phone_num")),
                c.getString(c.getColumnIndexOrThrow("email")),
                c.getString(c.getColumnIndexOrThrow("password")),
                c.getInt(c.getColumnIndexOrThrow("age")),
                c.getInt(c.getColumnIndexOrThrow("user_type")));

        c.close();
        return result;
    }

    public User getUserWithId(int id){

        String sql = "SELECT * FROM " + USER_TABLE + " WHERE _id = '" + id + "'";
        Cursor c = petBetterDb.rawQuery(sql, null);

        Log.e("cursor", c.getCount() + "");

        c.moveToFirst();

        User result = new User(c.getLong(c.getColumnIndexOrThrow("_id")),
                c.getString(c.getColumnIndexOrThrow("first_name")),
                c.getString(c.getColumnIndexOrThrow("last_name")),
                c.getString(c.getColumnIndexOrThrow("mobile_num")),
                c.getString(c.getColumnIndexOrThrow("phone_num")),
                c.getString(c.getColumnIndexOrThrow("email")),
                c.getString(c.getColumnIndexOrThrow("password")),
                c.getInt(c.getColumnIndexOrThrow("age")),
                c.getInt(c.getColumnIndexOrThrow("user_type")));

        c.close();
        return result;
    }

    public ArrayList<Post> getPosts(){

        ArrayList<Post> results = new ArrayList<>();

        String sql = "SELECT * FROM "+POST_TABLE+" INNER JOIN "+USER_TABLE+" ON posts.user_id = users._id";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            Post post= new Post(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("user_id")),
                    c.getString(c.getColumnIndexOrThrow("topic_name")),
                    c.getString(c.getColumnIndexOrThrow("topic_content")),
                    c.getString(c.getColumnIndexOrThrow("first_name")),
                    c.getString(c.getColumnIndexOrThrow("last_name")));
            results.add(post);
        }

        c.close();
        return results;
    }

    public ArrayList<Message> getMessages(long userId){
        //probably needs userid as parameter

        ArrayList<Message> results = new ArrayList<>();

        String sql = "SELECT * FROM "+MESSAGE_TABLE+" INNER JOIN "+USER_TABLE+" ON messages.from_id = users._id WHERE user_id = '" + userId + "'";


        //SELECT * FROM messages INNER JOIN users ON messages.from_id = users._id WHERE messages.user_id = messageId
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            Message message = new Message(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("user_id")),
                    c.getLong(c.getColumnIndexOrThrow("from_id")),
                    c.getString(c.getColumnIndexOrThrow("first_name")),
                    c.getString(c.getColumnIndexOrThrow("last_name")));
            message.setMessageContent(getLatestRep((int) message.getId()));
            results.add(message);
        }

        c.close();
        return results;
    }

    public String getLatestRep(int messageId) {

        String result;

        String sql = "SELECT * FROM " + MESSAGE_REP_TABLE + " WHERE message_id = '" + messageId + "'";
        Cursor c = petBetterDb.rawQuery(sql, null);

        try{

            c.moveToLast();
            result = c.getString(c.getColumnIndexOrThrow("rep_content"));
            c.close();
        }catch(CursorIndexOutOfBoundsException e){
            result = "";
        }

        return result;
    }


        public ArrayList<MessageRep> getMessageReps(int messageId){
        //probably needs userid as parameter

        ArrayList<MessageRep> results = new ArrayList<>();

        String sql = "SELECT * FROM "+MESSAGE_REP_TABLE + " WHERE message_id = '" + messageId + "'";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            MessageRep messagerep = new MessageRep(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("user_id")),
                    c.getInt(c.getColumnIndexOrThrow("message_id")),
                    c.getString(c.getColumnIndexOrThrow("rep_content")),
                    c.getInt(c.getColumnIndexOrThrow("is_sent")));
            results.add(messagerep);
        }

        c.close();
        return results;
    }

    public ArrayList<PostRep> getPostReps(int postId){
        //probably needs userid as parameter

        ArrayList<PostRep> results = new ArrayList<>();

        String sql = "SELECT * FROM "+POST_REP_TABLE + " WHERE post_id = '" + postId + "'";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            PostRep postrep = new PostRep(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("user_id")),
                    c.getInt(c.getColumnIndexOrThrow("post_id")),
                    c.getString(c.getColumnIndexOrThrow("rep_content")));
            results.add(postrep);
        }

        c.close();
        return results;
    }

    public ArrayList<Integer> getMarkerIds () {

        ArrayList<Integer> ids = new ArrayList<>();

        String sql = "SELECT _id FROM "+MARKER_TABLE;
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            ids.add(c.getInt(c.getColumnIndexOrThrow("_id")));
        }

        c.close();
        return ids;
    }

    public ArrayList<Integer> getUserIds () {

        ArrayList<Integer> ids = new ArrayList<>();

        String sql = "SELECT _id FROM "+USER_TABLE;
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            ids.add(c.getInt(c.getColumnIndexOrThrow("_id")));
        }

        c.close();
        return ids;
    }

    public ArrayList<Integer> getVetIds () {

        ArrayList<Integer> ids = new ArrayList<>();

        String sql = "SELECT _id FROM "+VET_TABLE;
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            ids.add(c.getInt(c.getColumnIndexOrThrow("_id")));
        }

        c.close();
        return ids;
    }

    public ArrayList<Marker> loadMarkers(long userId){
        ArrayList<Marker> results = new ArrayList<>();

        String sql = "SELECT * FROM "+MARKER_TABLE + " WHERE user_id = '" + userId + "'";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            Marker marker = new Marker(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getString(c.getColumnIndexOrThrow("bldg_name")),
                    c.getDouble(c.getColumnIndexOrThrow("longitude")),
                    c.getDouble(c.getColumnIndexOrThrow("latitude")),
                    c.getString(c.getColumnIndexOrThrow("location")),
                    c.getLong(c.getColumnIndexOrThrow("user_id")),
                    c.getInt(c.getColumnIndexOrThrow("type")));
            results.add(marker);
        }

        c.close();
        return results;
    }

    public ArrayList<Marker> getBookmarks(long userId, int type){
        ArrayList<Marker> results = new ArrayList<>();

        String sql = "SELECT * FROM "+MARKER_TABLE + " WHERE user_id = '" + userId + "' AND type = '" + type + "'";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            Marker marker = new Marker(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getString(c.getColumnIndexOrThrow("bldg_name")),
                    c.getDouble(c.getColumnIndexOrThrow("longitude")),
                    c.getDouble(c.getColumnIndexOrThrow("latitude")),
                    c.getString(c.getColumnIndexOrThrow("location")),
                    c.getLong(c.getColumnIndexOrThrow("user_id")),
                    c.getInt(c.getColumnIndexOrThrow("type")));
            results.add(marker);
        }

        c.close();
        return results;
    }

    public ArrayList<Veterinarian> getVeterinarians(){
        ArrayList<Veterinarian> results = new ArrayList<>();
        int userId;
        User user;

        String sql = "SELECT * FROM "+VET_TABLE;
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            userId = c.getInt(c.getColumnIndexOrThrow("user_id"));
            user = getUserWithId(userId);
            Veterinarian vet = new Veterinarian(c.getInt(c.getColumnIndexOrThrow("_id")),
                    user.getUserId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getMobileNumber(),
                    user.getPhoneNumber(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getAge(),
                    user.getUserType(),
                    c.getString(c.getColumnIndexOrThrow("specialty")),
                    c.getInt(c.getColumnIndexOrThrow("rating")));
            results.add(vet);
        }

        c.close();
        return results;
    }

    public ArrayList<Facility> getClinics(){
        ArrayList<Facility> results = new ArrayList<>();
        String temp;

        //String sql = "SELECT * FROM " + FACI_TABLE + " WHERE vet_id = '" + veterinarian.getId() + "'";
        String sql = "SELECT * FROM " + FACI_TABLE;
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            Facility facility = new Facility(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getString(c.getColumnIndexOrThrow("faci_name")),
                    c.getString(c.getColumnIndexOrThrow("location")),
                    c.getString(c.getColumnIndexOrThrow("hours_open")),
                    c.getString(c.getColumnIndexOrThrow("hours_close")),
                    c.getString(c.getColumnIndexOrThrow("contact_info")),
                    c.getInt(c.getColumnIndexOrThrow("vet_id")),
                    c.getInt(c.getColumnIndexOrThrow("rating")));
            results.add(facility);
        }

        c.close();
        return results;
    }

    public long addMarker(int rowId, String bldgName, String location){
        long result;

        ContentValues cv = new ContentValues();
        cv.put("_id", rowId);
        cv.put("bldg_name", bldgName);


        result = petBetterDb.insert(MARKER_TABLE, null, cv);

        System.out.println("THE RESULT IS "+result);
        System.out.println("MARKER ID IS " +rowId);

        return result;
    }

    public long addUser(int userId, String firstName, String lastName, String email, String password, int userType){
        long result;

        ContentValues cv = new ContentValues();
        cv.put("_id", userId);
        cv.put("first_name", firstName);
        cv.put("last_name", lastName);
        cv.put("mobile_num", "");
        cv.put("phone_num", "");
        cv.put("email", email);
        cv.put("password", password);
        cv.put("age", 0);
        cv.put("user_type", userType);


        result = petBetterDb.insert(USER_TABLE, null, cv);

        return result;
    }

    public long touchMarker(int rowId, String bldgName, double longitude, double latitude, String location, long userId, int type){
        long result;

        ContentValues cv = new ContentValues();
        cv.put("_id", rowId);
        cv.put("bldg_name", bldgName);
        cv.put("longitude", longitude);
        cv.put("latitude", latitude);
        cv.put("location", location);
        cv.put("user_id", userId);
        cv.put("type", type);


        result = petBetterDb.insert(MARKER_TABLE, null, cv);

        System.out.println("THE RESULT IS "+result);
        System.out.println("MARKER ID IS " +rowId);

        return result;
    }

}
