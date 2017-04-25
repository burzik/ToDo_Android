package com.god.magic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.firebase.client.Firebase;

import java.util.ArrayList;

/**
 * HISTORY
 * 	Date			Author				Comments
 * 	17.04.2017		Eduard Arefjev 		Database core
 */

final class ToDoDB extends SQLiteOpenHelper {
    //EA init static variables
    //EA database
    private static final int DATABASE_VERSION = 28;
    private static final String DATABASE_NAME = "ToDoDB";
    private static final String TABLE_TODO = "TODO";

    //EA table Columns names
    private static final String KEY_ID = "COLUMN_ID";
    private static final String KEY_NAME = "COLUMN_NAME";
    private static final String KEY_INFO = "COLUMN_INFO";
    private static final String KEY_CHECK = "COLUMN_CHECK";
    private static final String KEY_DATE = "COLUMN_DATE";
    private static final String KEY_IMAGE = "COLUMN_IMAGE";



    //firebase.addValueEventListener();

    ToDoDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //EA creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ELEMS_TABLE = "CREATE TABLE " + TABLE_TODO + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_INFO + " TEXT, " + KEY_CHECK + " TEXT, "
                + KEY_DATE + " TEXT, " + KEY_IMAGE + " BLOB" + ")";
        db.execSQL(CREATE_ELEMS_TABLE);
    }

    //EA upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //EA drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        onCreate(db);
    }

    //EA adding new elem
    void addElem(ToDoList elem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //EA put all elements
        values.put(KEY_NAME, elem.getName());
        values.put(KEY_INFO, elem.getInfo());
        values.put(KEY_CHECK, Boolean.toString(elem.getSelected()));
        values.put(KEY_DATE, elem.dateToString(elem.getDate()));
        values.put(KEY_IMAGE, elem.bitmapToByte(elem.getPhoto()));
        //EA inserting row
        db.insert(TABLE_TODO, null, values);
        elem.setId(getHighestID());
        db.close();
    }

    // EA Getting single elem
    public ToDoList getElem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TODO, new String[] { KEY_ID,
                        KEY_NAME, KEY_INFO, KEY_CHECK, KEY_DATE, KEY_IMAGE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        ToDoList elem = null;
        assert cursor != null;
        //EA get elem from db
        elem = new ToDoList(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), Boolean.parseBoolean(cursor.getString(3)), elem.stringToDate(cursor.getString(4)), elem.byteToBitmap(cursor.getBlob(5)));
        cursor.close();
        return elem;

    }

    // EA Getting All Elems
    ArrayList<ToDoList> getAllElems() {
        ArrayList<ToDoList> elemList = new ArrayList<>();
        // EA Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TODO;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //EA looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ToDoList elem = new ToDoList();
                elem.setId(Integer.parseInt(cursor.getString(0)));
                elem.setName(cursor.getString(1));
                elem.setInfo(cursor.getString(2));
                elem.setSelected(Boolean.parseBoolean(cursor.getString(3)));
                elem.setDate(elem.stringToDate(cursor.getString(4)));
                elem.setPhoto(elem.byteToBitmap(cursor.getBlob(5)));
                // Adding elem to list
                elemList.add(elem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return elemList;
    }

    // Getting elems Count
    public int getElemCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TODO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    //EA Updating single elem
    int updateElem(ToDoList elem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, elem.getName());
        values.put(KEY_INFO, elem.getInfo());
        values.put(KEY_CHECK, Boolean.toString(elem.check));
        values.put(KEY_DATE, elem.dateToString(elem.getDate()));
        values.put(KEY_IMAGE, elem.bitmapToByte(elem.getPhoto()));
        //EA update db
        return db.update(TABLE_TODO, values, KEY_ID + " = ?", new String[] { String.valueOf(elem.getId()) });
    }

    //EA Deleting single elem
    void deleteElem(ToDoList elem) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO, KEY_ID + " = ?",
                new String[] { String.valueOf(elem.getId()) });
        db.close();
    }

    private int getHighestID() {
        SQLiteDatabase db = this.getWritableDatabase();
        final String MY_QUERY = "SELECT last_insert_rowid()";
        Cursor cur = db.rawQuery(MY_QUERY, null);
        cur.moveToFirst();
        int ID = cur.getInt(0);
        cur.close();
        return ID;
    }
}
