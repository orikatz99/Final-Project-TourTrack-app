package com.example.tourtrack.models;

public class ReportRequest {
    private String userId;

    private String photo;
    private String description;
    private String location;
    private String type;

    public ReportRequest() {
    }

    public ReportRequest(String userId, String photo, String description, String location, String type) {
        this.userId = userId;
        this.photo = photo;
        this.description = description;
        this.location = location;
        this.type = type;
    }


    public ReportRequest setPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    public ReportRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public ReportRequest setLocation(String location) {
        this.location = location;
        return this;
    }

    public ReportRequest setType(String type) {
        this.type = type;
        return this;
    }
}
