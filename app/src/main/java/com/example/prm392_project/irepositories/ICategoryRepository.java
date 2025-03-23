package com.example.prm392_project.irepositories;

import com.example.prm392_project.bean.Category;

import java.util.List;

public interface ICategoryRepository {
    List<Category> getAllCategories();
    void AddCategory(String name, boolean isAvailable);
    void UpdateCategory(int id, String name, boolean isAvailable);
}
