package com.example.choresandshop.ui.kidfind;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.choresandshop.R;
import com.example.choresandshop.UserApi.ApiController;
import com.example.choresandshop.UserApi.ObjectApi;
import com.example.choresandshop.boundaries.ObjectBoundary;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KidFindFragmentParent extends Fragment implements OnMapReadyCallback{

    private MaterialButton Find_MB_FindLocation;
    private GoogleMap googleMap;
    private android.location.LocationManager location;
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
        View view = inflater.inflate(R.layout.fragment_kid_find_parent, container, false);
        // Get reference to the Spinner
        Spinner spinner = view.findViewById(R.id.spinner);

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
                Toast.makeText(getActivity(), "Selected: " + email, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        findViews(view);

        Find_MB_FindLocation.setOnClickListener(v -> {
            testLocation();
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

        location = (android.location.LocationManager) getContext().getSystemService(getContext().LOCATION_SERVICE);

        String coarse = "android.permission.ACCESS_COARSE_LOCATION";
        String fine = "android.permission.ACCESS_FINE_LOCATION";
        if (ContextCompat.checkSelfPermission(getContext(), coarse) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{coarse}, 101);
        }
        if (ContextCompat.checkSelfPermission(getContext(), fine) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{fine}, 102);
        }
        Location locNew = location.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER);

        if (locNew != null) {
            this.latitude = locNew.getLatitude();
            this.longitude = locNew.getLongitude();

            System.out.println("LATITUDE: " + locNew.getLatitude());
            System.out.println("LONGITUDE: " + locNew.getLongitude());
        } else {
            this.latitude = 0.0;
            this.longitude = 0.0;
            System.out.println("LOCATION IS NULL, 0,0 values given");
        }
        return view;
    }
    private void findViews(View view) {
        Find_MB_FindLocation = view.findViewById(R.id.Find_MB_FindLocation);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        LatLng sydney = new LatLng(-34, 151);
        this.googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void testLocation(){
        Toast.makeText(getContext(), "Finding test", Toast.LENGTH_SHORT).show();
        // TODO : Send the kid email to miniapp KidFind and get location boundary
        ObjectApi apiService = ApiController.getRetrofitInstance().create(ObjectApi.class);
        Call<ObjectBoundary[]> call = apiService.findAllObjects("MiniHeros", email.toLowerCase(), 1000, 0);
        call.enqueue(new Callback<ObjectBoundary[]>() {
            @Override
            public void onResponse(Call<ObjectBoundary[]> call, Response<ObjectBoundary[]> response) {
                if (response.isSuccessful()) {
                    try {
                        ObjectBoundary[] responseAllBody = response.body();
                        // Log or display the response
                        ObjectBoundary responseBody = responseAllBody[0];
                        LatLng location = new LatLng(responseBody.getLocation().getLat(), responseBody.getLocation().getLng());
                        googleMap.addMarker(new MarkerOptions().position(location).title("Marker in Sydney"));
                        googleMap.getUiSettings().setZoomControlsEnabled(true);

                        // Set initial zoom level
                        float zoomLevel = 10.0f; // Example zoom level
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));

                        Toast.makeText(getContext(), "LoginResponse "+responseBody.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("LoginResponse", responseBody.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext(), "Finding test"+response.code(), Toast.LENGTH_SHORT).show();
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

}