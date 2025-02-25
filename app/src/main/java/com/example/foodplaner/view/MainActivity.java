package com.example.foodplaner.view;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.foodplaner.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        if (bottomNavigationView == null) {
            Log.e("MainActivity", "bottomNavigationView is null!");
        }

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainerView);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
        }
    }
}
