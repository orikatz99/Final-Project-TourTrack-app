package com.example.tourtrack.models;
public class LoginResponse {
    private String token;
    private UserDTO user;

    public String getToken() {
        return token;
    }

    public UserDTO getUser() {
        return user;
    }

    public static class UserDTO {
        private String id;
        private String email;

        public String getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }
    }
}
