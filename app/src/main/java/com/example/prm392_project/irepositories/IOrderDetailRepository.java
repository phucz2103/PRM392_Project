package com.example.prm392_project.irepositories;

import com.example.prm392_project.bean.OrderDetail;

import java.util.List;

public interface IOrderDetailRepository {
    List<OrderDetail> getOrderDetailsByOrder(int orderID);
}
