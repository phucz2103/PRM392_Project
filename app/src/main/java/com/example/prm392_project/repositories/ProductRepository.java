package com.example.prm392_project.repositories;

import android.content.Context;
import android.util.Log;

import com.example.prm392_project.bean.Product;
import com.example.prm392_project.dao.CategoryDao;
import com.example.prm392_project.dao.ProductDao;
import com.example.prm392_project.databse.AppDatabase;
import com.example.prm392_project.irepositories.IProductRepository;
import com.example.prm392_project.bean.pojo.TopProduct;

import java.util.Collections;
import java.util.List;

public class ProductRepository implements IProductRepository {
    private ProductDao productDao;
    private CategoryDao categoryDao;

    public ProductRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        productDao = db.productDao();
        categoryDao = db.categoryDao();

    }

    @Override
    public void insertProduct(Product product) {
        productDao.insert(product);
    }

    @Override
    public void updateProduct(Product product) {
        productDao.update(product);
    }

    @Override
    public Product getProductById(int productId) {
        return productDao.getProductById(productId);
    }

    @Override
    public void updateProductsByCategory(int categoryID, boolean status) {
        productDao.updateProductsByCategory(categoryID,status);
    }

    @Override
    public List<Product> getFilteredProducts(int categoryID, String searchQuery, int onlyAvailable, int onlySale, int limit, int offset) {
        return productDao.getFilteredProducts(categoryID, searchQuery, onlyAvailable, onlySale, limit, offset);
    }


    @Override
    public List<TopProduct> getTopSellingProductsLastMonth() {
        return productDao.getTopSellingProductsLastMonth();
    }
}
