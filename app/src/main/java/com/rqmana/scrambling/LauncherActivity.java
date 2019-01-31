package com.rqmana.scrambling;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        Utils.initPassword(this);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(LauncherActivity.this, LoginActivity.class));
                finish();
            }
        }, 2500);
    }
}
