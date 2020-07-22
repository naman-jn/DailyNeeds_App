package com.njain.dailyneeds.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.njain.dailyneeds.model.Item;
import com.njain.dailyneeds.util.Constants;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private final Context context;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ITEM_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY,"
                + Constants.KEY_ITEM + " INTEGER,"
                + Constants.KEY_QTY + " INTEGER,"
                + Constants.KEY_ITEM_DESCRIPTION + " INTEGER,"
                + Constants.KEY_DATE_NAME + " LONG);";

        db.execSQL(CREATE_ITEM_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);

        onCreate(db);
    }

    // CRUD operations
    public void addItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_ITEM, item.getName());
        values.put(Constants.KEY_QTY, item.getQty());
        values.put(Constants.KEY_ITEM_DESCRIPTION, item.getDescription());
        values.put(Constants.KEY_DATE_NAME, java.lang.System.currentTimeMillis());//timestamp of the system

        //Inset the row
        db.insert(Constants.TABLE_NAME, null, values);

        Log.d("DBHandler", "added Item: ");
    }

    //Get an Item
    public Item getItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME,
                new String[]{Constants.KEY_ID,
                        Constants.KEY_ITEM,
                        Constants.KEY_QTY,
                        Constants.KEY_ITEM_DESCRIPTION,
                        Constants.KEY_DATE_NAME},
                Constants.KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Item item = new Item();
        if (cursor != null) {
            item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
            item.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM)));
            item.setQty(cursor.getInt(cursor.getColumnIndex(Constants.KEY_QTY)));
            item.setDescription(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM_DESCRIPTION)));

            //convert Timestamp to something readable
            DateFormat dateFormat = DateFormat.getDateInstance();
            String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME)))
                    .getTime()); // Feb 23, 2020

            item.setDateItemAdded(formattedDate);


        }

        return item;
    }

    //Get all Items
    public List<Item> getAllItems() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<Item> itemList = new ArrayList<>();

        Cursor cursor = db.query(Constants.TABLE_NAME,
                new String[]{Constants.KEY_ID,
                        Constants.KEY_ITEM,
                        Constants.KEY_QTY,
                        Constants.KEY_ITEM_DESCRIPTION,
                        Constants.KEY_DATE_NAME},
                null, null, null, null,
                Constants.KEY_DATE_NAME + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                item.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM)));
                item.setQty(cursor.getInt(cursor.getColumnIndex(Constants.KEY_QTY)));
                item.setDescription(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM_DESCRIPTION)));

                //convert Timestamp to something readable
                DateFormat dateFormat = DateFormat.getDateInstance();
                String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME)))
                        .getTime()); // Feb 23, 2020
                item.setDateItemAdded(formattedDate);

                //Add to arraylist
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        return itemList;

    }

    //Todo: Add updateItem
    public int updateItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_ITEM, item.getName());
        values.put(Constants.KEY_QTY, item.getQty());
        values.put(Constants.KEY_ITEM_DESCRIPTION, item.getDescription());
        values.put(Constants.KEY_DATE_NAME, java.lang.System.currentTimeMillis());//timestamp of the system

        //update row
        return db.update(Constants.TABLE_NAME, values,
                Constants.KEY_ID + "=?",
                new String[]{String.valueOf(item.getId())});

    }

    //Todo: Add Delete Item
    public void deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME,
                Constants.KEY_ID + "=?",
                new String[]{String.valueOf(id)});

        //close
        db.close();

    }

    //Todo: getItemCount
    public int getItemsCount() {
        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();

    }
}
