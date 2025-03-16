package com.example.prm392_project.Bean;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Cart",
        foreignKeys = {
                @ForeignKey(entity = Product.class, parentColumns = "ProductID", childColumns = "ProductID"),
                @ForeignKey(entity = User.class, parentColumns = "UserID", childColumns = "UserID")
        })
public class Cart {
    @PrimaryKey(autoGenerate = true)
    private int CartID;
    private double price;
    private int QTY_int;
    private int ProductID; // FK
    private int UserID; // FK

    // Constructor
    public Cart(double price, int QTY_int, int ProductID, int UserID) {
        this.price = price;
        this.QTY_int = QTY_int;
        this.ProductID = ProductID;
        this.UserID = UserID;
    }


    // Getters and Setters
    public int getCartID() { return CartID; }
    public void setCartID(int CartID) { this.CartID = CartID; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getQTY_int() { return QTY_int; }
    public void setQTY_int(int QTY_int) { this.QTY_int = QTY_int; }
    public int getProductID() { return ProductID; }
    public void setProductID(int ProductID) { this.ProductID = ProductID; }
    public int getUserID() { return UserID; }
    public void setUserID(int UserID) { this.UserID = UserID; }
}
