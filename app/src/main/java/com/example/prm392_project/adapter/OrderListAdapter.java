package com.example.prm392_project.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_project.activities.CustomerOrderDetailActivity;
import com.example.prm392_project.activities.OrderListActivity;
import com.example.prm392_project.bean.pojo.OrderWithUser;
import com.example.prm392_project.helpers.Notification;
import com.example.prm392_project.R;
import com.example.prm392_project.repositories.OrderRepository;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderListViewHolder> {
    List<OrderWithUser> orderWithUserList;
    private Context context;
    public OrderListAdapter(List<OrderWithUser> orderWithUserList, Context context) {
        this.orderWithUserList = orderWithUserList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderListAdapter.OrderListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_list, parent, false);
        return new OrderListAdapter.OrderListViewHolder(view, orderWithUserList, context, this);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListAdapter.OrderListViewHolder holder, int position) {
        OrderWithUser orderWithUser = orderWithUserList.get(position);
        holder.bind(orderWithUser);
    }

    @Override
    public int getItemCount() {
        return orderWithUserList != null ? orderWithUserList.size() : 0;
    }

    public void updateData(List<OrderWithUser> newData) {
        orderWithUserList = newData;
        notifyDataSetChanged();
    }

    public static class OrderListViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProductImage;
        private TextView tvOrderCode, tvFullName, tvTotalAmount, tvOrderDate, tvOrderStatus;
        private Button btnUpdate, btnDetails;
        private List<OrderWithUser> orderWithUserList;
        private Context context;
        private OrderRepository orderRepository;
        private OrderListAdapter orderListAdapter;
        public OrderListViewHolder(@NonNull View itemView, List<OrderWithUser> orderWithUserList, Context context, OrderListAdapter adapter) {
            super(itemView);
            // map local variable to global variable
            this.orderWithUserList = orderWithUserList;
            this.context = context;
            this.orderListAdapter = adapter;
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvOrderCode = itemView.findViewById(R.id.tvOrderCode);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDetails = itemView.findViewById(R.id.btnDetails);

            // Xử lý sự kiện nút Cancel
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && orderWithUserList != null && position < orderWithUserList.size()) {
                        OrderWithUser orderWithUser = orderWithUserList.get(position);
                        showUpdateStatusDialog(orderWithUser, position);

                    } else {
                        Log.e("OrderListAdapter", "Cannot update order: invalid position or list is null/empty");
                    }
                }
            });

            // Xử lý sự kiện nút Details
            btnDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        OrderWithUser orderWithUser = orderWithUserList.get(position);
                        Intent intent = new Intent(v.getContext(), CustomerOrderDetailActivity.class);
                        intent.putExtra("ORDER_ID", orderWithUser.order.getOrderID());
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }

        public void bind(OrderWithUser orderWithUser) {
            tvOrderCode.setText(orderWithUser.order.getOrderCode().toString());
            tvFullName.setText(orderWithUser.user.getFullName());
            tvTotalAmount.setText(String.format("Total: %,dđ", (long) orderWithUser.order.getTotalPrice()));
            tvOrderDate.setText(orderWithUser.order.getOrderDate());
            // convert int status to text status
            String statusText = getStatusString(orderWithUser.order.getStatus());
            tvOrderStatus.setText(statusText);
            tvOrderStatus.setTypeface(null, Typeface.BOLD);
            // change color of text status
            String color = changeTextColor(orderWithUser.order.getStatus());
            try {
                tvOrderStatus.setTextColor(Color.parseColor(color));
            } catch (IllegalArgumentException e) {
                tvOrderStatus.setTextColor(Color.BLACK);
            }
            // hide update button
            btnUpdate.setVisibility(orderWithUser.order.getStatus() == 0 ? View.VISIBLE : View.GONE);
        }

        private void showUpdateStatusDialog(OrderWithUser orderWithUser, int position){
            // Tạo dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Update order status");
            builder.setMessage("Please choose new status for this order:");

            // Tạo SpannableString cho nút Confirm với icon tick
            SpannableString confirmText = new SpannableString(" Confirm");
            ImageSpan confirmIcon = new ImageSpan(context, R.drawable.tick_icon_resize);
            confirmText.setSpan(confirmIcon, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            // Tạo SpannableString cho nút Reject với icon X
            SpannableString rejectText = new SpannableString(" Reject");
            ImageSpan rejectIcon = new ImageSpan(context, R.drawable.cross_icon_resize);
            rejectText.setSpan(rejectIcon, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            // Tạo SpannableString cho nút Cancel
            SpannableString cancelText = new SpannableString("Cancel");

            // Thiết lập các nút
            builder.setPositiveButton(confirmText, (dialog, which) -> {
                orderRepository = new OrderRepository(context);
                orderRepository.updateOrderStatus(orderWithUser.order.getOrderID(), 1); // 1: Confirmed
                orderWithUser.order.setStatus(1);
                orderListAdapter.notifyItemChanged(position);
                Notification notified = new Notification(context);
                notified.sendNotification("Order", "Your order has been confirmed, your order will be delivered in 3-5 days");
            });

            builder.setNegativeButton(rejectText, (dialog, which) -> {
                orderRepository = new OrderRepository(context);
                orderRepository.updateOrderStatus(orderWithUser.order.getOrderID(), 2); // 2: Rejected
                orderWithUser.order.setStatus(2);
                orderListAdapter.notifyItemChanged(position);
                Notification notified = new Notification(context);
                notified.sendNotification("Order", "Your order has been rejected");
            });

            builder.setNeutralButton(cancelText, null);

            // Hiển thị dialog và tùy chỉnh màu sắc nút
            AlertDialog dialog = builder.create();
            dialog.show();

            // Tùy chỉnh màu sắc cho các nút
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(ContextCompat.getColor(context, android.R.color.black));
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

        private String getStatusString(int status) {
            switch (status) {
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
    }
}
