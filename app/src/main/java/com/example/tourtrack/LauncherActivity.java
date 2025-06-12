package com.example.tourtrack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class LauncherActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", null);

        Log.d("LauncherActivityTOKEN", "Token = " + token);

        if (token != null) {
            // User is already logged in
            startActivity(new Intent(this, MainActivity.class));
        } else {
            // User is not logged in
            startActivity(new Intent(this, com.example.tourtrack.ui.start.StartActivity.class));
        }

        // Close this launcher activity so it's not in the back stack
        finish();
    }
}
