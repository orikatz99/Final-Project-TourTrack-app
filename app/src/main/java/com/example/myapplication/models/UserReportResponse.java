package com.example.myapplication.models;

public class UserReportResponse {
    private String userId;
    private String photo;
    private String description;
    private String location;
    private String type;

    public UserReportResponse() {
    }

    public UserReportResponse(String userId, String photo, String description, String location, String type) {
        this.userId = userId;
        this.photo = photo;
        this.description = description;
        this.location = location;
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public String getPhoto() {
        return photo;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }
}
