package com.example.prm392_project.Repositories;

import android.content.Context;

import com.example.prm392_project.Bean.OrderDetail;
import com.example.prm392_project.DAO.OrderDetailDao;
import com.example.prm392_project.Database.AppDatabase;

public class OrderDetailRepository {
    private OrderDetailDao orderDetailDao;
    public  OrderDetailRepository(Context context){
        AppDatabase db = AppDatabase.getInstance(context);
        orderDetailDao = db.orderDetailDao();
    }
    public void insertOrderDetail(OrderDetail orderDetail){
        orderDetailDao.insert(orderDetail);
    }
}
