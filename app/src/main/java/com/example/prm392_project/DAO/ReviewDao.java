package com.example.prm392_project.DAO;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392_project.Bean.Review;

import java.util.List;

@Dao
public interface ReviewDao {
    @Insert
    void insert(Review review);

    @Query("SELECT * FROM Review WHERE ProductID = :productID")
    List<Review> getReviewsByProduct(int productID);
}
