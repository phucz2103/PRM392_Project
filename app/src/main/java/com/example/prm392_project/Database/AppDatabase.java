package com.example.prm392_project.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.prm392_project.Bean.Cart;
import com.example.prm392_project.Bean.Category;
import com.example.prm392_project.Bean.Order;
import com.example.prm392_project.Bean.OrderDetail;
import com.example.prm392_project.Bean.Product;
import com.example.prm392_project.Bean.Review;
import com.example.prm392_project.Bean.Sale;
import com.example.prm392_project.Bean.User;
import com.example.prm392_project.DAO.CartDao;
import com.example.prm392_project.DAO.CategoryDao;
import com.example.prm392_project.DAO.OrderDao;
import com.example.prm392_project.DAO.OrderDetailDao;
import com.example.prm392_project.DAO.ProductDao;
import com.example.prm392_project.DAO.ReviewDao;
import com.example.prm392_project.DAO.SaleDao;
import com.example.prm392_project.DAO.UserDao;

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
