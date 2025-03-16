package com.example.prm392_project.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.prm392_project.Bean.Category;
import com.example.prm392_project.DAO.CategoryDao;
import com.example.prm392_project.Database.AppDatabase;
import com.example.prm392_project.R;
import com.example.prm392_project.Repositories.CategoryRepository;

public class AddCategoryActivity extends AppCompatActivity {

    private CategoryRepository categoryRepository;
    private EditText edtCategoryName;
    private Button btnCreate;
    private ImageView btnBack;
    private CategoryDao categoryDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);


        edtCategoryName = findViewById(R.id.edtCategoryName);
        btnCreate = findViewById(R.id.btnCreate);
        btnBack = findViewById(R.id.btnBack);


        categoryRepository = new CategoryRepository(this);


        btnBack.setOnClickListener(v -> finish());


        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName = edtCategoryName.getText().toString().trim();


                if (categoryName.isEmpty()) {
                    Toast.makeText(AddCategoryActivity.this, "Please enter a category name", Toast.LENGTH_SHORT).show();
                    return;
                }


                try {
                    categoryRepository.AddCategory(categoryName, true);
                    Toast.makeText(AddCategoryActivity.this, "Category created successfully!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } catch (Exception e) {
                    Toast.makeText(AddCategoryActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
