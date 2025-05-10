package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.network.SignUpRequest;
import com.example.myapplication.network.SignUpResponse;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button btnLogin = findViewById(R.id.btn_to_login);
        Button btnSignup = findViewById(R.id.btn_Sign_up);
        TextInputEditText editDate = findViewById(R.id.edit_date);

        // מעבר למסך התחברות
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(SignUp.this, Login.class);
            startActivity(intent);
        });

        // הרשמה
        btnSignup.setOnClickListener(v -> {
            String firstName = ((TextInputEditText) findViewById(R.id.firstN_signup)).getText().toString();
            String lastName = ((TextInputEditText) findViewById(R.id.lastN_signup)).getText().toString();
            String email = ((TextInputEditText) findViewById(R.id.email_signup)).getText().toString();
            String password = ((TextInputEditText) findViewById(R.id.password_signup)).getText().toString();
            String phone = ((TextInputEditText) findViewById(R.id.phone_signup)).getText().toString();
            String birthDateStr = editDate.getText().toString();

            SignUpRequest request = new SignUpRequest(firstName, lastName, email, password, phone, birthDateStr);
            ApiService apiService = RetrofitClient.getApiService();

            apiService.signUp(request).enqueue(new Callback<SignUpResponse>() {
                @Override
                public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String token = response.body().getToken();

                        // שמירת הטוקן בזיכרון
                        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                        prefs.edit().putString("token", token).apply();

                        // מעבר למסך העדפות
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

        // בחירת תאריך בלחיצה
        editDate.setOnClickListener(v -> openTripDatePicker());
    }

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
                    TextInputEditText editDate = findViewById(R.id.edit_date);
                    editDate.setText(updatedBirthDate);
                },
                year, month, day
        );

        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis()); // לא לאפשר תאריך עתידי
        datePickerDialog.show();
    }
}
