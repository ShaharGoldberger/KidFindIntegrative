package com.example.choresandshop.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choresandshop.Adapters.NotificationsAdapter;
import com.example.choresandshop.Model.Notification;
import com.example.choresandshop.R;
import com.example.choresandshop.UserApi.ApiController;
import com.example.choresandshop.UserApi.ObjectApi;
import com.example.choresandshop.boundaries.ObjectBoundary;
import com.example.choresandshop.databinding.FragmentNotificationsBinding;
import com.example.choresandshop.ui.CurrentUserManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment { //Family Manager
    private FragmentNotificationsBinding binding;


    private RecyclerView recyclerView;
    private NotificationsAdapter adapter;
    private ArrayList<Notification> dataList;
    private ObjectApi objectApi;
    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        findViews(view);
        initViews(view);

        super.onViewCreated(view, savedInstanceState);

    }

    private void initViews(View view) {
        objectApi = ApiController.getRetrofitInstance().create(ObjectApi.class);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dataList = new ArrayList<>();

        objectApi.getObjectsByType("ALERT",
                "MiniHeros",
                CurrentUserManager.getInstance().getUser().getUserId().getEmail(),
                10,
                0).enqueue(new Callback<ObjectBoundary[]>() {
            @Override
            public void onResponse(Call<ObjectBoundary[]> call, Response<ObjectBoundary[]> response) {
                if (response.isSuccessful()){
                    dataList = new ArrayList<>();

                    ArrayList<ObjectBoundary> objects = new ArrayList<>(Arrays.asList(response.body()));
                    if ( objects.size() == 0 ) return;
                    Log.e("Parent Objects", "Successful result" + objects.get(0));

                    for (ObjectBoundary object : objects) {
                        if (!object.getActive()) continue;
                        Log.e("Notification", "Send notification " + objects.get(0));

                        String title = object.getAlias();
                        String email = object.getCreatedBy().getUserId().getEmail();
                        boolean isActive = object.getActive();

                        dataList.add(new Notification(title, email, isActive));

                    }

                    adapter.updateData(dataList);
                    if ( dataList.size() == 0 ){
                        notificationsViewModel.setTextNone();
                    }else{
                        notificationsViewModel.clearText();
                    }
                }
                else
                    Log.e("Error", "Request failed with code: " + response.code());

            }
            @Override
            public void onFailure(Call<ObjectBoundary[]> call, Throwable t) {
                Log.e("Failure", t.getMessage());
            }
        });

        adapter = new NotificationsAdapter(getContext(), dataList);
        recyclerView.setAdapter(adapter);

    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.notification_RV_list);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}