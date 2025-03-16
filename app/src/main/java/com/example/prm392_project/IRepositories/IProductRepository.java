package com.example.prm392_project.IRepositories;

import com.example.prm392_project.Bean.Product;

import java.util.List;

public interface IProductRepository {
    void insertProduct(Product product);
    void updateProduct(Product product);
    Product getProductById(int productId);
    List<Product> getAllProducts();
}
