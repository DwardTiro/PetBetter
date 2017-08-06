package com.example.owner.petbetter.database;

/**
 * Created by owner on 27/7/2017.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.owner.petbetter.classes.User;

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
    private static final String CASE_RECORD_TABLE = "tbl_case_records";
    private static final String CASE_RECORD_TABLE_UPLOAD = "tbl_case_records_upload";
    private static final String CASE_RECORD_HISTORY_TABLE = "tbl_case_record_history";
    private static final String CASE_RECORD_ATTACHMENTS_TABLE = "tbl_case_record_attachments";
    private static final String HEALTH_CENTER_TABLE = "tbl_health_centers";


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
        String sql = "SELECT _id FROM tbl_users WHERE email = '" + username + "'";
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

    public long addMarker(int rowId, String bldgNum, String street, String bldgName, String city, String province,
                             double longitude, double latitude, long userId){
        long result;

        ContentValues cv = new ContentValues();
        cv.put("_id", rowId);
        cv.put("bldg_num", bldgNum);
        cv.put("street", street);
        cv.put("bldg_name", bldgName);
        cv.put("city", city);
        cv.put("province", province);
        cv.put("longitude", longitude);
        cv.put("latitude", latitude);
        cv.put("user_id", userId);


        result = petBetterDb.insert(MARKER_TABLE, null, cv);

        System.out.println("THE RESULT IS "+result);
        System.out.println("MARKER ID IS " +rowId);

        return result;
    }

}
