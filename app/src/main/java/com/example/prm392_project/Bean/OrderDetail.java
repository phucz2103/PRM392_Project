package com.example.prm392_project.Bean;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "OrderDetail",
        foreignKeys = {
                @ForeignKey(entity = User.class, parentColumns = "UserID", childColumns = "UserID"),
                @ForeignKey(entity = Product.class, parentColumns = "ProductID", childColumns = "ProductID"),
                @ForeignKey(entity = Order.class, parentColumns = "OrderID", childColumns = "OrderID")
        })
public class OrderDetail {
    @PrimaryKey(autoGenerate = true)
    private int OrderDetailID;
    private int QTY_int;
    private double price;
    private int UserID; // FK
    private int ProductID; // FK
    private int OrderID; // FK

    // Constructor
    public OrderDetail(int QTY_int, double price, int UserID, int ProductID, int OrderID) {
        this.QTY_int = QTY_int;
        this.price = price;
        this.UserID = UserID;
        this.ProductID = ProductID;
        this.OrderID = OrderID;
    }

    // Getters and Setters
    public int getOrderDetailID() { return OrderDetailID; }
    public void setOrderDetailID(int OrderDetailID) { this.OrderDetailID = OrderDetailID; }
    public int getQTY_int() { return QTY_int; }
    public void setQTY_int(int QTY_int) { this.QTY_int = QTY_int; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getUserID() { return UserID; }
    public void setUserID(int UserID) { this.UserID = UserID; }
    public int getProductID() { return ProductID; }
    public void setProductID(int ProductID) { this.ProductID = ProductID; }
    public int getOrderID() { return OrderID; }
    public void setOrderID(int OrderID) { this.OrderID = OrderID; }
}
