package com.example.myapplication.network;

import com.example.myapplication.models.LocationUpdateRequest;
import com.example.myapplication.models.PreferencesRequest;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
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

    // ✅ get user info
    @GET("api/users/profile")
    Call<UserInfoResponse> getUserInfo(@Header("Authorization") String token);

    // get privacy settings
    @GET("api/users/privacy")
    Call<PrivacyResponseWrapper> getPrivacySettings(@Header("Authorization") String token);

    // update privacy settings
    @PUT("api/users/privacy")
    Call<Void> updatePrivacySettings(@Header("Authorization") String token, @Body Map<String, Object> body);

    @PUT("api/users/location/{id}")
    Call<Void> updateLocation(@Path("id") String userId, @Body LocationUpdateRequest location);

}
