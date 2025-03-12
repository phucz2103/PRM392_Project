package com.example.prm392_project.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392_project.Adapter.MonthRevenue;
import com.example.prm392_project.Bean.Order;

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
}

