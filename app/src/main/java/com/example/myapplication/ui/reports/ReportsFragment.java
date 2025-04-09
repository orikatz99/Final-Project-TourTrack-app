package com.example.myapplication.ui.reports;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

public class ReportsFragment extends Fragment {

    private Spinner spinnerReportType;

    public ReportsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout (fragment_reports.xml)
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        // Connect to Spinner from layout
        spinnerReportType = view.findViewById(R.id.spinnerReportType);

        // Create adapter from danger_types array in strings.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.danger_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReportType.setAdapter(adapter);

        return view;
    }
}
