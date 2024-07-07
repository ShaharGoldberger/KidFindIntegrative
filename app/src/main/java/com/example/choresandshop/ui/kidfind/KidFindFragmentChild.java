package com.example.choresandshop.ui.kidfind;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_kid_find_child, container, false);
        // Get reference to the Spinner

        findViews(view);

        Send_MB_SendAlert.setOnClickListener(v -> {
            zoom();
            sendAlert();
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);


        return view;
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

    private void testLocation(){

        Toast.makeText(getContext(), "Alert test", Toast.LENGTH_SHORT).show();
        // TODO : Send the kid email to miniapp KidFind and get location boundary
        LatLng location = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(location).title("Marker in Sydney"));
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        float zoomLevel = 10.0f; // zoom level
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));

    }

    private void sendAlert(){


        ObjectBoundary locationBoundary = makeCurrentLocationBoundary();


        ObjectApi objectApi = ApiController.getRetrofitInstance().create(ObjectApi.class);
        Call<ObjectBoundary> createObjectCall = objectApi.createObject(locationBoundary);
        createObjectCall.enqueue(new Callback<ObjectBoundary>() {
            @Override
            public void onResponse(Call<ObjectBoundary> call, Response<ObjectBoundary> response) {

                if (response.isSuccessful()) {
                    // get the object in return
                    ObjectBoundary responseObject = response.body();
                    Log.i("Response object: ", responseObject.toString() );

                    // use the Id to make a command (for target object)
                    MiniAppCommandBoundary command = makeCommandByObjectId(
                            responseObject.getObjectId().getId()
                    );

                    // make a call to create alert command with command boundary
                    CommandApi apiService = ApiController.getRetrofitInstance().create(CommandApi.class);
                    Call<MiniAppCommandBoundary[]> createCommandCal = apiService.create("MiniHeros", command);
                    createCommandCal.enqueue(new Callback<MiniAppCommandBoundary[]>() {
                        @Override
                        public void onResponse(Call<MiniAppCommandBoundary[]> call, Response<MiniAppCommandBoundary[]> response) {
                            if (response.isSuccessful()) {
                                MiniAppCommandBoundary[] responseCommands = response.body();
                                for (MiniAppCommandBoundary b : responseCommands) {
                                    Log.i("All commands: ", b.toString() );
                                }
                            } else  Log.e("Error", "Request failed with code: " + response.code());
                        }

                        @Override
                        public void onFailure(Call<MiniAppCommandBoundary[]> call, Throwable t) {
                            Log.e("Failure", t.getMessage());
                        }
                    });
                    Log.d("Response", "Request was successful");
                } else Log.e("Error", "Request failed with code: " + response.code());

            }
            @Override
            public void onFailure(Call<ObjectBoundary> call, Throwable t) { Log.e("Failure", t.getMessage()); }
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
        boundary.setType("GENERAL");
        boundary.setAlias("location");
        boundary.setLocation(new com.example.choresandshop.Model.Location(latitude, longitude));
        boundary.setActive(true);
        boundary.setCreatedBy(new CreatedBy(new UserId("MiniHeros", CurrentUserManager.getInstance().getUser().getUserId().getEmail())));

        return boundary;
    }
}