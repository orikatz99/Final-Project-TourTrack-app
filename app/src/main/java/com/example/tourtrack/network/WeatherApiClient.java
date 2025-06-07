package com.example.tourtrack.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherApiClient {

    private static final String BASE_URL = "https://api.openweathermap.org/";
    private static Retrofit retrofit = null;

    public static WeatherService getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(WeatherService.class);
    }
}
