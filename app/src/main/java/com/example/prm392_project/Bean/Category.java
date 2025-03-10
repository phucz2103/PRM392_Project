package com.example.prm392_project.Bean;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Category")
public class Category {
    @PrimaryKey(autoGenerate = true)
    private int CategoryID;
    private String CategoryName;
    private boolean IsAvailable;

    // Constructor
    public Category(String CategoryName, boolean IsAvailable) {
        this.CategoryName = CategoryName;
        this.IsAvailable = IsAvailable;
    }

    // Getters and Setters
    public int getCategoryID() { return CategoryID; }
    public void setCategoryID(int CategoryID) { this.CategoryID = CategoryID; }
    public String getCategoryName() { return CategoryName; }
    public void setCategoryName(String CategoryName) { this.CategoryName = CategoryName; }
    public boolean getIsAvailable() { return IsAvailable; }
    public void setIsAvailable(boolean IsAvailable) { this.IsAvailable = IsAvailable; }
}