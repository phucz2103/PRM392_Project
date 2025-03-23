package com.example.prm392_project.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_project.R;
import com.example.prm392_project.repositories.CategoryRepository;

public class UpdateCategoryActivity extends AppCompatActivity {
    private EditText edtCategoryName;
    private CheckBox checkActive;
    private Button btnUpdate;
    private ImageView btnBack;
    private int categoryId;
    private CategoryRepository categoryRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_category);

        // Ánh xạ các thành phần giao diện
        edtCategoryName = findViewById(R.id.edtCategoryName);
        checkActive = findViewById(R.id.checkActive);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnBack = findViewById(R.id.btnBack1);

        // Khởi tạo CategoryRepository
        categoryRepository = new CategoryRepository(this);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        categoryId = intent.getIntExtra("category_id", -1);
        String categoryName = intent.getStringExtra("category_name");
        boolean isActive = intent.getBooleanExtra("is_active", false);

        // Gán dữ liệu vào giao diện
        if (categoryName != null) {
            edtCategoryName.setText(categoryName);
        }
        checkActive.setChecked(isActive);

        // Xử lý nút Update
        btnUpdate.setOnClickListener(v -> {
            String updatedName = edtCategoryName.getText().toString().trim();
            boolean updatedActive = checkActive.isChecked();

            if (updatedName.isEmpty()) {
                Toast.makeText(this, "Please enter a category name", Toast.LENGTH_SHORT).show();
                return;
            }

            // Cập nhật vào cơ sở dữ liệu
            categoryRepository.UpdateCategory(categoryId, updatedName, updatedActive);

            // Trả dữ liệu về ListCategoriesActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("category_id", categoryId);
            resultIntent.putExtra("updated_name", updatedName);
            resultIntent.putExtra("updated_active", updatedActive);
            setResult(RESULT_OK, resultIntent);
            Toast.makeText(this, "Category updated successfully!", Toast.LENGTH_SHORT).show();
            finish();
        });

        // Xử lý nút Back
        btnBack.setOnClickListener(v -> finish());
    }
}
