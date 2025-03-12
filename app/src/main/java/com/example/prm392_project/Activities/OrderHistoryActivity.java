package com.example.prm392_project.Activities;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_project.Adapter.OrderHistoryAdapter;
import com.example.prm392_project.Bean.Order;
import com.example.prm392_project.Database.AppDatabase;
import com.example.prm392_project.R;
import com.example.prm392_project.Repositories.OrderRepository;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {
    private RecyclerView orderRecyclerView;
    private OrderHistoryAdapter adapter;
    private List<Order> orderList;
    private List<Order> filteredOrder;
    private OrderRepository orderRepository;
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // khoi tao repository
        orderRepository = new OrderRepository(this);
        // insert data test
        // khoi tao recyclerview
        orderRecyclerView = findViewById(R.id.recyclerViewOrderHistory);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // demo order
        orderList = orderRepository.getOrderByUser(userID);
        // lay order pending theo user
        filteredOrder = orderRepository.getOrderPendingByUser(userID);

        Log.d(TAG, "Initial filteredOrders (Pending) size: " + filteredOrder.size());

        adapter = new OrderHistoryAdapter(filteredOrder);
        orderRecyclerView.setAdapter(adapter);

        TextView tabPending = findViewById(R.id.tvTabPending);
        TextView tabPurchased = findViewById(R.id.tvTabPurchased);
        // ban dau se hien thi tab pending truoc
        tabPending.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        tabPurchased.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        // khi tab pending duoc click
        tabPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filteredOrder.clear();
                filteredOrder = orderRepository.getOrderPendingByUser(userID);
                adapter.updateData(filteredOrder);
                Log.d(TAG, "Updated to Pending, filteredOrders size: " + filteredOrder.size());
                tabPending.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                tabPurchased.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            }
        });

        // khi tab Purchased duoc click
        tabPurchased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filteredOrder.clear();
                filteredOrder = orderRepository.getOrderPurchasedByUser(userID);
                adapter.updateData(filteredOrder);
                Log.d(TAG, "Updated to Purchased, filteredOrders size: " + filteredOrder.size());
                tabPurchased.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                tabPending.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            }
        });
    }
}