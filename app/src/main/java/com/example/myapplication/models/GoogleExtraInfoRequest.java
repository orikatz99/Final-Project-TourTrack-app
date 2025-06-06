package com.example.myapplication.models;

public class GoogleExtraInfoRequest {
    private String phone;
    private String birthDate;

    public GoogleExtraInfoRequest(String phone, String birthDate) {
        this.phone = phone;
        this.birthDate = birthDate;
    }

    // Optional: getters (if needed by Retrofit or for testing)
    public String getPhone() {
        return phone;
    }

    public String getBirthDate() {
        return birthDate;
    }
}
