package com.example.prm392_project.Activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392_project.Bean.Category;
import com.example.prm392_project.Bean.Product;
import com.example.prm392_project.IRepositories.ICategoryRepository;
import com.example.prm392_project.IRepositories.IProductRepository;
import com.example.prm392_project.R;
import com.example.prm392_project.Repositories.CategoryRepository;
import com.example.prm392_project.Repositories.ProductRepository;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UpdateProductActivity extends AppCompatActivity {
    private EditText edtProductName, edtDescription, edtQuantity, edtImageUrl, edtPrice;
    private Spinner spnCategory;
    private Button btnUpdate, btnPreviewImage;
    private ImageView imgProduct;
    private RadioGroup rgIsSaled, rgIsAvailable;
    private ICategoryRepository categoryRepository;
    private IProductRepository productRepository;
    private List<Category> categories;
    private int productId;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbarUpdateProduct);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initViews();
        loadCategories();
        loadProductData();
        setupListeners();
    }

    private void initViews() {
        categoryRepository = new CategoryRepository(this);
        productRepository = new ProductRepository(this);
        edtProductName = findViewById(R.id.edtProductName);
        edtDescription = findViewById(R.id.edtDescription);
        edtQuantity = findViewById(R.id.edtQuantity);
        spnCategory = findViewById(R.id.spnCategory);
        btnUpdate = findViewById(R.id.btnUpdate);
        imgProduct = findViewById(R.id.imgProduct);
        rgIsSaled = findViewById(R.id.rgIsSaled);
        rgIsAvailable = findViewById(R.id.rgIsAvailable);
        edtPrice = findViewById(R.id.edtPrice);
        edtImageUrl = findViewById(R.id.edtImageUrl);
        btnPreviewImage = findViewById(R.id.btnPreviewImage);
    }

    private void loadCategories() {
        categories = categoryRepository.getAllCategories();

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

    private void loadProductData() {
        productId = getIntent().getIntExtra("product_id", 1);
        if (productId != -1) {
            Product product = productRepository.getProductById(productId);
            edtProductName.setText(product.getProductName());
            edtDescription.setText(product.getDescription());
            edtQuantity.setText(String.valueOf(product.getQuantity()));
            edtPrice.setText(String.valueOf(product.getPrice()));

            if (product.getIMAGE_URL() != null && !product.getIMAGE_URL().isEmpty()) {
                imageUrl = product.getIMAGE_URL();
                edtImageUrl.setText(imageUrl);

                try {
                    Picasso.get()
                            .load(imageUrl)
                            .placeholder(R.drawable.avatar)
                            .error(R.drawable.error)
                            .into(imgProduct);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            int categoryPosition = 0;
            for (int i = 0; i < categories.size(); i++) {
                if (categories.get(i).getCategoryID() == product.getCategoryID()) {
                    categoryPosition = i;
                    break;
                }
            }
            spnCategory.setSelection(categoryPosition);

            // Set radio buttons
            if (product.getIsSaled()) {
                rgIsSaled.check(R.id.rbSaledTrue);
            } else {
                rgIsSaled.check(R.id.rbSaledFalse);
            }

            if (product.getIsAvailable()) {
                rgIsAvailable.check(R.id.rbAvailableTrue);
            } else {
                rgIsAvailable.check(R.id.rbAvailableFalse);
            }
        }
    }

    private void setupListeners() {
        btnPreviewImage.setOnClickListener(v -> previewImage());

        btnUpdate.setOnClickListener(v -> {
            if (validateInput()) {
                updateProduct();
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
                .placeholder(R.drawable.avatar)
                .error(R.drawable.error)
                .into(imgProduct, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(UpdateProductActivity.this, "Image loaded successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(UpdateProductActivity.this, "Failed to load image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void updateProduct() {
        String name = edtProductName.getText().toString();
        String description = edtDescription.getText().toString();
        int quantity = Integer.parseInt(edtQuantity.getText().toString());
        double price = Double.parseDouble(edtPrice.getText().toString());
        int selectedPosition = spnCategory.getSelectedItemPosition();
        int categoryID = categories.get(selectedPosition).getCategoryID();
        boolean isSaled = rgIsSaled.getCheckedRadioButtonId() == R.id.rbSaledTrue;
        boolean isAvailable = rgIsAvailable.getCheckedRadioButtonId() == R.id.rbAvailableTrue;

        Product updatedProduct = new Product(name, description, price, imageUrl,
                LocalDateTime.now().toString(), LocalDateTime.now().toString(), isAvailable, categoryID, quantity, isSaled);
        updatedProduct.setProductID(productId);

        productRepository.updateProduct(updatedProduct);
        Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Close activity and return to previous screen
        return true;
    }
}