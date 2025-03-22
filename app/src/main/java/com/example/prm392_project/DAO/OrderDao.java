package com.example.prm392_project.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.prm392_project.Adapter.MonthRevenue;
import com.example.prm392_project.Bean.Order;
import com.example.prm392_project.Bean.POJO.OrderDetailWithProduct;
import com.example.prm392_project.Bean.POJO.OrderWithUser;

import java.util.List;

@Dao
public interface OrderDao {
    @Insert
    void insert(Order order);
    @Query("SELECT * FROM `Order` WHERE UserID = :userID")
    List<Order> getOrdersByUser(int userID);
    @Query("SELECT strftime('%m', orderdate) AS month, SUM(totalprice) AS revenue " +
            "FROM `Order` WHERE strftime('%Y', orderdate) = :year " +
            "GROUP BY month ORDER BY month")
    List<MonthRevenue> getMonthRevenue(String year);

    @Query("SELECT * FROM `Order`")
    List<Order> getAll();

    @Query("SELECT * FROM `Order` WHERE status = 0 and UserID = :userID")
    List<Order>  getOrderPendingByUser(int userID);

    @Query("SELECT * FROM `Order` WHERE status > 0 and UserID = :userID")
    List<Order>  getOrderPurchasedByUser(int userID);

    @Query("UPDATE `Order` SET status = :status WHERE OrderID = :orderID")
    void updateOrderStatus(int orderID, int status);

    @Transaction
    @Query("SELECT * FROM `Order` ORDER BY `OrderDate` DESC")
    List<OrderWithUser> getAllOrdersWithUsers();

    @Transaction
    @Query("SELECT * FROM `Order` WHERE OrderID = :orderID")
    OrderWithUser getOrderWithUserByID(int orderID);

    @Transaction
    @Query("SELECT * FROM `OrderDetail` WHERE OrderID = :orderID")
    List<OrderDetailWithProduct> getOrderDetailsWithProduct(int orderID);
}
