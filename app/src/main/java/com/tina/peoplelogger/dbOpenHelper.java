package com.tina.peoplelogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

class dbOpenHelper extends SQLiteOpenHelper {

    // Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database import and backup folder names
    private static final String DATABASE_IMPORT_DIR = "PLogImport";
    private static final String DATABASE_BACKUP_DIR = "PLogBackup";

    // Database Name
    private static final String DATABASE_NAME = "peopleLogger.sqlite";

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
        String CREATE_PLOG_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PLOGS + "("
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

    // Get top 5 groups
    public ArrayList<String> getTop5PLogs() {
        ArrayList<String> plogList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT groups, count(groups) as number FROM "+ TABLE_PLOGS +
                " GROUP BY groups ORDER BY number DESC LIMIT 5";

        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        int i = 0;

        // loop through all rows and add to list
        try {
            while (!cursor.isAfterLast()) {
                i++;
                plogList.add(
                        i + ". " + cursor.getString(cursor.getColumnIndex(KEY_GROUP))
                        + " (" + cursor.getString(cursor.getColumnIndex("number")) + ")"
                );
                cursor.moveToNext();
            }
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
                db.close();
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
                new String[]{String.valueOf(plog.getID())});
        db.close();
    }

    //import database from SdCard
    public boolean importDatabase() throws IOException {
        boolean status = false;
        if (Environment.getExternalStorageState() != null) {
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    +  File.separator + DATABASE_IMPORT_DIR);

            String toPath;
            if (android.os.Build.VERSION.SDK_INT >= 4.2) {
                toPath = ResultsActivity.PACKAGE_APPINFO.dataDir + "/databases/" + DATABASE_NAME;
            } else {
                toPath = "/data/data/" + ResultsActivity.PACKAGE_NAME + "/databases/" + DATABASE_NAME;
            }

            String fromPath = dir.getAbsolutePath() + File.separator + DATABASE_NAME;

            //close current database
            SQLiteDatabase db = this.getReadableDatabase();
            db.close();

            //check to see if import db exists
            File file = new File(fromPath);
            if (file.exists()) {
                //copy file to internal storage folder for app
                status = true;
                fileCopy(file, new File(toPath));
            }
        }
        return status;
    }

    //Export Database to SdCard
    public boolean backupDatabase() throws IOException {
        boolean status = false;
        if (Environment.getExternalStorageState() != null) {
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    +  File.separator + DATABASE_BACKUP_DIR);
            if (!dir.exists()) {
                //if directory does not exist try to create it
                status = dir.mkdir();
            } else {
                status = true;
            }
            if (status) {
                status = false; //reset status
                String fromPath;
                if (android.os.Build.VERSION.SDK_INT >= 4.2) {
                    fromPath = ResultsActivity.PACKAGE_APPINFO.dataDir + "/databases/" + DATABASE_NAME;
                } else {
                    fromPath = "/data/data/" + ResultsActivity.PACKAGE_NAME + "/databases/" + DATABASE_NAME;
                }

                String toPath = dir.getAbsolutePath() + File.separator + DATABASE_NAME;

                SQLiteDatabase db = this.getReadableDatabase();
                db.close();

                //copy file to external storage folder for app
                fileCopy(new File(fromPath), new File(toPath));
                File file = new File(toPath);

                //check to see if backup db exists
                if (file.exists()) {
                    status = true;
                }
            }
        }
        return status;
    }

    private void fileCopy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
        int len;
        byte[] buf = new byte[1024];
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
}

