package com.example.myapplication.models;

import java.util.List;

public class PreferencesRequest {
    private List<String> preferences;

    public PreferencesRequest(List<String> preferences) {
        this.preferences = preferences;
    }

    public List<String> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<String> preferences) {
        this.preferences = preferences;
    }
}
