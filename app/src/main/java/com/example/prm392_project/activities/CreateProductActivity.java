package com.example.prm392_project.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392_project.bean.Category;
import com.example.prm392_project.bean.Product;
import com.example.prm392_project.irepositories.ICategoryRepository;
import com.example.prm392_project.irepositories.IProductRepository;
import com.example.prm392_project.R;
import com.example.prm392_project.repositories.CategoryRepository;
import com.example.prm392_project.repositories.ProductRepository;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CreateProductActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private Uri imageUri;
    private EditText edtProductName, edtDescription, edtQuantity;
    private Spinner spnCategory;
    private Button btnCreate;
    private ImageView imgProduct;
    private ICategoryRepository categoryRepository;
    private List<Category> categories;
    private EditText edtImageUrl;
    private Button btnPreviewImage;
    private String imageUrl;
    private EditText edtPrice;
    private IProductRepository productRepository;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbarCreateProduct);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initViews();
        loadCategories();
        setupListeners();
    }

    private void initViews() {
        categoryRepository = new CategoryRepository(this);
        productRepository = new ProductRepository(this);
        edtProductName = findViewById(R.id.edtProductName);
        edtDescription = findViewById(R.id.edtDescription);
        edtQuantity = findViewById(R.id.edtQuantity);
        spnCategory = findViewById(R.id.spnCategory);
        btnCreate = findViewById(R.id.btnCreate);
        imgProduct = findViewById(R.id.imgProduct);
        edtPrice = findViewById(R.id.edtPrice);
        edtImageUrl = findViewById(R.id.edtImageUrl);
        btnPreviewImage = findViewById(R.id.btnPreviewImage);
    }
    private void loadCategories() {
        categories = categoryRepository.getAllCategories();

        // Create list of category names for the spinner
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categories) {
            categoryNames.add(category.getCategoryName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categoryNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(adapter);
    }

    private void setupListeners() {
        btnPreviewImage.setOnClickListener(v -> previewImage());

        btnCreate.setOnClickListener(v -> {
            if (validateInput()) {
                createProduct();
            }
        });

    }
    private void previewImage() {
        imageUrl = edtImageUrl.getText().toString().trim();

        if (TextUtils.isEmpty(imageUrl)) {
            Toast.makeText(this, "Please enter an image URL", Toast.LENGTH_SHORT).show();
            return;
        }

        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.avatar) // Add a placeholder image
                .error(R.drawable.error) // Add an error image
                .into(imgProduct, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(CreateProductActivity.this, "Image loaded successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(CreateProductActivity.this, "Failed to load image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean validateInput() {
        if (edtProductName.getText().toString().trim().isEmpty()) {
            edtProductName.setError("Product name is required");
            return false;
        }
        if (edtQuantity.getText().toString().trim().isEmpty()) {
            edtQuantity.setError("Quantity is required");
            return false;
        }
        if (edtPrice.getText().toString().trim().isEmpty()) {
            edtPrice.setError("Price is required");
            return false;
        }
        if (TextUtils.isEmpty(imageUrl)) {
            Toast.makeText(this, "Please enter and preview an image URL", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void createProduct() {
        String name = edtProductName.getText().toString();
        String description = edtDescription.getText().toString();
        int quantity = Integer.parseInt(edtQuantity.getText().toString());
        double price = Double.parseDouble(edtPrice.getText().toString());
        int selectedPosition = spnCategory.getSelectedItemPosition();
        int categoryID = categories.get(selectedPosition).getCategoryID();

        // Use the URL directly
        Product product = new Product(name, description, price, imageUrl, LocalDateTime.now().toString(),
                LocalDateTime.now().toString(), true, categoryID, quantity, false);
        productRepository.insertProduct(product);
        Toast.makeText(this, "Product created successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Đóng Activity và quay lại màn hình trước đó
        return true;
    }
}