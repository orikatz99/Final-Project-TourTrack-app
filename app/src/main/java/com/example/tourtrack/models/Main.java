package com.example.tourtrack.models;

import com.google.gson.annotations.SerializedName;

public class Main {

    @SerializedName("temp")
    private double temp;

    @SerializedName("humidity")
    private int humidity;

    public double getTemp() {
        return temp;
    }

    public int getHumidity() {
        return humidity;
    }
}

