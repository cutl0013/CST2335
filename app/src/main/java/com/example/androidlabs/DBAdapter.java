package com.example.androidlabs;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter extends SQLiteOpenHelper {
    static final String KEY_MESSAGEID = "_id";
    static final String KEY_MESSAGE = "message";
    static final String KEY_SEND = "isSent";
    // create database
    static final String TAG = "DBAdapter";
    static final String DATABASE_NAME = "DbMessage";
    static final String DATABASE_TABLE = "storeMessage";
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_CREATE =
            "create table " + DATABASE_TABLE + " (" + KEY_MESSAGEID + " integer primary key autoincrement, "
                    + KEY_MESSAGE + " TEXT not null, " + KEY_SEND + " BIT not null);";


    public DBAdapter(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(DATABASE_CREATE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }

    //---opens the database---

    //---closes the database---

    //---insert a message into the database---
    public boolean insertMessage(String message, boolean isSent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_MESSAGE, message);
        if (isSent) {
            initialValues.put(KEY_SEND, 0);
        } else {
            initialValues.put(KEY_SEND, 1);
        }
        long result = db.insert(DATABASE_TABLE, null, initialValues);
        return result != -1;
    }


    public Cursor getAllMessages() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select * from " + DATABASE_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        printCursor(cursor);
        return cursor;
    }

    public void printCursor(Cursor cursor) {
        SQLiteDatabase db = this.getReadableDatabase();
        Log.v("Database Version:", Integer.toString(db.getVersion()));
        Log.v("Number of columns: ", Integer.toString(cursor.getColumnCount()));
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            Log.v("Column " + (i + 1) + ": ", cursor.getColumnName(i));
        }
        Log.v("Number of rows:", Integer.toString(cursor.getCount()));
        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));


    }

}