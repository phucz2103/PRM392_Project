package com.example.prm392_project.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392_project.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BaseActivity extends AppCompatActivity {
    protected BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (bottomNavigationView == null) {
            return;
        }

        // Set the selected item based on current activity
        int selectedItemId = getBottomNavItemIdForActivity();
        if (selectedItemId != -1) {
            bottomNavigationView.setSelectedItemId(selectedItemId);
        }

        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);
        boolean isAdmin = sharedPreferences.getBoolean("isAdmin", false);

        bottomNavigationView.getMenu().findItem(R.id.nav_admin).setVisible(isAdmin);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            // Only navigate if we're not already on this screen
            if (itemId != getBottomNavItemIdForActivity()) {
                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(this, HomeActivity.class));
                }

                if(userId!=-1){
                    if (itemId == R.id.nav_cart) {
                        startActivity(new Intent(this, CartActivity.class));
                    } else if (itemId == R.id.nav_order_history) {
                        startActivity(new Intent(this, OrderHistoryActivity.class));
                    } else if (itemId == R.id.nav_profile) {
                        startActivity(new Intent(this, UserProfileActivity.class));
                    } else if (itemId == R.id.nav_admin) {
                        startActivity(new Intent(this, DashboardActivity.class));
                    }
                }else{
                    if (itemId == R.id.nav_cart) {
                        startActivity(new Intent(this, LoginActivity.class));
                    } else if (itemId == R.id.nav_order_history) {
                        startActivity(new Intent(this, LoginActivity.class));
                    } else if (itemId == R.id.nav_profile) {
                        startActivity(new Intent(this, LoginActivity.class));
                    }
                }

                return true;
            }
            return true;
        });
    }

    // Return the navigation ID that corresponds to the current activity
    protected int getBottomNavItemIdForActivity() {
        String className = this.getClass().getName();

        if (className.equals(HomeActivity.class.getName())) {
            return R.id.nav_home;}
        else if (className.equals(OrderHistoryActivity.class.getName())) {
            return R.id.nav_order_history;}
         else if (className.equals(UserProfileActivity.class.getName())) {
            return R.id.nav_profile;
        }
         else if(className.equals(CartActivity.class.getName())){
            return R.id.nav_cart;}
        return -1;
    }
}