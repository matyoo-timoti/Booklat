package com.example.booklat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class SqLiteDatabase extends SQLiteOpenHelper {
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "BookDB";
    static final String DATABASE_TABLE = "Books";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_YEAR_PUBLISHED = "yearPublished";
    public static final String ORDER_DESCENDING = "DESC";
    public static final String ORDER_ASCENDING = "ASC";

    SqLiteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        try {
            String CREATE_BOOKS_TABLE = "CREATE TABLE "
                    + DATABASE_TABLE + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_TITLE + " TEXT," +
                    COLUMN_AUTHOR + " TEXT," +
                    COLUMN_YEAR_PUBLISHED + " INTEGER" + ")";
            database.execSQL(CREATE_BOOKS_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w("SQLiteDatabase", "Upgrading database from version " + oldVersion +
                "to" + newVersion + ", which will destroy all old data.");
        database.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(database);
    }

    ArrayList<Book> listOfBooks() {
        String sqlSelect = String.format("SELECT * FROM %s", DATABASE_TABLE);
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<Book> storeBooks = new ArrayList<>();
        Cursor cursor = database.rawQuery(sqlSelect, null);
        if (cursor.moveToFirst()) {
            do {
                int ID = Integer.parseInt(cursor.getString(0));
                String title = cursor.getString(1);
                String author = cursor.getString(2);
                String yearPublished = cursor.getString(3);
                storeBooks.add(new Book(ID, title, author, yearPublished));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return storeBooks;
    }
    ArrayList<Book> listOfBooks(String column, String order) {
        String sqlSelect = String.format("SELECT * FROM %s ORDER BY %s %s", DATABASE_TABLE, column, order);
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<Book> storeBooks = new ArrayList<>();
        Cursor cursor = database.rawQuery(sqlSelect, null);
        if (cursor.moveToFirst()) {
            do {
                int ID = Integer.parseInt(cursor.getString(0));
                String title = cursor.getString(1);
                String author = cursor.getString(2);
                String yearPublished = cursor.getString(3);
                storeBooks.add(new Book(ID, title, author, yearPublished));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return storeBooks;
    }

    void addBook(Book book) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, book.getTitle());
        values.put(COLUMN_AUTHOR, book.getAuthor());
        values.put(COLUMN_YEAR_PUBLISHED, book.getPublishedYear());
        SQLiteDatabase database = this.getWritableDatabase();
        database.insert(DATABASE_TABLE, null, values);
    }

    void updateBook(Book book) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, book.getTitle());
        values.put(COLUMN_AUTHOR, book.getAuthor());
        values.put(COLUMN_YEAR_PUBLISHED, book.getPublishedYear());
        SQLiteDatabase database = this.getWritableDatabase();
        database.update(DATABASE_TABLE, values, COLUMN_ID + " = ?", new String[]{String.valueOf(book.getId())});
    }

    void deleteBook(int ID) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(DATABASE_TABLE, COLUMN_ID + " = ?", new String[]{String.valueOf(ID)});
    }

    void deleteAll() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(DATABASE_TABLE, null, null);
        database.execSQL("VACUUM");
    }
}