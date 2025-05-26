package Adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.VerticalPeopleBinding;

import java.util.List;

public class verticalPeopleAdapter extends RecyclerView.Adapter<verticalPeopleAdapter.VerticalViewHolder> {

    public static class Person {
        private final String name;
        private final int imageResId;
        private final String phoneNumber;

        public Person(String name, int imageResId, String phoneNumber) {
            this.name = name;
            this.imageResId = imageResId;
            this.phoneNumber = phoneNumber;
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
            String formattedNumber = person.phoneNumber.replaceFirst("^0", "972"); // המרה לפורמט בינלאומי בלי +

            String message = "היי! 👋 ראיתי שאנחנו מחוברים באפליקציית TourTrack – אולי נצא לטייל ביחד? 😊";

            String url = "https://wa.me/" + formattedNumber + "?text=" + Uri.encode(message);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            intent.setPackage("com.whatsapp");

            try {
                v.getContext().startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(v.getContext(), "WhatsApp לא מותקן או שאין תמיכה במספר", Toast.LENGTH_SHORT).show();
            }
        });



        holder.binding.listBTNCalls.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + person.phoneNumber));
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
