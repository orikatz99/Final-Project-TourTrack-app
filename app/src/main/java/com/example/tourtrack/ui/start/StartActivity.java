package com.example.tourtrack.ui.start;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tourtrack.Login;
import com.example.tourtrack.R;
import com.example.tourtrack.SignUp;

public class StartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnSignIn = findViewById(R.id.btnSignIn);

        // Start --> LOGIN
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, Login.class);
            startActivity(intent);
            finish();
        });

        // start --> sign up
        btnSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, SignUp.class);
            startActivity(intent);
        });
    }
}
