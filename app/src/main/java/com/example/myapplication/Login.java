package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.network.ApiService;
import com.example.myapplication.models.LoginRequest;
import com.example.myapplication.models.LoginResponse;
import com.example.myapplication.network.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    Button toRegisterBtn;
    Button login;
    TextInputEditText password_login;
    TextInputEditText email_login;
    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();

        // Navigate to registration screen
        toRegisterBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, SignUp.class);
            startActivity(intent);
            finish(); // Close login screen so user can't return to it with back button
        });

        // Handle login button click
        login.setOnClickListener(v -> {
            email = email_login.getText().toString();
            password = password_login.getText().toString();

            // Basic validation
            if (email.isEmpty()) {
                email_login.setError("Please enter your email");
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                email_login.setError("Invalid email address");
                return;
            }

            if (password.isEmpty()) {
                password_login.setError("Please enter your password");
                return;
            }

            // Send login request to server
            ApiService apiService = RetrofitClient.getApiService();
            LoginRequest loginRequest = new LoginRequest(email, password);

            apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Save token and userId in SharedPreferences
                        String token = response.body().getToken();
                        String userId = response.body().getUser().getId();

                        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                        prefs.edit()
                                .putString("token", token)
                                .putString("userId", userId)
                                .apply();

                        Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this, MainActivity.class));
                        finish(); // Prevent going back to login
                    } else {
                        Toast.makeText(Login.this, "Email or password incorrect", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(Login.this, "Server error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    // Bind all views
    private void findViews() {
        toRegisterBtn = findViewById(R.id.toRegisterBtn);
        login = findViewById(R.id.btn_log_in);
        password_login = findViewById(R.id.password_login);
        email_login = findViewById(R.id.email_login);
    }
}
