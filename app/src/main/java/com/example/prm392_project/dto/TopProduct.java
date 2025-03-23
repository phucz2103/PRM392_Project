package com.example.prm392_project.dto;

public class TopProduct {
    public String ProductName;
    public double Price;
    public int totalQuantitySold;

    public TopProduct() {
    }

    public TopProduct(String productName, double price, int totalQuantitySold) {
        ProductName = productName;
        Price = price;
        this.totalQuantitySold = totalQuantitySold;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public int getTotalQuantitySold() {
        return totalQuantitySold;
    }

    public void setTotalQuantitySold(int totalQuantitySold) {
        this.totalQuantitySold = totalQuantitySold;
    }
}