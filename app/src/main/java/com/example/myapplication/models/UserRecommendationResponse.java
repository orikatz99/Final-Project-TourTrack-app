package com.example.myapplication.models;

public class UserRecommendationResponse {
    private String userId;
    private String  recommend_id;
    private String  description;
    private String photo;
    private String location;
    private  String date;

    public UserRecommendationResponse() {
    }

    public UserRecommendationResponse(String userId, String description, String photo, String recommend_id, String location, String date) {
        this.userId = userId;
        this.description = description;
        this.photo = photo;
        this.recommend_id = recommend_id;
        this.location = location;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public UserRecommendationResponse setDate(String date) {
        this.date = date;
        return this;
    }

    public String getRecommend_id() {
        return recommend_id;
    }

    public UserRecommendationResponse setRecommend_id(String recommend_id) {
        this.recommend_id = recommend_id;
        return this;
    }

    public UserRecommendationResponse setDescription(String description) {
        this.description = description;
        return this;
    }

    public UserRecommendationResponse setPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    public UserRecommendationResponse setLocation(String location) {
        this.location = location;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public String getUserId() {
        return userId;
    }
    public String getRecommendId() {
        return recommend_id;
    }

    public String getDescription() {
        return description;
    }

    public String getPhoto() {
        return photo;
    }
}
