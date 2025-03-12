package com.example.prm392_project.Repositories;

import android.content.Context;

import com.example.prm392_project.Bean.Product;
import com.example.prm392_project.DAO.ProductDao;
import com.example.prm392_project.Database.AppDatabase;
import com.example.prm392_project.IRepositories.IProductRepository;

import java.util.List;

public class ProductRepository implements IProductRepository {
    private ProductDao productDao;

    public ProductRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        productDao = db.productDao();
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
    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }
}
