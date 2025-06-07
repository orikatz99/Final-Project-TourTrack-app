package com.example.tourtrack.models;

import java.util.List;

public class RouteModel {
    private String name;
    private String difficulty;
    private int lengthKm;
    private List<String> tags;
    private String imageUrl;
    private String description;
    private double latitude;
    private double longitude;
    private CurrentWeather currentWeather;

    // Getters
    public String getName() {
        return name;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getLengthKm() {
        return lengthKm;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public CurrentWeather getCurrentWeather() {
        return currentWeather;
    }

    // Nested class for weather
    public static class CurrentWeather {
        private String type;

        public String getType() {
            return type;
        }
    }
}
