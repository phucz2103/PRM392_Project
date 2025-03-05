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

    public User login(String email, String password){
        return userDao.login(email,password);
    }
    public User getUserByID(String userID){
        return userDao.getUserByID(userID);
    }
    public void updateUser(User user){
        userDao.updateUser(user);
    }
    public void insertUser(User user){
        userDao.insert(user);
    }

    public void deleteUser(String userID){
        userDao.deleteUser(userID);
    }
}
