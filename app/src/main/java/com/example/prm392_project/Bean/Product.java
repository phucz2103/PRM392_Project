package com.example.prm392_project.Bean;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Product",
        foreignKeys = {
                @ForeignKey(entity = Category.class, parentColumns = "CategoryID", childColumns = "CategoryID")
        })
public class Product {
    @PrimaryKey(autoGenerate = true)
    private int ProductID;
    private String ProductName;
    private String Description;
    private double Price;
    private String IMAGE_URL;
    private String CreatedAt;
    private String UpdatedAt;
    private boolean IsAvailable;
    private int CategoryID; // FK

    // Constructor
    public Product(String ProductName, String Description, double Price, String IMAGE_URL, String CreatedAt, String UpdatedAt, boolean IsAvailable, int CategoryID) {
        this.ProductName = ProductName;
        this.Description = Description;
        this.Price = Price;
        this.IMAGE_URL = IMAGE_URL;
        this.CreatedAt = CreatedAt;
        this.UpdatedAt = UpdatedAt;
        this.IsAvailable = IsAvailable;
        this.CategoryID = CategoryID;
    }

    // Getters and Setters
    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int ProductID) {
        this.ProductID = ProductID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String ProductName) {
        this.ProductName = ProductName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double Price) {
        this.Price = Price;
    }

    public String getIMAGE_URL() {
        return IMAGE_URL;
    }

    public void setIMAGE_URL(String IMAGE_URL) {
        this.IMAGE_URL = IMAGE_URL;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String CreatedAt) {
        this.CreatedAt = CreatedAt;
    }

    public String getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(String UpdatedAt) {
        this.UpdatedAt = UpdatedAt;
    }

    public boolean getIsAvailable() {
        return IsAvailable;
    }

    public void setIsAvailable(boolean IsAvailable) {
        this.IsAvailable = IsAvailable;
    }

    public int getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(int CategoryID) {
        this.CategoryID = CategoryID;
    }
}
