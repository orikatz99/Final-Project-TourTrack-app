package com.example.myapplication.models;

public class UserInfoResponse {
    private String email;
    private String phone;
    private String name;

    public UserInfoResponse(String email, String phone, String name) {
        this.email = email;
        this.phone = phone;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
