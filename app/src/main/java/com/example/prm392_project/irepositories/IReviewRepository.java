package com.example.prm392_project.irepositories;

import com.example.prm392_project.bean.Product;
import com.example.prm392_project.bean.Review;

import java.util.List;

public interface IReviewRepository {
    void insertReview(Review review);
    List<Review> getReviewsByProduct(int productID);
}
