package com.example.prm392_project.repositories;

import android.content.Context;

import com.example.prm392_project.bean.Category;
import com.example.prm392_project.dao.CategoryDao;
import com.example.prm392_project.databse.AppDatabase;
import com.example.prm392_project.irepositories.ICategoryRepository;

import java.util.List;

public class CategoryRepository implements ICategoryRepository {
    private CategoryDao categoryDao;

    public CategoryRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        categoryDao = db.categoryDao();
    }


    @Override
    public List<Category> getAllCategories() {
        return categoryDao.getAllCategories();
    }

    @Override
    public void AddCategory(String name, boolean isAvailable) {
        categoryDao.insert(new Category(name,isAvailable));
    }

    @Override
    public void UpdateCategory(int id, String name, boolean isAvailable) {
        categoryDao.updateCategory(id,name,isAvailable);
    }
}
