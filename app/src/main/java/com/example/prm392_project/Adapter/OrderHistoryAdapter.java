package com.example.prm392_project.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_project.Activities.CustomerOrderDetailActivity;
import com.example.prm392_project.Bean.Order;
import com.example.prm392_project.R;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryItemViewHolder> {

    private List<Order> orderList;

    public OrderHistoryAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderHistoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_history, parent, false);
        return new OrderHistoryItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryItemViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orderList != null ? orderList.size() : 0;
    }

    public void updateData(List<Order> newOrderList) {
        this.orderList = newOrderList;
        notifyDataSetChanged();
    }

    public static class OrderHistoryItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProductImage;
        private TextView tvOrderCode;
        private TextView tvTotalPrice;
        private Button btnCancel;
        private Button btnDetails;
        private Order order;

        public OrderHistoryItemViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ các view thủ công
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvOrderCode = itemView.findViewById(R.id.tvOrderCode);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            btnCancel = itemView.findViewById(R.id.btnCancel);
            btnDetails = itemView.findViewById(R.id.btnDetails);

            // Xử lý sự kiện nút Cancel
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Logic hủy đơn hàng
                    }
                }
            });

            // Xử lý sự kiện nút Details
            btnDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && order != null) {
                            Intent intent = new Intent(v.getContext(), CustomerOrderDetailActivity.class);
                            intent.putExtra("ORDER_ID", order.getOrderID());
                            v.getContext().startActivity(intent);
                    }
                }
            });
        }

        public void bind(Order order) {
            // Set dữ liệu thủ công
            tvOrderCode.setText(order.getOrderCode().toString());
            tvTotalPrice.setText(order.getTotalPrice() + "đ");
            btnCancel.setVisibility(order.getStatus() == 0 ? View.VISIBLE : View.GONE);
            // Có thể thêm logic để set hình ảnh cho ivProductImage nếu cần
        }
    }
}