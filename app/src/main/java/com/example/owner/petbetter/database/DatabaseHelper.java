package com.example.owner.petbetter.database;

/**
 * Created by owner on 27/7/2017.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

/**
 * Created by mikedayupay on 10/01/2016.
 * GetBetter 2016
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    private static String DB_PATH = "";

    private static String DB_NAME = "petbetter.db";

    private static int DB_VERSION = 2;

    private SQLiteDatabase petBetterDatabase;

    private final Context myContext;

    private static DatabaseHelper sInstance;

    public static DatabaseHelper getInstance (Context context) {

        if(sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }

        return sInstance;
    }

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.myContext = context;
        DB_PATH="/data/data/"+context.getPackageName()+"/"+"databases/";
    }

    public void createDatabase() throws IOException {

        boolean databaseExists = checkDatabase();

        if(!databaseExists) {
            this.getReadableDatabase();
            this.close();

            try {
                copyDatabase();
                String TAG = "DatabaseHelper";
                Log.e(TAG, "createDatabase database created");
            }catch (IOException ioe) {
                throw new Error("Error creating database");
            }
        }
    }

    private boolean checkDatabase() {

        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    private void copyDatabase() throws IOException {

        InputStream mInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];

        int mLength;

        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }

        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public void openDatabase() throws SQLException {

        String myPath = DB_PATH + DB_NAME;
        petBetterDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

    }

    @Override
    public synchronized void close() {

        if(petBetterDatabase != null)
            petBetterDatabase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

