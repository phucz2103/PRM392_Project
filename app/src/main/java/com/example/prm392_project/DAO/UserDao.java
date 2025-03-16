package com.example.prm392_project.DAO;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.prm392_project.Bean.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Query("SELECT * FROM User WHERE Email = :email AND Password = :password")
    User login(String email, String password);

    @Query("SELECT * FROM User WHERE IsAdmin = 0")
    List<User> getAllUsers();

    @Query("SELECT * FROM User WHERE Email = :email")
    User getUserByEmail(String email);

    @Query("SELECT * FROM User WHERE Mobile = :phone")
    User getUserByPhone(String phone);

    @Query("UPDATE User SET Password = :newPassword WHERE Email = :email")
    void resetPassword(String newPassword, String email);

    @Query("SELECT * FROM User WHERE UserID = :userID")
    User getUserByID(String userID);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateUser(User user);

    @Query("SELECT * FROM User WHERE FullName LIKE :fullName AND Gender LIKE :gender AND IsAdmin = 0 ORDER BY FullName ASC")
    List<User> searchUserAsc(String fullName, String gender);

    @Query("SELECT * FROM User WHERE FullName LIKE :fullName AND Gender LIKE :gender AND IsAdmin = 0 ORDER BY FullName DESC")
    List<User> searchUserDesc(String fullName, String gender);


}
