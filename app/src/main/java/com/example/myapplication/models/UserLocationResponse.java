
package com.example.myapplication.models;

import java.util.List;

public class UserLocationResponse {
    private String userId;
    private Location location;

    public String getUserId() {
        return userId;
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
}
