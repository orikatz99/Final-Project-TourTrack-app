package com.example.myapplication.network;

public class SignUpRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String birthDate; // בפורמט טקסט – לדוגמה "01/01/2000"

    public SignUpRequest(String firstName, String lastName, String email, String password, String phone, String birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.birthDate = birthDate;
    }
}
