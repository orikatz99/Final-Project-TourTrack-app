package Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.myapplication.databinding.VerticalPeopleBinding;
import com.example.tourtrack.databinding.VerticalPeopleBinding;

import java.util.List;

public class verticalPeopleAdapter extends RecyclerView.Adapter<verticalPeopleAdapter.VerticalViewHolder> {

    public static class Person {
        private final String name;
        private final int imageResId;
        private final String phoneNumber;
        private final boolean allowPhoneCalls;
        private final boolean enableWhatsapp;

        public Person(String name, int imageResId, String phoneNumber, boolean allowPhoneCalls, boolean enableWhatsapp) {
            this.name = name;
            this.imageResId = imageResId;
            this.phoneNumber = phoneNumber;
            this.allowPhoneCalls = allowPhoneCalls;
            this.enableWhatsapp = enableWhatsapp;
        }

        public String getName() {
            return name;
        }

        public int getImageResId() {
            return imageResId;
        }
        public String getPhoneNumber() {
            return phoneNumber;
        }
        public boolean isAllowPhoneCalls() {
            return allowPhoneCalls;
        }
        public boolean isEnableWhatsapp() {
            return enableWhatsapp;
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

        //whatsapp button
        whatsappButton(holder, person);

        //phone call button
        phoneCAllButton(holder, person);


    }
    private void vibrate(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(100);
            }
        }
    }


    private void phoneCAllButton(VerticalViewHolder holder, Person person) {

        holder.binding.listBTNCalls.setOnClickListener(v -> {
            if (!person.isAllowPhoneCalls()) {
                Toast.makeText(v.getContext(), "This user does not allow phone calls", Toast.LENGTH_SHORT).show();
                vibrate(v.getContext());
                return;
            }
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + person.phoneNumber));
            v.getContext().startActivity(intent);
        });
    }

    private void whatsappButton(VerticalViewHolder holder, Person person) {
        holder.binding.IBWhatsapp.setOnClickListener(v -> {
            if (!person.isEnableWhatsapp()) {
                Toast.makeText(v.getContext(), "This user does not allow WhatsApp contact", Toast.LENGTH_SHORT).show();
                vibrate(v.getContext());
                return;
            }
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
