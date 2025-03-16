package com.example.prm392_project.Repositories;

import android.content.Context;

import com.example.prm392_project.Adapter.MonthRevenue;
import com.example.prm392_project.Bean.Order;
import com.example.prm392_project.Bean.POJO.OrderDetailWithProduct;
import com.example.prm392_project.Bean.POJO.OrderWithUser;
import com.example.prm392_project.DAO.OrderDao;
import com.example.prm392_project.Database.AppDatabase;
import com.example.prm392_project.IRepositories.IOrderRepository;

import java.util.Collections;
import java.util.List;

public class OrderRepository implements IOrderRepository {
    private OrderDao orderDao;

    public OrderRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        orderDao = db.orderDao();
    }

    @Override
    public List<Order> getAll() {
        return orderDao.getAll();
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
