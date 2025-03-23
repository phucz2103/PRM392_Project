package com.example.prm392_project.bean;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Sale",
        foreignKeys = {
                @ForeignKey(entity = Product.class, parentColumns = "ProductID", childColumns = "ProductID")
        })
public class Sale {
    @PrimaryKey(autoGenerate = true)
    private int SaleID;
    private String CreatedAt;
    private String FinishAt;
    private boolean IsAvailable;
    private int ProductID; // FK

    // Constructor
    public Sale(String CreatedAt, String FinishAt, boolean IsAvailable, int ProductID) {
        this.CreatedAt = CreatedAt;
        this.FinishAt = FinishAt;
        this.IsAvailable = IsAvailable;
        this.ProductID = ProductID;
    }

    // Getters and Setters
    public int getSaleID() { return SaleID; }
    public void setSaleID(int SaleID) { this.SaleID = SaleID; }
    public String getCreatedAt() { return CreatedAt; }
    public void setCreatedAt(String CreatedAt) { this.CreatedAt = CreatedAt; }
    public String getFinishAt() { return FinishAt; }
    public void setFinishAt(String FinishAt) { this.FinishAt = FinishAt; }
    public boolean getIsAvailable() { return IsAvailable; }
    public void setIsAvailable(boolean IsAvailable) { this.IsAvailable = IsAvailable; }
    public int getProductID() { return ProductID; }
    public void setProductID(int ProductID) { this.ProductID = ProductID; }
}
