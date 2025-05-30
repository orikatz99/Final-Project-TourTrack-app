package com.example.myapplication.models;

public class UserRecommendation {
    private String userId;
    private String  recommend_id;
    private String  description;
    private String photo;

    public UserRecommendation() {
    }

    public UserRecommendation(String userId, String description, String photo,String recommend_id) {
        this.userId = userId;
        this.description = description;
        this.photo = photo;
        this.recommend_id = recommend_id;
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
