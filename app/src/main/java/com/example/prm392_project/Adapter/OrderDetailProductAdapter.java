package com.example.prm392_project.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_project.Bean.POJO.OrderDetailWithProduct;
import com.example.prm392_project.R;

import java.util.List;

public class OrderDetailProductAdapter extends RecyclerView.Adapter<OrderDetailProductAdapter.OrderProductViewHolder> {
    private List<OrderDetailWithProduct> productList;

    public OrderDetailProductAdapter(List<OrderDetailWithProduct> productList){
        this.productList = productList;
    }
    @NonNull
    @Override
    public OrderDetailProductAdapter.OrderProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_detail_product, parent, false);
        return new OrderProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailProductAdapter.OrderProductViewHolder holder, int position) {
        OrderDetailWithProduct item = productList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    public void updateData(List<OrderDetailWithProduct> newProductList){
        this.productList = newProductList;
        notifyDataSetChanged();
    }
    public class OrderProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProductImage;
        private TextView tvProductName;
        private TextView tvProductPrice;
        private TextView tvOrderDetailQuantity;

        public OrderProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvOrderDetailQuantity = itemView.findViewById(R.id.tvOrderDetailQuantity);
        }

        // bind data tu backend vao item layout
        private void bind(OrderDetailWithProduct item){
            tvProductName.setText(item.product.getProductName());
            tvProductPrice.setText(String.format("%,dđ", (long) item.product.getPrice()));
            tvOrderDetailQuantity.setText("Quantity: " + item.orderDetail.getQTY_int());
        }
    }
}
