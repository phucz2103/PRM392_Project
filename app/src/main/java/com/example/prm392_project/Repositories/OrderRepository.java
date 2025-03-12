package com.example.prm392_project.Repositories;

import android.content.Context;

import com.example.prm392_project.Adapter.MonthRevenue;
import com.example.prm392_project.Bean.Order;
import com.example.prm392_project.DAO.OrderDao;
import com.example.prm392_project.Database.AppDatabase;

import java.util.List;

public class OrderRepository {
    private OrderDao orderDao;
    public  OrderRepository(Context context){
        AppDatabase db = AppDatabase.getInstance(context);
        orderDao = db.orderDao();
    }
    public void insertOrder(Order order){
        orderDao.insert(order);
    }

    public List<MonthRevenue> getMonthRevenue(String year){
        return orderDao.getMonthRevenue(year);
    }

}
