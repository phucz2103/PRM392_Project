package com.example.prm392_project.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392_project.Bean.Sale;

import java.util.List;

@Dao
public interface SaleDao {
    @Insert
    void insert(Sale sale);

    @Query("SELECT * FROM Sale WHERE ProductID = :productID")
    List<Sale> getSalesByProduct(int productID);
}