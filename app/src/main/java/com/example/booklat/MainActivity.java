package com.example.booklat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    SqLiteDatabase SQLiteDatabase;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    BooksAdapter booksAdapter;
    Button btnAddNew;
    List<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase = new SqLiteDatabase(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        booksAdapter = new BooksAdapter(this, bookList, recyclerView);
        recyclerView.setAdapter(booksAdapter);

        btnAddNew = findViewById(R.id.btnAddNewBook);
        btnAddNew.setOnClickListener(view -> {
            Intent intent = new Intent(this, UpsertBook.class);
            startActivity(intent);
        });

        SQLiteDatabase.close();
    }


}