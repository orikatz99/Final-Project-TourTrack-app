package com.example.tourtrack.models;

import com.google.gson.annotations.SerializedName;

public class Weather {

    @SerializedName("id")
    private int conditionId;

    @SerializedName("description")
    private String description;

    public String getDescription() {
        return description;
    }

    public int getId() {
        return conditionId;
    }
}
