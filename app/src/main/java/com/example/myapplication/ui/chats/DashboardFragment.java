package com.example.myapplication.ui.chats;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentDashboardBinding;
import com.example.myapplication.models.UserConnectedResponse;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;

import java.util.ArrayList;
import java.util.Arrays;
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

        // Setup horizontal RecyclerView for connected users
        binding.connectedFriendsRecycler.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.connectedFriendsRecycler.setVisibility(View.VISIBLE); // Ensure visibility

        // Call API to get connected users
        ApiService apiService = RetrofitClient.getApiService();
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
                        Log.d("ConnectedUsers", "User: " + fullName);
                        peopleList.add(new connectedAdapter.Person(fullName, R.drawable.user));
                    }

                    connectedAdapter adapter = new connectedAdapter(peopleList);
                    binding.connectedFriendsRecycler.setAdapter(adapter);
                } else {
                    Log.e("DashboardFragment", "Failed to load connected users: " + response.code());
                    Log.e("DashboardFragment", "Response message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<UserConnectedResponse>> call, Throwable t) {
                Log.e("DashboardFragment", "Error fetching users: " + t.getMessage());
            }
        });

        // Static vertical RecyclerView for messages (not dynamic)
        List<verticalPeopleAdapter.Person> people = Arrays.asList(
                new verticalPeopleAdapter.Person("Shani", R.drawable.user),
                new verticalPeopleAdapter.Person("Ori", R.drawable.user),
                new verticalPeopleAdapter.Person("Lior", R.drawable.user),
                new verticalPeopleAdapter.Person("Ran", R.drawable.user),
                new verticalPeopleAdapter.Person("Eden", R.drawable.user),
                new verticalPeopleAdapter.Person("Romy", R.drawable.user)
        );

        verticalPeopleAdapter adapter = new verticalPeopleAdapter(people);
        binding.messagesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.messagesRecycler.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
