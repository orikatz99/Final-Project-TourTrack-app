package com.example.myapplication.network;
public class SignUpResponse {
    private String message;
    private String token;
    private UserDTO user;

    public String getMessage() { return message; }
    public String getToken() { return token; }
    public UserDTO getUser() { return user; }

    public static class UserDTO {
        private String id;
        private String email;
        private String firstName;
        private String[] preferences;

        public String getId() { return id; }
        public String getEmail() { return email; }
        public String getFirstName() { return firstName; }
        public String[] getPreferences() { return preferences; }
    }
}
