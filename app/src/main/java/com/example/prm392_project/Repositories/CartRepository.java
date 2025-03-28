package com.example.prm392_project.Repositories;

import android.content.Context;

import com.example.prm392_project.Bean.Cart;
import com.example.prm392_project.DAO.CartDao;
import com.example.prm392_project.DAO.CategoryDao;
import com.example.prm392_project.Database.AppDatabase;
import com.example.prm392_project.IRepositories.ICartRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CartRepository implements ICartRepository {

    private CartDao cartDao;

    public CartRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        cartDao = db.cartDao();
    }
    @Override
    public void insert(Cart cart) {
        cartDao.insert(cart);
    }

    public void increaseQuantity(Cart cart) {
        cart.setQTY_int(cart.getQTY_int() + 1);
        updateCartItem(cart.getCartID(),cart.getQTY_int());
    }

    public void decreaseQuantity(Cart cart) {
        if (cart.getQTY_int() > 1) {
            cart.setQTY_int(cart.getQTY_int() - 1);
            updateCartItem(cart.getCartID(),cart.getQTY_int());
        } else {
            deleteCartItem(cart.getCartID());
        }
    }

    @Override
    public List<Cart> getCartByUser(int userID) {
        return cartDao.getCartByUser(userID);
    }

    public List<Cart> getCartByProductID(int productID) {
        return cartDao.getCartByProduct(productID);
    }

    @Override
    public int countDistinctCategoriesInCart(int userID) {
        return cartDao.countDistinctCategoriesInCart(userID);
    }

    @Override
    public void updateCartItem(int cartID, int quantity) {
        cartDao.updateCart(cartID,quantity);
    }

    @Override
    public void deleteCartItem(int cartID) {
        cartDao.deleteCartItem(cartID);
    }

    @Override
    public void deleteCartbyUserID(int UserID) {
        cartDao.deleteCartByUserId(UserID);
    }

    public List<Cart> getCartByUserWithPagination(int userId, int page, int limit) {
        List<Cart> fullCart = getCartByUser(userId);

        // Tính toán vị trí bắt đầu và kết thúc
        int start = (page - 1) * limit;
        int end = Math.min(start + limit, fullCart.size());

        if (start >= fullCart.size()) {
            return new ArrayList<>();
        }

        return fullCart.subList(start, end);
    }

}
