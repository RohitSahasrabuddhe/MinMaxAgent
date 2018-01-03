package com.example.android.minmaxagent.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Rohit on 1/3/2018.
 */

public class DBHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "MinMaxDB";
    // Contacts table name
    private static final String TABLE_NAME = "profile";
    // Shops Table Columns names
    private static final String KEY_NAME = "name";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_FRUIT_TYPE = "fruittype";
    private static final String KEY_GRID_SIZE = "gridsize";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table profile(Name varchar(20), PASSWORD varchar(20), FRUITTYPE integer, GRIDSIZE integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("drop table if exists profile");
        // Creating tables again
        onCreate(sqLiteDatabase);
    }

    public void addProfile(Profile profile){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, profile.getName()); // Shop Name
        values.put(KEY_PASSWORD, profile.getPassword()); //Password
        values.put(KEY_FRUIT_TYPE, profile.getFruitType()); //Fruit Type
        values.put(KEY_GRID_SIZE, profile.getGridSize()); //Grid Size
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Getting one shop
    public Profile getProfile() {
        Profile profile = new Profile();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,null,null,null,null,null,null,null);
        if (cursor != null) {
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getColumnCount(); i++) {
                profile.setName(cursor.getString(0));
                profile.setPassword(cursor.getString(1));
                profile.setFruitType(Integer.parseInt(cursor.getString(2)));
                profile.setGridSize(Integer.parseInt(cursor.getString(3)));
            }
        }
        return profile;
    }
}
