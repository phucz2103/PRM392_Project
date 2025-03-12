package com.example.prm392_project.Repositories;

import android.content.Context;

import com.example.prm392_project.Bean.Category;
import com.example.prm392_project.DAO.CategoryDao;
import com.example.prm392_project.DAO.UserDao;
import com.example.prm392_project.Database.AppDatabase;
import com.example.prm392_project.IRepositories.ICategoryRepository;

public class CategoryRepository implements ICategoryRepository {
    private CategoryDao categoryDao;
    public CategoryRepository(Context context){
        AppDatabase db = AppDatabase.getInstance(context);
        categoryDao = db.categoryDao();
    }
    public void insertCategory(Category category){
        categoryDao.insert(category);
    }
}
