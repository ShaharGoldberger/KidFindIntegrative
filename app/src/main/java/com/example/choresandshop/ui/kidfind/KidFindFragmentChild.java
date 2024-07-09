package com.example.choresandshop.ui.kidfind;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.choresandshop.Model.CreatedBy;
import com.example.choresandshop.Model.InvokedBy;
import com.example.choresandshop.Model.ObjectId;
import com.example.choresandshop.Model.TargetObject;
import com.example.choresandshop.Model.UserId;
import com.example.choresandshop.R;
import com.example.choresandshop.UserApi.ApiController;
import com.example.choresandshop.UserApi.CommandApi;
import com.example.choresandshop.UserApi.ObjectApi;
import com.example.choresandshop.boundaries.MiniAppCommandBoundary;
import com.example.choresandshop.boundaries.ObjectBoundary;
import com.example.choresandshop.ui.CurrentLocationManager;
import com.example.choresandshop.ui.CurrentUserManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class KidFindFragmentChild extends Fragment implements OnMapReadyCallback{

    private MaterialButton Send_MB_SendAlert;
    private GoogleMap googleMap;
    private FusedLocationProviderClient location;
    private double latitude;
    private double longitude;
    private String email;
    private Handler handler = new Handler();
    private Runnable runnable;
    private final int POLL_INTERVAL = 5000; // time in millis

    private ObjectApi objectApi;
    private CommandApi commandService;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_kid_find_child, container, false);

        findViews(view);
        initViews(view);
        startPolling();

        return view;
    }

    private void initViews(View view) {
        objectApi = ApiController.getRetrofitInstance().create(ObjectApi.class);
        commandService = ApiController.getRetrofitInstance().create(CommandApi.class);

        Send_MB_SendAlert.setOnClickListener(v -> {
            zoom();
            sendAlert();
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
    }

    private void startPolling() {
        ObjectBoundary locationObject = makeCurrentLocationBoundary();
        locationObject.setType("GENERAL");
        runnable = () -> objectApi.createObject(locationObject).enqueue(new Callback<ObjectBoundary>() {
            @Override
            public void onResponse(Call<ObjectBoundary> call, Response<ObjectBoundary> response) {
                if (response.isSuccessful())
                    Log.i("Response object: ", response.body().toString() );

                else
                    Log.e("Error", "Request failed with code: " + response.code());

                handler.postDelayed(runnable, POLL_INTERVAL);
            }
            @Override
            public void onFailure(Call<ObjectBoundary> call, Throwable t) {
                Log.e("Failure", t.getMessage());
                handler.postDelayed(runnable, POLL_INTERVAL);
            }
        });
        handler.post(runnable);
    }

    private void stopPolling() {
        handler.removeCallbacks(runnable);
    }

    private void zoom() {
        latitude = CurrentLocationManager.getInstance().getLatitude();
        longitude = CurrentLocationManager.getInstance().getLongitude();

        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 10.0F));
    }

    private void findViews(View view) {
        Send_MB_SendAlert = view.findViewById(R.id.Send_MB_SendAlert);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng sydney = new LatLng(-34, 151);
        this.googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void sendAlert(){
        ObjectBoundary locationBoundary = makeCurrentLocationBoundary();
        Call<ObjectBoundary> createObjectCall = objectApi.createObject(locationBoundary);
        createObjectCall.enqueue(new Callback<ObjectBoundary>() {
            @Override
            public void onResponse(Call<ObjectBoundary> call, Response<ObjectBoundary> response) {

                if (response.isSuccessful()) {
                    // get the object in return
                    ObjectBoundary responseObject = response.body();
                    Log.e("SuperappObjectBoundary", responseObject.toString() );

                    // Send command to the API
                    // use the Id to make a command (for target object)
                    sendAlertCommand(responseObject.getObjectId().getId());

                } else Log.e("Error", "Request failed with code: " + response.code());
            }
            @Override
            public void onFailure(Call<ObjectBoundary> call, Throwable t) { Log.e("Failure", t.getMessage()); }
        });
    }

    private void sendAlertCommand(String objectId) {
        MiniAppCommandBoundary command = makeCommandByObjectId(objectId);

        // make a call to create alert command with command boundary
        Call<MiniAppCommandBoundary[]> createCommandCal = commandService.create("MiniHeros", command);
        createCommandCal.enqueue(new Callback<MiniAppCommandBoundary[]>() {
            @Override
            public void onResponse(Call<MiniAppCommandBoundary[]> call, Response<MiniAppCommandBoundary[]> response) {
                if (response.isSuccessful()) {
                    Arrays.stream(response.body())
                            .peek( (i) -> Log.e("MiniAppCommandBoundary", i.toString()));
                } else {
                    Log.e("Error", "Request failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<MiniAppCommandBoundary[]> call, Throwable t) {
                Log.e("Failure", t.getMessage());
            }
        });
    }

    public MiniAppCommandBoundary makeCommandByObjectId(String id){
        MiniAppCommandBoundary command = new MiniAppCommandBoundary();
        command.setCommand("alert");
        ObjectId objectId = new ObjectId("MiniHeros", id);
        command.setTargetObject(new TargetObject(objectId));
        UserId userId = new UserId("MiniHeros", CurrentUserManager.getInstance().getUser().getUserId().getEmail() );
        command.setInvokedBy(new InvokedBy(userId));

        return command;
    }

    private ObjectBoundary makeCurrentLocationBoundary(){
        ObjectBoundary boundary = new ObjectBoundary();
        boundary.setType("ALERT");
        boundary.setAlias("location");
        boundary.setLocation(new com.example.choresandshop.Model.Location(latitude, longitude));
        boundary.setActive(true);
        boundary.setCreatedBy(new CreatedBy(new UserId("MiniHeros", CurrentUserManager.getInstance().getUser().getUserId().getEmail())));

        return boundary;
    }
}