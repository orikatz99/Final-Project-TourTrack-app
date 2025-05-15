package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.models.SignUpRequest;
import com.example.myapplication.models.SignUpResponse;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;
import com.example.myapplication.ui.preferences.PreferencesActivity;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    private Calendar birthDate;
    private TextInputEditText editFirstN, editLastN, editEmail, editPassword, editPhone, editDate;
    private Button btnLogin, btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize views
        findViews();

        // Navigate to login screen
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(SignUp.this, Login.class);
            startActivity(intent);
        });

        // Set date picker dialog on birth date field
        editDate.setOnClickListener(v -> openTripDatePicker());

        // Sign-up logic
        btnSignup.setOnClickListener(v -> {
            String firstName = editFirstN.getText().toString().trim();
            String lastName = editLastN.getText().toString().trim();
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();
            String phone = editPhone.getText().toString().trim();

            // Validate input fields
            if (firstName.isEmpty()) {
                editFirstN.setError("Please enter your first name");
                return;
            }
            if (lastName.isEmpty()) {
                editLastN.setError("Please enter your last name");
                return;
            }
            if (email.isEmpty()) {
                editEmail.setError("Please enter your email");
                return;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editEmail.setError("Invalid email address");
                return;
            }
            if (phone.isEmpty()) {
                editPhone.setError("Please enter your phone number");
                return;
            }
            if (!phone.matches("\\d{10}")) {
                editPhone.setError("Phone number must be 10 digits");
                return;
            }
            if (password.length() < 6) {
                editPassword.setError("Password must be at least 6 characters");
                return;
            }
            if (birthDate == null) {
                editDate.setError("Please select your birth date");
                return;
            }

            // Format birth date as string
            String birthDateStr = birthDate.get(Calendar.DAY_OF_MONTH) + "/" +
                    (birthDate.get(Calendar.MONTH) + 1) + "/" +
                    birthDate.get(Calendar.YEAR);

            // Create request object
            SignUpRequest request = new SignUpRequest(firstName, lastName, email, password, phone, birthDateStr);
            ApiService apiService = RetrofitClient.getApiService();

            // Call API
            apiService.signUp(request).enqueue(new Callback<SignUpResponse>() {
                @Override
                public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String token = response.body().getToken();

                        // Save token in SharedPreferences
                        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                        prefs.edit().putString("token", token).apply();
                        String userId = response.body().getUser().getId();
                        prefs.edit().putString("userId", userId).apply();


                        // Go to preferences screen
                        Intent intent = new Intent(SignUp.this, PreferencesActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignUp.this, "Signup failed: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SignUpResponse> call, Throwable t) {
                    Toast.makeText(SignUp.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    // Initialize UI components
    private void findViews() {
        editFirstN = findViewById(R.id.firstN_signup);
        editLastN = findViewById(R.id.lastN_signup);
        editEmail = findViewById(R.id.email_signup);
        editPassword = findViewById(R.id.password_signup);
        editPhone = findViewById(R.id.phone_signup);
        editDate = findViewById(R.id.edit_date);
        btnLogin = findViewById(R.id.btn_to_login);
        btnSignup = findViewById(R.id.btn_Sign_up);
    }

    // Show a date picker dialog for selecting birth date
    private void openTripDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    birthDate = Calendar.getInstance();
                    birthDate.set(year1, monthOfYear, dayOfMonth);

                    String updatedBirthDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    editDate.setText(updatedBirthDate);
                },
                year, month, day
        );

        // Disallow future dates
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }
}
