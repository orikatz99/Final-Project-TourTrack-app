package com.example.myapplication.models;

import com.google.gson.annotations.SerializedName;

public class Rain {

    @SerializedName("1h")
    private double oneHour;

    public double getOneHour() {
        return oneHour;
    }
}

