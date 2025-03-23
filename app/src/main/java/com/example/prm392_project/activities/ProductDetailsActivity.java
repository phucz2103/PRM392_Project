package com.example.prm392_project.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.prm392_project.bean.Cart;
import com.example.prm392_project.bean.Product;
import com.example.prm392_project.R;
import com.example.prm392_project.repositories.CartRepository;
import com.example.prm392_project.repositories.ProductRepository;

public class ProductDetailsActivity extends AppCompatActivity {
    TextView txtCartCount,txtProductName, txtDescription, txtPrice;
    Button btnAddToCart,btnSend;
    ImageView productimage;
    private static int quantity = 0;
    private int curQuantity = 0;
    private ProductRepository productRepository;
    private CartRepository cartRepository;
    private ImageView btnBack;
    private Cart newcart;
    private int userId = 0;
    RatingBar edtRate;
    EditText edtReview;

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
        productimage = findViewById(R.id.imgProduct);
        Button btnSend = findViewById(R.id.btnSend);
        EditText edtReview = findViewById(R.id.edtReview);
        RatingBar edtRate = findViewById(R.id.edtRate);

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
            if(product.getIsSaled()){
                txtPrice.setText("Price: " + product.getPrice()*80/100 + " VND");
            }
            else txtPrice.setText("Price: " + product.getPrice() + " VND");

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

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
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
            btnAddToCart.setOnClickListener(v -> addToCart());
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
            intent.putExtra("product_id",productId);
            intent.putExtra("product_price",txtPrice.getText().toString());
            startActivity(intent);
            finish();
        });
        }

    }


//    private void updateQuantity(int change) {
//        int quantity = Integer.parseInt(txtQuantity.getText().toString());
//        quantity += change;
//
//        if (quantity < 0) quantity = 0;
//        txtQuantity.setText(String.valueOf(quantity));
//    }


    private void addToCart() {
        cartRepository = new CartRepository(this);
        curQuantity++;
        int productId = getIntent().getIntExtra("product_id", -1);
        List<Cart> list = cartRepository.getCartByProductID(productId);
        if(curQuantity > 1 || list.size() == 1){
            //curQuantity++;
            cartRepository.increaseQuantity(newcart);
            int x = newcart.getQTY_int();
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
}

