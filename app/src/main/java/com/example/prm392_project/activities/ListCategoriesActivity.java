package com.example.prm392_project.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.prm392_project.Adapter.CategoryAdapter;
import com.example.prm392_project.Bean.Category;
import com.example.prm392_project.R;
import com.example.prm392_project.Repositories.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

public class ListCategoriesActivity extends AppCompatActivity {

    public static final int UPDATE_CATEGORY_REQUEST = 1;
    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private CategoryRepository categoryRepository;
    private EditText editSearch;
    private List<Category> categoryList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_categories);

        recyclerView = findViewById(R.id.listView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        editSearch = findViewById(R.id.search);

        categoryRepository = new CategoryRepository(this);

        if (categoryRepository.getAllCategories().isEmpty()) {
            categoryRepository.AddCategory("Foods", true);
            categoryRepository.AddCategory("Drinks", true);
            categoryRepository.AddCategory("Toys", true);
            categoryRepository.AddCategory("Healthcare", true);
            //Toast.makeText(this, "Sample categories added!", Toast.LENGTH_SHORT).show();
        }

        // Xử lý sự kiện cho nút add category
        Button btnAddCategory = findViewById(R.id.btn_add_category);
        btnAddCategory.setOnClickListener(v -> {
            Intent intent = new Intent(ListCategoriesActivity.this, AddCategoryActivity.class);
            startActivity(intent);
        });

        // Lấy danh sách ban đầu
        loadCategories();

        // Xử lý tìm kiếm với EditText
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCategories(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private ActivityResultLauncher<Intent> updateCategoryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        int categoryId = data.getIntExtra("category_id", 1);
                        String updatedName = data.getStringExtra("updated_name");
                        boolean updatedActive = data.getBooleanExtra("updated_active", false);

                        // Cập nhật trong danh sách và adapter
                        for (Category category : categoryList) {
                            if (category.getCategoryID() == categoryId) {
                                category.setCategoryName(updatedName);
                                category.setIsAvailable(updatedActive);
                                break;
                            }
                        }
                        adapter.notifyDataSetChanged();
                        //Toast.makeText(this, "Category updated!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private void filterCategories(String keyword) {
        List<Category> filteredList = new ArrayList<>();
        for (Category category : categoryList) {
            if (category.getCategoryName().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(category);
            }
        }


        adapter.setCategoryList(filteredList);
    }

    private void updateCategory(Category category) {
        Intent intent = new Intent(ListCategoriesActivity.this, UpdateCategoryActivity.class);
        intent.putExtra("category_id", category.getCategoryID());  // Truyền ID của Category
        intent.putExtra("category_name", category.getCategoryName());
        intent.putExtra("is_active", category.getIsAvailable());
        updateCategoryLauncher.launch(intent);  // Sử dụng launcher thay vì startActivityForResult
    }



    @Override
    protected void onResume() {
        super.onResume();
        loadCategories();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_CATEGORY_REQUEST && resultCode == RESULT_OK) {
            int categoryId = data.getIntExtra("category_id", -1);
            String updatedName = data.getStringExtra("updated_name");
            boolean updatedActive = data.getBooleanExtra("updated_active", false);

            // Cập nhật trong danh sách và adapter
            for (Category category : categoryList) {
                if (category.getCategoryID() == categoryId) {
                    category.setCategoryName(updatedName);
                    category.setIsAvailable(updatedActive);
                    break;
                }
            }
            adapter.notifyDataSetChanged();
        }
    }


    private void loadCategories() {
        categoryList = categoryRepository.getAllCategories();

        if (categoryList.isEmpty()) {
            Toast.makeText(this, "No categories available", Toast.LENGTH_SHORT).show();
        } else {
            adapter = new CategoryAdapter(this, categoryList, category -> {
                //Toast.makeText(this, "Clicked: " + category.getCategoryName(), Toast.LENGTH_SHORT).show();
                updateCategory(category);
            });
            recyclerView.setAdapter(adapter);
        }
    }
}
