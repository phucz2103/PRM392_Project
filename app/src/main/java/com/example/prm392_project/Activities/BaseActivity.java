package com.example.prm392_project.Activities;

import android.content.Intent;
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
            // The layout doesn't include the bottom navigation
            return;
        }

        // Set the selected item based on current activity
        int selectedItemId = getBottomNavItemIdForActivity();
        if (selectedItemId != -1) {
            bottomNavigationView.setSelectedItemId(selectedItemId);
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            // Only navigate if we're not already on this screen
            if (itemId != getBottomNavItemIdForActivity()) {
                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(this, HomeActivity.class));
                } else if (itemId == R.id.nav_sales) {
                    startActivity(new Intent(this, SaleActivity.class));
                } else if(itemId == R.id.nav_order_history){
                    //startActivity(new Intent(this, OrderHistoryActivity.class));
                }
                else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(this, UserProfileActivity.class));
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
//        } else if (className.equals("com.example.prm392_project.Activities.CartActivity")) {
//            return R.id.nav_cart;
         else if (className.equals(UserProfileActivity.class.getName())) {
            return R.id.nav_profile;
        }
        return -1;
    }
}