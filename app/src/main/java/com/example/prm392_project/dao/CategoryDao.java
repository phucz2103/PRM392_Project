package com.example.prm392_project.dao;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.prm392_project.bean.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Category category);

    @Query("SELECT * FROM CATEGORY WHERE CategoryID = :categoryId")
    Category getCategoryById(int categoryId);

    @Query("DELETE FROM category")
    void deleteAllCategories();

    @Query("SELECT * FROM Category")
    List<Category> getAllCategories();

    @Query("SELECT * FROM Category Where IsAvailable = 1")
    List<Category> getAllAvailableCategories();

    @Query("DELETE FROM Cart WHERE UserID = :userID")
    void deleteCartByUserId(int userID);

    @Query("UPDATE category SET CategoryName = :name, IsAvailable = :active WHERE CategoryID = :id")
    void updateCategory(int id, String name, boolean active);
}

