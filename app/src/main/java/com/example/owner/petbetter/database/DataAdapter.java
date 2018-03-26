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

import com.example.owner.petbetter.classes.Bookmark;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.FacilityMembership;
import com.example.owner.petbetter.classes.Follower;
import com.example.owner.petbetter.classes.LocationMarker;
import com.example.owner.petbetter.classes.Message;
import com.example.owner.petbetter.classes.MessageRep;
import com.example.owner.petbetter.classes.Notifications;
import com.example.owner.petbetter.classes.Pending;
import com.example.owner.petbetter.classes.Pet;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.PostRep;
import com.example.owner.petbetter.classes.Rating;
import com.example.owner.petbetter.classes.Services;
import com.example.owner.petbetter.classes.Topic;
import com.example.owner.petbetter.classes.Upvote;
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
    private static final String NOTIF_TABLE = "notifications";
    private static final String TOPIC_TABLE = "topics";
    private static final String VET_RANK_TABLE = "veterinarians_rating";
    private static final String FACI_RANK_TABLE = "facilities_rating";
    private static final String FOLLOWER_TABLE = "followers";
    private static final String RATE_TABLE = "ratings";
    private static final String PET_TABLE = "pets";
    private static final String SERVICE_TABLE = "services";
    private static final String UPVOTE_TABLE = "upvotes";
    private static final String BOOKMARK_TABLE = "bookmarks";
    private static final String PENDING_TABLE = "pending";
    private static final String FACI_MEMBER_TABLE = "facility_membership";



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

        c.moveToFirst();

        User result = new User(c.getLong(c.getColumnIndexOrThrow("_id")),
                c.getString(c.getColumnIndexOrThrow("first_name")),
                c.getString(c.getColumnIndexOrThrow("last_name")),
                c.getString(c.getColumnIndexOrThrow("mobile_num")),
                c.getString(c.getColumnIndexOrThrow("phone_num")),
                c.getString(c.getColumnIndexOrThrow("email")),
                c.getString(c.getColumnIndexOrThrow("password")),
                c.getInt(c.getColumnIndexOrThrow("age")),
                c.getInt(c.getColumnIndexOrThrow("user_type")),
                c.getString(c.getColumnIndexOrThrow("user_photo")),
                c.getInt(c.getColumnIndexOrThrow("is_disabled")));
        c.close();
        return true;
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

    public User getPostUser(long userId) {

        String sql = "SELECT * FROM " + USER_TABLE + " WHERE _id = '" + userId + "'";
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
                c.getInt(c.getColumnIndexOrThrow("user_type")),
                c.getString(c.getColumnIndexOrThrow("user_photo")),
                c.getInt(c.getColumnIndexOrThrow("is_disabled")));

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
        try{
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
                    c.getInt(c.getColumnIndexOrThrow("user_type")),
                    c.getString(c.getColumnIndexOrThrow("user_photo")),
                    c.getInt(c.getColumnIndexOrThrow("is_disabled")));

            c.close();

            return result;
        }catch(CursorIndexOutOfBoundsException e){
            return null;
        }
    }

    public Follower getFollowerWithId(long id){

        String sql = "SELECT * FROM "+FOLLOWER_TABLE+" WHERE _id = '" + id + "'";
        Cursor c = petBetterDb.rawQuery(sql, null);

        Log.e("cursor", c.getCount() + "");

        c.moveToFirst();

        Follower result = new Follower(c.getLong(c.getColumnIndexOrThrow("_id")),
                c.getLong(c.getColumnIndexOrThrow("topic_id")),
                c.getLong(c.getColumnIndexOrThrow("user_id")),
                c.getInt(c.getColumnIndexOrThrow("is_allowed")));

        c.close();
        return result;
    }

    public Follower getFollowerWithTopicUser(long topicId, long userId){

        String sql = "SELECT * FROM "+FOLLOWER_TABLE+" WHERE user_id = '" + userId + "' AND topic_id = '"+topicId+"'";
        Cursor c = petBetterDb.rawQuery(sql, null);

        Log.e("cursor", c.getCount() + "");

        c.moveToFirst();

        Follower result = new Follower(c.getLong(c.getColumnIndexOrThrow("_id")),
                c.getLong(c.getColumnIndexOrThrow("topic_id")),
                c.getLong(c.getColumnIndexOrThrow("user_id")),
                c.getInt(c.getColumnIndexOrThrow("is_allowed")));

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
                c.getInt(c.getColumnIndexOrThrow("user_type")),
                c.getString(c.getColumnIndexOrThrow("user_photo")),
                c.getInt(c.getColumnIndexOrThrow("is_disabled")));

        c.close();
        return result;
    }

    public Veterinarian getVeterinarianFromId(long id){
        String sql = "SELECT * FROM " + VET_TABLE + " WHERE _id = '" + id + "'";


        Cursor c = petBetterDb.rawQuery(sql, null);

        c.moveToFirst();
        User user = getUserWithId((int) c.getLong(c.getColumnIndexOrThrow("user_id")));
        Veterinarian result = new Veterinarian(
                c.getInt(c.getColumnIndexOrThrow("_id")),
                c.getLong(c.getColumnIndexOrThrow("user_id")),
                user.getLastName(),
                user.getFirstName(),
                user.getMobileNumber(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getPassword(),
                user.getAge(),
                user.getUserType(),
                user.getUserPhoto(),
                user.getIsDisabled(),
                c.getString(c.getColumnIndexOrThrow("specialty")),
                c.getFloat(c.getColumnIndexOrThrow("rating")),
                c.getString(c.getColumnIndexOrThrow("education")),
                c.getInt(c.getColumnIndexOrThrow("is_licensed")),
                c.getString(c.getColumnIndexOrThrow("profile_desc"))
        );
        return result;
    }

    public Veterinarian getVeterinarianWithId(long user_id){
        User user = getUserWithId((int) user_id);
        String sql = "SELECT * FROM " + VET_TABLE + " WHERE user_id = '" + user_id + "'";

        Cursor c = petBetterDb.rawQuery(sql, null);

        c.moveToFirst();

        Veterinarian result = new Veterinarian(
                c.getInt(c.getColumnIndexOrThrow("_id")),
                c.getLong(c.getColumnIndexOrThrow("user_id")),
                user.getLastName(),
                user.getFirstName(),
                user.getMobileNumber(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getPassword(),
                user.getAge(),
                user.getUserType(),
                user.getUserPhoto(),
                user.getIsDisabled(),
                c.getString(c.getColumnIndexOrThrow("specialty")),
                c.getFloat(c.getColumnIndexOrThrow("rating")),
                c.getString(c.getColumnIndexOrThrow("education")),
                c.getInt(c.getColumnIndexOrThrow("is_licensed")),
                c.getString(c.getColumnIndexOrThrow("profile_desc"))
        );
        return result;
    }

    public LocationMarker getMarkerWithId(int faci_id){
        String sql = "SELECT * FROM " + MARKER_TABLE + " WHERE faci_id = '" + faci_id + "'";

        Cursor c = petBetterDb.rawQuery(sql, null);
        c.moveToFirst();

        LocationMarker result = new LocationMarker(
                c.getLong(c.getColumnIndexOrThrow("_id")),
                c.getString(c.getColumnIndexOrThrow("bldg_name")),
                c.getDouble(c.getColumnIndexOrThrow("latitude")),
                c.getDouble(c.getColumnIndexOrThrow("longitude")),
                c.getString(c.getColumnIndexOrThrow("location")),
                c.getLong(c.getColumnIndexOrThrow("user_id")),
                c.getInt(c.getColumnIndexOrThrow("type")),
                c.getLong(c.getColumnIndexOrThrow("faci_id"))
        );

        return result;
    }

    public Facility getNewFacilityWithId(int vet_id){
        String sql = "SELECT f._id AS _id, f.faci_name AS faci_name, f.location AS location, f.hours_open AS hours_open, " +
                "f.hours_close AS hour_close, f.contact_info AS contact_info, f.rating AS rating, f.faci_photo AS faci_photo, " +
                "f.is_disabled AS is_disabled FROM facilities AS f INNER JOIN facility_membership AS fm ON f._id = fm.faci_id"+
                " INNER JOIN veterinarians AS v ON fm.vet_id = v._id WHERE v._id = '"+vet_id+"'";

        Cursor c = petBetterDb.rawQuery(sql, null);
        c.moveToLast();

        Facility result = new Facility(
                c.getInt(c.getColumnIndexOrThrow("_id")),
                c.getString(c.getColumnIndexOrThrow("faci_name")),
                c.getString(c.getColumnIndexOrThrow("location")),
                c.getString(c.getColumnIndexOrThrow("hours_open")),
                c.getString(c.getColumnIndexOrThrow("hours_close")),
                c.getString(c.getColumnIndexOrThrow("contact_info")),
                c.getFloat(c.getColumnIndexOrThrow("rating")),
                c.getString(c.getColumnIndexOrThrow("faci_photo")),
                c.getInt(c.getColumnIndexOrThrow("is_disabled"))
        );

        return result;
    }

    public User getNewUser(String email){
        String sql = "SELECT * FROM " + USER_TABLE + " WHERE email = '" + email + "'";

        Cursor c = petBetterDb.rawQuery(sql, null);
        c.moveToLast();

        User result = new User(c.getLong(c.getColumnIndexOrThrow("_id")),
                c.getString(c.getColumnIndexOrThrow("first_name")),
                c.getString(c.getColumnIndexOrThrow("last_name")),
                c.getString(c.getColumnIndexOrThrow("mobile_num")),
                c.getString(c.getColumnIndexOrThrow("phone_num")),
                c.getString(c.getColumnIndexOrThrow("email")),
                c.getString(c.getColumnIndexOrThrow("password")),
                c.getInt(c.getColumnIndexOrThrow("age")),
                c.getInt(c.getColumnIndexOrThrow("user_type")),
                c.getString(c.getColumnIndexOrThrow("user_photo")),
                c.getInt(c.getColumnIndexOrThrow("is_disabled"))
        );

        return result;
    }

    public ArrayList<Post> getPosts(){

        ArrayList<Post> results = new ArrayList<>();

        String sql = "SELECT p._id AS _id, p.user_id, p.topic_name AS topic_name, p.topic_content AS topic_content, " +
                "p.date_created AS date_created, u.first_name AS first_name, u.last_name AS last_name, p.is_deleted AS is_deleted," +
                " p.post_photo AS post_photo, p.id_link AS id_link, p.id_type AS id_type FROM posts AS p INNER JOIN users u ON p.user_id = u._id WHERE p.is_deleted != 1";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            System.out.println("DATA ADAPTER "+ c.getInt(c.getColumnIndexOrThrow("_id")));
            Post post= new Post(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("user_id")),
                    c.getString(c.getColumnIndexOrThrow("topic_name")),
                    c.getString(c.getColumnIndexOrThrow("topic_content")),
                    c.getString(c.getColumnIndexOrThrow("date_created")),
                    c.getString(c.getColumnIndexOrThrow("first_name")),
                    c.getString(c.getColumnIndexOrThrow("last_name")),
                    c.getString(c.getColumnIndexOrThrow("post_photo")),
                    c.getInt(c.getColumnIndexOrThrow("id_link")),
                    c.getInt(c.getColumnIndexOrThrow("id_type")),
                    c.getInt(c.getColumnIndexOrThrow("is_deleted")));
            results.add(post);
        }

        c.close();
        return results;
    }

    public ArrayList<Message> getMessages(long userId){
        //probably needs userid as parameter

        ArrayList<Message> results = new ArrayList<>();

        //String sql = "SELECT * FROM "+MESSAGE_TABLE+" INNER JOIN "+USER_TABLE+" ON messages.from_id = users._id WHERE user_id = '" + userId + "'";


        String sql = "SELECT m._id AS _id, m.user_one AS user_one, m.user_two AS user_two, m.is_allowed AS is_allowed, u.first_name AS first_name, " +
                "u.last_name AS last_name FROM messages AS m INNER JOIN users AS u ON m.user_one = u._id WHERE u._id = '" + userId + "' AND m.is_allowed = 1 " +
                "UNION " +
                "SELECT m._id AS _id, m.user_one AS user_one, m.user_two AS user_two, m.is_allowed AS is_allowed, u.first_name AS first_name, " +
                "u.last_name AS last_name FROM messages AS m INNER JOIN users AS u ON m.user_two = u._id WHERE u._id = '" + userId + "' AND m.is_allowed = 1";


        //SELECT * FROM messages INNER JOIN users ON messages.from_id = users._id WHERE messages.user_id = messageId
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            Message message = new Message(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("user_one")),
                    c.getLong(c.getColumnIndexOrThrow("user_two")),
                    c.getInt(c.getColumnIndexOrThrow("is_allowed")),
                    c.getString(c.getColumnIndexOrThrow("first_name")),
                    c.getString(c.getColumnIndexOrThrow("last_name")));
            message.setMessageContent(getLatestRep((int) message.getId()));
            results.add(message);
        }

        c.close();
        return results;
    }

    public ArrayList<Integer> generateBookmarkIds(){
        ArrayList<Integer> ids = new ArrayList<>();

        String sql = "SELECT _id FROM " + BOOKMARK_TABLE;
        Cursor c = petBetterDb.rawQuery(sql, null);
        while (c.moveToNext()){
            ids.add(c.getInt(c.getColumnIndexOrThrow("_id")));
        }
        c.close();

        return ids;
    }

    public ArrayList<Bookmark> getUnsyncedBookmarks(){
        ArrayList<Bookmark> results = new ArrayList<>();

        String sql = "SELECT * FROM "+BOOKMARK_TABLE+" WHERE is_synced = 0";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            Bookmark bookmark = new Bookmark(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getInt(c.getColumnIndexOrThrow("item_id")),
                    c.getInt(c.getColumnIndexOrThrow("bookmark_type")),
                    c.getInt(c.getColumnIndexOrThrow("user_id")));
            results.add(bookmark);
        }

        c.close();
        return results;
    }

    public ArrayList<FacilityMembership> getUnsyncedMembers(){
        ArrayList<FacilityMembership> results = new ArrayList<>();

        String sql = "SELECT * FROM "+FACI_MEMBER_TABLE+" WHERE is_synced = 0";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            FacilityMembership facilityMembership = new FacilityMembership(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getInt(c.getColumnIndexOrThrow("faci_id")),
                    c.getInt(c.getColumnIndexOrThrow("vet_id")));
            results.add(facilityMembership);
        }

        c.close();
        return results;
    }

    public ArrayList<Pending> getUnsyncedPending(){
        ArrayList<Pending> results = new ArrayList<>();

        String sql = "SELECT * FROM "+PENDING_TABLE+" WHERE is_synced = 0";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            Pending pending = new Pending(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getInt(c.getColumnIndexOrThrow("foreign_id")),
                    c.getInt(c.getColumnIndexOrThrow("type")),
                    c.getInt(c.getColumnIndexOrThrow("is_approved")));
            results.add(pending);
        }

        c.close();
        return results;
    }

    public ArrayList<Message> getPendingMessages(long userId){
        //probably needs userid as parameter

        ArrayList<Message> results = new ArrayList<>();

        //String sql = "SELECT * FROM "+MESSAGE_TABLE+" INNER JOIN "+USER_TABLE+" ON messages.from_id = users._id WHERE user_id = '" + userId + "'";

        String sql = "SELECT m._id AS _id, m.user_one AS user_one, m.user_two AS user_two, m.is_allowed AS is_allowed, u.first_name AS first_name, " +
                "u.last_name AS last_name FROM messages AS m INNER JOIN users AS u ON m.user_one = u._id WHERE u._id = '" + userId + "' AND m.is_allowed = 0 " +
                "UNION " +
                "SELECT m._id AS _id, m.user_one AS user_one, m.user_two AS user_two, m.is_allowed AS is_allowed, u.first_name AS first_name, " +
                "u.last_name AS last_name FROM messages AS m INNER JOIN users AS u ON m.user_two = u._id WHERE u._id = '" + userId + "' AND m.is_allowed = 0";


        //SELECT * FROM messages INNER JOIN users ON messages.from_id = users._id WHERE messages.user_id = messageId
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            Message message = new Message(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("user_one")),
                    c.getLong(c.getColumnIndexOrThrow("user_two")),
                    c.getInt(c.getColumnIndexOrThrow("is_allowed")),
                    c.getString(c.getColumnIndexOrThrow("first_name")),
                    c.getString(c.getColumnIndexOrThrow("last_name")));
            message.setMessageContent(getLatestRep((int) message.getId()));
            if(!(message.getUserId()==userId)){
                results.add(message);
            }
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


    public ArrayList<MessageRep> getMessageReps(long messageId){
        //probably needs userid as parameter

        ArrayList<MessageRep> results = new ArrayList<>();

        String sql = "SELECT mr._id AS _id, mr.user_id AS user_id, mr.sender_id AS sender_id, mr.message_id AS message_id, " +
                "mr.rep_content AS rep_content, mr.is_sent AS is_sent, mr.date_performed AS date_performed, " +
                "mr.message_photo AS message_photo, u.first_name AS first_name, u.last_name AS last_name FROM messagereps AS mr INNER JOIN users AS u " +
                "ON mr.sender_id = u._id WHERE mr.message_id = '" + messageId + "'";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            MessageRep messagerep = new MessageRep(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("user_id")),
                    c.getLong(c.getColumnIndexOrThrow("sender_id")),
                    c.getInt(c.getColumnIndexOrThrow("message_id")),
                    c.getString(c.getColumnIndexOrThrow("rep_content")),
                    c.getInt(c.getColumnIndexOrThrow("is_sent")),
                    c.getString(c.getColumnIndexOrThrow("date_performed")),
                    c.getString(c.getColumnIndexOrThrow("first_name")),
                    c.getString(c.getColumnIndexOrThrow("last_name")),
                    c.getString(c.getColumnIndexOrThrow("message_photo")));
            results.add(messagerep);
        }

        c.close();
        return results;
    }

    public ArrayList<MessageRep> getAllMessageReps(){
        //probably needs userid as parameter

        ArrayList<MessageRep> results = new ArrayList<>();

        String sql = "SELECT * FROM "+MESSAGE_REP_TABLE;
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            MessageRep messagerep = new MessageRep(c.getLong(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("user_id")),
                    c.getLong(c.getColumnIndexOrThrow("sender_id")),
                    c.getInt(c.getColumnIndexOrThrow("message_id")),
                    c.getString(c.getColumnIndexOrThrow("rep_content")),
                    c.getInt(c.getColumnIndexOrThrow("is_sent")),
                    c.getString(c.getColumnIndexOrThrow("date_performed")));
            results.add(messagerep);
        }

        c.close();
        return results;
    }

    public ArrayList<MessageRep> getMessageRepsFromUser(long userId){
        //probably needs userid as parameter

        ArrayList<MessageRep> results = new ArrayList<>();

        String sql = "SELECT mr._id AS _id, mr.user_id AS user_id, mr.sender_id AS sender_id, mr.message_id AS message_id, " +
                "mr.rep_content AS rep_content, mr.is_sent AS is_sent, mr.date_performed AS date_performed, " +
                "mr.message_photo AS message_photo, u.first_name AS first_name, u.last_name AS last_name FROM messagereps AS mr INNER JOIN users AS u " +
                "ON mr.sender_id = u._id WHERE mr.user_id = '" + userId + "'";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            MessageRep messagerep = new MessageRep(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("user_id")),
                    c.getLong(c.getColumnIndexOrThrow("sender_id")),
                    c.getInt(c.getColumnIndexOrThrow("message_id")),
                    c.getString(c.getColumnIndexOrThrow("rep_content")),
                    c.getInt(c.getColumnIndexOrThrow("is_sent")),
                    c.getString(c.getColumnIndexOrThrow("date_performed")),
                    c.getString(c.getColumnIndexOrThrow("first_name")),
                    c.getString(c.getColumnIndexOrThrow("last_name")),
                    c.getString(c.getColumnIndexOrThrow("message_photo")));
            results.add(messagerep);
        }

        c.close();
        return results;
    }

    public ArrayList<PostRep> getAllPostReps(){
        //probably needs userid as parameter

        System.out.println("We done???");
        ArrayList<PostRep> results = new ArrayList<>();

        String sql = "SELECT * FROM "+POST_REP_TABLE + " WHERE is_deleted != 1";
        Cursor c = petBetterDb.rawQuery(sql, null);


        while(c.moveToNext()) {
            PostRep postrep = new PostRep(c.getLong(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("user_id")),
                    c.getInt(c.getColumnIndexOrThrow("post_id")),
                    c.getInt(c.getColumnIndexOrThrow("parent_id")),
                    c.getString(c.getColumnIndexOrThrow("rep_content")),
                    c.getString(c.getColumnIndexOrThrow("date_performed")),
                    c.getString(c.getColumnIndexOrThrow("postrep_photo")),
                    c.getInt(c.getColumnIndexOrThrow("is_deleted")));
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

    public ArrayList<LocationMarker> loadMarkers(long userId){
        ArrayList<LocationMarker> results = new ArrayList<>();

        String sql = "SELECT * FROM "+MARKER_TABLE + " WHERE user_id = '" + userId + "'";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            LocationMarker locationMarker = new LocationMarker(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getString(c.getColumnIndexOrThrow("bldg_name")),
                    c.getDouble(c.getColumnIndexOrThrow("longitude")),
                    c.getDouble(c.getColumnIndexOrThrow("latitude")),
                    c.getString(c.getColumnIndexOrThrow("location")),
                    c.getLong(c.getColumnIndexOrThrow("user_id")),
                    c.getInt(c.getColumnIndexOrThrow("type")));
            results.add(locationMarker);
        }

        c.close();
        return results;
    }

    public ArrayList<LocationMarker> getBookmarks(long userId, int type){
        ArrayList<LocationMarker> results = new ArrayList<>();

        String sql = "SELECT * FROM "+MARKER_TABLE + " WHERE user_id = '" + userId + "' AND type = '" + type + "'";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            LocationMarker locationMarker = new LocationMarker(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getString(c.getColumnIndexOrThrow("bldg_name")),
                    c.getDouble(c.getColumnIndexOrThrow("longitude")),
                    c.getDouble(c.getColumnIndexOrThrow("latitude")),
                    c.getString(c.getColumnIndexOrThrow("location")),
                    c.getLong(c.getColumnIndexOrThrow("user_id")),
                    c.getInt(c.getColumnIndexOrThrow("type")));
            results.add(locationMarker);
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
            Veterinarian vet = new Veterinarian(
                    c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("user_id")),
                    user.getLastName(),
                    user.getFirstName(),
                    user.getMobileNumber(),
                    user.getPhoneNumber(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getAge(),
                    user.getUserType(),
                    user.getUserPhoto(),
                    user.getIsDisabled(),
                    c.getString(c.getColumnIndexOrThrow("specialty")),
                    c.getFloat(c.getColumnIndexOrThrow("rating")),
                    c.getString(c.getColumnIndexOrThrow("education")),
                    c.getInt(c.getColumnIndexOrThrow("is_licensed")),
                    c.getString(c.getColumnIndexOrThrow("profile_desc"))
            );
            results.add(vet);
        }

        c.close();
        return results;
    }

    public void setClinics(ArrayList<Facility> clinics){

        int id = 1;
        ArrayList<Facility> clinicList = getClinics();
        ContentValues cv = new ContentValues();
        while(id <= clinicList.size()){
            cv.put("faci_name", clinics.get(id-1).getFaciName());
            cv.put("location", clinics.get(id-1).getLocation());
            cv.put("hours_open", clinics.get(id-1).getHoursOpen());
            cv.put("hours_close", clinics.get(id-1).getHoursClose());
            cv.put("contact_info", clinics.get(id-1).getContactInfo());
            cv.put("rating", clinics.get(id-1).getRating());

            String[] whereArgs = new String[]{String.valueOf(id)};
            petBetterDb.update(FACI_TABLE,cv,"_id=?", whereArgs);
            id+=1;
        }
        //continue here


        petBetterDb.close();
    }

    public ArrayList<Veterinarian> getUnsyncedVets(){
        ArrayList<Veterinarian> results = new ArrayList<>();
        int userId;
        User user;

        String sql = "SELECT * FROM "+VET_TABLE+" WHERE is_synced = 0";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            userId = c.getInt(c.getColumnIndexOrThrow("user_id"));
            user = getUserWithId(userId);
            Veterinarian vet = new Veterinarian(
                    c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("user_id")),
                    user.getLastName(),
                    user.getFirstName(),
                    user.getMobileNumber(),
                    user.getPhoneNumber(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getAge(),
                    user.getUserType(),
                    user.getUserPhoto(),
                    user.getIsDisabled(),
                    c.getString(c.getColumnIndexOrThrow("specialty")),
                    c.getFloat(c.getColumnIndexOrThrow("rating")),
                    c.getString(c.getColumnIndexOrThrow("education")),
                    c.getInt(c.getColumnIndexOrThrow("is_licensed")),
                    c.getString(c.getColumnIndexOrThrow("profile_desc"))
            );
            results.add(vet);
        }

        c.close();
        return results;
    }

    public ArrayList<User> getUnsyncedUsers(){
        ArrayList<User> results = new ArrayList<>();

        String sql = "SELECT * FROM "+USER_TABLE+" WHERE is_synced = 0";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            User user = new User(c.getString(c.getColumnIndexOrThrow("first_name")),
                    c.getString(c.getColumnIndexOrThrow("last_name")),
                    c.getString(c.getColumnIndexOrThrow("email")),
                    c.getString(c.getColumnIndexOrThrow("password")),
                    c.getInt(c.getColumnIndexOrThrow("user_type")),
                    c.getInt(c.getColumnIndexOrThrow("is_disabled")));
            results.add(user);
        }

        c.close();
        return results;
    }


    public ArrayList<Upvote> getUnsyncedUpvotes(){
        ArrayList<Upvote> results = new ArrayList<>();

        String sql = "SELECT * FROM "+UPVOTE_TABLE+" WHERE is_synced = 0";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            Upvote upvote = new Upvote(c.getLong(c.getColumnIndexOrThrow("_id")),
                    c.getInt(c.getColumnIndexOrThrow("feed_id")),
                    c.getInt(c.getColumnIndexOrThrow("user_id")),
                    c.getInt(c.getColumnIndexOrThrow("value")),
                    c.getInt(c.getColumnIndexOrThrow("type")));
            results.add(upvote);
        }

        c.close();
        return results;
    }

    public ArrayList<Post> getUnsyncedPosts(){
        ArrayList<Post> results = new ArrayList<>();
        int userId;
        User user;

        String sql = "SELECT * FROM "+POST_TABLE+" WHERE is_synced = 0";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            Post post= new Post(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("user_id")),
                    c.getString(c.getColumnIndexOrThrow("topic_name")),
                    c.getString(c.getColumnIndexOrThrow("topic_content")),
                    c.getLong(c.getColumnIndexOrThrow("topic_id")),
                    c.getString(c.getColumnIndexOrThrow("date_created")),
                    c.getString(c.getColumnIndexOrThrow("post_photo")),
                    c.getInt(c.getColumnIndexOrThrow("id_link")),
                    c.getInt(c.getColumnIndexOrThrow("id_type")),
                    c.getInt(c.getColumnIndexOrThrow("is_deleted")));
            results.add(post);
        }

        c.close();
        return results;
    }

    public ArrayList<Rating> getUnsyncedRatings(){
        ArrayList<Rating> results = new ArrayList<>();
        int userId;
        User user;

        String sql = "SELECT * FROM "+RATE_TABLE+" WHERE is_synced = 0";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            Rating rating= new Rating(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("rater_id")),
                    c.getLong(c.getColumnIndexOrThrow("rated_id")),
                    c.getFloat(c.getColumnIndexOrThrow("rating")),
                    c.getString(c.getColumnIndexOrThrow("comment")),
                    c.getInt(c.getColumnIndexOrThrow("rating_type")),
                    c.getString(c.getColumnIndexOrThrow("date_created")),
                    c.getInt(c.getColumnIndexOrThrow("is_deleted")));
            results.add(rating);
        }

        c.close();
        return results;
    }

    public ArrayList<Services> getUnsyncedServices(){
        ArrayList<Services> results = new ArrayList<>();
        int userId;
        User user;

        String sql = "SELECT * FROM "+SERVICE_TABLE+" WHERE is_synced = 0";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            Services services= new Services(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("faci_id")),
                    c.getString(c.getColumnIndexOrThrow("service_name")),
                    c.getFloat(c.getColumnIndexOrThrow("service_price")),
                    c.getInt(c.getColumnIndexOrThrow("is_deleted")));
            results.add(services);
        }

        c.close();
        return results;
    }

    public ArrayList<PostRep> getUnsyncedPostReps(){
        ArrayList<PostRep> results = new ArrayList<>();
        int userId;
        User user;

        String sql = "SELECT * FROM "+POST_REP_TABLE+" WHERE is_synced = 0";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            PostRep postrep= new PostRep(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("user_id")),
                    c.getInt(c.getColumnIndexOrThrow("post_id")),
                    c.getInt(c.getColumnIndexOrThrow("parent_id")),
                    c.getString(c.getColumnIndexOrThrow("rep_content")),
                    c.getString(c.getColumnIndexOrThrow("date_performed")),
                    c.getString(c.getColumnIndexOrThrow("postrep_photo")),
                    c.getInt(c.getColumnIndexOrThrow("is_deleted")));
            results.add(postrep);
        }

        c.close();
        return results;
    }

    public ArrayList<Topic> getUnsyncedTopics(){
        ArrayList<Topic> results = new ArrayList<>();
        int userId;
        User user;

        String sql = "SELECT * FROM "+TOPIC_TABLE+" WHERE is_synced = 0";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            Topic topic = new Topic(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("creator_id")),
                    c.getString(c.getColumnIndexOrThrow("topic_name")),
                    c.getString(c.getColumnIndexOrThrow("topic_desc")),
                    c.getString(c.getColumnIndexOrThrow("date_created")),
                    c.getInt(c.getColumnIndexOrThrow("is_deleted")));
            results.add(topic);
        }

        c.close();
        return results;
    }

    public ArrayList<MessageRep> getUnsyncedMessageReps(){
        ArrayList<MessageRep> results = new ArrayList<>();
        int userId;
        User user;

        String sql = "SELECT * FROM "+MESSAGE_REP_TABLE+" WHERE is_synced = 0";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            MessageRep messagerep = new MessageRep(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("user_id")),
                    c.getLong(c.getColumnIndexOrThrow("sender_id")),
                    c.getInt(c.getColumnIndexOrThrow("message_id")),
                    c.getString(c.getColumnIndexOrThrow("rep_content")),
                    c.getInt(c.getColumnIndexOrThrow("is_sent")),
                    c.getString(c.getColumnIndexOrThrow("date_performed")),
                    c.getString(c.getColumnIndexOrThrow("message_photo")));
            results.add(messagerep);
        }

        c.close();
        return results;
    }

    public ArrayList<LocationMarker> getUnsyncedMarkers(){
        ArrayList<LocationMarker> results = new ArrayList<>();
        int userId;
        User user;

        String sql = "SELECT * FROM "+MARKER_TABLE+" WHERE is_synced = 0";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            LocationMarker locationMarker = new LocationMarker(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getString(c.getColumnIndexOrThrow("bldg_name")),
                    c.getDouble(c.getColumnIndexOrThrow("longitude")),
                    c.getDouble(c.getColumnIndexOrThrow("latitude")),
                    c.getString(c.getColumnIndexOrThrow("location")),
                    c.getLong(c.getColumnIndexOrThrow("user_id")),
                    c.getInt(c.getColumnIndexOrThrow("type")),
                    c.getInt(c.getColumnIndexOrThrow("faci_id")));
            results.add(locationMarker);
        }

        c.close();
        return results;
    }

    public ArrayList<Follower> getUnsyncedFollowers(){
        ArrayList<Follower> results = new ArrayList<>();
        int userId;
        User user;

        String sql = "SELECT * FROM "+FOLLOWER_TABLE+" WHERE is_synced = 0";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            Follower follower = new Follower(c.getLong(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("topic_id")),
                    c.getLong(c.getColumnIndexOrThrow("user_id")),
                    c.getInt(c.getColumnIndexOrThrow("is_allowed")));
            results.add(follower);
        }

        c.close();
        return results;
    }

    public ArrayList<Message> getUnsyncedMessages(){
        ArrayList<Message> results = new ArrayList<>();
        int userId;
        User user;

        String sql = "SELECT * FROM "+MESSAGE_TABLE+" WHERE is_synced = 0";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            Message message = new Message(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("user_one")),
                    c.getLong(c.getColumnIndexOrThrow("user_two")),
                    c.getInt(c.getColumnIndexOrThrow("is_allowed")));
            results.add(message);
        }

        c.close();
        return results;
    }

    public ArrayList<Notifications> getUnsyncedNotifications(){
        ArrayList<Notifications> results = new ArrayList<>();
        int userId;
        User user;

        String sql = "SELECT * FROM "+NOTIF_TABLE+" WHERE is_synced = 0";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            Notifications notifs = new Notifications(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("user_id")),
                    c.getLong(c.getColumnIndexOrThrow("doer_id")),
                    c.getInt(c.getColumnIndexOrThrow("is_read")),
                    c.getInt(c.getColumnIndexOrThrow("type")),
                    c.getString(c.getColumnIndexOrThrow("date_performed")),
                    c.getLong(c.getColumnIndexOrThrow("source_id")));
            results.add(notifs);
        }
        c.close();
        return results;
    }

    public ArrayList<Pet> getUnsyncedPets(){
        ArrayList<Pet> results = new ArrayList<>();
        int userId;
        User user;

        String sql = "SELECT * FROM "+PET_TABLE+" WHERE is_synced = 0";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            Pet pet = new Pet(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("user_id")),
                    c.getString(c.getColumnIndexOrThrow("name")),
                    c.getString(c.getColumnIndexOrThrow("classification")),
                    c.getString(c.getColumnIndexOrThrow("breed")),
                    c.getFloat(c.getColumnIndexOrThrow("height")),
                    c.getFloat(c.getColumnIndexOrThrow("weight")));
            results.add(pet);
        }
        c.close();
        return results;
    }

    public ArrayList<Facility> getUnsyncedFacilities(){
        ArrayList<Facility> results = new ArrayList<>();

        String sql = "SELECT * FROM "+FACI_TABLE+" WHERE is_synced = 0";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            Facility facility = new Facility(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getString(c.getColumnIndexOrThrow("faci_name")),
                    c.getString(c.getColumnIndexOrThrow("location")),
                    c.getString(c.getColumnIndexOrThrow("hours_open")),
                    c.getString(c.getColumnIndexOrThrow("hours_close")),
                    c.getString(c.getColumnIndexOrThrow("contact_info")),
                    c.getFloat(c.getColumnIndexOrThrow("rating")),
                    c.getInt(c.getColumnIndexOrThrow("is_disabled")));
            results.add(facility);
        }

        c.close();
        return results;
    }

    public void dataSynced(int n){
        ContentValues cv = new ContentValues();
        //cv.put("specialty","Canine Behavior");
        cv.put("is_synced", 1);
        String[] whereArgs = new String[]{String.valueOf(0)};
        if(n==1){
            petBetterDb.update(VET_TABLE,cv,"is_synced=?", whereArgs);
        }
        if(n==2){
            petBetterDb.update(FACI_TABLE,cv,"is_synced=?", whereArgs);
        }
        if(n==3){
            petBetterDb.update(FOLLOWER_TABLE,cv,"is_synced=?", whereArgs);
        }
        if(n==4){
            petBetterDb.update(MARKER_TABLE,cv,"is_synced=?", whereArgs);
        }
        if(n==5){
            petBetterDb.update(MESSAGE_TABLE,cv,"is_synced=?", whereArgs);
        }
        if(n==6){
            petBetterDb.update(MESSAGE_REP_TABLE,cv,"is_synced=?", whereArgs);
            ContentValues cv2 = new ContentValues();
            cv2.put("is_sent", 1);
            petBetterDb.update(MESSAGE_REP_TABLE,cv2,"is_sent=?", whereArgs);
        }
        if(n==7){
            petBetterDb.update(NOTIF_TABLE,cv,"is_synced=?", whereArgs);
        }
        if(n==8){
            petBetterDb.update(PET_TABLE,cv,"is_synced=?", whereArgs);
        }
        if(n==9){
            petBetterDb.update(POST_TABLE,cv,"is_synced=?", whereArgs);
        }
        if(n==10){
            petBetterDb.update(POST_REP_TABLE,cv,"is_synced=?", whereArgs);
        }
        if(n==11){
            petBetterDb.update(SERVICE_TABLE,cv,"is_synced=?", whereArgs);
        }
        if(n==12){
            petBetterDb.update(USER_TABLE,cv,"is_synced=?", whereArgs);
        }
        if(n==13){
            petBetterDb.update(TOPIC_TABLE,cv,"is_synced=?", whereArgs);
        }
        if(n==14){
            petBetterDb.update(RATE_TABLE,cv,"is_synced=?", whereArgs);
        }
        if(n==15){
            petBetterDb.update(UPVOTE_TABLE,cv,"is_synced=?", whereArgs);
        }
        if(n==16){
            petBetterDb.update(BOOKMARK_TABLE,cv,"is_synced=?", whereArgs);
        }
        if(n==17){
            petBetterDb.update(PENDING_TABLE,cv,"is_synced=?", whereArgs);
        }
        if(n==18){
            petBetterDb.update(FACI_MEMBER_TABLE,cv,"is_synced=?", whereArgs);
        }
        petBetterDb.close();
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
                    c.getFloat(c.getColumnIndexOrThrow("rating")),
                    c.getString(c.getColumnIndexOrThrow("faci_photo")),
                    c.getInt(c.getColumnIndexOrThrow("is_disabled")));
            results.add(facility);
        }

        c.close();
        return results;
    }

    public ArrayList<LocationMarker> getMarkers(){
        ArrayList<LocationMarker> results = new ArrayList<>();
        String temp;

        //String sql = "SELECT * FROM " + FACI_TABLE + " WHERE vet_id = '" + veterinarian.getId() + "'";
        String sql = "SELECT * FROM " + MARKER_TABLE;
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            LocationMarker locationMarker = new LocationMarker(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getString(c.getColumnIndexOrThrow("bldg_name")),
                    c.getDouble(c.getColumnIndexOrThrow("longitude")),
                    c.getDouble(c.getColumnIndexOrThrow("latitude")),
                    c.getString(c.getColumnIndexOrThrow("location")),
                    c.getLong(c.getColumnIndexOrThrow("user_id")),
                    c.getInt(c.getColumnIndexOrThrow("type")));
            results.add(locationMarker);
        }

        c.close();
        return results;
    }

    public ArrayList<Follower> getFollowers(){
        ArrayList<Follower> results = new ArrayList<>();
        String temp;

        //String sql = "SELECT * FROM " + FACI_TABLE + " WHERE vet_id = '" + veterinarian.getId() + "'";
        String sql = "SELECT * FROM " + FOLLOWER_TABLE;
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            Follower follower = new Follower(c.getLong(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("topic_id")),
                    c.getLong(c.getColumnIndexOrThrow("user_id")),
                    c.getInt(c.getColumnIndexOrThrow("is_allowed")));
            results.add(follower);
        }

        c.close();
        return results;
    }

    public ArrayList<User> getUsers(){
        ArrayList<User> results = new ArrayList<>();
        String temp;

        //String sql = "SELECT * FROM " + FACI_TABLE + " WHERE vet_id = '" + veterinarian.getId() + "'";
        String sql = "SELECT * FROM " + USER_TABLE;
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            User user = new User(c.getLong(c.getColumnIndexOrThrow("_id")),
                    c.getString(c.getColumnIndexOrThrow("first_name")),
                    c.getString(c.getColumnIndexOrThrow("last_name")),
                    c.getString(c.getColumnIndexOrThrow("mobile_num")),
                    c.getString(c.getColumnIndexOrThrow("phone_num")),
                    c.getString(c.getColumnIndexOrThrow("email")),
                    c.getString(c.getColumnIndexOrThrow("password")),
                    c.getInt(c.getColumnIndexOrThrow("age")),
                    c.getInt(c.getColumnIndexOrThrow("user_type")),
                    c.getString(c.getColumnIndexOrThrow("user_photo")),
                    c.getInt(c.getColumnIndexOrThrow("is_disabled")));
            results.add(user);
        }

        c.close();
        return results;
    }

    public ArrayList<Follower> getPendingFollowers(long topicId){
        ArrayList<Follower> results = new ArrayList<>();
        String temp;

        String sql = "SELECT * FROM " + FOLLOWER_TABLE + " WHERE topic_id = '" + topicId + "' AND is_allowed = 0";
        //String sql = "SELECT * FROM " + FOLLOWER_TABLE;
        Cursor c = petBetterDb.rawQuery(sql, null);

        try{
            while(c.moveToNext()) {
                Follower follower = new Follower(c.getLong(c.getColumnIndexOrThrow("_id")),
                        c.getLong(c.getColumnIndexOrThrow("topic_id")),
                        c.getLong(c.getColumnIndexOrThrow("user_id")),
                        c.getInt(c.getColumnIndexOrThrow("is_allowed")));
                results.add(follower);
            }

            c.close();
            return results;
        }catch(CursorIndexOutOfBoundsException e){
            return null;
        }

    }

    public ArrayList<Pet> getPets(long userId){
        ArrayList<Pet> results = new ArrayList<>();
        String temp;

        String sql = "SELECT * FROM " + PET_TABLE + " WHERE user_id = '" + userId + "'";
        //String sql = "SELECT * FROM " + FOLLOWER_TABLE;
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            Pet pet = new Pet(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("user_id")),
                    c.getString(c.getColumnIndexOrThrow("name")),
                    c.getString(c.getColumnIndexOrThrow("classification")),
                    c.getString(c.getColumnIndexOrThrow("breed")),
                    c.getFloat(c.getColumnIndexOrThrow("height")),
                    c.getFloat(c.getColumnIndexOrThrow("weight")));
            results.add(pet);
        }

        c.close();
        return results;
    }

    public ArrayList<Services> getServices(){
        ArrayList<Services> results = new ArrayList<>();
        String temp;

        //String sql = "SELECT * FROM " + FACI_TABLE + " WHERE vet_id = '" + veterinarian.getId() + "'";
        String sql = "SELECT * FROM " + SERVICE_TABLE;
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            Services services = new Services(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("faci_id")),
                    c.getString(c.getColumnIndexOrThrow("service_name")),
                    c.getFloat(c.getColumnIndexOrThrow("service_price")),
                    c.getInt(c.getColumnIndexOrThrow("is_deleted")));
            results.add(services);
        }

        c.close();
        return results;
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
        cv.put("is_synced", 0);


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

    public ArrayList<Integer> getPostRepIds () {

        ArrayList<Integer> ids = new ArrayList<>();

        String sql = "SELECT _id FROM "+POST_REP_TABLE;
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            ids.add(c.getInt(c.getColumnIndexOrThrow("_id")));
        }

        c.close();
        return ids;
    }

    public ArrayList<Integer> getMessageRepIds () {

        ArrayList<Integer> ids = new ArrayList<>();

        String sql = "SELECT _id FROM "+MESSAGE_REP_TABLE;
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            ids.add(c.getInt(c.getColumnIndexOrThrow("_id")));
        }

        c.close();
        return ids;
    }

    public long addFacility(int faci_id, String faci_name, String location, String hours_open, String hours_close,
                            String contact_info, String faciPhoto){
        long result;

        ContentValues cv = new ContentValues();
        cv.put("_id", faci_id);
        cv.put("faci_name", faci_name);
        cv.put("location", location);
        cv.put("hours_open", hours_open);
        cv.put("hours_close", hours_close);
        cv.put("contact_info", contact_info);
        cv.put("rating", 0);
        cv.put("faci_photo", faciPhoto);
        cv.put("is_disabled", 0);
        cv.put("is_synced", 0);

        result = petBetterDb.insert(FACI_TABLE, null, cv);

        return result;
    }

    public long addFacilityMember(int id, long faciId, long vetId){
        long result;

        ContentValues cv = new ContentValues();
        cv.put("_id", id);
        cv.put("faci_id", faciId);
        cv.put("vet_id", vetId);
        cv.put("is_synced", 0);

        result = petBetterDb.insert(FACI_MEMBER_TABLE, null, cv);

        return result;
    }

    public long addMarker(int rowId, String bldgName, double latitude, double longitude, String location, long user_id, int type, long faciId){
        long result;

        ContentValues cv = new ContentValues();
        cv.put("_id", rowId);
        cv.put("bldg_name", bldgName);
        cv.put("latitude", latitude);
        cv.put("longitude", longitude);
        cv.put("location", location);
        cv.put("user_id", user_id);
        cv.put("type", type);
        cv.put("is_synced", 0);
        cv.put("faci_id", faciId);


        result = petBetterDb.insert(MARKER_TABLE, null, cv);


        return result;
    }


    public long addPending(int id, int foreignId, int type, int isApproved){
        long result;

        ContentValues cv = new ContentValues();
        cv.put("_id", id);
        cv.put("foreign_id", foreignId);
        cv.put("type", type);
        cv.put("is_approved", isApproved);
        cv.put("is_synced", 0);

        result = petBetterDb.insert(PENDING_TABLE, null, cv);

        return result;
    }

    public long addPostRep(int postRepId, int userId, int postId, int parentId, String repContent, String datePerformed){
        long result;

        ContentValues cv = new ContentValues();
        cv.put("_id", postRepId);
        cv.put("user_id", userId);
        cv.put("post_id", postId);
        cv.put("parent_id", parentId);
        cv.put("rep_content", repContent);
        cv.put("date_performed", datePerformed);
        cv.put("is_deleted", 0);
        cv.put("is_synced", 0);

        result = petBetterDb.insert(POST_REP_TABLE, null, cv);

        return result;
    }

    public Post getPost(long postId){

        String sql = "SELECT * FROM " + POST_TABLE + " WHERE _id = '" + postId + "' AND is_deleted != 1";

        Cursor c = petBetterDb.rawQuery(sql, null);

        Log.e("cursor", c.getCount() + "");

        c.moveToFirst();


        Post result = new Post(c.getInt(c.getColumnIndexOrThrow("_id")),
                c.getLong(c.getColumnIndexOrThrow("user_id")),
                c.getString(c.getColumnIndexOrThrow("topic_name")),
                c.getString(c.getColumnIndexOrThrow("topic_content")),
                c.getLong(c.getColumnIndexOrThrow("topic_id")));

        c.close();
        return result;

    }

    public ArrayList<PostRep> getPostReps(long postId){
        ArrayList<PostRep> results = new ArrayList<>();

        String sql = "SELECT pr._id AS _id, pr.user_id AS user_id, pr.post_id AS post_id, pr.parent_id AS parent_id, " +
                "pr.rep_content AS rep_content, pr.date_performed as date_performed, " +
                "pr.is_deleted AS is_deleted, u.first_name AS first_name, u.last_name AS last_name, pr.postrep_photo AS postrep_photo " +
                "FROM postreps AS pr INNER JOIN users AS u ON pr.user_id = u._id WHERE pr.post_id = '" + postId + "' AND " +
                "pr.is_deleted != 1 AND pr.parent_id = 0";

        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            PostRep postrep = new PostRep(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("user_id")),
                    c.getInt(c.getColumnIndexOrThrow("post_id")),
                    c.getInt(c.getColumnIndexOrThrow("parent_id")),
                    c.getString(c.getColumnIndexOrThrow("rep_content")),
                    c.getString(c.getColumnIndexOrThrow("date_performed")),
                    c.getInt(c.getColumnIndexOrThrow("is_deleted")),
                    c.getString(c.getColumnIndexOrThrow("first_name")),
                    c.getString(c.getColumnIndexOrThrow("last_name")),
                    c.getString(c.getColumnIndexOrThrow("postrep_photo")));
            results.add(postrep);
        }

        c.close();
        return results;
    }

    public long addMessageRep(int messageRepId, int userId, int senderId, int messageId, String repContent, int isSent, String datePerformed,
                              String image, int isSynced){
        long result;

        ContentValues cv = new ContentValues();
        cv.put("_id", messageRepId);
        cv.put("user_id", userId);
        cv.put("sender_id", senderId);
        cv.put("message_id", messageId);
        cv.put("rep_content", repContent);
        cv.put("is_sent", isSent);
        cv.put("date_performed", datePerformed);
        cv.put("message_photo", image);
        cv.put("is_synced", isSynced);

        result = petBetterDb.insert(MESSAGE_REP_TABLE, null, cv);

        return result;
    }

    public long addVote(long id, int feedId, int userId, int value, int type, int isSynced){
        long result;

        ContentValues cv = new ContentValues();
        cv.put("_id", id);
        cv.put("feed_id", feedId);
        cv.put("user_id", userId);
        cv.put("value", value);
        cv.put("type", type);
        cv.put("is_synced", isSynced);

        result = petBetterDb.insert(UPVOTE_TABLE, null, cv);

        return result;
    }

    public ArrayList<Notifications> getNotifications(long userId){
        ArrayList<Notifications> results = new ArrayList<>();

        String sql = "SELECT n._id AS _id, n.user_id AS user_id, n.doer_id AS doer_id, n.is_read AS is_read, " +
                "n.type AS type, n.date_performed as date_performed, n.source_id as source_id, u.first_name AS first_name, u.last_name AS last_name " +
                "FROM notifications AS n INNER JOIN users AS u ON n.doer_id = u._id WHERE n.user_id = '" + userId + "'";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            Notifications notifs = new Notifications(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("user_id")),
                    c.getLong(c.getColumnIndexOrThrow("doer_id")),
                    c.getInt(c.getColumnIndexOrThrow("is_read")),
                    c.getInt(c.getColumnIndexOrThrow("type")),
                    c.getString(c.getColumnIndexOrThrow("date_performed")),
                    c.getLong(c.getColumnIndexOrThrow("source_id")),
                    c.getString(c.getColumnIndexOrThrow("first_name")),
                    c.getString(c.getColumnIndexOrThrow("last_name")));
            results.add(notifs);
        }

        c.close();
        return results;
    }


    public ArrayList<Topic> getTopics(){
        ArrayList<Topic> results = new ArrayList<>();

        String sql = "SELECT t._id AS _id, t.creator_id AS creator_id, t.topic_name AS topic_name, " +
                "t.topic_desc AS topic_desc, t.date_created AS date_created, t.is_deleted AS is_deleted, " +
                "u.first_name AS first_name, u.last_name AS last_name FROM topics AS t LEFT JOIN users as u " +
                "ON t.creator_id = u._id WHERE t.is_deleted != 1";

        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            Topic topic = new Topic(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("creator_id")),
                    c.getString(c.getColumnIndexOrThrow("topic_name")),
                    c.getString(c.getColumnIndexOrThrow("topic_desc")),
                    c.getString(c.getColumnIndexOrThrow("date_created")),
                    c.getInt(c.getColumnIndexOrThrow("is_deleted")),
                    c.getString(c.getColumnIndexOrThrow("first_name")),
                    c.getString(c.getColumnIndexOrThrow("last_name")));
            results.add(topic);
        }

        c.close();
        return results;
    }

    public long createMessage(int messageId, long userId, long toId){
        long result;

        ContentValues cv = new ContentValues();
        cv.put("_id", messageId);
        cv.put("user_one", userId);
        cv.put("user_two", toId);
        cv.put("is_allowed", 0);
        cv.put("is_synced", 0);

        result = petBetterDb.insert(MESSAGE_TABLE, null, cv);

        return result;
    }

    public ArrayList<Integer> getMessageIds () {

        ArrayList<Integer> ids = new ArrayList<>();

        String sql = "SELECT _id FROM "+MESSAGE_TABLE;

        Cursor c = petBetterDb.rawQuery(sql, null);
        while(c.moveToNext()) {
            ids.add(c.getInt(c.getColumnIndexOrThrow("_id")));
        }

        c.close();
        return ids;
    }

    public ArrayList<Integer> generateMessageRepIds () {

        ArrayList<Integer> ids = new ArrayList<>();

        String sql = "SELECT _id FROM "+MESSAGE_REP_TABLE;
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            ids.add(c.getInt(c.getColumnIndexOrThrow("_id")));
        }

        c.close();
        return ids;
    }

    public ArrayList<Integer> generateMembershipIds () {

        ArrayList<Integer> ids = new ArrayList<>();

        String sql = "SELECT _id FROM "+FACI_MEMBER_TABLE;
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            ids.add(c.getInt(c.getColumnIndexOrThrow("_id")));
        }

        c.close();
        return ids;
    }

    public ArrayList<Integer> generatePendingIds () {

        ArrayList<Integer> ids = new ArrayList<>();

        String sql = "SELECT _id FROM "+PENDING_TABLE;
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            ids.add(c.getInt(c.getColumnIndexOrThrow("_id")));
        }

        c.close();
        return ids;
    }

    public ArrayList<Integer> getNotifIds () {

        ArrayList<Integer> ids = new ArrayList<>();

        String sql = "SELECT _id FROM "+NOTIF_TABLE;
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            ids.add(c.getInt(c.getColumnIndexOrThrow("_id")));
        }

        c.close();
        return ids;
    }

    public ArrayList<Integer> generateLocationMarkerIds () {

        ArrayList<Integer> ids = new ArrayList<>();

        String sql = "SELECT _id FROM "+MARKER_TABLE;
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            ids.add(c.getInt(c.getColumnIndexOrThrow("_id")));
        }

        c.close();
        return ids;
    }

    public long notifyUser(int notifId, long toId, long userId, int isRead, int type, String timeStamp, int sourceId, int isSynced){
        long result;

        ContentValues cv = new ContentValues();
        cv.put("_id", notifId);
        cv.put("user_id", toId);
        cv.put("doer_id", userId);
        cv.put("is_read", isRead);
        cv.put("type", type);
        cv.put("date_performed", timeStamp);
        cv.put("source_id", sourceId);
        cv.put("is_synced", isSynced);

        result = petBetterDb.insert(NOTIF_TABLE, null, cv);

        return result;
    }

    public ArrayList<Integer> getTopicIds () {

        ArrayList<Integer> ids = new ArrayList<>();

        String sql = "SELECT _id FROM "+TOPIC_TABLE;
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            ids.add(c.getInt(c.getColumnIndexOrThrow("_id")));
        }

        c.close();
        return ids;
    }

    public long createTopic(int topicId, long userId, String topicTitle, String topicDesc, String timeStamp, int isDeleted,
                            int isSynced){
        long result;

        ContentValues cv = new ContentValues();
        cv.put("_id", topicId);
        cv.put("creator_id", userId);
        cv.put("topic_name", topicTitle);
        cv.put("topic_desc", topicDesc);
        cv.put("date_created", timeStamp);
        cv.put("is_deleted", isDeleted);
        cv.put("is_synced", isSynced);

        result = petBetterDb.insert(TOPIC_TABLE, null, cv);

        return result;
    }

    public ArrayList<Post> getTopicPosts(long topicId){

        ArrayList<Post> results = new ArrayList<>();


        String sql = "SELECT p._id AS _id, p.user_id, p.topic_name AS topic_name, p.topic_content AS topic_content, " +
                "p.date_created AS date_created, u.first_name AS first_name, u.last_name AS last_name, p.is_deleted AS is_deleted, " +
                "p.post_photo AS post_photo, p.id_link AS id_link, p.id_type AS id_type FROM posts AS p INNER JOIN users u ON p.user_id = u._id INNER JOIN topics t " +
                "ON p.topic_id = t._id WHERE p.topic_id = '" + topicId + "' AND p.is_deleted != 1";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            System.out.println("DATA ADAPTER "+ c.getInt(c.getColumnIndexOrThrow("_id")));
            Post post= new Post(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("user_id")),
                    c.getString(c.getColumnIndexOrThrow("topic_name")),
                    c.getString(c.getColumnIndexOrThrow("topic_content")),
                    c.getString(c.getColumnIndexOrThrow("date_created")),
                    c.getString(c.getColumnIndexOrThrow("first_name")),
                    c.getString(c.getColumnIndexOrThrow("last_name")),
                    c.getString(c.getColumnIndexOrThrow("post_photo")),
                    c.getInt(c.getColumnIndexOrThrow("id_link")),
                    c.getInt(c.getColumnIndexOrThrow("id_type")),
                    c.getInt(c.getColumnIndexOrThrow("is_deleted")));
            results.add(post);
        }

        c.close();
        return results;
    }

    public ArrayList<Integer> getPostIds () {

        ArrayList<Integer> ids = new ArrayList<>();

        String sql = "SELECT _id FROM "+POST_TABLE;
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            ids.add(c.getInt(c.getColumnIndexOrThrow("_id")));
        }

        c.close();
        return ids;
    }

    public long createPost(int pId, long userId, String postTitle, String postDesc, long topicId, String timeStamp,
                           String postPhoto, int idLink, int idType, int isDeleted, int isSynced){
        long result;

        ContentValues cv = new ContentValues();
        cv.put("_id", pId);
        cv.put("user_id", userId);
        cv.put("topic_name", postTitle);
        cv.put("topic_content", postDesc);
        cv.put("topic_id", topicId);
        cv.put("date_created", timeStamp);
        cv.put("post_photo", postPhoto);
        cv.put("id_link", idLink);
        cv.put("id_type", idType);
        cv.put("is_deleted", isDeleted);
        cv.put("is_synced", isSynced);

        result = petBetterDb.insert(POST_TABLE, null, cv);

        return result;
    }

    public ArrayList<Post> getPostsWithUserId(long userId){

        ArrayList<Post> results = new ArrayList<>();


        String sql = "SELECT p._id AS _id, p.user_id, p.topic_name AS topic_name, p.topic_content AS topic_content, " +
                "p.date_created AS date_created, u.first_name AS first_name, u.last_name AS last_name, p.post_photo AS post_photo, " +
                "p.id_link AS id_link, p.id_type AS id_type, p.is_deleted AS is_deleted FROM posts AS p INNER JOIN users u ON p.user_id = u._id INNER JOIN topics t " +
                "ON p.topic_id = t._id WHERE p.user_id = '" + userId + "' AND p.is_deleted != 1";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            System.out.println("DATA ADAPTER "+ c.getInt(c.getColumnIndexOrThrow("_id")));
            Post post= new Post(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("user_id")),
                    c.getString(c.getColumnIndexOrThrow("topic_name")),
                    c.getString(c.getColumnIndexOrThrow("topic_content")),
                    c.getString(c.getColumnIndexOrThrow("date_created")),
                    c.getString(c.getColumnIndexOrThrow("first_name")),
                    c.getString(c.getColumnIndexOrThrow("last_name")),
                    c.getString(c.getColumnIndexOrThrow("post_photo")),
                    c.getInt(c.getColumnIndexOrThrow("id_link")),
                    c.getInt(c.getColumnIndexOrThrow("id_type")),
                    c.getInt(c.getColumnIndexOrThrow("is_deleted")));
            results.add(post);
        }

        c.close();
        return results;
    }

    public ArrayList<Topic> getTopicsWithUserId(long userId){

        ArrayList<Topic> results = new ArrayList<>();


        String sql = "SELECT t._id AS _id, t.creator_id AS creator_id, t.topic_name AS topic_name, t.topic_desc AS topic_desc, " +
                "t.date_created AS date_created, u.first_name AS first_name, u.last_name AS last_name, t.is_deleted AS is_deleted " +
                " FROM topics AS t INNER JOIN users u ON t.creator_id = u._id " +
                "WHERE t.creator_id = '" + userId + "' AND t.is_deleted != 1";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            System.out.println("DATA ADAPTER "+ c.getInt(c.getColumnIndexOrThrow("_id")));
            Topic topic= new Topic(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("creator_id")),
                    c.getString(c.getColumnIndexOrThrow("topic_name")),
                    c.getString(c.getColumnIndexOrThrow("topic_desc")),
                    c.getString(c.getColumnIndexOrThrow("date_created")),
                    c.getInt(c.getColumnIndexOrThrow("is_deleted")),
                    c.getString(c.getColumnIndexOrThrow("first_name")),
                    c.getString(c.getColumnIndexOrThrow("last_name")));
            results.add(topic);
        }

        c.close();
        return results;
    }

    public long createVetRating(int pId,long userId, long vet_id, float rating, String comment, String timeStamp, int isDeleted){
        long result;

        ContentValues cv = new ContentValues();
        cv.put("_id", pId);
        cv.put("rater_id", userId);
        cv.put("rated_id", vet_id);
        cv.put("rating", rating);
        cv.put("rating_type", 1);
        cv.put("comment", comment);
        cv.put("date_created", timeStamp);
        cv.put("is_deleted", isDeleted);
        cv.put("is_synced", 0);

        result = petBetterDb.insert(RATE_TABLE, null, cv);

        System.out.println("did we go here");
        return result;
    }

    public long createFacilityRating(int pId,long userId, long facility_id, float rating, String comment, String timeStamp, int isDeleted){
        long result;

        ContentValues cv = new ContentValues();
        cv.put("_id", pId);
        cv.put("rater_id", userId);
        cv.put("rated_id", facility_id);
        cv.put("rating", rating);
        cv.put("rating_type", 2);
        cv.put("comment", comment);
        cv.put("date_created", timeStamp);
        cv.put("is_deleted", isDeleted);
        cv.put("is_synced", 0);

        result = petBetterDb.insert(RATE_TABLE, null, cv);

        System.out.println("did we go here");
        return result;
    }

    public ArrayList<Integer> getRatingIds() {

        ArrayList<Integer> ids = new ArrayList<>();

        String sql = "SELECT _id FROM ratings";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            ids.add(c.getInt(c.getColumnIndexOrThrow("_id")));
        }

        c.close();
        return ids;
    }/*
    public ArrayList<Integer> getFacilityRatingIds() {

        ArrayList<Integer> ids = new ArrayList<>();

        String sql = "SELECT _id FROM ratings WHERE rated_id = 2";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            ids.add(c.getInt(c.getColumnIndexOrThrow("_id")));
        }

        c.close();
        return ids;
    }*/


    //rewrite this
    public ArrayList<Facility> getFacilitiesByVetId(long vetId){
        ArrayList<Facility> results = new ArrayList<>();

        String sql = "SELECT f._id AS _id, f.faci_name AS faci_name, f.location AS location, f.hours_open AS hours_open, " +
                "f.hours_close AS hour_close, f.contact_info AS contact_info, f.rating AS rating, f.faci_photo AS faci_photo, " +
                "f.is_disabled AS is_disabled FROM facilities AS f INNER JOIN facility_membership AS fm ON f._id = fm.faci_id"+
                " INNER JOIN veterinarians AS v ON fm.vet_id = v._id WHERE v._id = '"+vetId+"'";
        //String sql = "SELECT * FROM " + FACI_TABLE;
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            Facility facility = new Facility(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getString(c.getColumnIndexOrThrow("faci_name")),
                    c.getString(c.getColumnIndexOrThrow("location")),
                    c.getString(c.getColumnIndexOrThrow("hours_open")),
                    c.getString(c.getColumnIndexOrThrow("hours_close")),
                    c.getString(c.getColumnIndexOrThrow("contact_info")),
                    c.getFloat(c.getColumnIndexOrThrow("rating")),
                    c.getString(c.getColumnIndexOrThrow("faci_photo")),
                    c.getInt(c.getColumnIndexOrThrow("is_disabled")));
            results.add(facility);
        }

        c.close();
        return results;
    }

    public ArrayList<Float> getVeterinarianRatings(long vet_id){

        ArrayList<Float> ratings = new ArrayList<>();

        String sql = "SELECT rating FROM ratings WHERE rating_type = 1 AND rated_id = "+vet_id;

        try{
            Cursor c = petBetterDb.rawQuery(sql,null);

            System.out.println("rate received");
            while (c.moveToNext()){
                ratings.add(c.getFloat(c.getColumnIndexOrThrow("rating")));
            }

            c.close();

            return ratings;
        }catch(CursorIndexOutOfBoundsException cpe){
            return null;
        }

    }
    public ArrayList<Float> getFacilityRatings(long facility_id){

        ArrayList<Float> ratings = new ArrayList<>();

        try{
            String sql = "SELECT rating FROM ratings WHERE rating_type = 2 AND rated_id = "+ facility_id;
            Cursor c = petBetterDb.rawQuery(sql,null);

            while (c.moveToNext()){
                ratings.add(c.getFloat(c.getColumnIndexOrThrow("rating")));
            }

            c.close();

            return ratings;
        }catch(CursorIndexOutOfBoundsException cpe){
            return null;
        }


    }

    public void setNewVetRating(float newRating, long vet_id){
        ContentValues cv = new ContentValues();
        //cv.put("specialty","Canine Behavior");
        cv.put("rating", newRating);

        System.out.println("Newrating in SetNewVet: "+newRating);
        String[] whereArgs = new String[]{String.valueOf(vet_id)};
        petBetterDb.update(VET_TABLE,cv,"_id=?", whereArgs);
        petBetterDb.close();
    }

    public void notifRead(long id){
        ContentValues cv = new ContentValues();
        //cv.put("specialty","Canine Behavior");
        cv.put("is_read", 1);
        cv.put("is_synced", 0);

        String[] whereArgs = new String[]{String.valueOf(id)};
        petBetterDb.update(NOTIF_TABLE,cv,"_id=?", whereArgs);
        petBetterDb.close();
    }

    public void setNewFacilityRating(float newRating, long facility_id){
        ContentValues cv = new ContentValues();
        //cv.put("specialty","Canine Behavior");
        cv.put("rating", newRating);

        System.out.println("Newrating in SetNewVet: "+newRating);
        String[] whereArgs = new String[]{String.valueOf(facility_id)};
        petBetterDb.update(FACI_TABLE,cv,"_id=?", whereArgs);
        petBetterDb.close();
    }


    public ArrayList<Integer> getFollowerIds () {

        ArrayList<Integer> ids = new ArrayList<>();

        String sql = "SELECT _id FROM "+FOLLOWER_TABLE;
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            ids.add(c.getInt(c.getColumnIndexOrThrow("_id")));
        }

        c.close();
        return ids;
    }

    public long addFollower(int followerId, int topicId, int userId, int isAllowed, int isSynced){
        long result;

        ContentValues cv = new ContentValues();
        cv.put("_id", followerId);
        cv.put("topic_id", topicId);
        cv.put("user_id", userId);
        cv.put("is_allowed", isAllowed);
        cv.put("is_synced", isSynced);

        result = petBetterDb.insert(FOLLOWER_TABLE, null, cv);

        return result;
    }

    public int getFollowerCount (int topicId) {

        int result=0;

        String sql = "SELECT _id FROM "+FOLLOWER_TABLE+" WHERE topic_id = '"+topicId+"'";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            result++;
        }

        c.close();
        return result;
    }

    public ArrayList<Upvote> getUpvotes(int feedId, int type){
        ArrayList<Upvote> results = new ArrayList<>();

        String sql = "SELECT * FROM " + UPVOTE_TABLE + " WHERE feed_id = '"+feedId+"' AND type = '"+type+"'";

        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {

            Upvote upvote = new Upvote(c.getLong(c.getColumnIndexOrThrow("_id")),
                    c.getInt(c.getColumnIndexOrThrow("feed_id")),
                    c.getInt(c.getColumnIndexOrThrow("user_id")),
                    c.getInt(c.getColumnIndexOrThrow("value")),
                    c.getInt(c.getColumnIndexOrThrow("type")),
                    c.getInt(c.getColumnIndexOrThrow("is_synced")));
            results.add(upvote);
            System.out.println("How many times boi");
        }

        c.close();
        return results;
    }

    public int getVoteCount (int feedId, int type) {

        int result= 0;
        ArrayList<Upvote> voteList = getUpvotes(feedId, type);

        for(Upvote thisVote : voteList){
            if(thisVote.getValue()==1){
                result++;
            }
            else{
                result--;
            }
        }

        return result;
    }

    public Upvote getUserVote (int feedId, int userId, int type) {


        ArrayList<Integer> ids = new ArrayList<>();

        String sql = "SELECT * FROM "+UPVOTE_TABLE+" WHERE user_id = '"+userId+"' AND type = '"+ type +"' AND feed_id = '"
                +feedId+"'";
        Upvote result = null;

        try{
            Cursor c = petBetterDb.rawQuery(sql, null);

            c.moveToFirst();
            result = new Upvote(c.getLong(c.getColumnIndexOrThrow("_id")),
                    c.getInt(c.getColumnIndexOrThrow("feed_id")),
                    c.getInt(c.getColumnIndexOrThrow("user_id")),
                    c.getInt(c.getColumnIndexOrThrow("value")),
                    c.getInt(c.getColumnIndexOrThrow("type")),
                    c.getInt(c.getColumnIndexOrThrow("is_synced")));

            c.close();
            return result;
        }catch(CursorIndexOutOfBoundsException c){
            return null;
        }


    }

    public boolean checkIfFollower (int topicId, int userId) {

        ArrayList<Integer> ids = new ArrayList<>();

        String sql = "SELECT _id FROM "+FOLLOWER_TABLE+" WHERE user_id = '"+userId+"' AND topic_id = '"+ topicId +"'";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            ids.add(c.getInt(c.getColumnIndexOrThrow("_id")));
        }

        c.close();
        if(ids.size()>0)
            return true;
        else
            return false;
    }

    public boolean checkIfBookMark (int item_id, int user_id, int bookmark_type) {

        ArrayList<Integer> ids = new ArrayList<>();

        String sql = "SELECT _id FROM "+BOOKMARK_TABLE+" WHERE user_id = '"+ user_id +"' AND bookmark_type = '"+ bookmark_type +"' AND item_id = '"+ item_id +"'";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            ids.add(c.getInt(c.getColumnIndexOrThrow("_id")));
        }

        c.close();
        if(ids.size()>0)
            return true;
        else
            return false;
    }

    public boolean checkIfRated (long rater_id, long rated_id, int rating_type) {

        ArrayList<Integer> ids = new ArrayList<>();

        String sql = "SELECT _id FROM "+RATE_TABLE+" WHERE rater_id = '"+ rater_id +"' AND rated_id = '"+ rated_id +"' AND rating_type = '"+ rating_type +"'";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            ids.add(c.getInt(c.getColumnIndexOrThrow("_id")));
        }

        c.close();
        if(ids.size()>0)
            return true;
        else
            return false;
    }

    public void deleteFollower (int topicId, int userId) {
        petBetterDb.delete(FOLLOWER_TABLE, "user_id = " + userId + " AND topic_id = "+ topicId, null);
    }

    public void deleteMessage (long id) {
        petBetterDb.delete(MESSAGE_TABLE, "_id = " + id, null);
    }

    public ArrayList<Follower> getTopicFollowers(long topicId){

        ArrayList<Follower> results = new ArrayList<>();


        String sql = "SELECT * FROM followers WHERE topic_id = '" + topicId + "'";
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            Follower follower = new Follower(c.getLong(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("topic_id")),
                    c.getLong(c.getColumnIndexOrThrow("user_id")),
                    c.getInt(c.getColumnIndexOrThrow("is_allowed")));
            results.add(follower);
        }

        c.close();
        return results;
    }

    public Message getMessage(long messageId){

        String sql = "SELECT m._id AS _id, m.user_one AS user_one, m.user_two AS user_two, m.is_allowed AS is_allowed, " +
                "u.first_name AS first_name, u.last_name AS last_name FROM messages AS m INNER JOIN users AS u " +
                "ON m.user_one = u._id WHERE m._id = '" + messageId + "'";

        //String sql = "SELECT * FROM " + MESSAGE_TABLE + " WHERE _id = '" + messageId + "'";
        Cursor c = petBetterDb.rawQuery(sql, null);

        Log.e("cursor", c.getCount() + "");

        c.moveToFirst();

        Message result = new Message(c.getInt(c.getColumnIndexOrThrow("_id")),
                c.getLong(c.getColumnIndexOrThrow("user_one")),
                c.getLong(c.getColumnIndexOrThrow("user_two")),
                c.getInt(c.getColumnIndexOrThrow("is_allowed")),
                c.getString(c.getColumnIndexOrThrow("first_name")),
                c.getString(c.getColumnIndexOrThrow("last_name")));
        result.setMessageContent(getLatestRep((int) result.getId()));

        c.close();
        return result;

    }

    public Topic getTopic(long topicId){

        String sql = "SELECT t._id AS _id, t.creator_id AS creator_id, t.topic_name AS topic_name, " +
                "t.topic_desc AS topic_desc, t.date_created AS date_created, t.is_deleted AS is_deleted, " +
                "u.first_name AS first_name, u.last_name AS last_name FROM topics AS t LEFT JOIN users as u " +
                "ON t.creator_id = u._id WHERE t._id= '"+topicId+"' AND t.is_deleted != 1";

        Cursor c = petBetterDb.rawQuery(sql, null);

        Log.e("cursor", c.getCount() + "");
        c.moveToFirst();

        Topic result = new Topic(c.getInt(c.getColumnIndexOrThrow("_id")),
                c.getLong(c.getColumnIndexOrThrow("creator_id")),
                c.getString(c.getColumnIndexOrThrow("topic_name")),
                c.getString(c.getColumnIndexOrThrow("topic_desc")),
                c.getString(c.getColumnIndexOrThrow("date_created")),
                c.getInt(c.getColumnIndexOrThrow("is_deleted")),
                c.getString(c.getColumnIndexOrThrow("first_name")),
                c.getString(c.getColumnIndexOrThrow("last_name")));



        c.close();
        return result;
    }

    public long deletePost(long postId){
        ContentValues cv = new ContentValues();
        cv.put("is_deleted",1);
        cv.put("is_synced", 1);

        String[] whereArgs = new String[]{String.valueOf(postId)};
        long result = petBetterDb.update(POST_TABLE,cv,"_id=?", whereArgs);
        petBetterDb.close();

        return result;
    }

    public long deletePostRep(long postRepId){
        ContentValues cv = new ContentValues();
        cv.put("is_deleted",1);

        String[] whereArgs = new String[]{String.valueOf(postRepId)};
        long result = petBetterDb.update(POST_REP_TABLE,cv,"_id=?", whereArgs);
        petBetterDb.close();

        return result;
    }

    public long deleteTopic(long topicId){
        ContentValues cv = new ContentValues();
        cv.put("is_deleted",1);
        cv.put("is_synced", 0);

        String[] whereArgs = new String[]{String.valueOf(topicId)};
        long result = petBetterDb.update(TOPIC_TABLE,cv,"_id=?", whereArgs);
        petBetterDb.close();

        return result;
    }

    public long deleteNotification(long topicId){
        ContentValues cv = new ContentValues();
        cv.put("is_deleted",1);
        cv.put("is_synced", 0);

        String[] whereArgs = new String[]{String.valueOf(topicId)};
        long result = petBetterDb.update(TOPIC_TABLE,cv,"_id=?", whereArgs);
        petBetterDb.close();

        return result;
    }

    public void approveRequest(long _id){


        ContentValues cv = new ContentValues();
        cv.put("is_allowed",1);
        cv.put("is_synced",0);

        String[] whereArgs= new String[]{String.valueOf(_id)};
        petBetterDb.update(FOLLOWER_TABLE,cv,"_id=?", whereArgs);
        petBetterDb.close();


    }

    public void approveMessage(long _id){


        ContentValues cv = new ContentValues();
        cv.put("is_allowed",1);
        cv.put("is_synced",0);

        String[] whereArgs= new String[]{String.valueOf(_id)};
        petBetterDb.update(MESSAGE_TABLE,cv,"_id=?", whereArgs);
        petBetterDb.close();


    }

    public void editProfile(long _id, String firstName, String lastName, String emailAddress,String mobileNum,
                            String landline, String image){


        ContentValues cv = new ContentValues();
        cv.put("first_name",firstName);
        cv.put("last_name",lastName);
        cv.put("mobile_num", mobileNum);
        cv.put("phone_num", landline);
        cv.put("email",emailAddress);
        cv.put("user_photo",image);

        String[] whereArgs= new String[]{String.valueOf(_id)};
        petBetterDb.update(USER_TABLE,cv,"_id=?", whereArgs);
        petBetterDb.close();


    }

    public void editFacility(long _id, String faciName, String location, String hoursOpen,String hoursClose,
                            String contactInfo, float rating, String faciPhoto){


        ContentValues cv = new ContentValues();
        cv.put("_id",_id);
        cv.put("faci_name",faciName);
        cv.put("location", location);
        cv.put("hours_open", hoursOpen);
        cv.put("hours_close",hoursClose);
        cv.put("contact_info", contactInfo);
        cv.put("rating", rating);
        cv.put("faci_photo", faciPhoto);
        cv.put("is_synced", 0);

        String[] whereArgs= new String[]{String.valueOf(_id)};
        petBetterDb.update(FACI_TABLE,cv,"_id=?", whereArgs);
        petBetterDb.close();


    }

    public long getMessageId(long fromId, long toId){

        long result;
        String sql = "SELECT m._id AS _id FROM messages AS m INNER JOIN users AS u ON m.user_one = u._id " +
                "WHERE u._id = '"+fromId+"' AND m.user_two = '"+toId+"' \n" +
                "UNION \n" +
                "SELECT m._id AS _id FROM messages AS m INNER JOIN users AS u ON m.user_two = u._id " +
                "WHERE u._id = '"+fromId+"' AND m.user_one = '"+toId+"'";

        //String sql = "SELECT * FROM " + MESSAGE_TABLE + " WHERE _id = '" + messageId + "'";
        Cursor c = petBetterDb.rawQuery(sql, null);

        Log.e("cursor", c.getCount() + "");

        try{
            c.moveToFirst();
            result = c.getLong(c.getColumnIndexOrThrow("_id"));
        }catch(CursorIndexOutOfBoundsException e){
             result = 0;
        }

        c.close();
        return result;

    }

    public ArrayList<Integer> generateFaciIds () {

        ArrayList<Integer> ids = new ArrayList<>();

        String sql = "SELECT _id FROM "+FACI_TABLE;
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            ids.add(c.getInt(c.getColumnIndexOrThrow("_id")));
        }

        c.close();
        return ids;
    }

    public ArrayList<Integer> generateUpvoteIds () {

        ArrayList<Integer> ids = new ArrayList<>();

        String sql = "SELECT _id FROM "+UPVOTE_TABLE;
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            ids.add(c.getInt(c.getColumnIndexOrThrow("_id")));
        }

        c.close();
        return ids;
    }

    public ArrayList<Integer> generateUserIds () {

        ArrayList<Integer> ids = new ArrayList<>();

        String sql = "SELECT _id FROM "+USER_TABLE;
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            ids.add(c.getInt(c.getColumnIndexOrThrow("_id")));
        }

        c.close();
        return ids;
    }

    public ArrayList<Integer> generateVetIds () {

        ArrayList<Integer> ids = new ArrayList<>();

        String sql = "SELECT _id FROM "+VET_TABLE;
        Cursor c = petBetterDb.rawQuery(sql, null);

        while(c.moveToNext()) {
            ids.add(c.getInt(c.getColumnIndexOrThrow("_id")));
        }

        c.close();
        return ids;
    }

    public long convertFaciToBookmark(int mId, String bldgName, double longitude, double latitude, String location, long userId, int type){

        long result;

        ContentValues cv = new ContentValues();
        cv.put("_id", mId);
        cv.put("bldg_name", bldgName);
        cv.put("longitude", longitude);
        cv.put("latitude", latitude);
        cv.put("location", location);
        cv.put("user_id", userId);
        cv.put("type", type);


        result = petBetterDb.insert(MARKER_TABLE, null, cv);


        return result;
    }


    public long convertBookmarkToFaci(int fId, String faciName, String location, long userId, int rating){
        long result;

        ContentValues cv = new ContentValues();
        cv.put("_id", fId);
        cv.put("faci_name", faciName);
        cv.put("location", location);
        cv.put("vet_id", userId);
        cv.put("rating", rating);


        result = petBetterDb.insert(FACI_TABLE, null, cv);


        return result;
    }

    public Facility getFacility(int id){

        String sql = "SELECT * FROM " + FACI_TABLE + " WHERE _id = '" + id + "'";
        Cursor c = petBetterDb.rawQuery(sql, null);

        Log.e("cursor", c.getCount() + "");

        try{
            c.moveToFirst();

            Facility result = new Facility(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getString(c.getColumnIndexOrThrow("faci_name")),
                    c.getString(c.getColumnIndexOrThrow("location")),
                    c.getString(c.getColumnIndexOrThrow("hours_open")),
                    c.getString(c.getColumnIndexOrThrow("hours_close")),
                    c.getString(c.getColumnIndexOrThrow("contact_info")),
                    c.getFloat(c.getColumnIndexOrThrow("rating")),
                    c.getInt(c.getColumnIndexOrThrow("is_disabled")));

            c.close();
            return result;
        }catch(CursorIndexOutOfBoundsException cpe){
            return null;
        }
    }

    public Services getServiceWithId(long id){
        ArrayList<Services> results = new ArrayList<>();
        String temp;

        //String sql = "SELECT * FROM " + FACI_TABLE + " WHERE vet_id = '" + veterinarian.getId() + "'";
        String sql = "SELECT * FROM " + SERVICE_TABLE + " WHERE _id = '" + id + "'";
        Cursor c = petBetterDb.rawQuery(sql, null);

        try{
            c.moveToFirst();

            Services result = new Services(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("faci_id")),
                    c.getString(c.getColumnIndexOrThrow("service_name")),
                    c.getFloat(c.getColumnIndexOrThrow("service_price")),
                    c.getInt(c.getColumnIndexOrThrow("is_deleted")));

            c.close();
            return result;
        }catch(CursorIndexOutOfBoundsException cpe){
            return null;
        }
    }

    public long addVet(int vetId, int userId, int rating){
        long result;

        ContentValues cv = new ContentValues();
        cv.put("_id", vetId);
        cv.put("user_id", userId);
        cv.put("rating", rating);

        result = petBetterDb.insert(VET_TABLE, null, cv);

        return result;
    }

    public long addFacilityBookmark(long _id, long item_id, long user_id){
        long result;

        ContentValues cv = new ContentValues();
        cv.put("_id", _id);
        cv.put("item_id", item_id);
        cv.put("bookmark_type", 1);
        cv.put("is_synced", 0);
        cv.put("user_id", user_id);

        result = petBetterDb.insert(BOOKMARK_TABLE, null, cv);
        return result;
    }

    public long addPostBookmark(long _id, long item_id, long user_id){
        long result;

        ContentValues cv = new ContentValues();
        cv.put("_id", _id);
        cv.put("item_id", item_id);
        cv.put("bookmark_type", 2);
        cv.put("is_synced", 0);
        cv.put("user_id", user_id);

        result = petBetterDb.insert(BOOKMARK_TABLE, null, cv);
        return result;
    }

    public void deleteFacilityBookmark (long item_id, long user_id) {
        petBetterDb.delete(BOOKMARK_TABLE, "user_id = " + user_id + " AND item_id = "+ item_id+" AND bookmark_type = 1", null);
    }

    public void deletePostBookmark (long item_id, long user_id) {
        petBetterDb.delete(BOOKMARK_TABLE, "user_id = " + user_id + " AND item_id = "+ item_id+" AND bookmark_type = 2", null);
    }

    public long setVeterinarians(ArrayList<Veterinarian> vetList){
        long result = 0;

        petBetterDb.delete(VET_TABLE, null, null);
        System.out.println("REAL NUM OF VETS "+getVeterinarians().size());

        for(Veterinarian vet:vetList){
            ContentValues cv = new ContentValues();
            cv.put("_id", vet.getId());
            cv.put("user_id", vet.getUserId());
            cv.put("rating", vet.getRating());
            cv.put("education", vet.getEducation());
            cv.put("specialty", vet.getSpecialty());
            cv.put("is_licensed", vet.getIsLicensed());
            cv.put("profile_desc", vet.getProfileDesc());
            result = petBetterDb.insert(VET_TABLE, null, cv);
        }
        //System.out.println("2ND REAL NUM OF VETS "+getVeterinarians().size());
        return result;
    }

    public long setRatings(ArrayList<Rating> rateList){
        long result = 0;

        petBetterDb.delete(RATE_TABLE, null, null);

        System.out.println("REAL NUM OF RATINGS: "+getRatingIds().size());

        for(Rating rating:rateList){
            ContentValues cv = new ContentValues();
            cv.put("_id", rating.getId());
            cv.put("rated_id", rating.getRatedId());
            cv.put("rater_id", rating.getRaterId());
            cv.put("rating", rating.getRating());
            cv.put("comment", rating.getComment());
            cv.put("rating_type", rating.getRatingType());
            cv.put("date_created", rating.getDateCreated());
            cv.put("is_deleted", rating.getIsDeleted());

            result = petBetterDb.insert(RATE_TABLE, null, cv);
        }
        System.out.println("2ND REAL NUM OF RATINGS: "+getRatingIds().size());

        return result;
    }

    public long setPosts(ArrayList<Post> postList){
        long result = 0;

        petBetterDb.delete(POST_TABLE, null, null);
        System.out.println("REAL NUM OF POSTS: "+getPosts().size());
        System.out.println("POST LIST SIZE "+postList.size());


        for(Post post:postList){
            ContentValues cv = new ContentValues();
            cv.put("_id", post.getId());
            cv.put("user_id", post.getUserId());
            cv.put("topic_name", post.getTopicName());
            cv.put("topic_content", post.getTopicContent());
            cv.put("topic_id", post.getTopicId());
            cv.put("date_created", post.getDateCreated());
            cv.put("post_photo", post.getPostPhoto());
            cv.put("id_link", post.getIdLink());
            cv.put("id_type", post.getIdType());
            cv.put("is_deleted", post.getIsDeleted());
            result = petBetterDb.insert(POST_TABLE, null, cv);
        }
        System.out.println("2ND REAL NUM OF POSTS: "+getPosts().size());

        return result;
    }

    public long setPending(ArrayList<Pending> pendingList){
        long result = 0;

        petBetterDb.delete(PENDING_TABLE, null, null);
        //System.out.println("REAL NUM OF POSTS: "+getPosts().size());
        //System.out.println("POST LIST SIZE "+postList.size());


        for(Pending pending:pendingList){
            ContentValues cv = new ContentValues();
            cv.put("_id", pending.getId());
            cv.put("foreign_id", pending.getForeignId());
            cv.put("type", pending.getType());
            cv.put("is_approved", pending.getIsApproved());
            result = petBetterDb.insert(PENDING_TABLE, null, cv);
        }
        //System.out.println("2ND REAL NUM OF POSTS: "+getPosts().size());

        return result;
    }

    public long setUsers(ArrayList<User> userList){
        long result = 0;

        petBetterDb.delete(USER_TABLE, null, null);



        for(User user:userList){
            ContentValues cv = new ContentValues();
            cv.put("_id", user.getUserId());
            cv.put("first_name", user.getFirstName());
            cv.put("last_name", user.getLastName());
            cv.put("mobile_num", user.getMobileNumber());
            cv.put("phone_num", user.getPhoneNumber());
            cv.put("email", user.getEmail());
            cv.put("password", user.getPassword());
            cv.put("age", user.getAge());
            cv.put("user_type", user.getUserType());
            cv.put("user_photo", user.getUserPhoto());
            cv.put("is_synced", 1);

            result = petBetterDb.insert(USER_TABLE, null, cv);
        }
        //System.out.println("2ND REAL NUM OF POSTS: "+getPosts().size());

        return result;
    }

    public long setUpvotes(ArrayList<Upvote> upvoteList){
        long result = 0;

        petBetterDb.delete(UPVOTE_TABLE, null, null);
        //System.out.println("REAL NUM OF POSTS: "+getPosts().size());
        //System.out.println("POST LIST SIZE "+postList.size());


        for(Upvote upvote:upvoteList){
            ContentValues cv = new ContentValues();
            cv.put("_id", upvote.getId());
            cv.put("feed_id", upvote.getFeedId());
            cv.put("user_id", upvote.getUserId());
            cv.put("value", upvote.getValue());
            cv.put("type", upvote.getType());
            cv.put("is_synced", 1);
            result = petBetterDb.insert(UPVOTE_TABLE, null, cv);
        }
        //System.out.println("2ND REAL NUM OF POSTS: "+getPosts().size());

        return result;
    }

    public long setTopics(ArrayList<Topic> topicList){
        long result = 0;

        petBetterDb.delete(TOPIC_TABLE, null, null);
        System.out.println("REAL NUM OF TOPICS: "+getTopicIds().size());
        System.out.println("TOPIC LIST SIZE "+topicList.size());


        for(Topic topic:topicList){
            ContentValues cv = new ContentValues();
            cv.put("_id", topic.getId());
            cv.put("creator_id", topic.getCreatorId());
            cv.put("topic_name", topic.getTopicName());
            cv.put("topic_desc", topic.getTopicDesc());
            cv.put("date_created", topic.getDateCreated());
            cv.put("is_deleted", topic.getIsDeleted());
            result = petBetterDb.insert(TOPIC_TABLE, null, cv);
        }
        System.out.println("2ND REAL NUM OF TOPICS: "+getTopicIds().size());

        return result;
    }

    public long setServices(ArrayList<Services> serviceList){
        long result = 0;

        petBetterDb.delete(SERVICE_TABLE, null, null);
        System.out.println("REAL NUM OF SERVICES: "+getServices().size());
        System.out.println("SERVICE LIST SIZE "+serviceList.size());


        for(Services services:serviceList){
            ContentValues cv = new ContentValues();
            cv.put("_id", services.getId());
            cv.put("faci_id", services.getFaciId());
            cv.put("service_name", services.getServiceName());
            cv.put("service_price", services.getServicePrice());
            cv.put("is_deleted", services.getIsDeleted());
            result = petBetterDb.insert(SERVICE_TABLE, null, cv);
        }
        System.out.println("2ND REAL NUM OF SERVICES: "+getServices().size());

        return result;
    }

    public long setPostReps(ArrayList<PostRep> postRepList){
        long result = 0;

        petBetterDb.delete(POST_REP_TABLE, null, null);
        System.out.println("REAL NUM OF POSTREPS: "+getPostRepIds().size());
        System.out.println("POST REP LIST SIZE "+postRepList.size());


        for(PostRep postrep:postRepList){
            ContentValues cv = new ContentValues();
            cv.put("_id", postrep.getId());
            cv.put("user_id", postrep.getUserId());
            cv.put("post_id", postrep.getPostId());
            cv.put("parent_id", postrep.getParentId());
            cv.put("rep_content", postrep.getRepContent());
            cv.put("date_performed", postrep.getDatePerformed());
            cv.put("postrep_photo", postrep.getPostRepPhoto());
            cv.put("is_deleted", postrep.getIsDeleted());
            result = petBetterDb.insert(POST_REP_TABLE, null, cv);
        }
        System.out.println("2ND REAL NUM OF POSTREPS: "+getPostRepIds().size());

        return result;
    }

    public long setMessages(ArrayList<Message> messageList){
        long result = 0;

        petBetterDb.delete(MESSAGE_TABLE, null, null);
        System.out.println("REAL NUM OF MESSAGES "+getMessageIds().size());

        System.out.println("REAL NUM OF MESSAGELIST "+messageList.size());
        for(Message message:messageList){
            ContentValues cv = new ContentValues();
            cv.put("_id", message.getId());
            cv.put("user_one", message.getUserId());
            cv.put("user_two", message.getFromId());
            cv.put("is_allowed", message.getIsAllowed());
            result = petBetterDb.insert(MESSAGE_TABLE, null, cv);
        }
        System.out.println("2ND REAL NUM OF MESSAGES "+getMessageIds().size());

        return result;
    }

    public long setNotifications(ArrayList<Notifications> notifList){
        long result = 0;

        petBetterDb.delete(NOTIF_TABLE, null, null);

        for(Notifications notification:notifList){
            ContentValues cv = new ContentValues();
            cv.put("_id", notification.getId());
            cv.put("user_id", notification.getUserId());
            cv.put("doer_id", notification.getDoerId());
            cv.put("is_read", notification.getIsRead());
            cv.put("type", notification.getType());
            cv.put("date_performed", notification.getDatePerformed());
            cv.put("source_id", notification.getSourceId());
            result = petBetterDb.insert(NOTIF_TABLE, null, cv);
        }

        return result;
    }

    public long setPets(ArrayList<Pet> petList){
        long result = 0;

        petBetterDb.delete(PET_TABLE, null, null);
        System.out.println("REAL NUM OF PETS: "+getPets(petList.get(0).getUserId()).size());
        System.out.println("PET LIST SIZE: "+petList.size());

        for(Pet pet:petList){
            ContentValues cv = new ContentValues();
            cv.put("_id", pet.getId());
            cv.put("user_id", pet.getUserId());
            cv.put("name", pet.getName());
            cv.put("classification", pet.getClassification());
            cv.put("breed", pet.getBreed());
            cv.put("height", pet.getHeight());
            cv.put("weight", pet.getWeight());
            result = petBetterDb.insert(PET_TABLE, null, cv);
        }
        System.out.println("2ND REAL NUM OF PETS: "+getPets(petList.get(0).getUserId()).size());

        return result;
    }

    public long setMessageReps(ArrayList<MessageRep> messageRepList){
        long result = 0;

        petBetterDb.delete(MESSAGE_REP_TABLE, null, null);
        System.out.println("REAL NUM OF MESSAGEREPS "+getMessageRepIds().size());

        System.out.println("REAL NUM OF MESSAGEREPLIST "+messageRepList.size());
        for(MessageRep messagerep:messageRepList){
            ContentValues cv = new ContentValues();
            cv.put("_id", messagerep.getId());
            cv.put("user_id", messagerep.getUserId());
            cv.put("sender_id", messagerep.getSenderId());
            cv.put("message_id", messagerep.getMessageId());
            cv.put("rep_content", messagerep.getRepContent());
            cv.put("is_sent", messagerep.getIsSent());
            cv.put("date_performed", messagerep.getDatePerformed());
            //here
            cv.put("message_photo", messagerep.getMessagePhoto());
            result = petBetterDb.insert(MESSAGE_REP_TABLE, null, cv);
        }
        System.out.println("2ND REAL NUM OF MESSAGEREPS "+getMessageRepIds().size());

        return result;
    }

    public long setMarkers(ArrayList<LocationMarker> locationMarkerList){
        long result = 0;

        petBetterDb.delete(MARKER_TABLE, null, null);
        System.out.println("REAL NUM OF MARKERS "+getMarkerIds().size());

        for(LocationMarker locationMarker : locationMarkerList){
            ContentValues cv = new ContentValues();
            cv.put("_id", locationMarker.getId());
            cv.put("bldg_name", locationMarker.getBldgName());
            cv.put("longitude", locationMarker.getLongitude());
            cv.put("latitude", locationMarker.getLatitude());
            cv.put("location", locationMarker.getLocation());
            cv.put("user_id", locationMarker.getUserId());
            cv.put("type", locationMarker.getType());
            result = petBetterDb.insert(MARKER_TABLE, null, cv);
        }
        System.out.println("2ND REAL NUM OF MARKERS "+getMarkerIds().size());

        return result;
    }

    public long setMembers(ArrayList<FacilityMembership> facilityMemberships){
        long result = 0;

        petBetterDb.delete(FACI_MEMBER_TABLE, null, null);

        for(FacilityMembership facilityMembership : facilityMemberships){
            ContentValues cv = new ContentValues();
            cv.put("_id", facilityMembership.getId());
            cv.put("faci_id", facilityMembership.getFaciId());
            cv.put("vet_id", facilityMembership.getVetId());

            result = petBetterDb.insert(FACI_MEMBER_TABLE, null, cv);
        }

        return result;
    }

    public long setLocationMarkers(ArrayList<LocationMarker> locationMarkerList){
        long result = 0;

        petBetterDb.delete(MARKER_TABLE, null, null);
        System.out.println("REAL NUM OF MARKERS "+getMarkerIds().size());

        for(LocationMarker locationMarker : locationMarkerList){
            ContentValues cv = new ContentValues();
            cv.put("_id", locationMarker.getId());
            cv.put("bldg_name", locationMarker.getBldgName());
            cv.put("longitude", locationMarker.getLongitude());
            cv.put("latitude", locationMarker.getLatitude());
            cv.put("location", locationMarker.getLocation());
            cv.put("user_id", locationMarker.getUserId());
            cv.put("type", locationMarker.getType());
            cv.put("faci_id", locationMarker.getFaciId());
            result = petBetterDb.insert(MARKER_TABLE, null, cv);
        }
        System.out.println("2ND REAL NUM OF MARKERS "+getMarkerIds().size());

        return result;
    }

    public long setFollowers(ArrayList<Follower> followerList){
        long result = 0;

        petBetterDb.delete(FOLLOWER_TABLE, null, null);


        for(Follower follower:followerList){
            ContentValues cv = new ContentValues();
            cv.put("_id", follower.getId());
            cv.put("topic_id", follower.getTopicId());
            cv.put("user_id", follower.getUserId());
            cv.put("is_allowed", follower.getIsAllowed());
            result = petBetterDb.insert(FOLLOWER_TABLE, null, cv);
        }

        return result;
    }

    public long setFacilities(ArrayList<Facility> faciList){
        long result = 0;

        petBetterDb.delete(FACI_TABLE, null, null);
        System.out.println("REAL NUM OF FACIS "+getClinics().size());
        System.out.println("size of array: "+faciList.size());


        for(Facility facility:faciList){
            ContentValues cv = new ContentValues();
            System.out.println("From DB Faci ID: "+facility.getId());
            cv.put("_id", facility.getId());
            cv.put("faci_name", facility.getFaciName());
            cv.put("location", facility.getLocation());
            cv.put("hours_open", facility.getHoursOpen());
            cv.put("hours_close", facility.getHoursClose());
            cv.put("contact_info", facility.getContactInfo());
            cv.put("rating", facility.getRating());
            cv.put("faci_photo", facility.getFaciPhoto());
            result = petBetterDb.insert(FACI_TABLE, null, cv);
            System.out.println("how meni? "+result);
        }
        System.out.println("2ND REAL NUM OF FACIS "+getClinics().size());

        return result;
    }

    public LocationMarker getMarker(String bldgName){

        String sql = "SELECT * FROM " + MARKER_TABLE + " WHERE bldg_name = '" + bldgName + "'";
        Cursor c = petBetterDb.rawQuery(sql, null);

        Log.e("cursor", c.getCount() + "");

        c.moveToFirst();

        LocationMarker result = new LocationMarker(c.getInt(c.getColumnIndexOrThrow("_id")),
                c.getString(c.getColumnIndexOrThrow("bldg_name")),
                c.getDouble(c.getColumnIndexOrThrow("longitude")),
                c.getDouble(c.getColumnIndexOrThrow("latitude")),
                c.getString(c.getColumnIndexOrThrow("location")),
                c.getLong(c.getColumnIndexOrThrow("user_id")),
                c.getInt(c.getColumnIndexOrThrow("type")));

        c.close();
        return result;
    }

    public ArrayList<PostRep> getPostRepsFromParent(long parentId){

        ArrayList<PostRep> results = new ArrayList<>();
        String sql = "SELECT pr._id AS _id, pr.user_id AS user_id, pr.post_id AS post_id, pr.parent_id AS parent_id, pr.rep_content AS rep_content,\n" +
                "pr.date_performed AS date_performed, pr.is_deleted AS is_deleted, u.first_name AS first_name, u.last_name AS last_name, \n" +
                "pr.postrep_photo AS postrep_photo FROM postreps AS pr INNER JOIN users AS u ON pr.user_id = u._id WHERE pr.parent_id = '" + parentId + "' AND pr.is_deleted != 1";
        //String sql = "SELECT * FROM " + POST_REP_TABLE + " WHERE _id = '" + postRepId + "' AND is_deleted != 1";
        Cursor c = petBetterDb.rawQuery(sql, null);

        Log.e("cursor", c.getCount() + "");
        while(c.moveToNext()) {
            PostRep postrep = new PostRep(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getLong(c.getColumnIndexOrThrow("user_id")),
                    c.getInt(c.getColumnIndexOrThrow("post_id")),
                    c.getInt(c.getColumnIndexOrThrow("parent_id")),
                    c.getString(c.getColumnIndexOrThrow("rep_content")),
                    c.getString(c.getColumnIndexOrThrow("date_performed")),
                    c.getInt(c.getColumnIndexOrThrow("is_deleted")),
                    c.getString(c.getColumnIndexOrThrow("first_name")),
                    c.getString(c.getColumnIndexOrThrow("last_name")),
                    c.getString(c.getColumnIndexOrThrow("postrep_photo")));
            results.add(postrep);
        }

        c.close();
        return results;

    }

    public PostRep getPostRepFromId(long postRepId){
        String sql = "SELECT pr._id AS _id, pr.user_id AS user_id, pr.post_id AS post_id, pr.parent_id AS parent_id, pr.rep_content AS rep_content,\n" +
                "pr.date_performed AS date_performed, pr.is_deleted AS is_deleted, u.first_name AS first_name, u.last_name AS last_name, \n" +
                "pr.postrep_photo AS postrep_photo FROM postreps AS pr INNER JOIN users AS u ON pr.user_id = u._id WHERE pr._id = '"+postRepId+"' AND pr.is_deleted != 1";
        Cursor c = petBetterDb.rawQuery(sql, null);

        Log.e("cursor", c.getCount() + "");

        c.moveToFirst();

        PostRep result = new PostRep(c.getInt(c.getColumnIndexOrThrow("_id")),
                c.getLong(c.getColumnIndexOrThrow("user_id")),
                c.getInt(c.getColumnIndexOrThrow("post_id")),
                c.getInt(c.getColumnIndexOrThrow("parent_id")),
                c.getString(c.getColumnIndexOrThrow("rep_content")),
                c.getString(c.getColumnIndexOrThrow("date_performed")),
                c.getInt(c.getColumnIndexOrThrow("is_deleted")),
                c.getString(c.getColumnIndexOrThrow("first_name")),
                c.getString(c.getColumnIndexOrThrow("last_name")),
                c.getString(c.getColumnIndexOrThrow("postrep_photo")));

        c.close();
        return result;
    }
}
