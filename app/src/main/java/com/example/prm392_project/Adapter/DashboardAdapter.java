package com.example.prm392_project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_project.Bean.Product;
import com.example.prm392_project.R;
import com.example.prm392_project.dto.TopProduct;

import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {
    private Context context;
    private List<TopProduct> topProductList;



    public DashboardAdapter(Context context, List<TopProduct> topProductList) {
        this.context = context;
        this.topProductList = topProductList;
    }

    @NonNull
    @Override
    public DashboardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dashboard, parent, false);
        return new DashboardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardAdapter.ViewHolder holder, int position) {
        TopProduct topproduct = topProductList.get(position);
        holder.txtDashProductName.setText(topproduct.getProductName());
        holder.txtDashPrice.setText(String.valueOf(topproduct.Price));
        holder.txtQuantity.setText(String.valueOf(topproduct.totalQuantitySold));
    }

    @Override
    public int getItemCount() {
        return topProductList.size();
    }

    public void setProductList(List<TopProduct> topProductList) {
        this.topProductList = topProductList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDashProductName,txtDashPrice,txtQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDashProductName = itemView.findViewById(R.id.DashProductName);
            txtDashPrice = itemView.findViewById(R.id.DashPrice);
            txtQuantity = itemView.findViewById(R.id.DashQuantity);
        }
    }
}
