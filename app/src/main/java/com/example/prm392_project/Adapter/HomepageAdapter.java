package com.example.prm392_project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_project.Bean.Category;
import com.example.prm392_project.R;

import java.util.List;

public class HomepageAdapter extends RecyclerView.Adapter<HomepageAdapter.ViewHolder> {
        private Context context;
        private List<Category> categoryList;
        private OnCategoryClickListener listener;

        public interface OnCategoryClickListener {
            void onCategoryClick(Category category);
        }

        public HomepageAdapter(Context context, List<Category> categoryList, OnCategoryClickListener listener) {
            this.context = context;
            this.categoryList = categoryList;
            this.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_homepage, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Category category = categoryList.get(position);
            holder.txtCategoryName.setText(category.getCategoryName());

            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCategoryClick(category);
                }
            });
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

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txtCategoryName = itemView.findViewById(R.id.txtCategoryName);
            }
        }
}
