package com.example.prm392_project.repositories;

import android.content.Context;

import com.example.prm392_project.activities.ProductDetailsActivity;
import com.example.prm392_project.bean.Review;
import com.example.prm392_project.dao.CategoryDao;
import com.example.prm392_project.dao.ProductDao;
import com.example.prm392_project.dao.ReviewDao;
import com.example.prm392_project.databse.AppDatabase;
import com.example.prm392_project.irepositories.IReviewRepository;

import java.util.Collections;
import java.util.List;

public class ReviewRepository implements IReviewRepository {
    private ReviewDao reviewDao;


    public ReviewRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        reviewDao = db.reviewDao();
    }

    @Override
    public void insertReview(Review review) {
        reviewDao.insert(review);
    }

    @Override
    public List<Review> getReviewsByProduct(int productID) {
        return reviewDao.getReviewsByProduct(productID);
    }
}
