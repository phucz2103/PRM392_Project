package com.example.prm392_project.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_project.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BaseActivity extends AppCompatActivity {
    // Thanh điều hướng ở dưới
    protected BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Thiết lập thanh điều hướng ở dưới cho Activity
     */
    protected void setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Kiểm tra xem layout có chứa bottom navigation không
        if (bottomNavigationView == null) {
            return;
        }

        // Thiết lập mục đang chọn dựa trên activity hiện tại
        int selectedItemId = getBottomNavItemIdForActivity();
        if (selectedItemId != -1) {
            bottomNavigationView.setSelectedItemId(selectedItemId);
        }

        // Lấy thông tin người dùng từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);
        boolean isAdmin = sharedPreferences.getBoolean("isAdmin", false);

        // Chỉ hiển thị tính năng Admin cho người dùng có quyền admin
        bottomNavigationView.getMenu().findItem(R.id.nav_admin).setVisible(isAdmin);

        // Ẩn tính năng Cart đối với admin
        bottomNavigationView.getMenu().findItem(R.id.nav_cart).setVisible(!isAdmin);

        // Thay đổi hiển thị của "Order History" thành "Order Management" cho admin
        MenuItem orderItem = bottomNavigationView.getMenu().findItem(R.id.nav_order_history);
        if (isAdmin && orderItem != null) {
            orderItem.setTitle("Order Management");
            orderItem.setIcon(R.drawable.order_management);
        }

        // Xử lý sự kiện khi nhấp vào các mục trong thanh điều hướng
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            // Kiểm tra nếu đã đăng nhập trước khi cho phép truy cập một số tính năng
            if ((itemId == R.id.nav_cart || itemId == R.id.nav_order_history || itemId == R.id.nav_profile) && !isUserLoggedIn()) {
                showLoginRequiredDialog();
                return false;
            }

            // Nếu đang ở activity hiện tại, không cần chuyển đổi
            if (itemId == getBottomNavItemIdForActivity()) {
                return true;
            }

            // Chuyển đến activity tương ứng dựa trên mục được chọn
            navigateToSelectedActivity(itemId);
            return true;
        });
    }

    /**
     * Hiển thị hộp thoại yêu cầu đăng nhập
     */
    private void showLoginRequiredDialog() {
        new AlertDialog.Builder(BaseActivity.this)
                .setTitle("Login")
                .setMessage("Login to continue")
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    startActivity(new Intent(this, LoginActivity.class)); // Chuyển sang Login sau khi ấn OK
                })
                .setIcon(R.drawable.login)
                .show();
    }

    /**
     * Lấy ID của mục trong bottom navigation tương ứng với activity hiện tại
     * @return ID của mục tương ứng, hoặc -1 nếu không tìm thấy
     */
    protected int getBottomNavItemIdForActivity() {
        // Xác định ID mục dựa trên lớp Activity hiện tại
        if (this instanceof HomeActivity) {
            return R.id.nav_home;
        } else if (this instanceof CartActivity) {
            return R.id.nav_cart;
        } else if (this instanceof OrderHistoryActivity || this instanceof OrderListActivity) {
            return R.id.nav_order_history;
        } else if (this instanceof UserProfileActivity) {
            return R.id.nav_profile;
        } else if (this instanceof DashboardActivity) {
            return R.id.nav_admin;
        }
        return -1;
    }

    /**
     * Chuyển đến activity tương ứng với mục được chọn trong bottom navigation
     * @param itemId ID của mục được chọn
     */
    protected void navigateToSelectedActivity(int itemId) {
        // Tạo intent cho activity tương ứng
        Intent intent = null;

        if (itemId == R.id.nav_home) {
            intent = new Intent(this, HomeActivity.class);
        } else if (itemId == R.id.nav_cart) {
            intent = new Intent(this, CartActivity.class);
        } else if (itemId == R.id.nav_order_history) {
            // Kiểm tra quyền admin để quyết định chuyển đến trang quản lý đơn hàng hay lịch sử đơn hàng
            SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
            boolean isAdmin = sharedPreferences.getBoolean("isAdmin", false);

            if (isAdmin) {
                intent = new Intent(this, OrderListActivity.class);
            } else {
                intent = new Intent(this, OrderHistoryActivity.class);
            }
        } else if (itemId == R.id.nav_profile) {
            intent = new Intent(this, UserProfileActivity.class);
        } else if (itemId == R.id.nav_admin) {
            intent = new Intent(this, DashboardActivity.class);
        }

        // Thực hiện chuyển đến activity mới nếu intent đã được tạo
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
    }

    /**
     * Kiểm tra trạng thái đăng nhập của người dùng
     * @return true nếu đã đăng nhập, false nếu chưa
     */
    protected boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        return sharedPreferences.getInt("userId", -1) != -1;
    }

}