package com.example.prm392_project.activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_project.adapter.OrderDetailProductAdapter;
import com.example.prm392_project.bean.pojo.OrderDetailWithProduct;
import com.example.prm392_project.bean.pojo.OrderWithUser;
import com.example.prm392_project.R;
import com.example.prm392_project.repositories.OrderRepository;

import java.util.List;

public class CustomerOrderDetailActivity extends AppCompatActivity {
    private TextView tvOrderCode, tvOrderStatus, tvOrderDate, tvFullName, tvAddress, tvTotalAmount, tvTotalAmountFooter;
    private RecyclerView recyclerViewProductList;
    private OrderDetailProductAdapter orderDetailProductAdapter;
    private OrderRepository orderRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_order_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // tìm các thành phần trên trang detail
        tvOrderCode = findViewById(R.id.tvOrderCode);
        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvFullName = findViewById(R.id.tvFullName);
        tvAddress = findViewById(R.id.tvAddress);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvTotalAmountFooter = findViewById(R.id.tvTotalAmountFooter);
        // khởi tạo repository
        orderRepository = new OrderRepository(this);
        recyclerViewProductList = findViewById(R.id.recyclerViewProductList);
        // khởi tạo và setup recycler view cho list Product
        recyclerViewProductList.setLayoutManager(new LinearLayoutManager(this));
        orderDetailProductAdapter = new OrderDetailProductAdapter(null);
        recyclerViewProductList.setAdapter(orderDetailProductAdapter);
        // lấy dữ liệu từ activity khác truyền qua
        int orderId = getIntent().getIntExtra("ORDER_ID", -1);
        if(orderId != -1){
            OrderWithUser orderWithUser = orderRepository.getOderWithUserByID(orderId);
            if(orderWithUser != null && orderWithUser.order != null && orderWithUser.user != null){

                tvOrderCode.setText(orderWithUser.order.getOrderCode().toString());
                tvOrderStatus.setText(getStatusString(orderWithUser.order.getStatus()));
                tvOrderStatus.setTypeface(null, Typeface.BOLD);
                tvOrderDate.setText(orderWithUser.order.getOrderDate().toString());
                tvFullName.setText(orderWithUser.user.getFullName());
                tvAddress.setText(orderWithUser.user.getAddress());
                tvTotalAmount.setText(String.format("%,dđ", (long) orderWithUser.order.getTotalPrice()));
                tvTotalAmountFooter.setText(new StringBuilder().append("TOTAL AMOUNT: ").append(String.format("%,dđ", (long) orderWithUser.order.getTotalPrice())));
                // add color for status
                String color = changeTextColor(orderWithUser.order.getStatus());
                try {
                    tvOrderStatus.setTextColor(Color.parseColor(color));
                } catch (IllegalArgumentException e) {
                    tvOrderStatus.setTextColor(Color.BLACK);
                }
            }else{
                finish();
            }
            // lấy danh sách sản phẩm
            List<OrderDetailWithProduct> productList = orderRepository.getOrderDetailsWithProduct(orderId);
            if(productList != null && !productList.isEmpty()){
                orderDetailProductAdapter.updateData(productList);
            }else{
                tvOrderCode.setText("Invalid Order ID");
            }
        }

    }

    private String getStatusString(int status){
        switch(status){
            case 0:
                return "Pending";
            case 1:
                return "Confirmed";
            case 2:
                return "Rejected";
            case 3:
                return "Canceled";
            default:
                return "Unknown";
        }
    }
    private String changeTextColor(int status) {
        String color = "#000000";
        switch (status) {
            case 0:
                color = "#000000";
                break;
            case 1:
                color = "#27AE60";
                break;
            case 2:
                color = "#E74C3C";
                break;
            case 3:
                color = "#00FFFF";
                break;
        }
        return color;
    }

}