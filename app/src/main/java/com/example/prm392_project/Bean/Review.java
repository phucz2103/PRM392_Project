package com.example.prm392_project.Bean;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Review",
        foreignKeys = {
                @ForeignKey(entity = User.class, parentColumns = "UserID", childColumns = "CreatedBy"),
                @ForeignKey(entity = Product.class, parentColumns = "ProductID", childColumns = "ProductID")
        })
public class Review {
    @PrimaryKey(autoGenerate = true)
    private int ReviewID;
    private String Content;
    private String CreatedAt;
    private int Rate;
    private int CreatedBy; // FK
    private int ProductID; // FK

    // Constructor
    public Review(String Content, String CreatedAt, int Rate, int CreatedBy, int ProductID) {
        this.Content = Content;
        this.CreatedAt = CreatedAt;
        this.Rate = Rate;
        this.CreatedBy = CreatedBy;
        this.ProductID = ProductID;
    }

    // Getters and Setters
    public int getReviewID() { return ReviewID; }
    public void setReviewID(int ReviewID) { this.ReviewID = ReviewID; }
    public String getContent() { return Content; }
    public void setContent(String Content) { this.Content = Content; }
    public String getCreatedAt() { return CreatedAt; }
    public void setCreatedAt(String CreatedAt) { this.CreatedAt = CreatedAt; }
    public int getRate() { return Rate; }
    public void setRate(int Rate) { this.Rate = Rate; }
    public int getCreatedBy() { return CreatedBy; }
    public void setCreatedBy(int CreatedBy) { this.CreatedBy = CreatedBy; }
    public int getProductID() { return ProductID; }
    public void setProductID(int ProductID) { this.ProductID = ProductID; }
}
