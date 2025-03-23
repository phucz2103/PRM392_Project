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
    public List<Product> getAllProducts(int limit, int offset) {
        return productDao.getAllProducts(limit,offset);
    }

    @Override
    public List<Product> getAllProductsByCategory(int categoryID, int limit, int offset) {
        return productDao.getAllProductsByCategory(categoryID,limit,offset);
    }

    @Override
    public List<Product> searchAllProducts(String searchQuery, int limit, int offset) {
        return productDao.searchAllProducts(searchQuery,limit,offset);
    }

    @Override
    public List<Product> searchAllProductsByCategory(String searchQuery, int categoryID, int limit, int offset) {
        return searchAllProductsByCategory(searchQuery,categoryID,limit,offset);
    }

    @Override
    public List<Product> getAllSaleProducts(int limit, int offset) {
        return productDao.getAllSaleProducts(limit,offset);
    }

    @Override
    public List<Product> getAllSaleProductsByCategory(int categoryID, int limit, int offset) {
        return productDao.getAllSaleProductsByCategory(categoryID,limit,offset);
    }

    @Override
    public List<Product> searchAllSaleProducts(String searchQuery, int limit, int offset) {
        return productDao.searchAllSaleProducts(searchQuery,limit,offset);
    }

    @Override
    public List<Product> searchAllSaleProductsByCategory(String searchQuery, int categoryID, int limit, int offset) {
        return productDao.searchAllSaleProductsByCategory(searchQuery,categoryID,limit,offset);
    }

    @Override
    public List<Product> getProducts(int limit, int offset) {
        Log.d("ProductRepository", "getProducts called with limit: " + limit + ", offset: " + offset);
        return productDao.getProducts(limit, offset);
    }

    @Override
    public List<Product> getProductsByCategory(int categoryID, int limit, int offset) {
        Log.d("ProductRepository", "getProductsByCategory called with categoryID: " + categoryID + ", limit: " + limit + ", offset: " + offset);
        return productDao.getProductsByCategory(categoryID, limit, offset);
    }

    @Override
    public List<Product> searchProducts(String query, int limit, int offset) {
        Log.d("ProductRepository", "searchProducts called with query: " + query + ", limit: " + limit + ", offset: " + offset);
        return productDao.searchProducts(query, limit, offset);
    }

    @Override
    public List<Product> searchProductsByCategory(String query, int categoryID, int limit, int offset) {
        Log.d("ProductRepository", "searchProductsByCategory called with query: " + query + ", categoryID: " + categoryID + ", limit: " + limit + ", offset: " + offset);
        return productDao.searchProductsByCategory(query, categoryID, limit, offset);
    }

    @Override
    public List<Product> getSaleProducts(int limit, int offset) {
        Log.d("ProductRepository", "getSaleProducts called with limit: " + limit + ", offset: " + offset);
        return productDao.getSaleProducts(limit, offset);
    }

    @Override
    public List<Product> getSaleProductsByCategory(int categoryID, int limit, int offset) {
        Log.d("ProductRepository", "getSaleProductsByCategory called with categoryID: " + categoryID + ", limit: " + limit + ", offset: " + offset);
        return productDao.getSaleProductsByCategory(categoryID, limit, offset);
    }

    @Override
    public List<Product> searchSaleProducts(String query, int limit, int offset) {
        Log.d("ProductRepository", "searchSaleProducts called with query: " + query + ", limit: " + limit + ", offset: " + offset);
        return productDao.searchSaleProducts(query, limit, offset);
    }

    @Override
    public List<Product> searchSaleProductsByCategory(String query, int categoryID, int limit, int offset) {
        Log.d("ProductRepository", "searchSaleProductsByCategory called with query: " + query + ", categoryID: " + categoryID + ", limit: " + limit + ", offset: " + offset);
        return productDao.searchSaleProductsByCategory(query, categoryID, limit, offset);
    }

    @Override
    public List<TopProduct> getTopSellingProductsLastMonth() {
        return productDao.getTopSellingProductsLastMonth();
    }
}
