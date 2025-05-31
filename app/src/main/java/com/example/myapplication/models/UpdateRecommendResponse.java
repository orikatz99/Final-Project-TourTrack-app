package com.example.myapplication.models;

public class UpdateRecommendResponse {
    private String message;
    private UserRecommendationResponse recommend;

    public String getMessage() {
        return message;
    }

    public UserRecommendationResponse getRecommend() {
        return recommend;
    }
}
