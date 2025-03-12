package com.example.prm392_project.IRepositories;

import com.example.prm392_project.Bean.Order;

import java.util.List;

public interface IOrderRepository {
    List<Order> getAll();
    List<Order> getOrderPendingByUser(int userID);
    List<Order> getOrderPurchasedByUser(int userID);
    void updateOrderStatus(int orderID, int status);
    List<Order> getOrderByUser(int userID);
}
