package com.example.prm392_project.dao;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392_project.bean.Cart;

import java.util.List;

@Dao
public interface CartDao {
    @Insert
    void insert(Cart cart);

    @Query("SELECT COUNT(DISTINCT CartID) FROM Cart WHERE userID = :userID")
    int countDistinctCategoriesInCart(int userID);

    @Query("SELECT * FROM Cart WHERE UserID = :userID")
    List<Cart> getCartByUser(int userID);

    @Query("SELECT * FROM Cart WHERE ProductID = :productID")
    List<Cart> getCartByProduct(int productID);

    @Query("UPDATE Cart SET QTY_int = :quantity WHERE CartID = :cartID")
    void updateCart(int cartID, int quantity);

    @Query("UPDATE Cart SET Price = :price WHERE CartID = :cartID")
    void updateCart(double price, int cartID);

    @Query("DELETE FROM Cart WHERE CartID = :cartID")
    void deleteCartItem(int cartID);

    @Query("DELETE FROM Cart WHERE UserID = :userID")
    void deleteCartByUserId(int userID);

}
