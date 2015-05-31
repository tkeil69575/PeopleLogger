package com.tina.peoplelogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


class dbOpenHelper extends SQLiteOpenHelper {

    // Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "peopleLoggerDb";

    // PLogs table name
    private static final String TABLE_PLOGS = "PLogs";

    // PLogs Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DATETIME = "datetime";
    private static final String KEY_GROUP = "groups";
    private static final String KEY_SEX = "sex";
    private static final String KEY_AGE = "age";
    private static final String KEY_NOTE = "note";

    public dbOpenHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create Table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PLOG_TABLE = "CREATE TABLE " + TABLE_PLOGS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_DATETIME + " TEXT,"
                + KEY_GROUP + " TEXT,"
                + KEY_SEX + " TEXT,"
                + KEY_AGE + " TEXT,"
                + KEY_NOTE + " TEXT"
                + ")";
        db.execSQL(CREATE_PLOG_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLOGS);
        // Create tables again
        onCreate(db);
    }

    // Add new log
    public void addPLog(PLog plog) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATETIME, plog.getDatetime()); // datetime
        values.put(KEY_GROUP, plog.getGroup()); // Group
        values.put(KEY_SEX, plog.getSex()); // Gender (Male, Female)
        values.put(KEY_AGE, plog.getAge()); // Age group
        values.put(KEY_NOTE, plog.getNote()); // Note

        // Insert new row
        db.insert(TABLE_PLOGS, null, values);
        db.close(); // Close db connection
    }

    // Get single log
    public PLog getPLog(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PLOGS, new String[]{
                        KEY_ID,
                        KEY_DATETIME,
                        KEY_GROUP,
                        KEY_AGE,
                        KEY_NOTE
                }, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) cursor.moveToFirst();

        PLog plog = new PLog(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5));

        // return log
        return plog;
    }

    // Get all logs
    public List<PLog> getAllPLogs() {
        List<PLog> plogList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PLOGS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // loop through all rows and add to list
        if (cursor.moveToFirst()) {
            do {
                PLog plog = new PLog();
                plog.setID(Integer.parseInt(cursor.getString(0)));
                plog.setDatetime(cursor.getString(1));
                plog.setGroup(cursor.getString(2));
                plog.setSex(cursor.getString(3));
                plog.setAge(cursor.getString(4));
                plog.setNote(cursor.getString(5));

                // Add log to list
                plogList.add(plog);
            } while (cursor.moveToNext());
        }

        // return plog list
        return plogList;
    }

    // Get log count
    public int getPLogCount() {
        String countQuery = "SELECT * FROM " + TABLE_PLOGS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        // return count
        return cursor.getCount();
    }

    // Update single log
    public int updatePLog(PLog plog) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATETIME, plog.getDatetime());
        values.put(KEY_GROUP, plog.getGroup());
        values.put(KEY_SEX, plog.getSex());
        values.put(KEY_AGE, plog.getAge());
        values.put(KEY_NOTE, plog.getNote());

        // update specific row
        return db.update(TABLE_PLOGS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(plog.getID())});
        }

    // Delete single log
    public void deletePLog(PLog plog) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLOGS, KEY_ID + " = ?",
                new String[] { String.valueOf(plog.getID()) });
        db.close();
    }
}
