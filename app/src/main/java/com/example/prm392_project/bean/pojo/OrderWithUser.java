package com.example.prm392_project.bean.pojo;
import com.example.prm392_project.bean.Order;
import com.example.prm392_project.bean.User;

import androidx.room.Embedded;
import androidx.room.Relation;
public class OrderWithUser {
    @Embedded
    public Order order;

    @Relation(
            parentColumn = "UserID",
            entityColumn = "UserID"
    )
    public User user;
}
