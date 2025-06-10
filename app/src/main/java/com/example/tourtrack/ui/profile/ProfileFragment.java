package com.example.tourtrack.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.tourtrack.Login;
import com.example.tourtrack.R;
import com.example.tourtrack.network.ApiService;
import com.example.tourtrack.models.PrivacyResponseWrapper;
import com.example.tourtrack.network.RetrofitClient;
import com.example.tourtrack.models.UserInfoResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private TextView tv_email, tv_phone, tv_name,user_role;
    private CheckBox cb_location_sharing, cb_show_online_status, cb_allow_phone_calls,
            cb_enable_whatsapp, cb_show_email_to_others, cb_email_notifications, cb_push_notifications;
    private ImageButton ib_edit_notifications, ib_edit_privacy,ib_logout;
    private String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialize views
        initializeViews(view);

        // Get the token from SharedPreferences
        getToken();
            //buttons
        //ib_edit_notifications.setOnClickListener(v -> updateNotificationSettings());
        ib_edit_privacy.setOnClickListener(v -> updatePrivacySettings());
        //logout button

        ib_logout.setOnClickListener(v -> {
            requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                    .edit().clear().apply();

            Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
            vibrate(getContext());
            FirebaseAuth.getInstance().signOut();

            // Start LoginActivity and clear the back stack
            Intent intent = new Intent(requireContext(), Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //do not move back
            startActivity(intent);
        });


    }
    private void vibrate(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(100);
            }
        }
    }
    private void initializeViews(View view) {
        tv_email = view.findViewById(R.id.tv_email);
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_name = view.findViewById(R.id.tv_name);
        cb_location_sharing = view.findViewById(R.id.cb_location_sharing);
        cb_show_online_status = view.findViewById(R.id.cb_online_status);
        cb_allow_phone_calls = view.findViewById(R.id.cb_allow_calls);
        cb_enable_whatsapp = view.findViewById(R.id.cb_whatsapp_messaging);
        cb_show_email_to_others = view.findViewById(R.id.cb_show_email);
        //cb_email_notifications = view.findViewById(R.id.cb_email_notifications);
        // cb_push_notifications = view.findViewById(R.id.cb_push_notifications);
        //ib_edit_notifications = view.findViewById(R.id.IB_edit_notifications);
        ib_edit_privacy = view.findViewById(R.id.IB_edit_privacy);
        user_role = view.findViewById(R.id.user_role);
        ib_logout = view.findViewById(R.id.IB_logOut);
    }
    private void getToken() {
        token = requireActivity().getSharedPreferences("MyAppPrefs", getContext().MODE_PRIVATE)
                .getString("token", null);

        if (token != null) {
            fetchUserInfo(token);
            fetchPrivacySettings(token);
        } else {
            tv_email.setText("User not logged in");
            tv_phone.setText("");
            tv_name.setText("");
        }
    }

    private void fetchUserInfo(String token) {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getUserInfo("Bearer " + token).enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserInfoResponse user = response.body();
                    tv_email.setText("Email: " + user.getEmail());
                    tv_phone.setText("Phone: " + user.getPhone());
                    tv_name.setText(user.getName());
                    user_role.setText(user.getRole());
                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                tv_email.setText("Error: " + t.getMessage());
                tv_phone.setText("");
            }
        });
    }

    private void fetchPrivacySettings(String token) {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getPrivacySettings("Bearer " + token).enqueue(new Callback<PrivacyResponseWrapper>() {
            @Override
            public void onResponse(Call<PrivacyResponseWrapper> call, Response<PrivacyResponseWrapper> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PrivacyResponseWrapper.PrivacyData data = response.body().getData();
                    if (data != null) {
                        PrivacyResponseWrapper.PrivacySettings p = data.getPrivacySettings();
                        PrivacyResponseWrapper.NotificationSettings n = data.getNotificationsSettings();

                        cb_location_sharing.setChecked(p.isLocationSharing());
                        cb_show_online_status.setChecked(p.isShowOnlineStatus());
                        cb_allow_phone_calls.setChecked(p.isAllowPhoneCalls());
                        cb_enable_whatsapp.setChecked(p.isEnableWhatsapp());
                        cb_show_email_to_others.setChecked(p.isShowEmailToOthers());

                       // cb_email_notifications.setChecked(n.isEmailNotifications());
                      //  cb_push_notifications.setChecked(n.isPushNotifications());
                    }
                }
            }

            @Override
            public void onFailure(Call<PrivacyResponseWrapper> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load privacy settings", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePrivacySettings() {
        ApiService apiService = RetrofitClient.getApiService();

        Map<String, Object> body = new HashMap<>();
        Map<String, Boolean> privacy = new HashMap<>();
        privacy.put("LocationSharing", cb_location_sharing.isChecked());
        privacy.put("ShowOnlineStatus", cb_show_online_status.isChecked());
        privacy.put("AllowPhoneCalls", cb_allow_phone_calls.isChecked());
        privacy.put("EnableWhatsapp", cb_enable_whatsapp.isChecked());
        privacy.put("ShowEmailToOthers", cb_show_email_to_others.isChecked());

        body.put("privacySettings", privacy);

        apiService.updatePrivacySettings("Bearer " + token, body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getContext(), "Privacy updated", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error updating privacy", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateNotificationSettings() {
        ApiService apiService = RetrofitClient.getApiService();

        Map<String, Object> body = new HashMap<>();
        Map<String, Boolean> notifications = new HashMap<>();
        notifications.put("emailNotifications", cb_email_notifications.isChecked());
        notifications.put("pushNotifications", cb_push_notifications.isChecked());

        body.put("notificationsSettings", notifications);

        apiService.updatePrivacySettings("Bearer " + token, body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getContext(), "Notifications updated", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error updating notifications", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
