package Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.HorizontalFriendBinding;

import java.util.List;

public class connectedAdapter extends RecyclerView.Adapter<connectedAdapter.ConnectedViewHolder> {

    public static class Person {
        private final String name;
        private final int imageResId;

        public Person(String name, int imageResId) {
            this.name = name;
            this.imageResId = imageResId;
        }

        public String getName() {
            return name;
        }

        public int getImageResId() {
            return imageResId;
        }
    }

    private final List<Person> people;

    public connectedAdapter(List<Person> people) {
        this.people = people;
    }

    @NonNull
    @Override
    public ConnectedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        HorizontalFriendBinding binding = HorizontalFriendBinding.inflate(inflater, parent, false);
        return new ConnectedViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ConnectedViewHolder holder, int position) {
        Person person = people.get(position);
        holder.binding.userLBLName.setText(person.getName());
        holder.binding.profileImage.setImageResource(person.getImageResId());
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    static class ConnectedViewHolder extends RecyclerView.ViewHolder {
        HorizontalFriendBinding binding;

        public ConnectedViewHolder(@NonNull HorizontalFriendBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
