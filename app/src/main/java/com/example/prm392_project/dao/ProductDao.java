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

    @Query("SELECT * FROM Product  LIMIT :limit OFFSET :offset")
    List<Product> getAllProducts(int limit, int offset);

    @Query("SELECT * FROM Product WHERE CategoryID = :categoryID LIMIT :limit OFFSET :offset")
    List<Product> getAllProductsByCategory(int categoryID, int limit, int offset);

    @Query("SELECT * FROM Product WHERE (ProductName LIKE '%' || :searchQuery || '%' ) LIMIT :limit OFFSET :offset")
    List<Product> searchAllProducts(String searchQuery, int limit, int offset);

    @Query("SELECT * FROM Product WHERE CategoryID = :categoryID AND (ProductName LIKE '%' || :searchQuery || '%') LIMIT :limit OFFSET :offset")
    List<Product> searchAllProductsByCategory(String searchQuery, int categoryID, int limit, int offset);

    @Query("SELECT * FROM Product WHERE IsSaled = 1 LIMIT :limit OFFSET :offset")
    List<Product> getAllSaleProducts(int limit, int offset);
    @Query("SELECT * FROM Product WHERE CategoryID = :categoryID AND IsSaled = 1  LIMIT :limit OFFSET :offset")
    List<Product> getAllSaleProductsByCategory(int categoryID, int limit, int offset);

    @Query("SELECT * FROM Product WHERE (ProductName LIKE '%' || :searchQuery || '%' ) AND IsSaled = 1  LIMIT :limit OFFSET :offset")
    List<Product> searchAllSaleProducts(String searchQuery, int limit, int offset);

    @Query("SELECT * FROM Product WHERE CategoryID = :categoryID AND (ProductName LIKE '%' || :searchQuery || '%' ) AND IsSaled = 1 LIMIT :limit OFFSET :offset")
    List<Product> searchAllSaleProductsByCategory(String searchQuery, int categoryID, int limit, int offset);
    @Update
    void update(Product product);

    @Query("SELECT * FROM Product WHERE ProductID = :productId")
    Product getProductById(int productId);

    @Query("SELECT * FROM Product WHERE IsAvailable = 1 LIMIT :limit OFFSET :offset")
    List<Product> getProducts(int limit, int offset);

    @Query("SELECT * FROM Product WHERE CategoryID = :categoryID AND IsAvailable = 1 LIMIT :limit OFFSET :offset")
    List<Product> getProductsByCategory(int categoryID, int limit, int offset);

    @Query("SELECT * FROM Product WHERE (ProductName LIKE '%' || :searchQuery || '%' ) AND IsAvailable = 1 LIMIT :limit OFFSET :offset")
    List<Product> searchProducts(String searchQuery, int limit, int offset);

    @Query("SELECT * FROM Product WHERE CategoryID = :categoryID AND (ProductName LIKE '%' || :searchQuery || '%') AND IsAvailable = 1 LIMIT :limit OFFSET :offset")
    List<Product> searchProductsByCategory(String searchQuery, int categoryID, int limit, int offset);

    @Query("SELECT * FROM Product WHERE IsSaled = 1 AND IsAvailable = 1 LIMIT :limit OFFSET :offset")
    List<Product> getSaleProducts(int limit, int offset);

    @Query("SELECT * FROM Product WHERE CategoryID = :categoryID AND IsSaled = 1 AND IsAvailable = 1 LIMIT :limit OFFSET :offset")
    List<Product> getSaleProductsByCategory(int categoryID, int limit, int offset);

    @Query("SELECT * FROM Product WHERE (ProductName LIKE '%' || :searchQuery || '%' ) AND IsSaled = 1 AND IsAvailable = 1 LIMIT :limit OFFSET :offset")
    List<Product> searchSaleProducts(String searchQuery, int limit, int offset);

    @Query("SELECT * FROM Product WHERE CategoryID = :categoryID AND (ProductName LIKE '%' || :searchQuery || '%' ) AND IsSaled = 1 AND IsAvailable = 1 LIMIT :limit OFFSET :offset")
    List<Product> searchSaleProductsByCategory(String searchQuery, int categoryID, int limit, int offset);

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
