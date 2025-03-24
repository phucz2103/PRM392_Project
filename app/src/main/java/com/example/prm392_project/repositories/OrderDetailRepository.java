package com.example.prm392_project.repositories;

import android.content.Context;

import com.example.prm392_project.bean.OrderDetail;
import com.example.prm392_project.dao.OrderDetailDao;
import com.example.prm392_project.databse.AppDatabase;
import com.example.prm392_project.irepositories.IOrderDetailRepository;

import java.util.Collections;
import java.util.List;

public class OrderDetailRepository implements IOrderDetailRepository {
    private OrderDetailDao orderDetailDao;
    public  OrderDetailRepository(Context context){
        AppDatabase db = AppDatabase.getInstance(context);
        orderDetailDao = db.orderDetailDao();
    }
    public void insertOrderDetail(OrderDetail orderDetail){
        orderDetailDao.insert(orderDetail);
    }

    @Override
    public List<OrderDetail> getOrderDetailsByOrder(int orderID) {
        return orderDetailDao.getOrderDetailsByOrder(orderID);
    }
}
