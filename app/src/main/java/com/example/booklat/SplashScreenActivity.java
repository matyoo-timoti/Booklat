package com.example.booklat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {
    int SPLASH_TIME_OUT = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Hide the title bar

        Objects.requireNonNull(getSupportActionBar()).hide();


        // Add delay before starting the main activity

        new Handler().postDelayed((Runnable) () -> {
            Intent mainActivity = new Intent(this, HomeActivity.class);
            startActivity(mainActivity);
            finish();
        }, SPLASH_TIME_OUT);
    }
}