package com.example.tourtrack.models;

public class UsersResponse {
    private String _id;
    private String firstName;
    private String lastName;
    private String phone;
    private boolean allowPhoneCalls;
    private boolean enableWhatsapp;


    public UsersResponse() {
    }

    public UsersResponse(String _id, String firstName, String lastName, String phone,
                         boolean allowPhoneCalls, boolean enableWhatsapp) {
        this._id = _id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.allowPhoneCalls = allowPhoneCalls;
        this.enableWhatsapp = enableWhatsapp;
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
    public boolean isAllowPhoneCalls() {
        return allowPhoneCalls;
    }
    public boolean isEnableWhatsapp() {
        return enableWhatsapp;
    }
}
