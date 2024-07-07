package com.example.choresandshop.ui.kidfind;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.choresandshop.R;
import com.example.choresandshop.ui.CurrentUserManager;
import com.google.android.material.button.MaterialButton;

import com.google.android.gms.maps.GoogleMap;

public class KidFindFragment extends Fragment {

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

        View view = inflater.inflate(R.layout.fragment_kid_find, container, false);
        // Get reference to the Spinner
        showFragment();

        return view;
    }



    private void showFragment() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (CurrentUserManager.getInstance().isChild()){
            transaction.replace(R.id.fragment_container, new KidFindFragmentChild());
            transaction.commit();
        }else{
            transaction.replace(R.id.fragment_container, new KidFindFragmentParent());
            transaction.commit();
        }

    }

}