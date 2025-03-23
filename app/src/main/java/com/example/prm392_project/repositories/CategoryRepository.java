package com.example.prm392_project.repositories;

import android.content.Context;

import com.example.prm392_project.bean.Category;
import com.example.prm392_project.dao.CategoryDao;
import com.example.prm392_project.dao.ProductDao;
import com.example.prm392_project.databse.AppDatabase;
import com.example.prm392_project.irepositories.ICategoryRepository;

import java.util.Collections;
import java.util.List;

public class CategoryRepository implements ICategoryRepository {
    private CategoryDao categoryDao;
    private ProductDao productDao;

    public CategoryRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        productDao = db.productDao();
        categoryDao = db.categoryDao();
    }


    @Override
    public Category getCategoryById(int categoryId) {
        return categoryDao.getCategoryById(categoryId);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryDao.getAllCategories();
    }

    @Override
    public List<Category> getAllAvailableCategories() {
        return categoryDao.getAllAvailableCategories();
    }

    @Override
    public void AddCategory(String name, boolean isAvailable) {
        categoryDao.insert(new Category(name,isAvailable));
    }

    @Override
    public void UpdateCategory(int id, String name, boolean isAvailable) {
        productDao.updateProductsByCategory(id,isAvailable);
        categoryDao.updateCategory(id,name,isAvailable);
    }
}
