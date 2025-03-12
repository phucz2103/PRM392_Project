package com.example.prm392_project.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.prm392_project.Bean.Order;

import java.util.List;

@Dao
public interface OrderDao {
    @Insert
    void insert(Order order);

    @Query("SELECT * FROM `Order` WHERE UserID = :userID")
    List<Order> getOrdersByUser(int userID);

    @Query("SELECT * FROM `Order`")
    List<Order> getAll();

    @Query("SELECT * FROM `Order` WHERE status = 0 and UserID = :userID")
    List<Order>  getOrderPendingByUser(int userID);

    @Query("SELECT * FROM `Order` WHERE status = 1 and UserID = :userID")
    List<Order>  getOrderPurchasedByUser(int userID);

    @Query("UPDATE `Order` SET status = :status WHERE OrderID = :orderID")
    void updateOrderStatus(int orderID, int status);

}
