package com.example.prm392_project.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392_project.bean.Cart;
import com.example.prm392_project.bean.Product;
import com.example.prm392_project.R;
import com.example.prm392_project.repositories.ProductRepository;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    private Context context;
    private List<Cart> cartList;
    private OnCartItemClickListener listener;
    private ProductRepository productRepository;
    private boolean isLoading = false; // Biến kiểm soát trạng thái loading

    public interface OnCartItemClickListener {
        void onIncrease(Cart cart);
        void onDecrease(Cart cart);
        void onRemove(Cart cart);
    }

    public CartAdapter(Context context, List<Cart> cartList, OnCartItemClickListener listener, ProductRepository productRepository) {
        this.context = context;
        this.cartList = cartList;
        this.listener = listener;
        this.productRepository = productRepository;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == cartList.size()) ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_shopping_cart, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            Cart cartItem = cartList.get(position);
            Product product = productRepository.getProductById(cartItem.getProductID());

            if (product == null) {
                Log.e("CartAdapter", "Product not found for ID: " + cartItem.getProductID());
                return;
            }

            ItemViewHolder itemHolder = (ItemViewHolder) holder;
            itemHolder.txtProductName.setText(product.getProductName());
            if(product.getIsSaled()){
                itemHolder.txtProductPrice.setText(product.getPrice()*80/100 + " VND");
            }
            else itemHolder.txtProductPrice.setText(product.getPrice() + " VND");
            itemHolder.txtQuantity.setText(String.valueOf(cartItem.getQTY_int()));
            if(itemHolder.txtQuantity.getText().equals("0")){
                itemHolder.btnDecrease.setEnabled(false);
            }

            Glide.with(context)
                    .load(product.getIMAGE_URL())
                    .placeholder(R.drawable.ic_example_foods)
                    .into(itemHolder.imgProduct);

            itemHolder.btnIncrease.setOnClickListener(v -> listener.onIncrease(cartItem));

            itemHolder.btnDecrease.setOnClickListener(v -> listener.onDecrease(cartItem));
            itemHolder.txtRemove.setOnClickListener(v -> listener.onRemove(cartItem));
        }
    }

    @Override
    public int getItemCount() {
        return isLoading ? cartList.size() + 1 : cartList.size();
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        if (loading) {
            notifyItemInserted(cartList.size());
        } else {
            notifyItemRemoved(cartList.size());
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtProductName, txtProductPrice, txtQuantity, txtRemove;
        Button btnDecrease, btnIncrease;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            txtRemove = itemView.findViewById(R.id.txtRemove);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}
