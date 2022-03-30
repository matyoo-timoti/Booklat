package com.example.booklat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
    static final String KEY_ID = "_id";
    static final String KEY_TITLE = "title";
    static final String KEY_YEAR = "year";
    static final String KEY_AUTHOR = "author";
    static final String TAG = "DBAdapter";
    static final String DATABASE_NAME = "BooklatDB";
    static final String DATABASE_TABLE = "books";
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_CREATE =
            "CREATE TABLE books (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " title TEXT NOT NULL, author TEXT NOT NULL, year INTEGER(4) NOT NULL);";
    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
            Log.w(TAG, "Upgrading databse from version " + oldVersion +
                    "to" + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS books");
            onCreate(db);
        }
    }


    // Opens the database

    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }


    // Closes the database

    public void close() {
        DBHelper.close();
    }


    // Insert book into the database

    public long insertBook(String title, String author, int year) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_AUTHOR, author);
        initialValues.put(KEY_YEAR, year);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }


    // Retrieve all the books

    public Cursor getAllBooks() {
        return db.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_TITLE, KEY_AUTHOR, KEY_YEAR},
                null, null, null, null, null);
    }


    // Retrieve a particular book

    public Cursor getBook(long ID) throws SQLException {
        Cursor cursor = db.query(true, DATABASE_TABLE, new String[]{KEY_ID, KEY_TITLE, KEY_AUTHOR, KEY_YEAR},
                KEY_TITLE + "=" + ID, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }


    // Update a book

    public boolean updateBook(long ID, String title, String author, int year) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_AUTHOR, author);
        args.put(KEY_YEAR, year);
        return db.update(DATABASE_TABLE, args, KEY_ID + "=" + ID, null) > 0;
    }


    // Delete a particular book

    public boolean deleteBook(long ID) {
        return db.delete(DATABASE_TABLE, KEY_ID + "=" + ID, null) > 0;
    }

}