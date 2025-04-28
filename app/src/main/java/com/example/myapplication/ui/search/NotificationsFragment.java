package com.example.myapplication.ui.search;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentNotificationsBinding;

import java.util.Calendar;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private Calendar tripCalendar; // Keep track of the selected departure date

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupSpinners();
        setupInputListeners();

        return root;
    }

    private void setupSpinners() {
        // Region Spinner
        ArrayAdapter<CharSequence> regionAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.galil_tahton_regions,
                android.R.layout.simple_spinner_item
        );
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spiRegions.setAdapter(regionAdapter);

        // Trail Categories Spinner
        ArrayAdapter<CharSequence> trailAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.trail_categories,
                android.R.layout.simple_spinner_item
        );
        trailAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spiTrailCatagories.setAdapter(trailAdapter);

        // Attractions Spinner
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
