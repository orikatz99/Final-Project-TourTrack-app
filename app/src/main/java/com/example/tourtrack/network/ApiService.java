package com.example.tourtrack.network;

import com.example.tourtrack.models.GoogleExtraInfoRequest;
import com.example.tourtrack.models.LocationUpdateRequest;
import com.example.tourtrack.models.LoginRequest;
import com.example.tourtrack.models.LoginResponse;
import com.example.tourtrack.models.PreferencesRequest;
import com.example.tourtrack.models.PrivacyResponseWrapper;
import com.example.tourtrack.models.RecommendRequest;
import com.example.tourtrack.models.ReportRequest;
import com.example.tourtrack.models.SignUpRequest;
import com.example.tourtrack.models.SignUpResponse;
import com.example.tourtrack.models.UpdateRecommendResponse;
import com.example.tourtrack.models.UpdateReportResponse;
import com.example.tourtrack.models.UserConnectedResponse;
import com.example.tourtrack.models.UserInfoResponse;
import com.example.tourtrack.models.UserLocationResponse;
import com.example.tourtrack.models.RouteModel;
import com.example.tourtrack.models.UserReportResponse;
import com.example.tourtrack.models.UserRecommendationResponse;
import com.example.tourtrack.models.UsersResponse;


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

    @GET("api/reports")
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

    // Google Sign Up
    @PUT("api/users/auth/google/complete-by-email/{email}")
    Call<LoginResponse> completeGoogleSignupByEmail(@Path("email") String email, @Body GoogleExtraInfoRequest request);


    @GET("api/users/exists/{email}")
    Call<Boolean> checkUserExists(@Path("email") String email);








}
