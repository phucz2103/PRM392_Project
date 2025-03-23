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
    List<Product> getFilteredProducts(
            int categoryID,
            String searchQuery,
            int onlyAvailable,
            int onlySale,
            int limit,
            int offset);
    List<TopProduct> getTopSellingProductsLastMonth();
}
