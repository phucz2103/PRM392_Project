package com.example.prm392_project.IRepositories;

import com.example.prm392_project.Bean.Product;
import com.example.prm392_project.dto.TopProduct;

import java.util.List;

public interface IProductRepository {
    void insertProduct(Product product);
    void updateProduct(Product product);
    Product getProductById(int productId);
    List<Product> getAllProducts();

    List<Product> getAllAvailableProducts();
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
