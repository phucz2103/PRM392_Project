package com.example.prm392_project.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_project.bean.Product;
import com.example.prm392_project.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_PRODUCT = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    private Context context;
    private List<Product> productList;
    private OnProductClickListener listener;
    private boolean showLoadingButton = false;
    private OnLoadMoreClickListener loadMoreListener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public interface OnLoadMoreClickListener {
        void onLoadMoreClick();
    }

    public ProductAdapter(Context context, List<Product> productList, OnProductClickListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    public void setLoadMoreListener(OnLoadMoreClickListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public void setShowLoadingButton(boolean showLoadingButton) {
        this.showLoadingButton = showLoadingButton;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == productList.size() && showLoadingButton) {
            return VIEW_TYPE_LOADING;
        }
        return VIEW_TYPE_PRODUCT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_load_more, parent, false);
            return new LoadingViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
            return new ProductViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ProductViewHolder) {
            bindProductViewHolder((ProductViewHolder) holder, position);
        } else if (holder instanceof LoadingViewHolder) {
            bindLoadingViewHolder((LoadingViewHolder) holder);
        }
    }

    private void bindProductViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        // Your existing binding code here
        holder.txtProductName.setText(product.getProductName());

        if (product.getIsSaled()) {
            // Product is on sale - show original price with strikethrough and discounted price
            double originalPrice = product.getPrice();
            double discountedPrice = originalPrice * 0.8; // 20% discount

            // Format original price with strikethrough
            String originalPriceFormatted = String.format("%,d", (int)originalPrice);
            holder.txtOriginalPrice.setVisibility(View.VISIBLE);
            holder.txtOriginalPrice.setText(originalPriceFormatted + " ₫");

            // Apply strikethrough to the original price
            holder.txtOriginalPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            // Format discounted price
            String discountedPriceFormatted = String.format("%,d", (int)discountedPrice);
            holder.txtProductPrice.setText(discountedPriceFormatted + " ₫");

            // Show discount tag
            holder.txtDiscountTag.setVisibility(View.VISIBLE);
        } else {
            // Regular price - no discount
            String formattedPrice = String.format("%,d", (int)product.getPrice());
            holder.txtProductPrice.setText(formattedPrice + " ₫");

            // Hide original price and discount tag
            holder.txtOriginalPrice.setVisibility(View.GONE);
            holder.txtDiscountTag.setVisibility(View.GONE);
        }

        if (product.getIMAGE_URL() != null && !product.getIMAGE_URL().isEmpty()) {
            try {
                Picasso.get()
                        .load(product.getIMAGE_URL())
                        .placeholder(R.drawable.avatar)
                        .error(R.drawable.error)
                        .into(holder.imgProduct);
            } catch (Exception e) {
                e.printStackTrace();
                holder.imgProduct.setImageResource(R.drawable.error);
            }
        } else {
            holder.imgProduct.setImageResource(R.drawable.avatar);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductClick(product);
            }
        });
    }

    private void bindLoadingViewHolder(LoadingViewHolder holder) {
        holder.btnLoadMore.setOnClickListener(v -> {
            if (loadMoreListener != null) {
                loadMoreListener.onLoadMoreClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return showLoadingButton ? productList.size() + 1 : productList.size();
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtProductName, txtProductPrice, txtOriginalPrice, txtDiscountTag;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            txtOriginalPrice = itemView.findViewById(R.id.txtOriginalPrice);
            txtDiscountTag = itemView.findViewById(R.id.txtDiscountTag);
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        Button btnLoadMore;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            btnLoadMore = itemView.findViewById(R.id.btnLoadMore);
        }
    }
}