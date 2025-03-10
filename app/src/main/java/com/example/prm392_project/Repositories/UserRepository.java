package com.example.prm392_project.Repositories;

import android.content.Context;

import com.example.prm392_project.Bean.User;
import com.example.prm392_project.DAO.UserDao;
import com.example.prm392_project.Database.AppDatabase;

public class UserRepository {
    private UserDao userDao;
    public UserRepository(Context context){
        AppDatabase db = AppDatabase.getInstance(context);
        userDao = db.userDao();
    }
    public void insertUser(User user){
        userDao.insert(user);
    }
}
