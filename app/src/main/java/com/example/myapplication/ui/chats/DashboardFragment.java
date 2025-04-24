package com.example.myapplication.ui.chats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentDashboardBinding;

import java.util.Arrays;
import java.util.List;

import Adapter.connectedAdapter;
import Adapter.verticalPeopleAdapter;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // יצירת רשימת שחקנים
        List<connectedAdapter.Person> demoList = Arrays.asList(
                new connectedAdapter.Person("Shani", R.drawable.user),
                new connectedAdapter.Person("Ori", R.drawable.user),
                new connectedAdapter.Person("Gal", R.drawable.user),
               new connectedAdapter.Person("Lior", R.drawable.user),
                new connectedAdapter.Person("Ori", R.drawable.user),
                new connectedAdapter.Person("Gal", R.drawable.user),
                new connectedAdapter.Person("Gal", R.drawable.user),
                new connectedAdapter.Person("Lior", R.drawable.user),
                new connectedAdapter.Person("Ori", R.drawable.user),
                new connectedAdapter.Person("Gal", R.drawable.user)
        );

        // הגדרת RecyclerView
        binding.connectedFriendsRecycler.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.connectedFriendsRecycler.setAdapter(new connectedAdapter(demoList));

        List<verticalPeopleAdapter.Person> people = Arrays.asList(
                new verticalPeopleAdapter.Person("Shani", R.drawable.user),
                new verticalPeopleAdapter.Person("Ori", R.drawable.user),
                new verticalPeopleAdapter.Person("Lior", R.drawable.user),
                new verticalPeopleAdapter.Person("Ran", R.drawable.user),
                new verticalPeopleAdapter.Person("Eden", R.drawable.user),
                new verticalPeopleAdapter.Person("Romy", R.drawable.user)
        );

        verticalPeopleAdapter adapter = new verticalPeopleAdapter(people);
        binding.messagesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.messagesRecycler.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
