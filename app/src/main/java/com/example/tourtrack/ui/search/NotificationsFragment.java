package com.example.tourtrack.ui.search;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.adslib.AdView;
import com.example.adslib.AdsManager;
import com.example.adslib.CityCallback;
import com.example.adslib.LocationHelper;
import com.example.tourtrack.R;
import com.example.tourtrack.databinding.FragmentNotificationsBinding;
import com.example.tourtrack.models.RouteModel;
import com.example.tourtrack.models.UserInfoResponse;
import com.example.tourtrack.network.ApiService;
import com.example.tourtrack.network.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Adapter.RoutesAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private Calendar tripCalendar;
    private List<RouteModel> allRoutes = new ArrayList<>();
    private List<RouteModel> visibleRoutes = new ArrayList<>();
    private static final int ROUTES_INCREMENT = 1;
    private int visibleRoutesCount = 3;
    private RoutesAdapter adapter;
    private AdView adView;
    private AdsManager adsManager;
    private String userCity;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        adView = binding.adView;
        adsManager = new AdsManager(getContext()); // Uses your app's package name automatically
        adsManager.setAdView(adView);
        adView.setAdsManager(adsManager);



        //adsManager.showHotelsAd();
        // adsManager.showRestaurantsAd(null);
        //adsManager.showRandomAdFromByLocation("Tel Aviv");
        adsManager.showExitButton(10);



        setupSpinners();
        setupInputListeners();
        // Initialize the rycler view and adapter
        initRecyclerViewAndAdapter();

        //find the best routes button
        findRoutesButtonListener();

        //load more options button
        loadMoreButtonListener();



        return root;
    }

    private void loadMoreButtonListener() {
        binding.btnLoadMore.setOnClickListener(v -> {
            visibleRoutesCount += ROUTES_INCREMENT;
            updateVisibleRoutes();
        });
    }

    private void findRoutesButtonListener() {
        binding.btnFind.setOnClickListener(v -> {
            // Validate individual fields

            String region = binding.spiRegions.getSelectedItem().toString();
            String category = binding.spiTrailCatagories.getSelectedItem().toString();
            String attraction = binding.spiAttractions.getSelectedItem().toString();
            String groupSize = binding.editNumPeople.getText().toString().trim();
            String age = binding.editAges.getText().toString().trim();
            String date = binding.editDate.getText().toString().trim();

            if (region.isEmpty() || region.equals("Item 1")) {
                Toast.makeText(getContext(), "Please select a region", Toast.LENGTH_SHORT).show();
                return;
            }

            if (category.isEmpty() || category.equals("Item 1")) {
                Toast.makeText(getContext(), "Please select trail type", Toast.LENGTH_SHORT).show();
                return;
            }

            if (attraction.isEmpty() || attraction.equals("Item 1")) {
                Toast.makeText(getContext(), "Please select attraction type", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!binding.radioOption1.isChecked() && !binding.radioOption2.isChecked() && !binding.radioOption3.isChecked()) {
                Toast.makeText(getContext(), "Please select difficulty level", Toast.LENGTH_SHORT).show();
                return;
            }

            if (groupSize.isEmpty()) {
                Toast.makeText(getContext(), "Please enter group size", Toast.LENGTH_SHORT).show();
                return;
            }

            if (age.isEmpty()) {
                Toast.makeText(getContext(), "Please enter age", Toast.LENGTH_SHORT).show();
                return;
            }

            if (date.isEmpty()) {
                Toast.makeText(getContext(), "Please select a trip date", Toast.LENGTH_SHORT).show();
                return;
            }

            // All fields valid - proceed
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnFind.setEnabled(false);
            fetchUserPreferencesAndSend();
        });
    }

    private void initRecyclerViewAndAdapter() {
        binding.recyclerRoutes.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RoutesAdapter(requireContext(), visibleRoutes, visibleRoutesCount);
        binding.recyclerRoutes.setAdapter(adapter);

    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> regionAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.galil_tahton_regions,
                android.R.layout.simple_spinner_item
        );
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spiRegions.setAdapter(regionAdapter);

        ArrayAdapter<CharSequence> trailAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.trail_categories,
                android.R.layout.simple_spinner_item
        );
        trailAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spiTrailCatagories.setAdapter(trailAdapter);

        ArrayAdapter<CharSequence> attractionAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.attraction_preferences,
                android.R.layout.simple_spinner_item
        );
        attractionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spiAttractions.setAdapter(attractionAdapter);
    }

    private void setupInputListeners() {
        binding.editDate.setOnClickListener(v -> openTripDatePicker());
    }

    private void openTripDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year1, monthOfYear, dayOfMonth) -> {
                    tripCalendar = Calendar.getInstance();
                    tripCalendar.set(year1, monthOfYear, dayOfMonth);

                    String updatedDepartureDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    binding.editDate.setText(updatedDepartureDate);
                },
                year, month, day);

        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void fetchUserPreferencesAndSend() {
        ApiService apiService = RetrofitClient.getApiService();
        String token = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                .getString("token", null);

        apiService.getUserInfo("Bearer " + token).enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<String> preferences = new ArrayList<>();
                    if (response.body().getPreferences() != null) {
                        preferences = response.body().getPreferences();
                    }
                    fetchRecommendations(preferences);
                } else {
                    Toast.makeText(getContext(), "Failed to load preferences", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Request error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchRecommendations(List<String> preferences) {
        String category = binding.spiTrailCatagories.getSelectedItem().toString();
        String attraction = binding.spiAttractions.getSelectedItem().toString();
        String region = binding.spiRegions.getSelectedItem().toString();

        String difficulty = null;
        if (binding.radioOption1.isChecked()) difficulty = "Easy";
        else if (binding.radioOption2.isChecked()) difficulty = "Medium";
        else if (binding.radioOption3.isChecked()) difficulty = "Hard";

        String ageStr = binding.editAges.getText().toString();
        String groupSizeStr = binding.editNumPeople.getText().toString();
        String tripDateStr = binding.editDate.getText().toString();

        Integer age = !ageStr.isEmpty() ? Integer.parseInt(ageStr) : null;
        Integer groupSize = !groupSizeStr.isEmpty() ? Integer.parseInt(groupSizeStr) : null;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("category", category);
        requestBody.put("attraction", attraction);
        requestBody.put("difficulty", difficulty);
        requestBody.put("region", region);
        requestBody.put("preferences", preferences);
        requestBody.put("age", age);
        requestBody.put("groupSize", groupSize);

        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = inputFormat.parse(tripDateStr);
            if (date != null) {
                requestBody.put("tripDate", isoFormat.format(date));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiService apiService = RetrofitClient.getApiService();
        Call<List<RouteModel>> call = apiService.getRecommendations(requestBody);

        call.enqueue(new Callback<List<RouteModel>>() {
            @Override
            public void onResponse(Call<List<RouteModel>> call, Response<List<RouteModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allRoutes = response.body();
                    visibleRoutesCount = 3;
                    updateVisibleRoutes();
                } else {
                    Toast.makeText(getContext(), "No routes found", Toast.LENGTH_SHORT).show();
                }
                binding.progressBar.setVisibility(View.GONE);
                binding.btnFind.setEnabled(true);
            }

            @Override
            public void onFailure(Call<List<RouteModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to fetch recommendations", Toast.LENGTH_SHORT).show();
                binding.progressBar.setVisibility(View.GONE);
                binding.btnFind.setEnabled(true);
            }
        });
    }

    private void updateVisibleRoutes() {
        visibleRoutes.clear();
        for (int i = 0; i < Math.min(visibleRoutesCount, allRoutes.size()); i++) {
            visibleRoutes.add(allRoutes.get(i));
        }
        adapter.setVisibleCount(visibleRoutesCount);
        adapter.notifyDataSetChanged();

        binding.recommendationsSection.setVisibility(View.VISIBLE);

        boolean allLoaded = visibleRoutes.size() >= allRoutes.size();
        binding.btnLoadMore.setVisibility(allLoaded ? View.GONE : View.VISIBLE);
        binding.tvNoMoreRoutes.setVisibility(allLoaded ? View.VISIBLE : View.GONE);
    }

    private boolean areAllFieldsFilled() {
        // Check spinners
        if (binding.spiTrailCatagories.getSelectedItem() == null ||
                binding.spiAttractions.getSelectedItem() == null ||
                binding.spiRegions.getSelectedItem() == null) {
            return false;
        }

        // Check difficulty radio group
        if (!binding.radioOption1.isChecked() &&
                !binding.radioOption2.isChecked() &&
                !binding.radioOption3.isChecked()) {
            return false;
        }

        // Check text inputs
        if (binding.editAges.getText().toString().trim().isEmpty() ||
                binding.editNumPeople.getText().toString().trim().isEmpty() ||
                binding.editDate.getText().toString().trim().isEmpty()) {
            return false;
        }

        return true;
    }

    private void getUserCityAndLoadAd() {
        LocationHelper.getCityNameFromDevice(getContext(), new CityCallback() {
            @Override
            public void onCityLoaded(String city) {
                userCity = city;
                Log.d("CITY_RESULT", "User's city: " + userCity);
                adsManager.showRandomAdFromByLocation(userCity);
            }

            @Override
            public void onError(String reason) {
                Log.e("CITY_ERROR", "Could not get city: " + reason);
                adsManager.showRandomAdFromByLocation("Tel Aviv"); //default -for example- tel aviv
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //user permission for location
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 101 && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getUserCityAndLoadAd();
        } else {
            Log.e("CITY_ERROR", "Permission denied – showing fallback ad");
            adsManager.showRandomAdFromByLocation("Tel Aviv"); //default
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        } else {
            getUserCityAndLoadAd();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
