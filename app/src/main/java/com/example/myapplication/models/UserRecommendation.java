package com.example.myapplication.models;

public class UserRecommendation {
    private String userId;
    private String  description;
    private String photo;

    public UserRecommendation() {
    }

    public UserRecommendation(String userId, String description, String photo) {
        this.userId = userId;
        this.description = description;
        this.photo = photo;
    }

    public String getUserId() {
        return userId;
    }

    public String getDescription() {
        return description;
    }

    public String getPhoto() {
        return photo;
    }
}
