package com.example.myapplication.models;

public class ReportRecommendation {
    private String userId;
    private String  description;
    private String photo;

    public ReportRecommendation() {
    }

    public ReportRecommendation(String userId, String description, String photo) {
        this.userId = userId;
        this.description = description;
        this.photo = photo;
    }


    public ReportRecommendation setDescription(String description) {
        this.description = description;
        return this;
    }

    public ReportRecommendation setPhoto(String photo) {
        this.photo = photo;
        return this;
    }
}
