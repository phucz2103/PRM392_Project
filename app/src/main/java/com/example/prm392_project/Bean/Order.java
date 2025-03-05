package com.example.prm392_project.Bean;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Order",
        foreignKeys = {
                @ForeignKey(entity = User.class, parentColumns = "UserID", childColumns = "UserID")
        })
public class Order {
    @PrimaryKey(autoGenerate = true)
    private int OrderID;
    private String OrderDate;
    private double TotalPrice;
    private int status;
    private int UserID; // FK

    // Constructor
    public Order(String OrderDate, double TotalPrice, int status, int UserID) {
        this.OrderDate = OrderDate;
        this.TotalPrice = TotalPrice;
        this.status = status;
        this.UserID = UserID;
    }

    // Getters and Setters
    public int getOrderID() { return OrderID; }
    public void setOrderID(int OrderID) { this.OrderID = OrderID; }
    public String getOrderDate() { return OrderDate; }
    public void setOrderDate(String OrderDate) { this.OrderDate = OrderDate; }
    public double getTotalPrice() { return TotalPrice; }
    public void setTotalPrice(double TotalPrice) { this.TotalPrice = TotalPrice; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public int getUserID() { return UserID; }
    public void setUserID(int UserID) { this.UserID = UserID; }
}
