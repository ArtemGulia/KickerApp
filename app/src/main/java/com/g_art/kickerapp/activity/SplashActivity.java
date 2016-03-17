package com.g_art.kickerapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Kicker App
 * Created by G_Art on 17/3/2016.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, KickerAppActivity.class);
        startActivity(intent);
        finish();
    }
}