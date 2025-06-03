package com.example.myapplication.network;

import com.example.myapplication.models.LocationUpdateRequest;
import com.example.myapplication.models.LoginRequest;
import com.example.myapplication.models.LoginResponse;
import com.example.myapplication.models.PreferencesRequest;
import com.example.myapplication.models.PrivacyResponseWrapper;
import com.example.myapplication.models.RecommendRequest;
import com.example.myapplication.models.ReportRequest;
import com.example.myapplication.models.SignUpRequest;
import com.example.myapplication.models.SignUpResponse;
import com.example.myapplication.models.UpdateRecommendResponse;
import com.example.myapplication.models.UpdateReportResponse;
import com.example.myapplication.models.UserConnectedResponse;
import com.example.myapplication.models.UserInfoResponse;
import com.example.myapplication.models.UserLocationResponse;
import com.example.myapplication.models.RouteModel;
import com.example.myapplication.models.UserReportResponse;
import com.example.myapplication.models.UserRecommendationResponse;
import com.example.myapplication.models.UsersResponse;


import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    @GET("api/users/nearby-users/{id}")
    Call<List<UserLocationResponse>> getNearbyUsers(@Path("id") String userId);

    @GET("/api/routes")
    Call<List<RouteModel>> getAllRoutes();

    @GET("api/users/connected-users")
    Call<List<UserConnectedResponse>> getConnectedUsers(@Header("Authorization") String token);

    //get all users
    @GET("api/users")
    Call<List<UsersResponse>> getAllUsers(@Header("Authorization") String token);


    @POST("api/routes/recommendations")
    Call<List<RouteModel>> getRecommendations(@Body Map<String, Object> body);

    //user reports
    @POST("api/users/report")
    Call<Void> sendReport(@Body ReportRequest reportRequest);

    @GET("api/users/report")
    Call<List<UserReportResponse>> getReports();

    @GET("api/report")
    Call<List<UserReportResponse>> getAllReports();
    @GET("api/recommendations")
    Call<List<UserRecommendationResponse>> getAllRecommendations();


    @PUT("api/users/report/{id}")
    Call<UpdateReportResponse> updateRecommend(
            @Header("Authorization") String token,
            @Path("id") String reportId,
            @Body ReportRequest updatedReport
    );

    @DELETE("api/users/report/{id}")
    Call<Void> deleteReport( @Path("id") String reportId);

    //user recommendation
    @POST("api/users/recommendation")
    Call<Void> sendRecommendation(@Body RecommendRequest recommendRequest);

    @GET("api/users/recommendation")
    Call<List<UserRecommendationResponse>> getRecommendations(@Header("Authorization") String token);

    @PUT("api/users/recommendation/{id}")
    Call<UpdateRecommendResponse> updateRecommendation(
            @Header("Authorization") String token,
            @Path("id") String recommendationId,
            @Body RecommendRequest updatedRecommendation
    );

    @DELETE("api/users/recommendation/{id}")
    Call<Void> deleteRecommendation(@Header("Authorization") String token, @Path("id") String recommendationId);






}
