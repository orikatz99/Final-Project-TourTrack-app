package com.example.tourtrack.models;

public class GoogleExtraInfoRequest {
    private String firstName;
    private String lastName;
    private String phone;
    private String birthDate;

    public GoogleExtraInfoRequest(String firstName, String lastName, String phone, String birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.birthDate = birthDate;
    }

    // Getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getBirthDate() {
        return birthDate;
    }
}
