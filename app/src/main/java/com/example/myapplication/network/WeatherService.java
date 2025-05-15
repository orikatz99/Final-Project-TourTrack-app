package com.example.myapplication.network;

import com.example.myapplication.models.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {

    @GET("data/2.5/weather")
    Call<WeatherResponse> getWeatherByLocation(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("appid") String apiKey,
            @Query("units") String units, // e.g., "metric" for Celsius
            @Query("lang") String lang    // e.g., "he" for Hebrew
    );
}
