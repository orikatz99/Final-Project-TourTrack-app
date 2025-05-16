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

       /*
        holder.binding.IBWhatsapp.setOnClickListener(v -> {
            String phoneNumber = "0548800173"; // מספר מקומי
            String formattedNumber = phoneNumber.replaceFirst("^0", "972"); // מספר בינלאומי ללא "+"

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "היי! 👋 ראיתי שאנחנו מחוברים באפליקציית TourTrack – אולי נצא לטייל ביחד? 😊");
            // intent.putExtra(Intent.EXTRA_TEXT, "Hey! 👋 I saw we're connected on TourTrack – want to go hiking together sometime? 😊");
            intent.putExtra("jid", formattedNumber + "@s.whatsapp.net"); // מזהה WhatsApp של הנמען
            intent.setPackage("com.whatsapp");

            try {
                v.getContext().startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(v.getContext(), "WhatsApp is not installed", Toast.LENGTH_SHORT).show();
            }
        });*/

        holder.binding.IBWhatsapp.setOnClickListener(v -> {
            String phoneNumber = "0556618801"; // static number
            String formattedNumber = phoneNumber.replaceFirst("^0", "972"); // המרה לפורמט בינלאומי בלי +

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
