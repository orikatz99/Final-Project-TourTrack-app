package com.example.myapplication.network;

import com.example.myapplication.models.PreferencesRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @PUT("api/users/preferences/{id}")
    Call<Void> updatePreferences(@Path("id") String userId, @Body PreferencesRequest preferences);
}
