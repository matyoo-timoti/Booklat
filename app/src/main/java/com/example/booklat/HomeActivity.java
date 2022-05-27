package com.example.booklat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private SqLiteDatabase database;
    private RecyclerView bookListView;
    private String[] sortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Continue drawing the main activity views.
        bookListView = findViewById(R.id.booksListView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        bookListView.setLayoutManager(linearLayoutManager);
        bookListView.setHasFixedSize(true);

        //Default order is by ID ascending
        sortOrder = new String[]{SqLiteDatabase.COLUMN_TITLE, SqLiteDatabase.ORDER_ASCENDING};

        //Populate the recycler view
        refresh(sortOrder);

        // Floating button
        ExtendedFloatingActionButton btnAddNew = findViewById(R.id.fabAddNew);
        btnAddNew.setOnClickListener(view -> addBookDialog());
    }

    //refresh recycler view
    public void refresh(String[] so) {
        database = new SqLiteDatabase(this);
        ArrayList<Book> allBooks = database.listOfBooks(so[0], so[1]);
        TextView txtViewEmptyList = findViewById(R.id.textViewNoItem);

        if (allBooks.size() > 0) {
            txtViewEmptyList.setVisibility(View.GONE);
            bookListView.setVisibility(View.VISIBLE);
            BookAdapter bkAdapter = new BookAdapter(this, allBooks);
            bookListView.setAdapter(bkAdapter);
            Toast.makeText(this, String.format("Sort by: %s %s", so[0], so[1]), Toast.LENGTH_SHORT).show();

        } else {
            txtViewEmptyList.setVisibility(View.VISIBLE);
            bookListView.setVisibility(View.GONE);
        }

    }

    // Inflate Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // Menu buttons
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.delAll:
                deleteAll();
                break;

            case R.id.Settings:
                Intent i = new Intent("Settings");
                startActivity(i);
                break;

            //Sub-menu items
            case R.id.sort_title_asc:
                sortOrder = new String[]{SqLiteDatabase.COLUMN_TITLE, SqLiteDatabase.ORDER_ASCENDING};
                break;

            case R.id.sort_title_desc:
                sortOrder = new String[]{SqLiteDatabase.COLUMN_TITLE, SqLiteDatabase.ORDER_DESCENDING};
                break;

            case R.id.sort_author_asc:
                sortOrder = new String[]{SqLiteDatabase.COLUMN_AUTHOR, SqLiteDatabase.ORDER_ASCENDING};
                break;

            case R.id.sort_author_desc:
                sortOrder = new String[]{SqLiteDatabase.COLUMN_AUTHOR, SqLiteDatabase.ORDER_DESCENDING};
                break;

            case R.id.sort_year_asc:
                sortOrder = new String[]{SqLiteDatabase.COLUMN_YEAR_PUBLISHED, SqLiteDatabase.ORDER_ASCENDING};
                break;

            case R.id.sort_year_desc:
                sortOrder = new String[]{SqLiteDatabase.COLUMN_YEAR_PUBLISHED, SqLiteDatabase.ORDER_DESCENDING};
                break;

            default:
                return false;
        }
        refresh(sortOrder);
        return true;
    }

    private void deleteAll() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Are you sure you want to delete all records? Records cannot be retrieved once deleted.")
                .setTitle("Delete Confirmation")
                .setNegativeButton("No", null);
        dialog.setPositiveButton("yes", (dialogInterface, i) -> {
            database = new SqLiteDatabase(this);
            ArrayList<Book> allBooks = database.listOfBooks(sortOrder[0], sortOrder[1]);
            if (allBooks.size() > 0) {
                database.deleteAll();
                Toast.makeText(HomeActivity.this, "All records have been deleted", Toast.LENGTH_LONG).show();
                refresh(sortOrder);
            }
            AlertDialog.Builder dialog1 = new AlertDialog.Builder(this)
                    .setTitle("ERROR")
                    .setMessage("There are no existing records. It may have already been deleted. Please insert a record/records to be able to use this operation.")
                    .setPositiveButton("Ok", null);
            dialog1.show();
        });
        dialog.show();
    }

    // Add new book entry dialog
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
        builder.setNegativeButton("CANCEL", null);

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
                Toast.makeText(this, String.format("New record: %s added", title), Toast.LENGTH_LONG).show();
                database.addBook(newBook);
                refresh(sortOrder);
                dialog.dismiss();
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