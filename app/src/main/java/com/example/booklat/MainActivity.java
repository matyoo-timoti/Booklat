package com.example.booklat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnAddNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Establish the database

        DBAdapter dbAdapter = new DBAdapter(this);


        // Open database connection

        dbAdapter.open();


        // Add a book

        long id = dbAdapter.insertBook("The Dark FrontierThe Dark Frontier", "Eric Ambler", 1936);
        id = dbAdapter.insertBook("GlamoramaGlamorama", "Bret Easton Ellis", 1998);
        id = dbAdapter.insertBook("Journey to the Center of the Earth", "Jules Verne", 1864);
        id = dbAdapter.insertBook("The 39 Steps", "John Buchan", 1915);


        // Close database connection

        dbAdapter.close();


        btnAddNew = findViewById(R.id.btnAddNewBook);

        btnAddNew.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddEditEntry.class);
            startActivity(intent);
        });

    }


}