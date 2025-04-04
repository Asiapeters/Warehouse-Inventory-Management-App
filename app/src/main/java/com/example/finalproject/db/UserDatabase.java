package com.example.finalproject.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.finalproject.Models.User;

public class UserDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "users.db";
    private static final int VERSION = 4;
    private Context context;
    public UserDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        this.context = context;
    }

    private static final class UserTable {
        private static final String TABLE = "users";
        private static final String COL_ID = "_id";
        private static final String COL_USERNAME = "username";
        private static final String COL_PASSWORD = "password";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " +UserTable.TABLE + " (" +
                UserTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UserTable.COL_USERNAME + " TEXT, " +
                UserTable.COL_PASSWORD + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + UserDatabase.UserTable.TABLE);
        onCreate(db);
    }

    // Method to insert data into the database
    public boolean createUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserTable.COL_USERNAME, username);
        values.put(UserTable.COL_PASSWORD, password );

        long result = db.insert(UserTable.TABLE, null, values);
        db.close(); // Don't forget to close the database
        if (result > -1) {
            return true;
        }

        return false;
    }

    public User getUserByUsername(String username) {
        SQLiteDatabase db = getReadableDatabase();
        User user;
        String sql = "select * from " + UserTable.TABLE + " where username = ?";
        Cursor cursor = db.rawQuery(sql, new String[] { username });
        if (cursor.moveToFirst()) {
            long id = cursor.getLong(0);
            String name = cursor.getString(1);
            String password = cursor.getString(2);
            return new User(username, password);
            }
        cursor.close();
        return null;
        }
    }



