package com.example.tourtrack.ui.reports;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tourtrack.R;
import com.example.tourtrack.databinding.FragmentReportsBinding;
import com.example.tourtrack.models.RecommendRequest;
import com.example.tourtrack.models.ReportRequest;
import com.example.tourtrack.models.UserRecommendationResponse;
import com.example.tourtrack.models.UserReportResponse;
import com.example.tourtrack.network.ApiService;
import com.example.tourtrack.network.RetrofitClient;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import Adapter.RecommendAdapter;
import Adapter.ReportAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportsFragment extends Fragment {

    private FragmentReportsBinding binding;
    private String token;
    private List<UserReportResponse> reportList = new ArrayList<>();
    private List<UserRecommendationResponse> recommendList = new ArrayList<>();

    private ReportAdapter reportAdapter;
    private RecommendAdapter recommendAdapter;
    private static final int IMAGE_PICK_REPORT = 1001;
    private static final int IMAGE_PICK_RECOMMEND = 1002;
    private static final int CAMERA_CAPTURE_REPORT = 1003;
    private static final int CAMERA_CAPTURE_RECOMMEND = 1004;


    private static final int CAMERA_PERMISSION_CODE = 2001;
    private static final int CAMERA_PERMISSION_REPORT = 2001;
    private static final int CAMERA_PERMISSION_RECOMMEND = 2002;

    private Uri selectedReportImageUri;
    private Uri selectedRecommendImageUri;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReportsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize spinners
        setupReportSpinner();
        setupRecommendSpinner();

        // Load token from SharedPreferences FIRST
        token = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                .getString("token", null);

        if (token == null) {
            Toast.makeText(getContext(), "Authentication token not found", Toast.LENGTH_SHORT).show();
            return root;
        }


        binding.recyclerViewReports.setLayoutManager(new LinearLayoutManager(getContext()));
        reportAdapter = new ReportAdapter(requireContext(), reportList, token);
        recommendAdapter = new RecommendAdapter(requireContext(), recommendList, token,true);
        binding.recyclerViewRecommendations.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.recyclerViewReports.setAdapter(reportAdapter);
        binding.recyclerViewRecommendations.setAdapter(recommendAdapter);


        // Load reports from server
        loadReports();
        // Load recommendations
         loadRecommendations();



        // Handle send report button click
        binding.btnSendReport.setOnClickListener(v -> sendReport());
        // Handle send recommend button click
        binding.btnShareTip.setOnClickListener(v -> sendRecommend());
        // Handle image selection from camera or gallery
        cameraReportButtonClickListener(binding.tvAddPhotoReport, CAMERA_CAPTURE_REPORT, IMAGE_PICK_REPORT);
        cameraRecommendButtonClickListener(binding.tvAddPhotoRecommend, CAMERA_CAPTURE_RECOMMEND, IMAGE_PICK_RECOMMEND);
        return root;
    }

    private void loadRecommendations() {
        ApiService apiService = RetrofitClient.getApiServiceWithAuth(token);

        apiService.getRecommendations(token).enqueue(new Callback<List<UserRecommendationResponse>>() {
            @Override
            public void onResponse(Call<List<UserRecommendationResponse>> call, Response<List<UserRecommendationResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recommendList.clear();
                    recommendList.addAll(response.body());
                    recommendAdapter.notifyDataSetChanged();
                    Log.d("ReportsFragment", "Loaded " + recommendList.size() + " recommends");
                    Toast.makeText(getContext(), "Recommendations loaded successfully", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(getContext(), "Failed to load recommends", Toast.LENGTH_SHORT).show();
                    Log.e("ReportsFragment", "Load recommends failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<UserRecommendationResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ReportsFragment", "Load recommends failure", t);
            }
        });
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


    private void cameraReportButtonClickListener(TextView tvAddPhoto,int cameraCode,int imagePickCode) {
        binding.IBCameraReport.setOnClickListener(v -> {
            String[] options = {"Choose from Gallery", "Take Photo"};
            new AlertDialog.Builder(requireContext())
                    .setTitle("Select Image")
                    .setItems(options, (dialog, which) -> {
                        if (which == 0) {
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(galleryIntent, imagePickCode);
                            tvAddPhoto.setText("Image Selected");
                        } else {
                            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                            } else {
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, cameraCode);
                                tvAddPhoto.setText("Image Selected");

                            }
                        }

                    })
                    .show();
        });

    }
    private void cameraRecommendButtonClickListener(TextView tvAddPhoto,int cameraCode,int imagePickCode) {
        binding.IBCameraRecommend.setOnClickListener(v -> {
            String[] options = {"Choose from Gallery", "Take Photo"};
            new AlertDialog.Builder(requireContext())
                    .setTitle("Select Image")
                    .setItems(options, (dialog, which) -> {
                        if (which == 0) {
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(galleryIntent, imagePickCode);
                            tvAddPhoto.setText("Image Selected");
                        } else {
                            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                            } else {
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, cameraCode);
                                tvAddPhoto.setText("Image Selected");

                            }
                        }

                    })
                    .show();
        });

    }

    private void setupReportSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.danger_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerReportType.setAdapter(adapter);
    }
    private void setupRecommendSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.recommendation_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerRecommendType.setAdapter(adapter);
    }




    private void sendReport() {
        String selectedType = binding.spinnerReportType.getSelectedItem().toString();
        String location = binding.etReportLocation.getText().toString().trim();
        String description = binding.etProblemDescription.getText().toString().trim();

        if (selectedType.isEmpty() || location.isEmpty() || description.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedReportImageUri != null) {
            // Upload image first
            Log.d("ReportsFragment", "Selected image URI: " + selectedReportImageUri);

            uploadImageToFirebaseStorage(selectedReportImageUri, "reports/",new OnImageUploadListener() {
                @Override
                public void onSuccess(String imageUrl, String folderName) {
                    sendReportToServer(imageUrl, description, location, selectedType);
                    Toast.makeText(getContext(), "Report send successfully", Toast.LENGTH_SHORT).show();

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

    private void sendRecommend(){
        String selectedType = binding.spinnerRecommendType.getSelectedItem().toString();
        String location = binding.etRecommendLocation.getText().toString().trim();
        String description = binding.etRecommendation.getText().toString().trim();

        if (selectedType.isEmpty() || location.isEmpty() || description.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedRecommendImageUri != null) {
            // Upload image first
            Log.d("ReportsFragment -", "Selected image URI - RECOMMENDATION: " + selectedRecommendImageUri);

            uploadImageToFirebaseStorage(selectedRecommendImageUri, "recommendations/", new OnImageUploadListener() {
                @Override
                public void onSuccess(String imageUrl, String folderName) {
                    sendRecommendToServer(imageUrl, description, location, selectedType);
                    Toast.makeText(getContext(), "Recommendation send successfully", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(getContext(), "Image upload for recommend failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // No image selected, continue without photo
            sendRecommendToServer(null, description, location, selectedType);
        }
    }

    private void sendRecommendToServer(String imageUrl, String description, String location, String type) {
        RecommendRequest newRecommend = new RecommendRequest(
                null,           // userId
                imageUrl,       // photo URL from Firebase
                description,
                location,
                type
        );

        ApiService apiService = RetrofitClient.getApiServiceWithAuth(token);

        apiService.sendRecommendation(newRecommend).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Recommend sent successfully", Toast.LENGTH_SHORT).show();
                    loadRecommendations();
                } else {
                    Toast.makeText(getContext(), "Failed to send recommend", Toast.LENGTH_SHORT).show();
                    Log.e("ReportsFragment", "Send recommend failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ReportsFragment", "Send recommend failure", t);
            }
        });
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

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (requestCode == CAMERA_PERMISSION_REPORT) {
                startActivityForResult(cameraIntent, CAMERA_CAPTURE_REPORT);

            } else if (requestCode == CAMERA_PERMISSION_RECOMMEND) {
                startActivityForResult(cameraIntent, CAMERA_CAPTURE_RECOMMEND);

            }
        } else {
            Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageToFirebaseStorage(Uri imageUri,String folderName, OnImageUploadListener listener) {
        if (imageUri == null) {
            listener.onFailure("Image Uri is null");
            Toast.makeText(getContext(), "Image Uri is null", Toast.LENGTH_SHORT).show();
            return;
        }

        String fileName = folderName + System.currentTimeMillis() + ".jpg";
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(fileName);

        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl()
                        .addOnSuccessListener(downloadUri -> {
                            String imageUrl = downloadUri.toString();
                            listener.onSuccess(imageUrl, folderName);
                        })
                        .addOnFailureListener(e -> listener.onFailure("Failed to get download URL: " + e.getMessage()))
                )
                .addOnFailureListener(e -> listener.onFailure("Upload failed: " + e.getMessage()));
    }
    public interface OnImageUploadListener {
        void onSuccess(String imageUrl, String folderName);
        void onFailure(String errorMessage);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == IMAGE_PICK_REPORT) {
                selectedReportImageUri = data.getData();
                binding.tvAddPhotoReport.setText("Image selected from gallery ✅");

            } else if (requestCode == IMAGE_PICK_RECOMMEND) {
                selectedRecommendImageUri = data.getData();
                binding.tvAddPhotoRecommend.setText("Image selected from gallery ✅");

            } else if (requestCode == CAMERA_CAPTURE_REPORT) {
                Bitmap photoBitmap = (Bitmap) data.getExtras().get("data");
                selectedReportImageUri = getImageUriFromBitmap(requireContext(), photoBitmap);
                binding.tvAddPhotoReport.setText("Image captured from camera ✅");

            } else if (requestCode == CAMERA_CAPTURE_RECOMMEND) {
                Bitmap photoBitmap = (Bitmap) data.getExtras().get("data");
                selectedRecommendImageUri = getImageUriFromBitmap(requireContext(), photoBitmap);
                binding.tvAddPhotoRecommend.setText("Image captured from camera ✅");
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

