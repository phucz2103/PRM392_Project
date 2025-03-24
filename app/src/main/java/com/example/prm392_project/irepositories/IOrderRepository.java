package com.example.prm392_project.irepositories;

import com.example.prm392_project.bean.Order;
import com.example.prm392_project.bean.pojo.OrderDetailWithProduct;
import com.example.prm392_project.bean.pojo.OrderWithUser;

import java.util.List;
import java.util.UUID;

public interface IOrderRepository {
    void insertOrder(Order order);
    List<Order> getAll();
    Order getOrdersByOrderCode(UUID uuid);
    List<Order> getOrderPendingByUser(int userID);
    List<Order> getOrderPurchasedByUser(int userID);
    void updateOrderStatus(int orderID, int status);
    List<Order> getOrderByUser(int userID);
    List<OrderWithUser> getOrderWithUserByStatus(int status);
    OrderWithUser getOderWithUserByID(int orderID);
    List<OrderDetailWithProduct> getOrderDetailsWithProduct(int orderID);
    List<OrderWithUser> getAllOrderWithUser();
}
