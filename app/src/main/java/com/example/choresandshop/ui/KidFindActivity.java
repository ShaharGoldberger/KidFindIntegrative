package com.example.choresandshop.ui;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.choresandshop.CurrentUserManager;
import com.example.choresandshop.R;
import com.example.choresandshop.ui.kidfind.KidFindFragment;
import com.example.choresandshop.ui.kidfind.KidFindFragmentChild;
import com.example.choresandshop.ui.kidfind.KidFindFragmentParent;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class KidFindActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_kid_find);
        showFragment();

    }

    private void showFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new KidFindFragment());
        transaction.commit();

    }
}