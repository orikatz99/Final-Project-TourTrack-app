package com.example.myapplication.models;

public class RecommendRequest {
    private String userId;

    private String photo;
    private String description;
    private String location;
    private String date;

    public RecommendRequest() {

    }

    public RecommendRequest(String userId, String photo, String description, String location, String date) {
        this.userId = userId;
        this.photo = photo;
        this.description = description;
        this.location = location;
        this.date = date;
    }

    public RecommendRequest setPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    public RecommendRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public RecommendRequest setLocation(String location) {
        this.location = location;
        return this;
    }

    public RecommendRequest setDate(String date) {
        this.date = date;
        return this;
    }
}

