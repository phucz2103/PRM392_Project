package com.example.prm392_project.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_project.Activities.CustomerOrderDetailActivity;
import com.example.prm392_project.Bean.Order;
import com.example.prm392_project.DAO.OrderDao;
import com.example.prm392_project.R;
import com.example.prm392_project.Repositories.OrderRepository;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryItemViewHolder> {

    private List<Order> orderList;
    private Context context;
    private OnOrderCancelListener cancelListener;
    public OrderHistoryAdapter(List<Order> orderList, Context context, OnOrderCancelListener cancelListener) {
        this.orderList = orderList;
        this.context = context;
        this.cancelListener = cancelListener;
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

    public void removeItem(int position) {
        if (position >= 0 && position < orderList.size()) {
            orderList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, orderList.size()); // Cập nhật lại các vị trí sau khi xóa
        } else {
            Log.e("OrderHistoryAdapter", "Invalid position for removal: " + position);
        }
    }
    public static class OrderHistoryItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProductImage;
        private TextView tvOrderCode;
        private TextView tvTotalPrice;
        private Button btnCancel;
        private Button btnDetails;
        private Order order;
        private Context context;
        private OrderRepository orderRepository;
        private OnOrderCancelListener cancelListener;
        public OrderHistoryItemViewHolder(@NonNull View itemView) {
            super(itemView);
            // lấy context từ itemView
            this.context = itemView.getContext();
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
                        showCancelConfirmationDialog(position);
                    }
                }
            });

            // Xử lý sự kiện nút Details
            btnDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Log.d("OrderHistoryAdapter", "Details clicked, position: " + position + ", order: " + (order != null ? order.getOrderID() : "null"));
                    if (position != RecyclerView.NO_POSITION && order != null) {
                            Intent intent = new Intent(v.getContext(), CustomerOrderDetailActivity.class);
                            intent.putExtra("ORDER_ID", order.getOrderID());
                            v.getContext().startActivity(intent);
                    }
                }
            });
        }

        public void bind(Order order) {
            this.order = order; // bind dữ liệu cho biến Order toàn cục
            // Set dữ liệu thủ công
            tvOrderCode.setText(order.getOrderCode().toString());
            tvTotalPrice.setText(String.format("%,dđ", (long) order.getTotalPrice()));
            btnCancel.setVisibility(order.getStatus() == 0 ? View.VISIBLE : View.GONE);
            // thêm logic để set hình ảnh cho ivProductImage
        }

        // hàm hiển thị dialog xác nhận hủy đơn hàng
        private void showCancelConfirmationDialog(final int position){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Cancel Confirmation")
                    .setMessage("Are you sure you want to cancel this order?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelOrder(position);
                            dialog.dismiss();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
        }

        // logic hủy đơn hàng
        private void cancelOrder(int position) {
            if (order != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        orderRepository = new OrderRepository(context);
                        orderRepository.updateOrderStatus(order.getOrderID(), 3);
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Thông báo cho adapter xóa item
                                if (cancelListener != null) {
                                    cancelListener.onOrderCanceled(position);
                                }
                            }
                        });
                    }
                }).start();
            } else {
                Log.e("OrderHistoryAdapter", "Cannot cancel order, order is null");
            }
        }
    }
    public interface OnOrderCancelListener {
        void onOrderCanceled(int position);
    }
}