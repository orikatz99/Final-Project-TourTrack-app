package Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.models.ReportRequest;
import com.example.myapplication.models.UserReportResponse;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;
import com.example.myapplication.models.UpdateReportResponse;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.text.DateFormat;
import java.util.TimeZone;


public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private Context context;
    private List<UserReportResponse> reportList;
    private String token;

    public ReportAdapter(Context context, List<UserReportResponse> reportList, String token) {
        this.context = context;
        this.reportList = reportList;
        this.token = token;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.vertical_reports, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        UserReportResponse report = reportList.get(position);

        holder.tvType.setText("Type: " + report.getType());
        holder.tvLocation.setText("Location: " + report.getLocation());
        holder.tvDescription.setText("Description: " + report.getDescription());
        String mongoDateStr = report.getDate();

        showDateInFormat(holder, mongoDateStr);
        String photoUrl = report.getPhoto();

        if (photoUrl != null && !photoUrl.isEmpty()) {
            holder.image.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(photoUrl)
                    .into(holder.image);
        } else {
            holder.image.setVisibility(View.GONE);
        }

        holder.btnEdit.setOnClickListener(v -> showEditDialog(report, position));
        holder.btnDelete.setOnClickListener(v ->deleteReport(report.getReportId(), position));

    }

    private void showDateInFormat(ReportViewHolder holder, String mongoDateStr) {
        // Cut milliseconds and timezone to parse easily:
        String cleanedDate = mongoDateStr.split("\\.")[0]; // "2025-05-30T17:50:50"

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Optional if Mongo dates are UTC

        SimpleDateFormat outputFormat = new SimpleDateFormat("yy/MM/dd HH:mm");

        try {
            Date date = inputFormat.parse(cleanedDate);
            String formattedDate = outputFormat.format(date);
            holder.tvReportDate.setText("Date: " + formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            holder.tvReportDate.setText("Date: N/A");
        }

    }

    private void deleteReport(String reportId, int position) {
        ApiService apiService = RetrofitClient.getApiServiceWithAuth(token);

        Call<Void> call = apiService.deleteReport(reportId);
        String photoUrl = reportList.get(position).getPhoto();
        if (photoUrl != null && !photoUrl.isEmpty()) {
            deleteImageFromFirebase(photoUrl);
        }
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    reportList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Report deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to delete report", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void deleteImageFromFirebase(String photoUrl) {
        StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(photoUrl);
        photoRef.delete()
                .addOnSuccessListener(aVoid -> Log.d("ReportAdapter", "Image deleted from Firebase"))
                .addOnFailureListener(e -> Log.e("ReportAdapter", "Failed to delete image: " + e.getMessage()));
    }


    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView tvType, tvLocation, tvDescription, tvReportDate;
        ImageView image;
        Button btnEdit, btnDelete;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tvReportType);
            tvLocation = itemView.findViewById(R.id.tvReportLocation);
            tvDescription = itemView.findViewById(R.id.tvReportDescription);
            image = itemView.findViewById(R.id.img_report_photo);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            tvReportDate = itemView.findViewById(R.id.tvReportDate);
        }
    }

    private void showEditDialog(UserReportResponse report, int position) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_report, null);

        Spinner spinnerReportType = dialogView.findViewById(R.id.spinnerReportType);
        EditText editLocation = dialogView.findViewById(R.id.editLocation);
        EditText editDescription = dialogView.findViewById(R.id.editDescription);

        editLocation.setText(report.getLocation());
        editDescription.setText(report.getDescription());

        invillizeSpinner(spinnerReportType);

        new AlertDialog.Builder(context)
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String newType = spinnerReportType.getSelectedItem().toString().trim();
                    String newLocation = editLocation.getText().toString().trim();
                    String newDescription = editDescription.getText().toString().trim();

                    ReportRequest updatedReport = new ReportRequest(
                            report.getUserId(),
                            report.getPhoto(),
                            newDescription,
                            newLocation,
                            newType
                    );

                    ApiService apiService = RetrofitClient.getApiServiceWithAuth(token);

                    Call<UpdateReportResponse> call = apiService.updateReport(
                            token,
                            report.getReportId(),
                            updatedReport
                    );

                    call.enqueue(new Callback<UpdateReportResponse>() {
                        @Override
                        public void onResponse(Call<UpdateReportResponse> call, Response<UpdateReportResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                UserReportResponse updated = response.body().getReport();

                                reportList.get(position).setDescription(updated.getDescription());
                                reportList.get(position).setLocation(updated.getLocation());
                                reportList.get(position).setType(updated.getType());
                                reportList.get(position).setDate(updated.getDate());

                                notifyItemChanged(position);
                                Toast.makeText(context, "Report updated successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UpdateReportResponse> call, Throwable t) {
                            Toast.makeText(context, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    private void invillizeSpinner(Spinner spinnerReportType) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                context,
                R.array.danger_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReportType.setAdapter(adapter);


    }


}
