package com.example.prm392_project.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.prm392_project.Bean.Product;

import java.util.List;

@Dao
public interface ProductDao {
    @Insert
    void insert(Product product);

    @Query("SELECT * FROM Product WHERE CategoryID = :categoryID")
    List<Product> getProductsByCategory(int categoryID);

    @Query("SELECT * FROM Product")
    List<Product> getAllProducts();

    @Update
    void update(Product product);

    @Query("SELECT * FROM Product WHERE ProductID = :productId")
    Product getProductById(int productId);


}
