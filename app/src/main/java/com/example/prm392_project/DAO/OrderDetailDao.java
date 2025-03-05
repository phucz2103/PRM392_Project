package com.example.prm392_project.DAO;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392_project.Bean.OrderDetail;

import java.util.List;

@Dao
public interface OrderDetailDao {
    @Insert
    void insert(OrderDetail orderDetail);

    @Query("SELECT * FROM OrderDetail WHERE OrderID = :orderID")
    List<OrderDetail> getOrderDetailsByOrder(int orderID);
}
