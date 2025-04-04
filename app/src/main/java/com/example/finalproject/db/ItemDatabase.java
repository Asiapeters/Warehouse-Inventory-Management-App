package com.example.finalproject.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.finalproject.Models.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "items.db";
    private static final int VERSION = 2;

    public ItemDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
    }

    private static final class ItemTable {
        private static final String TABLE = "items";
        private static final String COL_ID = "_id";
        private static final String COL_NAME = "name";
        private static final String COL_DESCRIPTION = "description";
        private static final String COL_QUANTITY = "quantity";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ItemTable.TABLE + " (" +
                ItemTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ItemTable.COL_NAME + " TEXT, " +
                ItemTable.COL_DESCRIPTION + " TEXT, " +
                ItemTable.COL_QUANTITY + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + ItemTable.TABLE);
        onCreate(db);
    }

    public boolean createItem(String name, String description, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ItemTable.COL_NAME, name);
        values.put(ItemTable.COL_DESCRIPTION, description );
        values.put(ItemTable.COL_QUANTITY, quantity );

        long result = db.insert(ItemTable.TABLE, null, values);
        db.close();
        return result > -1;
    }

    public Item getItemByID(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select * from " + ItemDatabase.ItemTable.TABLE + " where _id = ?";
        Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(id) });
        if (cursor.moveToFirst()) {
            long rowId = cursor.getLong(0);
            String name = cursor.getString(1);
            String description = cursor.getString(2);
            int quantity = cursor.getInt(2);
            return new Item(id, name, description, quantity);
        }
        cursor.close();
        return null;
    }

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + ItemTable.TABLE, null);

        if (cursor.moveToFirst()) {
            do {
                int rowId = cursor.getInt(0);
                String name = cursor.getString(1);
                String description = cursor.getString(2);
                int quantity = cursor.getInt(2);
                items.add(new Item(rowId, name, description, quantity));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return items;
    }

    public boolean deleteItem(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int rowsDeleted = db.delete(ItemTable.TABLE, ItemTable.COL_ID + " = ?",
                new String[] { Integer.toString(id) });
        return rowsDeleted > 0;
    }

    public boolean updateItem(int id, String name, String description, int quantity) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ItemTable.COL_NAME, name);
        values.put(ItemTable.COL_DESCRIPTION, description);
        values.put(ItemTable.COL_QUANTITY, quantity);

        int rowsUpdated = db.update(ItemTable.TABLE, values, "_id = ?",
                new String[] { Float.toString(id) });
        return rowsUpdated > 0;
    }
}
