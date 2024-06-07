package com.rodriguezalonso.rommiesapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.rodriguezalonso.rommiesapp.MainActivity;
import com.rodriguezalonso.rommiesapp.R;

public class SplashActivity extends AppCompatActivity {

    private long time_splash=3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }, time_splash);
    }
}