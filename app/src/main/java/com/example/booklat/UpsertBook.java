package com.example.booklat;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpsertBook extends AppCompatActivity {

    Button btnSave, btnCancel;
    EditText edTxtTitle, edTxtAuthor, edTxtYear;
    String title, author;
    Integer year;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upsert);

        SqLiteDatabase SQLiteDatabase = new SqLiteDatabase(this);

        edTxtTitle = findViewById(R.id.editTextTitle);
        edTxtAuthor = findViewById(R.id.editTextAuthor);
        edTxtYear = findViewById(R.id.editTextYear);


        // Save button Listener

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(view -> {

            if (isEmpty(edTxtTitle) || isEmpty(edTxtAuthor) || isEmpty(edTxtYear)) {
                Toast.makeText(this, "There should be no empty fields.", Toast.LENGTH_LONG).show();
                return;
            }

            // Open database connection.
            SQLiteDatabase.open();

            // Add the information from input to the database.
            long id = SQLiteDatabase.insertBook(title, author, Integer.parseInt(String.valueOf(year)));


            // Close database connection.
            SQLiteDatabase.close();
        });


        // Cancel button listener

        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(view -> {


            // Destroy activity and go back to previous activity.

            finish();
        });
    }

    private boolean isEmpty(EditText edTxt) {
        if (TextUtils.isEmpty(edTxt.getText())) {
            edTxt.setError("This field cannot be empty.");
            return true;
        }
        return false;
    }
}