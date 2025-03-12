package com.example.prm392_project.Repositories;

import android.content.Context;

import com.example.prm392_project.Bean.Product;
import com.example.prm392_project.DAO.ProductDao;
import com.example.prm392_project.Database.AppDatabase;

public class ProductRepository {
    private ProductDao productDao;
    public ProductRepository(Context context){
        AppDatabase db = AppDatabase.getInstance(context);
        productDao = db.productDao();
    }
    public void insertProduct(Product product){
        productDao.insert(product);
    }
}
