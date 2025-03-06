package com.example.prm392_project.Activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.prm392_project.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setupBarChart();
    }
        private void setupBarChart() {
            BarChart barChart = findViewById(R.id.barChart);

            // Dữ liệu mẫu cho lượt truy cập theo tháng
            ArrayList<BarEntry> entries = new ArrayList<>();
            entries.add(new BarEntry(0, 8f));  // Jan
            entries.add(new BarEntry(1, 16f)); // Feb
            entries.add(new BarEntry(2, 28f)); // Mar
            entries.add(new BarEntry(3, 8f));  // Apr
            entries.add(new BarEntry(4, 20f)); // May
            entries.add(new BarEntry(5, 24f)); // Jun
            entries.add(new BarEntry(6, 16f)); // Jul
            entries.add(new BarEntry(7, 8f));  // Aug
            entries.add(new BarEntry(8, 20f)); // Sep
            entries.add(new BarEntry(9, 16f)); // Oct
            entries.add(new BarEntry(10, 12f)); // Nov
            entries.add(new BarEntry(11, 16f)); // Dec

            // Thiết lập dataset
            BarDataSet dataSet = new BarDataSet(entries, "Profile Visits");
            dataSet.setColor(getResources().getColor(android.R.color.holo_blue_dark));
            dataSet.setValueTextSize(12f);

            // Thiết lập dữ liệu cho biểu đồ
            BarData barData = new BarData(dataSet);
            barData.setBarWidth(0.5f); // Độ rộng cột

            // Thiết lập trục X (các tháng)
            String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(months));
            barChart.getXAxis().setPosition(com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM);
            barChart.getXAxis().setGranularity(1f);
            barChart.getXAxis().setLabelCount(12);

            // Tùy chỉnh trục Y
            barChart.getAxisLeft().setAxisMinimum(0f);
            barChart.getAxisLeft().setAxisMaximum(32f);
            barChart.getAxisLeft().setLabelCount(5);
            barChart.getAxisRight().setEnabled(false); // Ẩn trục Y bên phải

            // Tắt mô tả và legend
            barChart.getDescription().setEnabled(false);
            barChart.getLegend().setEnabled(false);

            // Hiển thị dữ liệu
            barChart.setData(barData);
            barChart.invalidate(); // Cập nhật biểu đồ
        }
}
