package com.example.prm392_project.Activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392_project.Adapter.MonthRevenue;
import com.example.prm392_project.Bean.Order;
import com.example.prm392_project.R;
import com.example.prm392_project.Repositories.OrderRepository;
import com.example.prm392_project.Repositories.UserRepository;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DashboardActivity extends BaseActivity {
    private TextView tvUsers;
    private TextView tvOrders;
    private TextView tvRevenue;
    private UserRepository userRepository;
    private OrderRepository orderRepository;
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
        setupBarChart();
        tvUsers = findViewById(R.id.tvUsers);
        tvOrders = findViewById(R.id.tvOrders);
        tvRevenue = findViewById(R.id.tvRevenue);
        List<Order> orders = orderRepository.getAll();
        int totalRevenue = 0;
        for (Order order : orders) {
            totalRevenue += (int)order.getTotalPrice();
        }
        tvOrders.setText(String.valueOf(orders.stream().count()));
        tvUsers.setText(String.valueOf(userRepository.getAllUsers().stream().count()));
        tvRevenue.setText(String.valueOf(totalRevenue));
    }
        private void setupBarChart() {
            BarChart barChart = findViewById(R.id.barChart);

            // Dữ liệu mẫu cho lượt truy cập theo tháng
            List<MonthRevenue> result = orderRepository.getMonthRevenue("2024");
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
