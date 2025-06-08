package com.example.tourtrack.models;

import com.google.gson.annotations.SerializedName;

public class UserReportResponse {
    private String userId;
    @SerializedName("_id")

    private String reportId;
    private String photo;
    private String description;
    private String location;
    private String type;
    @SerializedName("updatedAt")
    private String updatedAt;
    private User user;

    public UserReportResponse() {
    }

    public UserReportResponse(String userId, String photo, String description, String location, String type, String reportId, String date) {
        this.userId = userId;
        this.photo = photo;
        this.description = description;
        this.location = location;
        this.type = type;
        this.reportId = reportId;
        this.updatedAt = date;
    }
    public String getUpdatedAt() {
        return updatedAt;
    }

    public UserReportResponse setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
    public String getUserId() {
        return userId;
    }
    public String getReportId() {
        return reportId;
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

    public UserReportResponse setDescription(String description) {
        this.description = description;
        return this;
    }

    public UserReportResponse setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public UserReportResponse setReportId(String reportId) {
        this.reportId = reportId;
        return this;
    }

    public UserReportResponse setPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    public UserReportResponse setLocation(String location) {
        this.location = location;
        return this;
    }

    public UserReportResponse setType(String type) {
        this.type = type;
        return this;
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
