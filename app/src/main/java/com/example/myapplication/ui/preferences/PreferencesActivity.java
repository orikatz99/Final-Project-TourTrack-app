package com.example.myapplication.ui.preferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.models.PreferencesRequest;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreferencesActivity extends AppCompatActivity {

    private List<String> selected = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        setupSelectableButton(R.id.btnForest, "Forest");
        setupSelectableButton(R.id.btnBeach, "Beach");
        setupSelectableButton(R.id.btnRiver, "River");
        setupSelectableButton(R.id.btnMountain, "Mountain");
        setupSelectableButton(R.id.btnDesert, "Desert");
        setupSelectableButton(R.id.btnLake, "Lake");
        setupSelectableButton(R.id.btnCave, "Cave");
        setupSelectableButton(R.id.btnAdventure, "Adventure");
        setupSelectableButton(R.id.btnCulture, "Culture");
        setupSelectableButton(R.id.btnWaterfall, "Waterfall");
        setupSelectableButton(R.id.btnHistorical, "Historical");
        setupSelectableButton(R.id.btnWildlife, "Wildlife");
        setupSelectableButton(R.id.btnScenic, "Scenic");
        setupSelectableButton(R.id.btnAgriculture, "Agriculture");
        setupSelectableButton(R.id.btnField, "Field");

        Button btnContinue = findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(v -> {
            if (selected.isEmpty()) {
                Toast.makeText(this, "Please select at least one category", Toast.LENGTH_SHORT).show();
            } else {
                // שליפת הטוקן מה־SharedPreferences
                SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                String token = prefs.getString("token", null);

                if (token == null) {
                    Toast.makeText(this, "Token not found. Please log in again.", Toast.LENGTH_SHORT).show();
                    return;
                }

                PreferencesRequest request = new PreferencesRequest(selected);
                ApiService apiService = RetrofitClient.getApiServiceWithAuth(token);

                apiService.updatePreferences(request).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(PreferencesActivity.this, "Preferences saved!", Toast.LENGTH_SHORT).show();

                            // מעבר ל־MainActivity
                            Intent intent = new Intent(PreferencesActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // סוגר את המסך הנוכחי כדי למנוע חזרה אליו
                        } else {
                            Toast.makeText(PreferencesActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(PreferencesActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void setupSelectableButton(int buttonId, String category) {
        Button button = findViewById(buttonId);
        button.setBackgroundColor(Color.parseColor("#FFF8DC"));
        button.setSelected(false);

        button.setOnClickListener(v -> {
            boolean isSelected = !v.isSelected();
            v.setSelected(isSelected);

            if (isSelected) {
                button.setBackgroundColor(Color.parseColor("#FFEE88"));
                selected.add(category);
            } else {
                button.setBackgroundColor(Color.parseColor("#FFF8DC"));
                selected.remove(category);
            }
        });
    }
}
