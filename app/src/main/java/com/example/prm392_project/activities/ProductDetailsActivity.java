package com.example.prm392_project.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392_project.adapter.OrderHistoryAdapter;
import com.example.prm392_project.adapter.ReviewAdapter;
import com.example.prm392_project.bean.Cart;
import com.example.prm392_project.bean.Product;
import com.example.prm392_project.R;
import com.example.prm392_project.bean.Review;
import com.example.prm392_project.bean.User;
import com.example.prm392_project.bean.pojo.OrderWithUser;
import com.example.prm392_project.repositories.CartRepository;
import com.example.prm392_project.repositories.OrderRepository;
import com.example.prm392_project.repositories.ProductRepository;
import com.example.prm392_project.repositories.ReviewRepository;
import com.example.prm392_project.repositories.UserRepository;

public class ProductDetailsActivity extends AppCompatActivity {
    TextView txtCartCount,txtProductName, txtDescription, txtPrice, txtDiscountPrice;
    Button btnAddToCart,btnSend;
    ImageView productimage;
    private static int quantity = 0;
    private int curQuantity = 0;
    private ProductRepository productRepository;
    private ReviewAdapter radapter;
    private ReviewRepository reviewRepository;
    private UserRepository userRepository;
    private OrderRepository orderRepository;
    private CartRepository cartRepository;
    private ImageView btnBack;
    private Cart newcart;
    private int userId = 0;
    private RecyclerView recyclerView;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList;
    EditText edtReview;
    RatingBar edtRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Toolbar toolbar = findViewById(R.id.toolbarProductDetail);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        txtCartCount = findViewById(R.id.txtCartCount);

        btnAddToCart = findViewById(R.id.btnAddToCart);
        txtProductName = findViewById(R.id.product_name);
        txtDescription = findViewById(R.id.product_des);
        txtPrice= findViewById(R.id.product_price);
        txtDiscountPrice = findViewById(R.id.product_discount_price);

        productimage = findViewById(R.id.imgProduct);
        btnSend = findViewById(R.id.btnSend);
        edtReview = findViewById(R.id.edtReview);
        edtRate = findViewById(R.id.edtRate);

        int productId = getIntent().getIntExtra("product_id", -1);
        cartRepository = new CartRepository(this);
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);

        boolean isAdmin = sharedPreferences.getBoolean("isAdmin", false);
        List<Cart> list = cartRepository.getCartByUser(userId);

        //Quantity
        txtCartCount.setText(String.valueOf(list.size()));

        if (productId == -1) {
            Toast.makeText(this, "Product not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        productRepository = new ProductRepository(this);
        Product product = productRepository.getProductById(productId);
        if (product != null) {
            txtProductName.setText(product.getProductName());
            txtDescription.setText(product.getDescription());
            double productPrice = product.getPrice();
            if(product.getIsSaled()){
                txtPrice.setText("Price: " + formatMoney(productPrice) + " VND");
                txtPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                txtPrice.setVisibility(View.VISIBLE);
                txtPrice.setTextColor(Color.BLACK);
                double discountPrice = productPrice*80/100;
                txtDiscountPrice.setText("Price: " + formatMoney(discountPrice) + " VND");
            }
            else{
                txtPrice.setText("Price: " + formatMoney(productPrice) + " VND");
            }

            // New Cart o day
            if(product.getIsSaled()){
                newcart = new Cart(product.getPrice() * 80/100, 1, productId,userId);
            }
            else newcart = new Cart(product.getPrice(), 1, productId,userId);
            // Load ảnh bằng Glide
            Glide.with(this).load(product.getIMAGE_URL()).into(productimage);
        } else {
            Toast.makeText(this, "Product not found!", Toast.LENGTH_SHORT).show();
            finish();
        }

        if(isAdmin){
            btnAddToCart.setText("Update Product");
            btnAddToCart.setOnClickListener( v -> {
                Intent intent = new Intent(ProductDetailsActivity.this, UpdateProductActivity.class);
                intent.putExtra("product_id", productId);
                //intent.putExtra("product_price",txtPrice.getText().toString());
                startActivity(intent);
            });
        }
        else{
            btnAddToCart.setOnClickListener(v -> addToCart(productId));
            if(userId == -1){
                new AlertDialog.Builder(ProductDetailsActivity.this)
                        .setTitle("Login")
                        .setMessage("Login to continue")
                        .setPositiveButton("OK", (dialog, which) -> {
                            dialog.dismiss();
                            startActivity(new Intent(this, LoginActivity.class));
                        })
                        .setIcon(R.drawable.admin_panel_settings)
                        .show();
            }
            ImageView btnCart = findViewById(R.id.btnCart);

        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailsActivity.this, CartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            intent.putExtra("product_id",productId);
//            intent.putExtra("product_price",txtPrice.getText().toString());
            startActivity(intent);
            finish();
        });

            // viet 1 ham lay userid thanh cong trong bang Order
            orderRepository = new OrderRepository(this);
            List<OrderWithUser> list1 = orderRepository.getOrderWithUserByStatus(1);
            boolean buingocduoc = false;
            for (OrderWithUser tmp: list1) {
                if(userId == tmp.user.getUserID()){
                    buingocduoc = true;
                }
            }
            if (buingocduoc) {
                edtReview.setVisibility(View.VISIBLE);
                edtRate.setVisibility(View.VISIBLE);
                btnSend.setVisibility(View.VISIBLE);
                btnSend.setOnClickListener(v -> {
                    String reviewContent = edtReview.getText().toString();
                    float rating = edtRate.getRating();
                    if (!reviewContent.isEmpty()) {
                        Review review = new Review(reviewContent,String.valueOf(System.currentTimeMillis()),(int)rating,2,productId);
                        reviewRepository = new ReviewRepository(this);
                        reviewRepository.insertReview(review);
                        Toast.makeText(this, "Review sent!", Toast.LENGTH_SHORT).show();
                        edtReview.setText("");
                        edtRate.setRating(0);
                        Toast.makeText(this, "Please enter a review!", Toast.LENGTH_SHORT).show();
                    }
                });
            }


        }

        userRepository = new UserRepository(this);
        reviewRepository = new ReviewRepository(this);
        String duoc = userRepository.getUserByID(String.valueOf(userId)).getFullName();
        recyclerView = findViewById(R.id.recyclerViewduoc);
        List<Review> getReviews = reviewRepository.getReviewsByProduct(productId);
        if(getReviews.size() > 0){
            radapter = new ReviewAdapter(this, getReviews, duoc);
            recyclerView.setAdapter(radapter);
        }
    }
    private void addToCart(int productId) {
        cartRepository = new CartRepository(this);
        curQuantity++;
        List<Cart> listcart = cartRepository.getCartByUser(userId);
        if(listcart.size() >0){
            //curQuantity++;
            Cart cart = cartRepository.getCartByProductID(productId);
            cartRepository.increaseQuantity(cart);
            //Toast.makeText(this, "You have already added this product to your cart!", Toast.LENGTH_SHORT).show();
        }
        else {
            // Insert new cart
            cartRepository.insert(newcart);
            txtCartCount.setVisibility(View.VISIBLE);
            // truyen UserId vao
            quantity = cartRepository.countDistinctCategoriesInCart(userId);
            txtCartCount.setText(String.valueOf(quantity));
        }
    }
    private String formatMoney(double price){
        String originalPriceFormatted = String.format("%,d", (long)price);
        return  originalPriceFormatted;
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

