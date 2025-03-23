package com.example.prm392_project.databse;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.prm392_project.bean.Cart;
import com.example.prm392_project.bean.Category;
import com.example.prm392_project.bean.Order;
import com.example.prm392_project.bean.OrderDetail;
import com.example.prm392_project.bean.Product;
import com.example.prm392_project.bean.Review;
import com.example.prm392_project.bean.Sale;
import com.example.prm392_project.bean.User;
import com.example.prm392_project.dao.CartDao;
import com.example.prm392_project.dao.CategoryDao;
import com.example.prm392_project.dao.OrderDao;
import com.example.prm392_project.dao.OrderDetailDao;
import com.example.prm392_project.dao.ProductDao;
import com.example.prm392_project.dao.ReviewDao;
import com.example.prm392_project.dao.SaleDao;
import com.example.prm392_project.dao.UserDao;

@Database(entities = {User.class, Review.class, Cart.class, OrderDetail.class, Product.class, Category.class, Order.class, Sale.class}, version = 1, exportSchema = false)
public abstract class AppDatabase  extends RoomDatabase{
    public abstract UserDao userDao();
    public abstract ReviewDao reviewDao();
    public abstract CartDao cartDao();
    public abstract OrderDetailDao orderDetailDao();
    public abstract ProductDao productDao();
    public abstract CategoryDao categoryDao();
    public abstract OrderDao orderDao();
    public abstract SaleDao saleDao();
        private static AppDatabase INSTANCE = null;

        public static AppDatabase getInstance(Context context){
            if(INSTANCE == null){
                synchronized (AppDatabase.class){
                    if(INSTANCE == null){
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                        AppDatabase.class, "shopping_marketDB")
                                .fallbackToDestructiveMigration()
                                .allowMainThreadQueries()
                                .build();
                    }
                }
                return INSTANCE;
            }
            return INSTANCE;
        }
}
