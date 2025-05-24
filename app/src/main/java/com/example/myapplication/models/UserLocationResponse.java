package com.example.myapplication.models;

import java.util.List;

public class UserLocationResponse {
    private User userId; // ← השתנה: מ-String לאובייקט
    private Location location;

    public String getFullName() {
        if (userId == null) return "Unknown";
        return userId.firstName + " " + userId.lastName;
    }

    public double getLat() {
        return location != null && location.coordinates != null ? location.coordinates.get(1) : 0;
    }

    public double getLng() {
        return location != null && location.coordinates != null ? location.coordinates.get(0) : 0;
    }

    public static class Location {
        public String type;
        public List<Double> coordinates;
    }

    public static class User {
        public String firstName;
        public String lastName;

        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
    }
}
