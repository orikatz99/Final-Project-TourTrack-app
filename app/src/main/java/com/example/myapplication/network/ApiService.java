package com.example.myapplication.network;

import com.example.myapplication.models.PreferencesRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @PUT("api/users/preferences")
    Call<Void> updatePreferences(@Body PreferencesRequest preferences);


    @POST("api/users/signup")
    Call<SignUpResponse> signUp(@Body SignUpRequest signUpRequest);

    // ✅ login
    @POST("api/users/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
