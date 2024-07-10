package com.example.choresandshop.ui.kidfind;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.choresandshop.Model.CreatedBy;
import com.example.choresandshop.Model.UserId;
import com.example.choresandshop.R;
import com.example.choresandshop.UserApi.ApiController;
import com.example.choresandshop.UserApi.ObjectApi;
import com.example.choresandshop.boundaries.ObjectBoundary;
import com.example.choresandshop.ui.CurrentUserManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class KidFindFragmentParent extends Fragment implements OnMapReadyCallback{

    private MaterialButton Find_MB_FindLocation;
    private Spinner spinner;
    private GoogleMap googleMap;
    private android.location.LocationManager location;
    private double latitude;
    private double longitude;
    private String email;
    private Handler handler = new Handler();
    private Runnable runnable;

    private ObjectApi objectApi;
    private final int POLL_INTERVAL = 5000; // time in millis
    private MediaPlayer mediaPlayer;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_kid_find_parent, container, false);

        objectApi = ApiController.getRetrofitInstance().create(ObjectApi.class);

        findViews(view);
        initViews(view);
        startPolling();
        return view;
    }

    private void initViews(View view) {
        // Create a list of items
        List<String> kidsNames = new ArrayList<>();
        kidsNames.add("Shahar@gmail.com");
        kidsNames.add("Matanel@gmail.com");
        // Add more items as needed

        // Create an ArrayAdapter using the list of items and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.custom_spinner_item, kidsNames);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                email = parent.getItemAtPosition(position).toString();
//                Toast.makeText(getActivity(), "Selected: " + email, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        Find_MB_FindLocation.setOnClickListener(v -> {
            findKidLocation();
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
    }

    private void findViews(View view) {
        Find_MB_FindLocation = view.findViewById(R.id.Find_MB_FindLocation);
        spinner = view.findViewById(R.id.spinner);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        LatLng sydney = new LatLng(-34, 151);
        this.googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void startPolling() {

        runnable = () -> objectApi.getObjectsByType("ALERT",
                "MiniHeros",
                "Yakir@gmail.com",
                10,
                0).enqueue(new Callback<ObjectBoundary[]>() {
            @Override
            public void onResponse(Call<ObjectBoundary[]> call, Response<ObjectBoundary[]> response) {
                if (response.isSuccessful()){
                    // if any is active
                    ArrayList<ObjectBoundary> objects = new ArrayList<>(Arrays.asList(response.body()));
                    if ( objects.size() == 0 ) return;
                    Log.e("Parent Objects", "Successful result" + objects.get(0));

                    for (ObjectBoundary object : objects) {
                        if (!object.getActive()) continue;
                        Log.e("Notification", "Send notification " + objects.get(0));
                        makeObjectNotActive(object);
                        showNotification("Emergency call from " + object.getCreatedBy().getUserId().getEmail());
//                        Toast.makeText(getActivity(), "Emergency call from " + object.getCreatedBy().getUserId().getEmail(), Toast.LENGTH_SHORT).show();
                        playSound(getContext());
                        vibrate();
                    }
                }
                else
                    Log.e("Error", "Request failed with code: " + response.code());

                handler.postDelayed(runnable, POLL_INTERVAL);
            }
            @Override
            public void onFailure(Call<ObjectBoundary[]> call, Throwable t) {
                Log.e("Failure", t.getMessage());
                handler.postDelayed(runnable, POLL_INTERVAL);
            }
        });
        handler.post(runnable);
    }

    private void makeObjectNotActive(ObjectBoundary boundary){
//        boundary.setActive(Boolean.valueOf(Boolean.FALSE));
        ObjectBoundary newBoundary = copyBoundary(boundary);
        newBoundary.setActive(Boolean.FALSE);
        Call<Void> call = objectApi.updateObject("MiniHeros", boundary.getObjectId().getId(), "MiniHeros", "yakir@gmail.com", newBoundary);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.d("Update Object Boundary", "Yay we updated to read!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("Update Error", "Request failed with code: " + response.code() + response.message() + response.toString()
                    );
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(getContext(), "Finding test"+ t.getMessage(), Toast.LENGTH_SHORT).show();

                Log.e("Update failed", t.getMessage());
            }
        });

    }

    private ObjectBoundary copyBoundary(ObjectBoundary boundary) {

        ObjectBoundary newBoundary = new ObjectBoundary();
        newBoundary.setType(
                boundary.getType()
        );
        newBoundary.setAlias(
                boundary.getAlias()
        );
        newBoundary.setLocation(new com.example.choresandshop.Model.Location(
                boundary.getLocation().getLat(),
                boundary.getLocation().getLng()
        ));
        newBoundary.setActive(
                boundary.getActive()
        );
        newBoundary.setCreatedBy(new CreatedBy(new UserId(
                boundary.getCreatedBy().getUserId().getSuperapp(),
                boundary.getCreatedBy().getUserId().getEmail()
        )));

        return newBoundary;
    }

    private void showNotification(String message) {
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(getContext().NOTIFICATION_SERVICE);

        // Create the notification channel if necessary
        createNotificationChannel();

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "EmergencyButton")
                .setContentTitle("Mini Heros - Emergency Alert ")
                .setSmallIcon(R.drawable.ic_location_black_24dp) // Replace with your app's icon
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true); // Dismiss the notification when the user taps on it

        // Show the notification
        notificationManager.notify(1, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "EmergencyButton";
            String channelName = "EmergencyButtonAlert";
            String channelDescription = "Emergency alert from kid";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    private void findKidLocation(){

        Call<ObjectBoundary[]> call = objectApi.findAllObjects("MiniHeros", email.toLowerCase(), 20, 0);
        call.enqueue(new Callback<ObjectBoundary[]>() {
            @Override
            public void onResponse(Call<ObjectBoundary[]> call, Response<ObjectBoundary[]> response) {
                if (response.isSuccessful()) {
                    try {
                        ObjectBoundary[] responseAllBody = response.body();
                        // Log or display the response
                        ObjectBoundary latest = null;
                        Log.e("Email looking for", email);
                        for (ObjectBoundary object: responseAllBody){
                            Log.e("Object looking for", object.toString());
                            if ( !object.getCreatedBy().getUserId().getEmail().toLowerCase().equals(email.toLowerCase())) continue;
                            if (latest == null)latest = object;
                            else if (object.getCreationTimestamp().after(latest.getCreationTimestamp()))
                                latest = object;
                        }

                        LatLng location = new LatLng(latest.getLocation().getLat(), latest.getLocation().getLng());
                        googleMap.addMarker(new MarkerOptions().position(location).title(email.toLowerCase()));
                        googleMap.getUiSettings().setZoomControlsEnabled(true);

                        float zoomLevel = 10.0f; // Example zoom level
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));

//                        Toast.makeText(getContext(), "LoginResponse "+latest.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("LoginResponse", latest.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
//                    Toast.makeText(getContext(), "Finding test"+response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("LoginError", "Request failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ObjectBoundary[]> call, Throwable t) {
                Toast.makeText(getContext(), "Finding test"+ t.getMessage(), Toast.LENGTH_SHORT).show();

                Log.e("LoginFailure", t.getMessage());
            }
        });

    }
    // Method to play sound from the raw folder
    public void playSound(Context context) {
        if (mediaPlayer != null) {
            mediaPlayer.release(); // Release any resources from previous MediaPlayer
        }

        // Create a MediaPlayer instance and set the audio resource
        mediaPlayer = MediaPlayer.create(context, R.raw.ring);

        if (mediaPlayer != null) {
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // Release the MediaPlayer once the audio is finished
                    mp.release();
                }
            });

            mediaPlayer.start(); // Start playing the sound
        }
    }

    private void vibrate(){
        Vibrator vibrate = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrate != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Create a vibration effect with a specific pattern
                // Pattern: Wait 0ms, Vibrate for 500ms, Wait 1000ms, Vibrate for 1000ms
                long[] mVibratePattern = new long[]{0, 500, 1000, 1000};
                int[] mAmplitudes = new int[]{0, 150, 0, 200};  // Amplitude 0 for off, or 1-255 for on

                VibrationEffect effect = VibrationEffect.createWaveform(mVibratePattern, mAmplitudes, -1);
                vibrate.vibrate(effect);
            } else {
                // Deprecated in API 26
                long[] pattern = {0, 100, 1000, 300};
                vibrate.vibrate(pattern, -1);  // The '-1' here means to vibrate once, as '0' would make it vibrate repeatedly.
            }
        }


    }

}