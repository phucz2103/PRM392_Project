package com.example.prm392_project.Bean.POJO;
import com.example.prm392_project.Bean.Order;
import com.example.prm392_project.Bean.User;

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
