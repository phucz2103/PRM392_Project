package com.example.prm392_project.irepositories;

import com.example.prm392_project.bean.Cart;

import java.util.List;

public interface ICartRepository {
    void insert(Cart cart);
    List<Cart> getCartByUser(int userID);
    int countDistinctCategoriesInCart(int userID);
    void updateCartItem(int cartID, int quantity);
    void deleteCartItem(int cartID);
    void deleteCartbyUserID(int UserID);
}
