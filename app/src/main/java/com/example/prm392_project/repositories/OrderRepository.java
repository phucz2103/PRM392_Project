package com.example.prm392_project.repositories;

import android.content.Context;

import com.example.prm392_project.bean.Order;
import com.example.prm392_project.bean.pojo.OrderDetailWithProduct;
import com.example.prm392_project.bean.pojo.OrderWithUser;
import com.example.prm392_project.dao.OrderDao;
import com.example.prm392_project.databse.AppDatabase;
import com.example.prm392_project.irepositories.IOrderRepository;
import com.example.prm392_project.bean.pojo.MonthRevenue;

import java.util.List;
import java.util.UUID;

public class OrderRepository implements IOrderRepository {
    private OrderDao orderDao;

    public OrderRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        orderDao = db.orderDao();
    }

    @Override
    public void insertOrder(Order order) {
        orderDao.insert(order);
    }

    @Override
    public List<Order> getAll() {
        return orderDao.getAll();
    }

    @Override
    public Order getOrdersByOrderCode(UUID uuid) {
        return orderDao.getOrdersByOrderCode(uuid);
    }

    @Override
    public List<Order> getOrderPendingByUser(int userID) {
        return orderDao.getOrderPendingByUser(userID);
    }

    @Override
    public List<Order> getOrderPurchasedByUser(int userID) {
        return orderDao.getOrderPurchasedByUser(userID);
    }

    @Override
    public void updateOrderStatus(int orderID, int status) {
        orderDao.updateOrderStatus(orderID, status);
    }
    @Override
    public List<Order> getOrderByUser(int userID) {
        return orderDao.getOrdersByUser(userID);
    }

    @Override
    public List<OrderWithUser> getOrderWithUserByStatus(int status) {
        return orderDao.getOrderWithUserByStatus(status);
    }

    public List<MonthRevenue> getMonthRevenue(String year){
        return orderDao.getMonthRevenue(year);
    }

    @Override
    public OrderWithUser getOderWithUserByID(int orderID) {
        return orderDao.getOrderWithUserByID(orderID);
    }

    @Override
    public List<OrderDetailWithProduct> getOrderDetailsWithProduct(int orderID) {
        return orderDao.getOrderDetailsWithProduct(orderID);
    }

    @Override
    public List<OrderWithUser> getAllOrderWithUser() {
        return orderDao.getAllOrdersWithUsers();
    }
}
