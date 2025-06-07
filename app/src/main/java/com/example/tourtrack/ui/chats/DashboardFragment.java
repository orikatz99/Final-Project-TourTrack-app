package com.example.tourtrack.ui.chats;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tourtrack.R;
import com.example.tourtrack.databinding.FragmentDashboardBinding;
import com.example.tourtrack.models.UserConnectedResponse;
import com.example.tourtrack.models.UsersResponse;
import com.example.tourtrack.network.ApiService;
import com.example.tourtrack.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import Adapter.connectedAdapter;
import Adapter.verticalPeopleAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private String token;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Get the token from SharedPreferences
        token = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                .getString("token", null);
        Log.d("ConnectedUsers", "Token value: " + token);

        if (token == null) {
            Log.e("DashboardFragment", "Token is null, cannot fetch connected users.");
            return root;
        }

        ApiService apiService = RetrofitClient.getApiService();

        // Setup horizontal RecyclerView for connected users
        binding.connectedFriendsRecycler.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        //load connected users
        loadConnectedUsers(apiService);

        // Setup vertical RecyclerView for all users
        binding.messagesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load all users
        loadAllUsers(apiService);



        return root;
    }

    private void loadAllUsers(ApiService apiService) {
        apiService.getAllUsers("Bearer " + token).enqueue(new Callback<List<UsersResponse>>() {
            @Override
            public void onResponse(Call<List<UsersResponse>> call, Response<List<UsersResponse>> response) {
                Log.d("users", "Response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    List<UsersResponse> users = response.body();
                    Log.d("users", "Users count: " + users.size());

                    List<verticalPeopleAdapter.Person> people = new ArrayList<>();
                    for (UsersResponse user : users) {
                        String firstName = user.getFirstName() != null ? user.getFirstName() : "Unknown";
                        String lastName = user.getLastName() != null ? user.getLastName() : "";
                        String fullName = firstName + " " + lastName;
                        String phone = user.getPhone() != null ? user.getPhone() : "No phone";

                        boolean allowPhone = user.isAllowPhoneCalls();
                        boolean allowWhatsapp = user.isEnableWhatsapp();

                        Log.d("privacy", "User: " + fullName + ", phone=" + allowPhone + ", whatsapp=" + allowWhatsapp);

                        people.add(new verticalPeopleAdapter.Person(
                                fullName,
                                R.drawable.user,
                                phone,
                                allowPhone,
                                allowWhatsapp
                        ));
                    }

                    people.sort((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()));
                    verticalPeopleAdapter adapter = new verticalPeopleAdapter(people);
                    binding.messagesRecycler.setAdapter(adapter);
                } else {
                    Log.e("DashboardFragment", "Failed to load users: " + response.code());
                }
            }


            @Override
            public void onFailure(Call<List<UsersResponse>> call, Throwable t) {
                Log.e("DashboardFragment", "Error fetching all users: " + t.getMessage());
            }
        });
    }

    private void loadConnectedUsers(ApiService apiService) {
        apiService.getConnectedUsers("Bearer " + token).enqueue(new Callback<List<UserConnectedResponse>>() {
            @Override
            public void onResponse(Call<List<UserConnectedResponse>> call, Response<List<UserConnectedResponse>> response) {
                Log.d("ConnectedUsers", "Response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    List<UserConnectedResponse> users = response.body();
                    Log.d("ConnectedUsers", "Users count: " + users.size());

                    List<connectedAdapter.Person> peopleList = new ArrayList<>();
                    for (UserConnectedResponse user : users) {
                        String fullName = user.getFullName() != null ? user.getFullName() : "Unknown";
                        peopleList.add(new connectedAdapter.Person(fullName, R.drawable.user));
                    }

                    connectedAdapter adapter = new connectedAdapter(peopleList);
                    binding.connectedFriendsRecycler.setAdapter(adapter);
                } else {
                    Log.e("DashboardFragment", "Failed to load connected users: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<UserConnectedResponse>> call, Throwable t) {
                Log.e("DashboardFragment", "Error fetching connected users: " + t.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
