package com.example.prm392_project.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.prm392_project.Bean.*;
import com.example.prm392_project.DAO.*;

@Database(entities = {Category.class,Product.class,User.class,Cart.class,Order.class
        ,OrderDetail.class,Review.class,Sale.class}, version = 1, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
    public abstract UserDao userDao();
    public abstract CategoryDao categoryDao();
    public abstract OrderDao orderDao();
    public abstract OrderDetailDao orderDetailDao();
    public abstract ReviewDao reviewDao();
    public abstract SaleDao saleDao();
    public abstract CartDao cartDao();

    private static AppDatabase INSTANCE = null;
    public static AppDatabase getInstance(Context context){
        if(INSTANCE == null){
            synchronized (AppDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "ShoppingMarketDB")
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
