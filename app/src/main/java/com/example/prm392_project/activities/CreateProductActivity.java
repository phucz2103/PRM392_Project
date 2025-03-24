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

public class CreateProductActivity extends BaseActivity {
    // Các thành phần UI
    private EditText edtProductName, edtDescription, edtQuantity, edtPrice, edtImageUrl;
    private Spinner spnCategory;
    private Button btnCreate, btnPreviewImage;
    private ImageView imgProduct;

    // Dữ liệu và repository
    private ICategoryRepository categoryRepository;
    private IProductRepository productRepository;
    private List<Category> categories;
    private String imageUrl;

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

        // Thiết lập toolbar với nút quay lại
        Toolbar toolbar = findViewById(R.id.toolbarCreateProduct);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Khởi tạo và thiết lập các thành phần
        initViews();
        loadCategories();
        setupListeners();
    }

    // Khởi tạo các thành phần UI và repository
    private void initViews() {
        // Khởi tạo repository
        categoryRepository = new CategoryRepository(this);
        productRepository = new ProductRepository(this);

        // Ánh xạ các thành phần giao diện
        edtProductName = findViewById(R.id.edtProductName);
        edtDescription = findViewById(R.id.edtDescription);
        edtQuantity = findViewById(R.id.edtQuantity);
        edtPrice = findViewById(R.id.edtPrice);
        spnCategory = findViewById(R.id.spnCategory);
        btnCreate = findViewById(R.id.btnCreate);
        imgProduct = findViewById(R.id.imgProduct);
        edtImageUrl = findViewById(R.id.edtImageUrl);
        btnPreviewImage = findViewById(R.id.btnPreviewImage);
    }

    // Tải danh sách danh mục cho spinner
    private void loadCategories() {
        categories = categoryRepository.getAllAvailableCategories();

        // Tạo danh sách tên danh mục cho spinner
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categories) {
            categoryNames.add(category.getCategoryName());
        }

        // Thiết lập adapter cho spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categoryNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(adapter);
    }

    // Thiết lập các sự kiện lắng nghe
    private void setupListeners() {
        // Xử lý sự kiện xem trước hình ảnh
        btnPreviewImage.setOnClickListener(v -> previewImage());

        // Xử lý sự kiện tạo sản phẩm
        btnCreate.setOnClickListener(v -> {
            if (validateInput()) {
                createProduct();
            }
        });
    }

    // Xem trước hình ảnh từ URL
    private void previewImage() {
        imageUrl = edtImageUrl.getText().toString().trim();

        if (TextUtils.isEmpty(imageUrl)) {
            Toast.makeText(this, "Please enter an image URL", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tải và hiển thị hình ảnh bằng Picasso
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.avatar)
                .error(R.drawable.error)
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

    // Kiểm tra đầu vào hợp lệ
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

    // Tạo sản phẩm mới và lưu vào cơ sở dữ liệu
    private void createProduct() {
        String name = edtProductName.getText().toString();
        String description = edtDescription.getText().toString();
        int quantity = Integer.parseInt(edtQuantity.getText().toString());
        double price = Double.parseDouble(edtPrice.getText().toString());
        int selectedPosition = spnCategory.getSelectedItemPosition();
        int categoryID = categories.get(selectedPosition).getCategoryID();

        // Tạo đối tượng sản phẩm mới
        Product product = new Product(name, description, price, imageUrl, LocalDateTime.now().toString(),
                LocalDateTime.now().toString(), true, categoryID, quantity, false);

        // Lưu sản phẩm vào cơ sở dữ liệu
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