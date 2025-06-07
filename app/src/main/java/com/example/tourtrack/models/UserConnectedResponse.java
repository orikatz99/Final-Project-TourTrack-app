package com.example.tourtrack.models;

public class UserConnectedResponse {

    private User userId; // this is populated from the server
    private String user_picture;

    public String getFullName() {
        if (userId == null) return "Unknown";
        return userId.getFirstName() + " " + userId.getLastName();
    }

    public String getUserPicture() {
        return user_picture;
    }

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
