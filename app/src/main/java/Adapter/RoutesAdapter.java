package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
//import com.example.myapplication.R;
import com.example.tourtrack.R;

import com.example.tourtrack.models.RouteModel;

import java.util.List;

public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.RouteViewHolder> {

    private Context context;
    private List<RouteModel> routes;
    private int visibleCount;

    public RoutesAdapter(Context context, List<RouteModel> routes, int visibleCount) {
        this.context = context;
        this.routes = routes;
        this.visibleCount = visibleCount;
    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.routes_item, parent, false);
        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        RouteModel route = routes.get(position);
        holder.name.setText(route.getName());
        holder.km.setText("Distance: " + route.getLengthKm() + " km");
        holder.difficulty.setText("Difficulty: " + route.getDifficulty());
        holder.description.setText(route.getDescription());

        if (route.getCurrentWeather() != null) {
            holder.weather.setText("Weather: " + route.getCurrentWeather().getType());
        }

        Glide.with(context)
                .load(route.getImageUrl())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return Math.min(visibleCount, routes.size());
    }

    public void setVisibleCount(int count) {
        this.visibleCount = count;
        notifyDataSetChanged();
    }

    public static class RouteViewHolder extends RecyclerView.ViewHolder {
        TextView name, km, difficulty, weather, description;
        ImageView image;

        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_route_name);
            km = itemView.findViewById(R.id.tv_route_km);
            difficulty = itemView.findViewById(R.id.tv_route_difficulty);
            weather = itemView.findViewById(R.id.tv_route_weather);
            description = itemView.findViewById(R.id.tv_route_description);
            image = itemView.findViewById(R.id.img_route);
        }
    }
}
