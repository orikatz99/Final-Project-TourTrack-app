package com.example.myapplication.ui.reports;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentReportsBinding;
import com.example.myapplication.models.ReportRequest;
import com.example.myapplication.models.UserReportResponse;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import Adapter.ReportAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportsFragment extends Fragment {

    private FragmentReportsBinding binding;
    private String token;
    private List<UserReportResponse> reportList = new ArrayList<>();
    private ReportAdapter reportAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReportsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize spinner
        setupSpinner();

        // Load token from SharedPreferences FIRST
        token = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                .getString("token", null);

        if (token == null) {
            Toast.makeText(getContext(), "Authentication token not found", Toast.LENGTH_SHORT).show();
            return root;
        }


        binding.recyclerViewReports.setLayoutManager(new LinearLayoutManager(getContext()));
        reportAdapter = new ReportAdapter(requireContext(), reportList, token);
        binding.recyclerViewReports.setAdapter(reportAdapter);


        // Load reports from server
        loadReports();

        // Handle send report button click
        binding.btnSendReport.setOnClickListener(v -> sendReport());

        return root;
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.danger_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerReportType.setAdapter(adapter);
    }

    private void loadReports() {
        ApiService apiService = RetrofitClient.getApiServiceWithAuth(token);

        apiService.getReports().enqueue(new Callback<List<UserReportResponse>>() {
            @Override
            public void onResponse(Call<List<UserReportResponse>> call, Response<List<UserReportResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reportList.clear();
                    reportList.addAll(response.body());
                    reportAdapter.notifyDataSetChanged();
                    Log.d("ReportsFragment", "Loaded " + reportList.size() + " reports");
                } else {
                    Toast.makeText(getContext(), "Failed to load reports", Toast.LENGTH_SHORT).show();
                    Log.e("ReportsFragment", "Load reports failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<UserReportResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ReportsFragment", "Load reports failure", t);
            }
        });
    }

    private void sendReport() {
        String selectedType = binding.spinnerReportType.getSelectedItem().toString();
        String location = binding.etReportLocation.getText().toString().trim();
        String description = binding.etProblemDescription.getText().toString().trim();

        if (selectedType.isEmpty() || location.isEmpty() || description.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        ReportRequest newReport = new ReportRequest(
                null,
                null,
                description,
                location,
                selectedType
        );

        ApiService apiService = RetrofitClient.getApiServiceWithAuth(token);

        // **גם כאן ללא פרמטר token**
        apiService.sendReport(newReport).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Report sent successfully", Toast.LENGTH_SHORT).show();
                    loadReports();
                } else {
                    Toast.makeText(getContext(), "Failed to send report", Toast.LENGTH_SHORT).show();
                    Log.e("ReportsFragment", "Send report failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ReportsFragment", "Send report failure", t);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
