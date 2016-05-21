package com.example.faisal.sudokugame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Notes.db";
    public static final String TABLE_NOTES = "notes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TIME = "time";
    public static ArrayList<Names> list = new ArrayList<Names>();
    String dbString = "";


    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTES + " ( " +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                COLUMN_NAME + " TEXT " + ", " + COLUMN_TIME + " TEXT " + ");";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        // onCreate(db);


    }

    //add new row
    public void addScore(Names name) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name.getName());
        values.put(COLUMN_TIME, name.getTime());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NOTES, null, values);
        db.close();

    }


    //PrintOut teh DB as String
    public void databaseToString() {
        String query = "SELECT * FROM " + TABLE_NOTES + " WHERE 1";
        SQLiteDatabase db = getWritableDatabase();
        //cursor to location

        Cursor c = db.rawQuery(query, null);

        //move result to the first row

        c.moveToFirst();
        list = new ArrayList<Names>();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("name")) != null && c.getString(c.getColumnIndex("time")) != null) {
                Names n = new Names(c.getString(c.getColumnIndex("name")), c.getString(c.getColumnIndex("time")));
                list.add(n);
            }
            c.moveToNext();
        }
        db.close();
    }
}