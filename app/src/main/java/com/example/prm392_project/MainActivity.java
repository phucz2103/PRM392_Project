package com.example.prm392_project;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392_project.Bean.User;
import com.example.prm392_project.Bean.Category;
import com.example.prm392_project.DAO.CategoryDao;
import com.example.prm392_project.DAO.ProductDao;
import com.example.prm392_project.DAO.UserDao;
import com.example.prm392_project.Database.AppDatabase;
import com.example.prm392_project.Repositories.CategoryRepository;
import com.example.prm392_project.Repositories.UserRepository;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private UserRepository userRepository;
    private CategoryRepository categoryRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_categories);

        //userRepository = new UserRepository(this);
        //User user = new User("Nguyen Van A", "0123456789", "nguyenvana@gmail.com", "123 Ho Chi Minh", "Male", "password123", true, false);
        //userRepository.insertUser(user);
        CategoryDao categoryDao = AppDatabase.getInstance(this).categoryDao();
        categoryDao.deleteAllCategories();

        categoryRepository = new CategoryRepository(this);
        for (String s : Arrays.asList("Thực phẩm", "Đồ uống", "Đồ gia dụng", "Thời trang"))
            categoryRepository.insertCategory(new Category(s));

        List<Category> categoryList = categoryDao.getAllCategories();

        // Chuyển đổi danh sách Category thành danh sách String
        String[] categories = new String[categoryList.size()];
        for (int i = 0; i < categoryList.size(); i++) {
            categories[i] = categoryList.get(i).getName();
        }
    }

}