package com.example.tourtrack.models;

import com.google.gson.annotations.SerializedName;

public class UserRecommendationResponse {
    private String userId;
    @SerializedName("_id")

    private String  recommend_id;
    private String  description;
    private String photo;
    private String location;
    @SerializedName("updatedAt")


    private  String updatedAt;
    private User user;
    public UserRecommendationResponse() {
    }

    public UserRecommendationResponse(String userId, String description, String photo, String recommend_id, String location, String updatedAt) {
        this.userId = userId;
        this.description = description;
        this.photo = photo;
        this.recommend_id = recommend_id;
        this.location = location;
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public UserRecommendationResponse setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
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

    public String getFullName(){
        if (user != null) {
            return user.getFirstName() + " " + user.getLastName();
        } else {
            return "Unknown User";
        }    }

    public static class User {
        private String firstName;
        private String lastName;

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }
    }
}

