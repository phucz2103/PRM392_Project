package com.example.prm392_project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_project.Activities.UserDetailActivity;
import com.example.prm392_project.Activities.UserListActivity;
import com.example.prm392_project.Bean.User;
import com.example.prm392_project.R;
import com.example.prm392_project.Repositories.UserRepository;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {
    private Context context;
    private List<User> customerList;
    public CustomerAdapter(Context context, List<User> customerList) {
        this.context = context;
        this.customerList = customerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtCustomerName.setText(customerList.get(position).getFullName());
        if (customerList.get(position).getIsActive()) {
            holder.btnBan.setText("Ban");
            holder.btnBan.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
        } else {
            holder.btnBan.setText("Unban");
            holder.btnBan.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
        }

        // Xử lý sự kiện click vào nút "View"
        holder.btnView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserDetailActivity.class);
            intent.putExtra("UserID", String.valueOf(customerList.get(position).getUserID()));
            context.startActivity(intent);
        });

        // Xử lý sự kiện click vào nút "Ban"
        holder.btnBan.setOnClickListener(v -> {
            new android.app.AlertDialog.Builder(context)
                    .setTitle(customerList.get(position).getIsActive() ? "Confirm Ban" : "Confirm Unban")
                    .setMessage(customerList.get(position).getIsActive() ? "Are you sure to ban this account?" : "Are you sure to unban this account?")
                    .setPositiveButton("Yes", (dialog, which) -> {

                        customerList.get(position).setIsActive(!customerList.get(position).getIsActive());
                        holder.btnBan.setText(customerList.get(position).getIsActive() ? "Ban" : "Unban");

                        UserRepository userRepository = new UserRepository(context);
                        userRepository.updateUser(customerList.get(position));

                        notifyDataSetChanged();
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }
    public void setUserList(List<User> newList) {
        this.customerList = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCustomerName;
        Button btnView, btnBan;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCustomerName = itemView.findViewById(R.id.txt_customer_name);
            btnView = itemView.findViewById(R.id.btn_view);
            btnBan = itemView.findViewById(R.id.btn_ban);
        }
    }
}
