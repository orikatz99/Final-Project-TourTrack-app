package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.models.GoogleExtraInfoRequest;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;
import com.example.myapplication.ui.preferences.PreferencesActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleExtraInfoActivity extends AppCompatActivity {

    private Calendar birthDate;
    private TextInputEditText editPhone, editDate;
    private Button continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_extra_info);

        editPhone = findViewById(R.id.phone_input);
        editDate = findViewById(R.id.birth_input);
        continueBtn = findViewById(R.id.continue_btn);

        editDate.setOnClickListener(v -> openDatePicker());

        continueBtn.setOnClickListener(v -> {
            String phone = editPhone.getText().toString().trim();

            if (phone.isEmpty()) {
                editPhone.setError("Please enter your phone number");
                return;
            }
            if (!phone.matches("\\d{10}")) {
                editPhone.setError("Phone number must be 10 digits");
                return;
            }
            if (birthDate == null) {
                editDate.setError("Please select your birth date");
                return;
            }

            String birthDateStr = birthDate.get(Calendar.DAY_OF_MONTH) + "/" +
                    (birthDate.get(Calendar.MONTH) + 1) + "/" +
                    birthDate.get(Calendar.YEAR);

            // ✅ get email from Firebase
            String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            if (email == null || email.isEmpty()) {
                Toast.makeText(this, "Email not found from Google sign-in", Toast.LENGTH_SHORT).show();
                return;
            }

            GoogleExtraInfoRequest request = new GoogleExtraInfoRequest(phone, birthDateStr);
            ApiService apiService = RetrofitClient.getApiService();

            apiService.completeGoogleSignupByEmail(email, request).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Intent intent = new Intent(GoogleExtraInfoActivity.this, PreferencesActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(GoogleExtraInfoActivity.this, "Failed to complete registration", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(GoogleExtraInfoActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void openDatePicker() {
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

        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }
}
