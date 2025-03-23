package com.example.prm392_project.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.prm392_project.bean.Product;
import com.example.prm392_project.bean.pojo.TopProduct;

import java.util.List;

@Dao
public interface ProductDao {
    @Insert
    void insert(Product product);

    @Query("Update Product SET IsAvailable = :status WHERE CategoryID = :categoryID")
    void updateProductsByCategory(int categoryID, boolean status);

    @Update
    void update(Product product);

    @Query("SELECT * FROM Product WHERE ProductID = :productId")
    Product getProductById(int productId);

    @Query("SELECT * FROM Product WHERE " +
            "(:categoryID = 0 OR CategoryID = :categoryID) AND " +
            "(:searchQuery = '' OR ProductName LIKE '%' || :searchQuery || '%') AND " +
            "(:onlyAvailable = 0 OR IsAvailable = 1) AND " +
            "(:onlySale = 0 OR IsSaled = 1) " +
            "LIMIT :limit OFFSET :offset")
    List<Product> getFilteredProducts(
            int categoryID,
            String searchQuery,
            int onlyAvailable,
            int onlySale,
            int limit,
            int offset);

    @Query("SELECT p.ProductName, p.Price, SUM(od.QTY_int) AS totalQuantitySold " +
            "FROM OrderDetail od " +
            "JOIN `Order` o ON od.OrderID = o.OrderID " +
            "JOIN Product p ON od.ProductID = p.ProductID " +
            "WHERE o.OrderDate >= DATE('now', '-1 month') " +
            "GROUP BY p.ProductID, p.ProductName, p.Price " +
            "ORDER BY totalQuantitySold DESC " +
            "LIMIT 10")
    List<TopProduct> getTopSellingProductsLastMonth();
}
