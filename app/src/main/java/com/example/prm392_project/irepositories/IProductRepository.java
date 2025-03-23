package com.example.prm392_project.irepositories;

import androidx.room.Query;

import com.example.prm392_project.bean.Product;
import com.example.prm392_project.bean.pojo.TopProduct;

import java.util.List;

public interface IProductRepository {
    void insertProduct(Product product);
    void updateProduct(Product product);
    Product getProductById(int productId);
    void updateProductsByCategory(int categoryID, boolean status);
    List<Product> getAllProducts(int limit, int offset);
    List<Product> getAllProductsByCategory(int categoryID, int limit, int offset);
    List<Product> searchAllProducts(String searchQuery, int limit, int offset);
    List<Product> searchAllProductsByCategory(String searchQuery, int categoryID, int limit, int offset);
    List<Product> getAllSaleProducts(int limit, int offset);
    List<Product> getAllSaleProductsByCategory(int categoryID, int limit, int offset);
    List<Product> searchAllSaleProducts(String searchQuery, int limit, int offset);
    List<Product> searchAllSaleProductsByCategory(String searchQuery, int categoryID, int limit, int offset);
    List<Product> getProducts(int limit, int offset);
    List<Product> getProductsByCategory(int categoryID, int limit, int offset);

    List<Product> searchProducts(String query, int limit, int offset);
    List<Product> searchProductsByCategory(String query, int categoryID, int limit, int offset);
    List<Product> getSaleProducts(int limit, int offset);
    List<Product> getSaleProductsByCategory(int categoryID, int limit, int offset);
    List<Product> searchSaleProducts(String query, int limit, int offset);
    List<Product> searchSaleProductsByCategory(String query, int categoryID, int limit, int offset);
    List<TopProduct> getTopSellingProductsLastMonth();
}
