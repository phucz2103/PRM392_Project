package com.example.prm392_project.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_project.Adapter.OrderListAdapter;
import com.example.prm392_project.Bean.POJO.OrderWithUser;
import com.example.prm392_project.R;
import com.example.prm392_project.Repositories.OrderRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class OrderListActivity extends AppCompatActivity {
    private RecyclerView orderRecyclerView;
    private OrderListAdapter orderListAdapter;
    private List<OrderWithUser> orderWithUserList;
    private List<OrderWithUser> filteredOrderList;
    private OrderRepository orderRepository;
    private ImageView ivnRefresh, ivFromDatePicker, ivEndDatePicker;
    private TextView tvFromDate, tvEndDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // khoi tao repository
        orderRepository = new OrderRepository(this);
        orderWithUserList = new ArrayList<>();
        filteredOrderList = new ArrayList<>();
        // khoi tao recyclerview
        orderRecyclerView = findViewById(R.id.recyclerViewOrderList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        orderRecyclerView.setLayoutManager(layoutManager);
        // add divider between item
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(orderRecyclerView.getContext(), layoutManager.getOrientation());
        orderRecyclerView.addItemDecoration(dividerItemDecoration);
        // khoi tao adapter
        orderListAdapter = new OrderListAdapter(orderWithUserList, this);
        orderRecyclerView.setAdapter(orderListAdapter);

        // Khởi tạo các view cho bộ chọn ngày
        tvFromDate = findViewById(R.id.tvFromDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        ivFromDatePicker = findViewById(R.id.ivFromDatePicker);
        ivEndDatePicker = findViewById(R.id.ivEndDatePicker);
        // Thiết lập bộ chọn ngày
        setupDatePicker(ivFromDatePicker, tvFromDate);
        setupDatePicker(ivEndDatePicker, tvEndDate);
        // get button refresh
        ivnRefresh = findViewById(R.id.ivRefresh);
        ivnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // xóa data sẵn có trên textview StartDate và EndDate
                tvFromDate.setText("");
                tvEndDate.setText("");
                loadOrders();
            }
        });
        loadOrders();
    }

    private void setupDatePicker(ImageView imageView, TextView textView) {
        imageView.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    OrderListActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, dayOfMonth);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        textView.setText(dateFormat.format(selectedDate.getTime())); // bind date từ DatePickerDialog sang TextView
                        // Áp dụng bộ lọc ngay sau khi chọn ngày
                        filterOrders();
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });
    }

    private void loadOrders() {
        new Thread(() -> {
            final List<OrderWithUser> orderWithUsers = orderRepository.getAllOrderWithUser();
            runOnUiThread(() -> {
                orderWithUserList.clear();
                if (orderWithUsers != null && !orderWithUsers.isEmpty()) {
                    orderWithUserList.addAll(orderWithUsers);
                } else {
                    Log.e("OrderListActivity", "Không tìm thấy đơn hàng");
                }
                filterOrders(); // Áp dụng bộ lọc (sẽ hiển thị toàn bộ danh sách nếu không có ngày được chọn)
            });
        }).start();
    }

    private void filterOrders() {
        String fromDateStr = tvFromDate.getText().toString();
        String endDateStr = tvEndDate.getText().toString();

        filteredOrderList.clear();
        if (fromDateStr.isEmpty() && endDateStr.isEmpty()) {
            filteredOrderList.addAll(orderWithUserList);
        } else {
            // Định dạng cho ngày đầu vào từ TextView (dd/MM/yyyy)
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            // Định dạng cho ngày trong cơ sở dữ liệu (yyyy-MM-dd HH:mm:ss)
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

            try {
                long fromDate = fromDateStr.isEmpty() ? Long.MIN_VALUE : inputFormat.parse(fromDateStr).getTime();
                long endDate = endDateStr.isEmpty() ? Long.MAX_VALUE : inputFormat.parse(endDateStr).getTime();

                for (OrderWithUser orderWithUser : orderWithUserList) {
                    String orderDateStr = orderWithUser.order.getOrderDate();
                    long orderDate = dateFormat.parse(orderDateStr).getTime();
                    if (orderDate >= fromDate && orderDate <= endDate) {
                        filteredOrderList.add(orderWithUser);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                filteredOrderList.addAll(orderWithUserList); // Quay lại tất cả đơn hàng nếu phân tích thất bại
            }
        }
        orderListAdapter.updateData(filteredOrderList);
    }
}