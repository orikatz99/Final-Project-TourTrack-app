package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;
import com.example.myapplication.network.SignUpRequest;
import com.example.myapplication.network.SignUpResponse;
import com.example.myapplication.ui.preferences.PreferencesActivity;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.Calendar;

public class SignUp extends AppCompatActivity {

    private Calendar birthDate; // משתנה לתאריך שנבחר
    TextInputEditText editFirstN;
    TextInputEditText editLastN;
    TextInputEditText editEmail;
    TextInputEditText editPassword ;
    TextInputEditText editPhone;
    Button btnLogin;
    Button btnSignup ;
    TextInputEditText editDate ;
    String firstName ;
    String lastName ;
    String email ;
    String password ;
    String phone ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        findViews();

        // calendar
        editDate.setOnClickListener(v -> openTripDatePicker());

        // Login button
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(SignUp.this, Login.class);
            startActivity(intent);
        });

      //sign up button
        signUpButton();


    }

    void signUpButton(){
        btnSignup.setOnClickListener(v -> {
             firstName = editFirstN.getText().toString();
             lastName = editLastN.getText().toString();
             email = editEmail.getText().toString();
             password = editPassword.getText().toString();
             phone = editPhone.getText().toString();


            if (firstName.isEmpty() ) {
                editFirstN.setError("Please Enter your first name");
                return;
            }
            if (lastName.isEmpty()) {
                editLastN.setError("Please Enter your last name");
                return;
            }
            if (email.isEmpty()) {
                editEmail.setError("Please Enter your email");
                return;
            }

            if (phone.isEmpty()) {
                editPhone.setError("Please Enter your phone number");
                return;
            }

            if (password.length() < 6) {
                editPassword.setError("Password must be at least 6 characters");
                return;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editEmail.setError("Invalid email address");
                return;
            }
            if (!phone.matches("\\d{10}")) {
                editPhone.setError("Invalid phone number");
                return;
            }
            if (birthDate == null) {
                editDate.setError("Please select a date");
                return;

            }
            successfulySignUp(btnSignup,firstName,lastName,email,password,phone,birthDate);
        });

    }

    void findViews() {
         editFirstN = findViewById(R.id.firstN_signup);
         editLastN = findViewById(R.id.lastN_signup);
         editEmail = findViewById(R.id.email_signup);
         editPassword = findViewById(R.id.password_signup);
         editPhone = findViewById(R.id.phone_signup);
         btnLogin = findViewById(R.id.btn_to_login);
         btnSignup = findViewById(R.id.btn_Sign_up);
         editDate = findViewById(R.id.edit_date);
    }
    public void successfulySignUp(Button btnSignup, String firstName, String lastName, String email, String password, String phone, Calendar birthDate) {
        String birthDateString = birthDate.get(Calendar.DAY_OF_MONTH) + "/" +
                (birthDate.get(Calendar.MONTH) + 1) + "/" +
                birthDate.get(Calendar.YEAR);

        SignUpRequest signUpRequest = new SignUpRequest(firstName, lastName, email, password, phone, birthDateString);
        ApiService apiService = RetrofitClient.getApiService();

        apiService.signUp(signUpRequest).enqueue(new retrofit2.Callback<SignUpResponse>() {
            @Override
            public void onResponse(retrofit2.Call<SignUpResponse> call, retrofit2.Response<SignUpResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(SignUp.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUp.this, PreferencesActivity.class));
                    finish(); // לא לחזור אחורה

                } else {
                    try {
                        String errorJson = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorJson);
                        String message = jsonObject.getString("message");
                        Toast.makeText(SignUp.this, message, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(SignUp.this, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<SignUpResponse> call, Throwable t) {
                Toast.makeText(SignUp.this, "Server error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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

                    String updatedDepartureDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;

                    TextInputEditText editDate = findViewById(R.id.edit_date);
                    editDate.setText(updatedDepartureDate);
                },
                year, month, day
        );
        //not to present days after the current date
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
}
