package com.example.myapplication.ui.preferences;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class PreferencesActivity extends AppCompatActivity {

    private List<String> selected = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        // חיבור כל כפתור לקטגוריה שלו
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
        setupSelectableButton(R.id.btnMuseum, "Museum");
        setupSelectableButton(R.id.btnValley, "Valley");

        // כפתור המשך
        Button btnContinue = findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(v -> {
            if (selected.isEmpty()) {
                Toast.makeText(this, "Please select at least one category", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Selected: " + selected, Toast.LENGTH_LONG).show();
                // כאן אפשר לעבור למסך הבא או לשמור את ההעדפות
            }
        });
    }

    // פונקציה שמגדירה התנהגות בחירה לכל כפתור
    private void setupSelectableButton(int buttonId, String category) {
        Button button = findViewById(buttonId);

        // צבע התחלה – צהוב בהיר
        button.setBackgroundColor(Color.parseColor("#FFF8DC"));
        button.setSelected(false);

        button.setOnClickListener(v -> {
            boolean isSelected = !v.isSelected();
            v.setSelected(isSelected);

            if (isSelected) {
                v.setBackgroundColor(Color.parseColor("#FFEE88")); // צהוב כהה
                selected.add(category);
            } else {
                v.setBackgroundColor(Color.parseColor("#FFF8DC")); // חזרה לצהוב בהיר
                selected.remove(category);
            }
        });
    }
}