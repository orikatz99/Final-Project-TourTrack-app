package com.example.myapplication.network;

public class LoginRequest {
    private String email;
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters and setters אם צריך – לא חובה ל־Retrofit
}
