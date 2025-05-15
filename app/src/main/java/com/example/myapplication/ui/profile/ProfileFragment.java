package com.example.myapplication.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.models.PrivacyResponseWrapper;
import com.example.myapplication.network.RetrofitClient;
import com.example.myapplication.models.UserInfoResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private TextView tv_email, tv_phone, tv_name;
    private CheckBox cb_location_sharing, cb_show_online_status, cb_allow_phone_calls,
            cb_enable_whatsapp, cb_show_email_to_others, cb_email_notifications, cb_push_notifications;
    private ImageButton ib_edit_notifications, ib_edit_privacy;
    private String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // UI references
        tv_email = view.findViewById(R.id.tv_email);
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_name = view.findViewById(R.id.tv_name);
        cb_location_sharing = view.findViewById(R.id.cb_location_sharing);
        cb_show_online_status = view.findViewById(R.id.cb_online_status);
        cb_allow_phone_calls = view.findViewById(R.id.cb_allow_calls);
        cb_enable_whatsapp = view.findViewById(R.id.cb_whatsapp_messaging);
        cb_show_email_to_others = view.findViewById(R.id.cb_show_email);
        cb_email_notifications = view.findViewById(R.id.cb_email_notifications);
        cb_push_notifications = view.findViewById(R.id.cb_push_notifications);
        ib_edit_notifications = view.findViewById(R.id.IB_edit_notifications);
        ib_edit_privacy = view.findViewById(R.id.IB_edit_privacy);

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

        ib_edit_privacy.setOnClickListener(v -> updatePrivacySettings());
        ib_edit_notifications.setOnClickListener(v -> updateNotificationSettings());
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

                        cb_email_notifications.setChecked(n.isEmailNotifications());
                        cb_push_notifications.setChecked(n.isPushNotifications());
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
