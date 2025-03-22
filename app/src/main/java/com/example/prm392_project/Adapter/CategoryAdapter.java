package com.example.prm392_project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_project.Bean.Category;
import com.example.prm392_project.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context context;
    private List<Category> categoryList;
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    public CategoryAdapter(Context context, List<Category> categoryList, OnCategoryClickListener listener) {
        this.context = context;
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.txtCategoryName.setText(category.getCategoryName());
        //holder.txtQuantity.setText("Quantity: " + category.getQuantity());


        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCategoryClick(category);
            }
        });


        holder.btnUpdate.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCategoryClick(category);
                Toast.makeText(context, "Updating: " + category.getCategoryName(), Toast.LENGTH_SHORT).show();
                // Cập nhật số lượng hoặc thực hiện hành động update khác
                //category.setQuantity(category.getQuantity() + 1);
                notifyDataSetChanged();
            }
        });

        if (!category.getIsAvailable()) {
            holder.txtCategoryName.setAlpha(0.5f);
            holder.txtQuantity.setAlpha(0.5f);
            holder.btnUpdate.setAlpha(1.0f);
        } else {
            holder.itemView.setAlpha(1.0f);
            holder.btnUpdate.setAlpha(1.0f);
        }

    }




    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCategoryName;
        TextView txtQuantity;
        Button btnUpdate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategoryName = itemView.findViewById(R.id.txtCategoryName);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
        }
    }


}