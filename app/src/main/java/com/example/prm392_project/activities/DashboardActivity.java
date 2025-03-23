package com.example.prm392_project.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_project.adapter.DashboardAdapter;
import com.example.prm392_project.bean.Category;
import com.example.prm392_project.bean.Order;
import com.example.prm392_project.R;
import com.example.prm392_project.repositories.CategoryRepository;
import com.example.prm392_project.repositories.OrderRepository;
import com.example.prm392_project.repositories.ProductRepository;
import com.example.prm392_project.repositories.UserRepository;
import com.example.prm392_project.bean.pojo.MonthRevenue;
import com.example.prm392_project.bean.pojo.TopProduct;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DashboardActivity extends BaseActivity {
    private TextView tvUsers,tvOrders,tvCategories,tvRevenue;
    private ImageView imgUsers,imgOrders,imgCategory;
    private UserRepository userRepository;
    private OrderRepository orderRepository;
    private CategoryRepository cateRepository;
    private ProductRepository productRepository;
    private RecyclerView recycleview;
    private DashboardAdapter dashboardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
        setupBottomNavigation();
        userRepository = new UserRepository(this);
        orderRepository = new OrderRepository(this);
        cateRepository = new CategoryRepository(this);
        productRepository = new ProductRepository(this);

        recycleview = findViewById(R.id.recycler_view);
        recycleview.setLayoutManager(new LinearLayoutManager(this));
        List<TopProduct> topProductList = productRepository.getTopSellingProductsLastMonth();
        if (topProductList == null) {
            topProductList = new ArrayList<>();
        }
        dashboardAdapter = new DashboardAdapter(this, topProductList);
        recycleview.setAdapter(dashboardAdapter);

        setupBarChart();
        tvUsers = findViewById(R.id.tvUsers);
        tvOrders = findViewById(R.id.tvOrders);
        tvRevenue = findViewById(R.id.tvRevenue);
        tvCategories = findViewById(R.id.tvCategory);

        List<Order> orders = orderRepository.getAll();
        List<Category> cates = cateRepository.getAllCategories();
        int totalRevenue = 0;
        for (Order order : orders) {
            totalRevenue += (int)order.getTotalPrice();
        }
        tvOrders.setText(String.valueOf(orders.stream().count()));
        tvCategories.setText(String.valueOf(cates.stream().count()));
        tvUsers.setText(String.valueOf(userRepository.getAllUsers().stream().count()));
        tvRevenue.setText(String.valueOf(totalRevenue));

        imgUsers = findViewById(R.id.negativeUser);
        imgCategory = findViewById(R.id.negativeCategory);
        imgOrders = findViewById(R.id.negativeOrder);

        imgUsers.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, UserListActivity.class);
            startActivity(intent);
        });
        imgOrders.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, OrderListActivity.class);
            startActivity(intent);
        });
        imgCategory.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ListCategoriesActivity.class);
            startActivity(intent);
        });
    }
        private void setupBarChart() {
            BarChart barChart = findViewById(R.id.barChart);

            // Dữ liệu mẫu cho lượt truy cập theo tháng
            List<MonthRevenue> result = orderRepository.getMonthRevenue("2025");
            float[] monthlyRevenue = new float[12];
            Arrays.fill(monthlyRevenue, 0);

            for (MonthRevenue monthRevenue : result) {
                int month = monthRevenue.month - 1;
                monthlyRevenue[month] = monthRevenue.revenue;
            }

            ArrayList<BarEntry> entries = new ArrayList<>();
            for (int i = 0; i < 12; i++) {
                entries.add(new BarEntry(i, monthlyRevenue[i]));
            }

            // Thiết lập dataset
            BarDataSet dataSet = new BarDataSet(entries, "Profile Visits");
            dataSet.setColor(getResources().getColor(android.R.color.holo_blue_dark));
            dataSet.setValueTextSize(12f);

            BarData barData = new BarData(dataSet);
            barData.setBarWidth(0.5f); // Độ rộng cột

            // Thiết lập trục X (các tháng)
            String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(months));
            barChart.getXAxis().setPosition(com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM);
            barChart.getXAxis().setGranularity(1f);
            barChart.getXAxis().setLabelCount(12);

            barChart.getAxisLeft().setAxisMinimum(0f);
            barChart.getAxisLeft().setAxisMaximum(10000f);
            barChart.getAxisLeft().setLabelCount(5);
            barChart.getAxisRight().setEnabled(false);

            // Tắt mô tả và legend
            barChart.getDescription().setEnabled(false);
            barChart.getLegend().setEnabled(false);

            // Hiển thị dữ liệu
            barChart.setData(barData);
            barChart.invalidate();
        }
}
