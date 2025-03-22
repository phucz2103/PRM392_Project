package com.example.prm392_project.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import java.util.List;


import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_project.Bean.Cart;
import com.example.prm392_project.Bean.Product;
import com.example.prm392_project.R;
import com.example.prm392_project.Repositories.CartRepository;
import com.example.prm392_project.Repositories.ProductRepository;

public class ProductDetailsActivity extends AppCompatActivity {
    TextView txtCartCount,txtProductName, txtDescription, txtPrice;
    Button btnAddToCart;
    ImageView productimage;
    private static int quantity = 0;
    private int curQuantity = 0;
    private ProductRepository productRepository;
    private CartRepository cartRepository;
    private ImageView btnBack;
    private Cart newcart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        txtCartCount = findViewById(R.id.txtCartCount);

        btnAddToCart = findViewById(R.id.btnAddToCart);
        txtProductName = findViewById(R.id.product_name);
        txtDescription = findViewById(R.id.product_des);
        txtPrice= findViewById(R.id.product_price);
        productimage = findViewById(R.id.imgProduct);

        int productId = getIntent().getIntExtra("product_id", -1);
        cartRepository = new CartRepository(this);
        List<Cart> list = cartRepository.getCartByUser(1);
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
            txtPrice.setText("Price: " + product.getPrice() + " VND");
            // New Cart o day
            newcart = new Cart(product.getPrice(), 1, productId,1);
            // Load ảnh bằng Glide
            //Glide.with(this).load(product.getIMAGE_URL()).into(productimage);
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

        btnAddToCart.setOnClickListener(v -> addToCart());
        ImageView btnCart = findViewById(R.id.btnCart);

        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailsActivity.this, CartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("product_id",productId);
            startActivity(intent);
            finish();
        });

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
            quantity = cartRepository.countDistinctCategoriesInCart(1);
            txtCartCount.setText(String.valueOf(quantity));
        }


    }
}

