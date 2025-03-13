package com.example.prm392_project.Activities;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import com.example.prm392_project.Bean.Category;
import com.example.prm392_project.DAO.CategoryDao;
import com.example.prm392_project.Database.AppDatabase;
import com.example.prm392_project.R;
import com.example.prm392_project.Repositories.CategoryRepository;


public class ListCategoriesActivity extends AppCompatActivity {

        private ListView listView;
        private ArrayAdapter<String> adapter;
        private CategoryRepository categoryRepository;

        @SuppressLint("MissingInflatedId")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.list_categories);

            listView = findViewById(R.id.listView);

            CategoryDao categoryDao = AppDatabase.getInstance(this).categoryDao();
            categoryRepository = new CategoryRepository(this);
            categoryDao.deleteAllCategories();
            //for (String s : Arrays.asList("Thực phẩm", "Đồ uống", "Đồ gia dụng", "Thời trang"))
               // categoryRepository.insert(new Category(s));

            List<Category> categoryList = categoryDao.getAllCategories();

            String[] categories = new String[categoryList.size()];
            for (int i = 0; i < categoryList.size(); i++) {
                categories[i] = categoryList.get(i).getName();
            }

            // Thiết lập ArrayAdapter cho ListView
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
            listView.setAdapter(adapter);
        }

}
