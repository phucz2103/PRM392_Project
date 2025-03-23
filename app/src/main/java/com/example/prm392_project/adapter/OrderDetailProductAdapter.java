package com.example.prm392_project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_project.bean.pojo.OrderDetailWithProduct;
import com.example.prm392_project.R;
import com.squareup.picasso.Picasso;

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
            if (item.product.getIMAGE_URL() != null && !item.product.getIMAGE_URL().isEmpty()) {
                try {
                    Picasso.get()
                            .load(item.product.getIMAGE_URL())
                            .placeholder(R.drawable.avatar)
                            .error(R.drawable.error)
                            .into(ivProductImage);
                } catch (Exception e) {
                    e.printStackTrace();
                    ivProductImage.setImageResource(R.drawable.error);
                }
            } else {
                ivProductImage.setImageResource(R.drawable.avatar);
            }
            tvProductName.setText(item.product.getProductName());
            tvProductPrice.setText(String.format("%,dđ", (long) item.product.getPrice()));
            tvOrderDetailQuantity.setText("Quantity: " + item.orderDetail.getQTY_int());
        }

    }
}
