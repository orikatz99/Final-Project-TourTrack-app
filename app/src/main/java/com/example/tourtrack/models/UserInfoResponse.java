package com.example.tourtrack.models;

import java.util.List;

public class UserInfoResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private List<String> preferences;

    public UserInfoResponse(String firstName, String lastName, String email, String phone, List<String> preferences) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.preferences = preferences;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public List<String> getPreferences() {
        return preferences;
    }
}
