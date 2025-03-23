package com.example.prm392_project.bean.pojo;
import androidx.room.Relation;
import androidx.room.Embedded;
import com.example.prm392_project.bean.OrderDetail;
import com.example.prm392_project.bean.Product;
public class OrderDetailWithProduct {
    @Embedded
    public OrderDetail orderDetail;

    @Relation(
            parentColumn = "ProductID",
            entityColumn = "ProductID"
    )
    public Product product;
}
