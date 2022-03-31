package com.example.booklat;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpsertBook extends AppCompatActivity {
    SqLiteDatabase database;
    Button btnSave, btnCancel;
    EditText titleField, authorField, yearPublishedField;
    String title, author;
    Integer year;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upsert);

        database = new SqLiteDatabase(this);

        titleField = findViewById(R.id.txtFieldTitle);
        authorField = findViewById(R.id.txtFieldName);
        yearPublishedField = findViewById(R.id.txtFieldYearPub);


        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(view -> {
            if (isEmpty(titleField) || isEmpty(authorField) || isEmpty(yearPublishedField)) {
                Toast.makeText(this, "There should be no empty fields.", Toast.LENGTH_LONG).show();
            }

        });


        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(view -> {
            finish(); // Destroy current activity
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