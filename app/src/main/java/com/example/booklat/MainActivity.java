package com.example.booklat;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SqLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView bookListView = findViewById(R.id.booksListView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        bookListView.setLayoutManager(linearLayoutManager);
        bookListView.setHasFixedSize(true);

        database = new SqLiteDatabase(this);
        ArrayList<Book> allBooks = database.listOfBooks();
        TextView noShow = findViewById(R.id.textViewNoItem);
        if (allBooks.size() > 0) {
            bookListView.setVisibility(View.VISIBLE);
            BookAdapter bkAdapter = new BookAdapter(this, allBooks);
            bookListView.setAdapter(bkAdapter);
            noShow.setVisibility(View.GONE);
        } else {
            bookListView.setVisibility(View.GONE);
            noShow.setVisibility(View.VISIBLE);
        }

        FloatingActionButton btnAddNew = findViewById(R.id.fabAddNew);
        btnAddNew.setOnClickListener(view -> addBookDialog());
    }

    private void addBookDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.dialog_add_book_layout, null);
        final EditText titleField = subView.findViewById(R.id.titleField);
        final EditText authorField = subView.findViewById(R.id.authorField);
        final EditText yearPubField = subView.findViewById(R.id.yearPubField);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Book");
        builder.setView(subView);
        builder.create();
        builder.setPositiveButton("ADD BOOK", (dialogInterface, i) -> {
            if (isEmpty(titleField) || isEmpty(authorField) || isEmpty(yearPubField)) {
                Toast.makeText(this, "There should be no empty fields.", Toast.LENGTH_LONG).show();
            } else {
                final String title = titleField.getText().toString();
                final String author = authorField.getText().toString();
                final int yearPub = Integer.parseInt(yearPubField.getText().toString());
                Book book = new Book(title, author, yearPub);
                database.addBook(book);
                finish();
                startActivity(getIntent());
            }
        });
        builder.setNegativeButton("CANCEL", (dialogInterface, i) -> Toast.makeText(MainActivity.this, "Add book cancelled", Toast.LENGTH_SHORT).show());
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (database != null) {
            database.close();
        }
    }

    private boolean isEmpty(EditText edTxt) {
        if (TextUtils.isEmpty(edTxt.getText())) {
            edTxt.setError("This field cannot be empty.");
            return true;
        }
        return false;
    }

}