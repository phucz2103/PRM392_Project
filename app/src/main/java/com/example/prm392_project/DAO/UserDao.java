package com.example.prm392_project.DAO;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.prm392_project.Bean.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Query("SELECT * FROM User WHERE Email = :email AND Password = :password")
    User login(String email, String password);

    @Query("SELECT * FROM User WHERE Email = :email")
    User getUserByEmail(String email);

    @Query("SELECT * FROM User WHERE Mobile = :phone")
    User getUserByPhone(String phone);

    @Query("UPDATE User SET Password = :newPassword WHERE Email = :email")
    void resetPassword(String newPassword, String email);

    @Query("SELECT * FROM User")
    List<User> getAllUsers();
}
