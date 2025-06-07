package com.example.tourtrack.models;

public class RecommendRequest {
    private String userId;

    private String photo;
    private String description;
    private String location;

    private String type;


    public RecommendRequest() {

    }

    public RecommendRequest(String userId, String photo, String description, String location, String type) {
        this.userId = userId;
        this.photo = photo;
        this.description = description;
        this.location = location;
        this.type = type;
    }

    public RecommendRequest setType(String type) {
        this.type = type;
        return this;
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


}

