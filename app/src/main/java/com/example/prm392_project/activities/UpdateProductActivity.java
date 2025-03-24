package com.example.prm392_project.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class UpdateProductActivity extends BaseActivity {
    // Các thành phần UI
    private EditText edtProductName, edtDescription, edtQuantity, edtImageUrl, edtPrice;
    private Spinner spnCategory;
    private Button btnUpdate, btnPreviewImage;
    private ImageView imgProduct;
    private RadioGroup rgIsSaled, rgIsAvailable;
    private RadioButton rbSaledTrue, rbSaledFalse, rbAvailableTrue, rbAvailableFalse;

    // Dữ liệu và repository
    private ICategoryRepository categoryRepository;
    private IProductRepository productRepository;
    private List<Category> categories;
    private Product currentProduct;
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

        // Thiết lập toolbar với nút quay lại
        Toolbar toolbar = findViewById(R.id.toolbarUpdateProduct);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Lấy ID sản phẩm từ intent
        Intent intent = getIntent();
        productId = intent.getIntExtra("product_id", -1);

        if (productId == -1) {
            Toast.makeText(this, "Product ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Khởi tạo và thiết lập các thành phần
        initViews();
        loadCategories();
        loadProductData();
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
        spnCategory = findViewById(R.id.spnCategory);
        btnUpdate = findViewById(R.id.btnUpdate);
        imgProduct = findViewById(R.id.imgProduct);
        rgIsSaled = findViewById(R.id.rgIsSaled);
        rgIsAvailable = findViewById(R.id.rgIsAvailable);
        edtPrice = findViewById(R.id.edtPrice);
        edtImageUrl = findViewById(R.id.edtImageUrl);
        btnPreviewImage = findViewById(R.id.btnPreviewImage);

        // Ánh xạ các RadioButton
        rbSaledTrue = findViewById(R.id.rbSaledTrue);
        rbSaledFalse = findViewById(R.id.rbSaledFalse);
        rbAvailableTrue = findViewById(R.id.rbAvailableTrue);
        rbAvailableFalse = findViewById(R.id.rbAvailableFalse);
    }

    // Tải danh sách danh mục cho spinner
    private void loadCategories() {
        categories = categoryRepository.getAllCategories();

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

    // Tải thông tin sản phẩm hiện tại
    private void loadProductData() {
        currentProduct = productRepository.getProductById(productId);

        if (currentProduct == null) {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Điền thông tin sản phẩm vào các trường
        edtProductName.setText(currentProduct.getProductName());
        edtDescription.setText(currentProduct.getDescription());
        edtQuantity.setText(String.valueOf(currentProduct.getQuantity()));
        edtPrice.setText(String.valueOf(currentProduct.getPrice()));
        edtImageUrl.setText(currentProduct.getIMAGE_URL());
        imageUrl = currentProduct.getIMAGE_URL();

        // Thiết lập trạng thái từ dữ liệu sản phẩm
        if (currentProduct.getIsSaled()) {
            rbSaledTrue.setChecked(true);
        } else {
            rbSaledFalse.setChecked(true);
        }

        if (currentProduct.getIsAvailable()) {
            rbAvailableTrue.setChecked(true);
        } else {
            rbAvailableFalse.setChecked(true);
        }

        // Tải hình ảnh sản phẩm
        if (!TextUtils.isEmpty(imageUrl)) {
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.avatar)
                    .error(R.drawable.error)
                    .into(imgProduct);
        }

        // Chọn danh mục tương ứng trong spinner
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getCategoryID() == currentProduct.getCategoryID()) {
                spnCategory.setSelection(i);
                break;
            }
        }
    }

    // Thiết lập các sự kiện lắng nghe
    private void setupListeners() {
        // Xử lý sự kiện xem trước hình ảnh
        btnPreviewImage.setOnClickListener(v -> previewImage());

        // Xử lý sự kiện cập nhật sản phẩm
        btnUpdate.setOnClickListener(v -> {
            if (validateInput()) {
                updateProduct();
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
                        Toast.makeText(UpdateProductActivity.this, "Image loaded successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(UpdateProductActivity.this, "Failed to load image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    // Cập nhật sản phẩm và lưu vào cơ sở dữ liệu
    private void updateProduct() {
        // Lấy dữ liệu từ form
        String name = edtProductName.getText().toString();
        String description = edtDescription.getText().toString();
        int quantity = Integer.parseInt(edtQuantity.getText().toString());
        double price = Double.parseDouble(edtPrice.getText().toString());
        int selectedPosition = spnCategory.getSelectedItemPosition();
        int categoryID = categories.get(selectedPosition).getCategoryID();
        boolean isSaled = rbSaledTrue.isChecked();
        boolean isAvailable = rbAvailableTrue.isChecked();

        // Cập nhật đối tượng sản phẩm
        currentProduct.setProductName(name);
        currentProduct.setDescription(description);
        currentProduct.setQuantity(quantity);
        currentProduct.setPrice(price);
        currentProduct.setCategoryID(categoryID);
        currentProduct.setIMAGE_URL(imageUrl);
        currentProduct.setIsSaled(isSaled);
        currentProduct.setIsAvailable(isAvailable);
        currentProduct.setUpdatedAt(LocalDateTime.now().toString());

        // Lưu sản phẩm vào cơ sở dữ liệu
        productRepository.updateProduct(currentProduct);
        Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Đóng Activity và quay lại màn hình trước đó
        return true;
    }
}