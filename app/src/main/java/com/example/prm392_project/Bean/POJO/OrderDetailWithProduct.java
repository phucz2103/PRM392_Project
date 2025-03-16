package com.example.prm392_project.Bean.POJO;
import androidx.room.Relation;
import androidx.room.Embedded;
import com.example.prm392_project.Bean.OrderDetail;
import com.example.prm392_project.Bean.Product;
public class OrderDetailWithProduct {
    @Embedded
    public OrderDetail orderDetail;

    @Relation(
            parentColumn = "ProductID",
            entityColumn = "ProductID"
    )
    public Product product;
}
