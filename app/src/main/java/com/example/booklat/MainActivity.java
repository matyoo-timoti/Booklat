package com.example.booklat;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DBAdapter dbAdapter;
    RecyclerView recyclerView;
    BooksAdapter booksAdapter;
    Button btnAddNew;
    List<Book> bookList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbAdapter = new DBAdapter(this);


        // Get all books from database

        dbAdapter.open();
        dbAdapter.insertBook("The Dark FrontierThe Dark Frontier", "Eric Ambler", 1936);
        dbAdapter.insertBook("GlamoramaGlamorama", "Bret Easton Ellis", 1998);
        dbAdapter.insertBook("Journey to the Center of the Earth", "Jules Verne", 1864);
        dbAdapter.insertBook("The 39 Steps", "John Buchan", 1915);
        dbAdapter.close();

        dbAdapter.open();
        Cursor c = dbAdapter.getAllBooks();
        if (c.moveToFirst()) {
            do {
                long id = Long.parseLong(c.getString(0));
                String title = c.getString(1);
                String author = c.getString(2);
                int year = Integer.parseInt(c.getString(3));
                Book book = new Book(id, title, author, year);
                bookList.add(book);
            } while (c.moveToNext());
        }
        dbAdapter.close();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(booksAdapter);

        // Add listener for add new button

        btnAddNew = findViewById(R.id.btnAddNewBook);
        btnAddNew.setOnClickListener(view -> {
            Intent intent = new Intent(this, UpsertBook.class);
            startActivity(intent);
        });

    }


}