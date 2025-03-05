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

    @Query("SELECT * FROM User")
    List<User> getAllUsers();

    @Query("SELECT * FROM User WHERE UserID = :userID")
    User getUserByID(String userID);

    @Query("DELETE FROM User WHERE UserID = :userID")
    void deleteUser(String userID);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateUser(User user);


}
