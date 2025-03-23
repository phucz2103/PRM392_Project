package com.example.prm392_project.bean;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "Order",
        foreignKeys = {
                @ForeignKey(entity = User.class, parentColumns = "UserID", childColumns = "UserID")
        })
public class Order {
    @PrimaryKey(autoGenerate = true)
    private int OrderID;
    private UUID OrderCode;
    private String OrderDate;
    private double TotalPrice;
    private int status; // 0 la pending, 1 la confirmed, 2 la rejected, 3 la nguoi dung huy
    private int UserID; // FK

    // Constructor
    public Order(String OrderDate, double TotalPrice, int status, int UserID) {
        this.OrderCode = UUID.randomUUID();
        this.OrderDate = OrderDate;
        this.TotalPrice = TotalPrice;
        this.status = status;
        this.UserID = UserID;
    }

    // Getters and Setters
    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int OrderID) {
        this.OrderID = OrderID;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String OrderDate) {
        this.OrderDate = OrderDate;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(double TotalPrice) {
        this.TotalPrice = TotalPrice;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int UserID) {
        this.UserID = UserID;
    }

    public UUID getOrderCode() {
        return OrderCode;
    }

    public void setOrderCode(UUID orderCode) {
        OrderCode = orderCode;
    }
}
