package com.example.booklat;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Continue drawing the main activity views.
        bookListView = findViewById(R.id.booksListView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        bookListView.setLayoutManager(linearLayoutManager);
        bookListView.setHasFixedSize(true);

        //Populate the recycler view
        refresh();

        // Floating button
        ExtendedFloatingActionButton btnAddNew = findViewById(R.id.fabAddNew);
        btnAddNew.setOnClickListener(view -> addBookDialog());
    }

    //refresh recycler view
    public void refresh() {
        database = new SqLiteDatabase(this);
        ArrayList<Book> allBooks = database.listOfBooks();
        TextView txtViewEmptyList = findViewById(R.id.textViewNoItem);

        if (allBooks.size() > 0) {
            txtViewEmptyList.setVisibility(View.GONE);
            bookListView.setVisibility(View.VISIBLE);
            BookAdapter bkAdapter = new BookAdapter(this, allBooks);
            bookListView.setAdapter(bkAdapter);
        } else {
            txtViewEmptyList.setVisibility(View.VISIBLE);
            bookListView.setVisibility(View.GONE);
        }
    }

    // Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // Menu buttons
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Are you sure you want to delete all records? Records cannot be retrieved once deleted.")
                .setTitle("Delete Confirmation")
                .setNegativeButton("No", null);
        dialog.setPositiveButton("yes", (dialogInterface, i) -> {
            database.deleteAll();
            Toast.makeText(HomeActivity.this, "All records have been deleted", Toast.LENGTH_LONG).show();
            refresh();
        });
        dialog.show();
        return true;
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
        builder.setNegativeButton("CANCEL", (dialogInterface, i) -> Toast.makeText(HomeActivity.this, "Add book cancelled", Toast.LENGTH_SHORT).show());

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
                refresh();
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