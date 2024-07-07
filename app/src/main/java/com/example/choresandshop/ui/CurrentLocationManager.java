package com.example.choresandshop.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class CurrentLocationManager {

    public final static int REQUEST_CODE = 100;
    private static CurrentLocationManager _instance = null;

    private Double latitude = 0.0;
    private Double longitude = 0.0;
    private android.location.LocationManager locationManager;
    private GoogleMap map;

    private CurrentLocationManager(Context context) {
        locationManager = (android.location.LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        requestLocationUpdates(context);
    }

    private void requestLocationUpdates(Context context) {
        String coarse = Manifest.permission.ACCESS_COARSE_LOCATION;
        String fine = Manifest.permission.ACCESS_FINE_LOCATION;

        if (ContextCompat.checkSelfPermission(context, coarse) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, fine) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) context, new String[]{coarse, fine}, REQUEST_CODE);
        } else {
            initializeLocationListener();
        }
    }

    private void initializeLocationListener() {
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                // Optionally update the map with the new location
                if (map != null) {
                    LatLng currentLocation = new LatLng(latitude, longitude);
                    map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };
        try {
            locationManager.requestLocationUpdates(android.location.LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
            Location locNew = locationManager.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER);
            if (locNew != null) {
                this.latitude = locNew.getLatitude();
                this.longitude = locNew.getLongitude();
            } else {
                this.latitude = 0.0;
                this.longitude = 0.0;
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void setGoogleMap(GoogleMap map) {
        this.map = map;
        if (latitude != 0.0 && longitude != 0.0) {
            LatLng currentLocation = new LatLng(latitude, longitude);
            map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        }
    }

    public GoogleMap getGoogleMap() {
        return this.map;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public static synchronized void init(Context context) {
        if (_instance == null) {
            _instance = new CurrentLocationManager(context);
        }
    }

    public static synchronized CurrentLocationManager getInstance() {
        return _instance;
    }


    /*
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private FusedLocationProviderClient fusedLocationClient;

    private static CurrentLocationManager _instance = null;
    private Double latitude = 0.0;
    private Double longitude = 0.0;

    private GoogleMap map;
    private Context context;

    private CurrentLocationManager(Context context) {
        // Request location permissions
        this.context = context;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            getLastLocation();
        }

    }

    public void setGoogleMap(GoogleMap map){
        this.map = map;
    }

    public GoogleMap getGoogleMap(){
        return this.map;
    }
    public Double getLatitude(){
        return this.latitude;
    }
    public Double getLongitude(){
        return this.longitude;
    }

    public static synchronized void init(Context context){
        if ( _instance == null) {
            _instance = new CurrentLocationManager(context);
        }
    }
    public void getLastLocation() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Toast.makeText(context, "Lat: " + latitude + ", Lon: " + longitude, Toast.LENGTH_LONG).show();
                        }else Log.i("Locatiion", "not found");
                    });
        }
    }


    public static synchronized CurrentLocationManager getInstance(){
        return _instance;
    }
*/
}
