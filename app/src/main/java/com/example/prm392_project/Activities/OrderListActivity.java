package com.example.prm392_project.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_project.Adapter.OrderListAdapter;
import com.example.prm392_project.Bean.POJO.OrderWithUser;
import com.example.prm392_project.DAO.OrderDao;
import com.example.prm392_project.R;
import com.example.prm392_project.Repositories.OrderRepository;

import java.util.ArrayList;
import java.util.List;

public class OrderListActivity extends AppCompatActivity {
    private RecyclerView orderRecyclerView;
    private OrderListAdapter orderListAdapter;
    private List<OrderWithUser> orderWithUserList;
    private OrderRepository orderRepository;
    private ImageView ivnRefresh;
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
        // khoi tao recyclerview
        orderRecyclerView = findViewById(R.id.recyclerViewOrderList);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // khoi tao adapter
        orderListAdapter = new OrderListAdapter(orderWithUserList, this);
        orderRecyclerView.setAdapter(orderListAdapter);
        // get button refresh
        ivnRefresh = findViewById(R.id.ivRefresh);
        ivnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadOrders();
            }
        });
        loadOrders();
    }

    private void loadOrders(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<OrderWithUser> orderWithUsers = orderRepository.getAllOrderWithUser();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        orderWithUserList.clear();
                        if(orderWithUsers != null && !orderWithUsers.isEmpty()){
                           orderWithUserList.addAll(orderWithUsers);
                        }else {
                            Log.e("OrderListActivity", "No orders found");
                        }
                        orderListAdapter.updateData(orderWithUserList);
                    }
                });
            }
        }).start();
    }
}