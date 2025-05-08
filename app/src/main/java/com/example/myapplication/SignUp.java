package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.google.android.material.textfield.TextInputEditText;
import com.example.myapplication.ui.preferences.PreferencesActivity;


import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class SignUp extends AppCompatActivity {

    private Calendar birthDate; // משתנה לתאריך שנבחר

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

        // מעבר למסך הראשי לאחר הרשמה
        btnSignup.setOnClickListener(v -> {
            Intent intent = new Intent(SignUp.this, PreferencesActivity.class);
            startActivity(intent);
        });

        // פתיחת לוח שנה בלחיצה על שדה התאריך
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

                    String updatedDepartureDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;

                    TextInputEditText editDate = findViewById(R.id.edit_date);
                    editDate.setText(updatedDepartureDate);
                },
                year, month, day
        );

        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis()); // לא לאפשר תאריכים עבר
        datePickerDialog.show();
    }
}
