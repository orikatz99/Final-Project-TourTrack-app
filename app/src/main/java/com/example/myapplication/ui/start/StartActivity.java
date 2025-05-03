package com.example.myapplication.ui.start;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.ui.preferences.PreferencesActivity;

public class StartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnSignIn = findViewById(R.id.btnSignIn);

        // LOGIN --> MAIN ACTIVITY (יגיע ל־HomeFragment כברירת מחדל)
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // לא לחזור אחורה
        });

        // SIGN IN --> PREFERENCES
        btnSignIn.setOnClickListener(v -> {
          /*  Intent intent = new Intent(StartActivity.this, PreferencesActivity.class);
            startActivity(intent);*/


            Intent intent = new Intent(StartActivity.this, PreferencesActivity.class);
            startActivity(intent);
        });
    }
}
