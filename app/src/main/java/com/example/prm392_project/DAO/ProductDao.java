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

    @Query("SELECT * FROM Product WHERE IsAvailable = 1")
    List<Product> getAllAvailableProducts();

    @Update
    void update(Product product);

    @Query("SELECT * FROM Product WHERE ProductID = :productId")
    Product getProductById(int productId);

    @Query("SELECT * FROM Product WHERE IsAvailable = 1 LIMIT :limit OFFSET :offset")
    List<Product> getProducts(int limit, int offset);

    @Query("SELECT * FROM Product WHERE CategoryID = :categoryID AND IsAvailable = 1 LIMIT :limit OFFSET :offset")
    List<Product> getProductsByCategory(int categoryID, int limit, int offset);

    @Query("SELECT * FROM Product WHERE (ProductName LIKE '%' || :searchQuery || '%' OR Description LIKE '%' || :searchQuery || '%') AND IsAvailable = 1 LIMIT :limit OFFSET :offset")
    List<Product> searchProducts(String searchQuery, int limit, int offset);

    @Query("SELECT * FROM Product WHERE CategoryID = :categoryID AND (ProductName LIKE '%' || :searchQuery || '%' OR Description LIKE '%' || :searchQuery || '%') AND IsAvailable = 1 LIMIT :limit OFFSET :offset")
    List<Product> searchProductsByCategory(String searchQuery, int categoryID, int limit, int offset);

    @Query("SELECT * FROM Product WHERE IsSale = 1 AND IsAvailable = 1 LIMIT :limit OFFSET :offset")
    List<Product> getSaleProducts(int limit, int offset);

    @Query("SELECT * FROM Product WHERE CategoryID = :categoryID AND IsSale = 1 AND IsAvailable = 1 LIMIT :limit OFFSET :offset")
    List<Product> getSaleProductsByCategory(int categoryID, int limit, int offset);

    @Query("SELECT * FROM Product WHERE (ProductName LIKE '%' || :searchQuery || '%' OR Description LIKE '%' || :searchQuery || '%') AND IsSale = 1 AND IsAvailable = 1 LIMIT :limit OFFSET :offset")
    List<Product> searchSaleProducts(String searchQuery, int limit, int offset);

    @Query("SELECT * FROM Product WHERE CategoryID = :categoryID AND (ProductName LIKE '%' || :searchQuery || '%' OR Description LIKE '%' || :searchQuery || '%') AND IsSale = 1 AND IsAvailable = 1 LIMIT :limit OFFSET :offset")
    List<Product> searchSaleProductsByCategory(String searchQuery, int categoryID, int limit, int offset);
}
