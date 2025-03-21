package com.example.prm392_project.Activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_project.Adapter.CartAdapter;
import com.example.prm392_project.Bean.Cart;
import com.example.prm392_project.R;
import com.example.prm392_project.Repositories.CartRepository;
import com.example.prm392_project.Repositories.ProductRepository;

import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartItemClickListener {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<Cart> cartList;
    private CartRepository cartRepository;
    private ImageView btnBack;
    private Button btnClearCart;
    private Button btnOrder;
    private ProductRepository productRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_shopping_cart); // Kiểm tra đúng tên file XML

        recyclerView = findViewById(R.id.recyclerCart); // Đảm bảo RecyclerView có trong XML
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnOrder = findViewById(R.id.btnOrder);
        btnOrder.setOnClickListener(v -> showLoadingAndOrder());

        cartRepository = new CartRepository(this);
        productRepository = new ProductRepository(this);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        btnClearCart = findViewById(R.id.btnClearCart);
        btnClearCart.setOnClickListener(v -> deleteCartByUserId());


        // Lấy danh sách sản phẩm trong giỏ hàng bo sung UserID o day
        cartList = cartRepository.getCartByUser(1);

        if (cartList == null || cartList.isEmpty()) {
            Log.e("CartActivity", "Giỏ hàng trống!");
        } else {
            Log.d("CartActivity", "Giỏ hàng có " + cartList.size() + " sản phẩm.");
        }

        // Khởi tạo Adapter và gán vào RecyclerView
        cartAdapter = new CartAdapter(this, cartList, this, productRepository);
        recyclerView.setAdapter(cartAdapter);
    }

    // Xử lý sự kiện tăng số lượng sản phẩm
    @Override
    public void onIncrease(Cart cart) {
        cartRepository.increaseQuantity(cart);
        refreshCart();
    }

    private void showLoadingAndOrder() {
        // Tạo Dialog với vòng tròn xoay
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_loading, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false); // Không cho phép đóng khi bấm ra ngoài
        dialog.show();

        // Giả lập thời gian xử lý đơn hàng (2 giây)
        new Handler().postDelayed(() -> {
            dialog.dismiss(); // Ẩn vòng xoay
            Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();

            // Xóa giỏ hàng sau khi đặt hàng thành công
            deleteCartByUserId();
        }, 2000);
    }

    // Xử lý sự kiện giảm số lượng sản phẩm
    @Override
    public void onDecrease(Cart cart) {
        cartRepository.decreaseQuantity(cart);
        refreshCart();
    }

    // Xử lý sự kiện xóa sản phẩm khỏi giỏ hàng
    @Override
    public void onRemove(Cart cart) {
        cartRepository.deleteCartItem(cart.getCartID());
        refreshCart();
    }

    private void calculateTotalCost() {
        double totalCost = 0;
        for (Cart cart : cartList) {
            totalCost += cart.getQTY_int() * cart.getPrice();
        }

        // Cập nhật UI hiển thị tổng tiền
        TextView txtTotalCost = findViewById(R.id.txtTotalCost);
        txtTotalCost.setText("Total Cost: " + String.format("%.1f", totalCost) + " VND");
    }

    private void deleteCartByUserId() {
        cartRepository.deleteCartbyUserID(1);
        refreshCart();
    }

    // Cập nhật lại danh sách giỏ hàng sau khi thay đổi
    private void refreshCart() {
        // Sua id user o day
        cartList = cartRepository.getCartByUser(1);
        cartAdapter = new CartAdapter(this, cartList, this, productRepository);
        recyclerView.setAdapter(cartAdapter);
        // Cập nhật tổng tiền
        calculateTotalCost();
    }



}
