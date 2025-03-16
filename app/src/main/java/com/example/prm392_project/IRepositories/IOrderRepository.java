package com.example.prm392_project.IRepositories;

import com.example.prm392_project.Bean.Order;
import com.example.prm392_project.Bean.POJO.OrderDetailWithProduct;
import com.example.prm392_project.Bean.POJO.OrderWithUser;

import java.util.List;

public interface IOrderRepository {
    void insertOrder(Order order);
    List<Order> getAll();
    List<Order> getOrderPendingByUser(int userID);
    List<Order> getOrderPurchasedByUser(int userID);
    void updateOrderStatus(int orderID, int status);
    List<Order> getOrderByUser(int userID);

    OrderWithUser getOderWithUserByID(int orderID);
    List<OrderDetailWithProduct> getOrderDetailsWithProduct(int orderID);
    List<OrderWithUser> getAllOrderWithUser();
}
