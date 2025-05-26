package com.example.myapplication.models;

public class UsersResponse {
    private String _id;
    private String firstName;
    private String lastName;
    private String phone;

    public UsersResponse() {
    }

    public UsersResponse(String _id, String firstName, String lastName, String phone) {
        this._id = _id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public String getId() {
        return _id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
