package com.example.bookmeup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DBAdapter {
    static final String KEY_USERID = "userID";
    static final String KEY_FIRSTNAME = "firstname";
    static final String KEY_LASTNAME = "lastname";
    static final String KEY_USERNAME = "username";
    static final String KEY_EMAIL = "email";
    static final String KEY_PASSWORD = "password";
    static final String TAG = "DBAdapter";
    static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "appDB";
    static final String DATABASE_TABLE_USERS = "users";

 static final String DATABASE_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS usersNew (userID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT" +
                    " UNIQUE, firstname TEXT NOT NULL, lastname TEXT NOT NULL, " +
                    "username TEXT NOT NULL, password TEXT NOT NULL, email TEXT NOT NULL);";


    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context context) {
        this.context = context;
        DBHelper = new DatabaseHelper(context);
        //Add a table into the DB here
    }

    public static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL(DATABASE_CREATE_TABLE);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS users");
            onCreate(db);
        }
    }
    //---opens the database---
    public DBAdapter open(String path) throws SQLException
    {

        db = DBHelper.getWritableDatabase();
        db.openDatabase(path, null, 0);
        return this;
    }

    //---closes the database---
    public void close()
    {
        DBHelper.close();
    }

    //---insert a contact into the database---
    public long insertUser(String firstName, String lastName, String userName, String email, String
                           password){ //Populate the DB
        ContentValues userValues = new ContentValues();
        userValues.put(KEY_FIRSTNAME, firstName);
        userValues.put(KEY_LASTNAME, lastName);
        userValues.put(KEY_USERNAME, userName);
        userValues.put(KEY_PASSWORD, password);
        userValues.put(KEY_EMAIL, email);
        return db.insert(DATABASE_TABLE_USERS, null, userValues);
    }
    //---deletes a particular contact---
    public boolean deleteContact(long rowId)
    {
        return db.delete(DATABASE_TABLE_USERS, KEY_USERID + "=" + rowId, null) > 0;
    }

    //---retrieves all the contacts---
    public Cursor getAllContacts()
    {
        try {
            return db.query(DATABASE_TABLE_USERS, new String[]{KEY_USERID, KEY_FIRSTNAME, KEY_LASTNAME,
                            KEY_USERNAME, KEY_EMAIL}, null, null, null,
                    null, null);
        }catch (SQLException sql){
            sql.printStackTrace();
        }
        return null;
    }
    //---retrieves a particular contact---
 /*
    public Cursor getContact(long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE_USERS, new String[] {KEY_USERID,
                                KEY_FIRSTNAME, KEY_EMAIL}, KEY_USERID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    */
    public Cursor getContact(String userName) throws SQLException
    {
        Cursor mCursor=null;
        try {
            mCursor = db.query(true, DATABASE_TABLE_USERS, new String[]{KEY_USERID,
                            KEY_FIRSTNAME, KEY_LASTNAME, KEY_USERNAME}, KEY_USERID + "="+1,
                    null,null, null, null, null);
            if (mCursor != null) {
                mCursor.moveToFirst();
            } else {
                mCursor.moveToLast();
            }
        }catch(SQLException sql){
            sql.printStackTrace();
        }
            return mCursor;

    }
    //---updates a contact---
    public boolean updateUser(long rowId, String firstName, String lastName, String userName,
                              String email, String password)
    {
        ContentValues modifyUser = new ContentValues();
        modifyUser.put(KEY_FIRSTNAME, firstName);
        modifyUser.put(KEY_LASTNAME, lastName);
        modifyUser.put(KEY_USERNAME, userName);
        modifyUser.put(KEY_PASSWORD, password);
        modifyUser.put(KEY_EMAIL, email);
        return db.update(DATABASE_TABLE_USERS, modifyUser, KEY_USERID + "=" + rowId, null) > 0;
    }
}
