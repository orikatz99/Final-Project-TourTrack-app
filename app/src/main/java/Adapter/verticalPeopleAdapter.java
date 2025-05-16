package Adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.VerticalPeopleBinding;

import java.util.List;

public class verticalPeopleAdapter extends RecyclerView.Adapter<verticalPeopleAdapter.VerticalViewHolder> {

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

    public verticalPeopleAdapter(List<Person> people) {
        this.people = people;
    }

    @NonNull
    @Override
    public VerticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VerticalPeopleBinding binding = VerticalPeopleBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new VerticalViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalViewHolder holder, int position) {
        Person person = people.get(position);
        holder.binding.playerLBLName.setText(person.getName());
        holder.binding.profileImage.setImageResource(person.getImageResId());

        holder.binding.IBWhatsapp.setOnClickListener(v -> {
            // TODO: Open WhatsApp
        });

        holder.binding.listBTNCalls.setOnClickListener(v -> {
            String phoneNumber = "0556618801"; // TODO: Make phone call by - specific phone number!
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            v.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    static class VerticalViewHolder extends RecyclerView.ViewHolder {
        VerticalPeopleBinding binding;

        public VerticalViewHolder(@NonNull VerticalPeopleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
