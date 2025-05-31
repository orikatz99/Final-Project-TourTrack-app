package com.example.myapplication.ui.reports;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentReportsBinding;
import com.example.myapplication.models.ReportRequest;
import com.example.myapplication.models.UserReportResponse;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
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
    private static final int IMAGE_PICK_CODE = 1001;
    private static final int CAMERA_CAPTURE_CODE = 1002;
    private static final int CAMERA_PERMISSION_CODE = 2001;

    private Uri selectedImageUri;



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
        // Handle image selection from camera or gallery
        cameraButtonClickListener();

        return root;
    }

    private void cameraButtonClickListener() {
        binding.IBCamera.setOnClickListener(v -> {
            String[] options = {"Choose from Gallery", "Take Photo"};
            new AlertDialog.Builder(requireContext())
                    .setTitle("Select Image")
                    .setItems(options, (dialog, which) -> {
                        if (which == 0) {
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(galleryIntent, IMAGE_PICK_CODE);
                            binding.tvAddPhoto.setText("Image Selected");
                        } else {
                            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                            } else {
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, CAMERA_CAPTURE_CODE);
                                binding.tvAddPhoto.setText("Image Selected");

                            }
                        }

                    })
                    .show();
        });

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

        if (selectedImageUri != null) {
            // Upload image first
            Log.d("ReportsFragment", "Selected image URI: " + selectedImageUri);

            uploadImageToFirebaseStorage(selectedImageUri, new OnImageUploadListener() {
                @Override
                public void onSuccess(String imageUrl) {
                    sendReportToServer(imageUrl, description, location, selectedType);
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(getContext(), "Image upload failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // No image selected, continue without photo
            sendReportToServer(null, description, location, selectedType);
        }
    }
    private void sendReportToServer(String imageUrl, String description, String location, String type) {
        ReportRequest newReport = new ReportRequest(
                null,           // userId
                imageUrl,       // photo URL from Firebase
                description,
                location,
                type
        );

        ApiService apiService = RetrofitClient.getApiServiceWithAuth(token);

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted – open camera
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_CAPTURE_CODE);
            } else {
                Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void uploadImageToFirebaseStorage(Uri imageUri, OnImageUploadListener listener) {
        if (imageUri == null) {
            listener.onFailure("Image Uri is null");
            Toast.makeText(getContext(), "Image Uri is null", Toast.LENGTH_SHORT).show();
            return;
        }

        String fileName = "reports/" + System.currentTimeMillis() + ".jpg";
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(fileName);

        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl()
                        .addOnSuccessListener(downloadUri -> {
                            String imageUrl = downloadUri.toString();
                            listener.onSuccess(imageUrl);
                        })
                        .addOnFailureListener(e -> listener.onFailure("Failed to get download URL: " + e.getMessage()))
                )
                .addOnFailureListener(e -> listener.onFailure("Upload failed: " + e.getMessage()));
    }
    public interface OnImageUploadListener {
        void onSuccess(String imageUrl);
        void onFailure(String errorMessage);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == IMAGE_PICK_CODE) {
                // Gallery selected image
                selectedImageUri = data.getData();
                binding.tvAddPhoto.setText("Image selected from gallery ✅");

            } else if (requestCode == CAMERA_CAPTURE_CODE) {
                // Captured image from camera returns Bitmap
                Bitmap photoBitmap = (Bitmap) data.getExtras().get("data");
                selectedImageUri = getImageUriFromBitmap(requireContext(), photoBitmap);
                binding.tvAddPhoto.setText("Image captured from camera ✅");
            }
        }
    }

    private Uri getImageUriFromBitmap(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "CapturedImage", null);
        return Uri.parse(path);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

