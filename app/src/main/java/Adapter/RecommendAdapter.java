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
import com.example.myapplication.models.RecommendRequest;
import com.example.myapplication.models.UpdateRecommendResponse;
import com.example.myapplication.models.UserRecommendationResponse;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.util.TimeZone;


public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.RecommendViewHolder> {

    private Context context;
    private List<UserRecommendationResponse> recommendationList;
    private String token;

    public RecommendAdapter(Context context, List<UserRecommendationResponse> recommendationList, String token) {
        this.context = context;
        this.recommendationList = recommendationList;
        this.token = token;

    }

    @NonNull
    @Override
    public RecommendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.vertical_recommend, parent, false);
        return new RecommendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendViewHolder holder, int position) {
        UserRecommendationResponse recommend = recommendationList.get(position);
        holder.RecommendLocation.setText("Location: " + recommend.getLocation());
        holder.tvRecommendationDescription.setText("Description: " + recommend.getDescription());

        String mongoDateStr = recommend.getDate();

        showDateInFormat(holder, mongoDateStr);
        String photoUrl = recommend.getPhoto();

        if (photoUrl != null && !photoUrl.isEmpty()) {
            holder.image.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(photoUrl)
                    .into(holder.image);
        } else {
            holder.image.setVisibility(View.GONE);
        }
        holder.tvUserName.setText("Posted by: " + recommend.getFullName());

        holder.btnEdit.setOnClickListener(v -> showEditDialog(recommend, position));
        holder.btnDelete.setOnClickListener(v -> deleteRecommend(recommend.getRecommend_id(), position));
    }

    private void showDateInFormat(RecommendViewHolder holder, String mongoDateStr) {
        if (mongoDateStr == null || mongoDateStr.isEmpty()) {
            holder.tvRecommendDate.setText("Date: N/A");
            return;
        }

        String cleanedDate = mongoDateStr.split("\\.")[0]; // "2025-05-30T17:50:50"
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        SimpleDateFormat outputFormat = new SimpleDateFormat("yy/MM/dd HH:mm");

        try {
            Date date = inputFormat.parse(cleanedDate);
            String formattedDate = outputFormat.format(date);
            holder.tvRecommendDate.setText("Date: " + formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            holder.tvRecommendDate.setText("Date: N/A");
        }
    }


    private void deleteRecommend(String recommendId, int position) {
        ApiService apiService = RetrofitClient.getApiServiceWithAuth(token);

        Call<Void> call = apiService.deleteRecommendation(token, recommendId);
        String photoUrl = recommendationList.get(position).getPhoto();
        if (photoUrl != null && !photoUrl.isEmpty()) {
            deleteImageFromFirebase(photoUrl);
        }
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    recommendationList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Recommendation deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to delete Recommendation", Toast.LENGTH_SHORT).show();
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
                .addOnSuccessListener(aVoid -> Log.d("RecommendAdapter", "Image deleted from Firebase"))
                .addOnFailureListener(e -> Log.e("RecommendAdapter", "Failed to delete image: " + e.getMessage()));
    }

    @Override
    public int getItemCount() {
        return recommendationList.size();
    }

    public static class RecommendViewHolder extends RecyclerView.ViewHolder {
        TextView tvRecommendDate,RecommendLocation,tvRecommendationDescription, tvUserName;
        ImageView image;
        Button btnEdit, btnDelete;

        public RecommendViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRecommendDate = itemView.findViewById(R.id.tvRecommenDate);
            RecommendLocation = itemView.findViewById(R.id.RecommendLocation);
            tvRecommendationDescription = itemView.findViewById(R.id.tvRecommendationDescription);
            image = itemView.findViewById(R.id.img_report_photo);
            btnEdit = itemView.findViewById(R.id.btnEditRecommendation);
            btnDelete = itemView.findViewById(R.id.btnDeleteRecommendation);
            tvUserName = itemView.findViewById(R.id.tvusername_recommend);

        }
    }

    private void showEditDialog(UserRecommendationResponse recommend, int position) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_recommend, null);

        Spinner spinnerRecommendType = dialogView.findViewById(R.id.spinnerRecommendType);
        EditText editLocation = dialogView.findViewById(R.id.editLocation);
        EditText editDescription = dialogView.findViewById(R.id.editDescription);

        editLocation.setText(recommend.getLocation());
        editDescription.setText(recommend.getDescription());

        initializeSpinner(spinnerRecommendType);

        new AlertDialog.Builder(context)
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String newType = spinnerRecommendType.getSelectedItem().toString().trim();
                    String newLocation = editLocation.getText().toString().trim();
                    String newDescription = editDescription.getText().toString().trim();

                    RecommendRequest updatedRecommendRequest = new RecommendRequest(
                            recommend.getUserId(),
                            recommend.getPhoto(),
                            newDescription,
                            newLocation,
                            newType
                    );


                    ApiService apiService = RetrofitClient.getApiServiceWithAuth(token);

                    Call<UpdateRecommendResponse> call = apiService.updateRecommendation(
                            token,
                            recommend.getRecommendId(),
                            updatedRecommendRequest
                    );



                    call.enqueue(new Callback<UpdateRecommendResponse>() {
                        @Override
                        public void onResponse(Call<UpdateRecommendResponse> call, Response<UpdateRecommendResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                UserRecommendationResponse updated = response.body().getRecommend();

                                recommendationList.get(position).setDescription(updated.getDescription());
                                recommendationList.get(position).setLocation(updated.getLocation());
                                recommendationList.get(position).setDate(updated.getDate());

                                notifyItemChanged(position);
                                Toast.makeText(context, "Recommendation updated successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UpdateRecommendResponse> call, Throwable t) {
                            Toast.makeText(context, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void initializeSpinner(Spinner spinnerReportType) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                context,
                R.array.danger_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReportType.setAdapter(adapter);
    }
}
