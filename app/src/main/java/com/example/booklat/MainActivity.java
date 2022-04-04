package com.example.booklat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SqLiteDatabase database;
    ArrayList<Book> allBooks;
    TextView txtViewEmptyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Continue drawing the main activity views.

        RecyclerView bookListView = findViewById(R.id.booksListView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        bookListView.setLayoutManager(linearLayoutManager);
        bookListView.setHasFixedSize(true);

        database = new SqLiteDatabase(this);
        allBooks = database.listOfBooks();
        txtViewEmptyList = findViewById(R.id.textViewNoItem);

        if (allBooks.size() > 0) {
            bookListView.setVisibility(View.VISIBLE);
            BookAdapter bkAdapter = new BookAdapter(this, allBooks);
            bookListView.setAdapter(bkAdapter);
            txtViewEmptyList.setVisibility(View.GONE);
        } else {
            bookListView.setVisibility(View.GONE);
            txtViewEmptyList.setVisibility(View.VISIBLE);
        }


        // Floating button handler

        FloatingActionButton btnAddNew = findViewById(R.id.fabAddNew);
        btnAddNew.setOnClickListener(view -> addBookDialog());
    }


    // Add new book entry dialog

    @SuppressLint("NotifyDataSetChanged")
    private void addBookDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.dialog_add_book_layout, null);

        final EditText titleField = subView.findViewById(R.id.titleField);
        final EditText authorField = subView.findViewById(R.id.authorField);
        final EditText yearPubField = subView.findViewById(R.id.yearPubField);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Book");
        builder.setView(subView);


        // Dialog positive button and instantiate positive button.

        builder.setPositiveButton("ADD BOOK", (dialogInterface, i) -> {

            // Do nothing here
        });


        // Dialog negative button

        builder.setNegativeButton("CANCEL", (dialogInterface, i) -> Toast.makeText(MainActivity.this, "Add book cancelled", Toast.LENGTH_SHORT).show());


        final AlertDialog dialog = builder.create();
        dialog.show();

        // Override the positive button handler after showing the dialog. This is to prevent termination of the dialog if the title field is empty.

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String title = titleField.getText().toString();
            String author = authorField.getText().toString();
            String yearPub = yearPubField.getText().toString();

            if (TextUtils.isEmpty(title)) {
                Toast.makeText(this, "Title field cannot be empty.", Toast.LENGTH_LONG).show();
                titleField.setError("This field cannot be empty.");
            } else {
                Book newBook = new Book(title, author, yearPub);
                Toast.makeText(this, "New book: " + title + " added", Toast.LENGTH_LONG).show();
                database.addBook(newBook);
                dialog.dismiss();
                finish();
                startActivity(getIntent());
            }
        });
    }


    // Close the database when activity is destroyed.

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (database != null) {
            database.close();
        }
    }

}