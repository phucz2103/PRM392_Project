package com.example.prm392_project.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UpdateProductActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private Uri imageUri;
    private EditText edtProductName, edtDescription, edtQuantity;
    private Spinner spnCategory;
    private Button btnUpdate;
    private ImageView imgProduct;
    private ImageButton btnSelectImage;
    private EditText edtPrice;
    private ICategoryRepository categoryRepository;
    private List<Category> categories;
    private RadioGroup rgIsSaled, rgIsAvailable;
    private IProductRepository productRepository;
    private int productId;

    private ActivityResultLauncher<Intent> imagePickerLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null && result.getData().getData() != null) {
                        imageUri = result.getData().getData();

                        // Take persistable permission for the URI
                        try {
                            getContentResolver().takePersistableUriPermission(
                                    imageUri,
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                            );

                            // Use Picasso to load image more reliably
                            Picasso.get()
                                    .load(imageUri)
                                    .placeholder(R.drawable.avatar)
                                    .error(R.drawable.error)
                                    .into(imgProduct);

                            //btnSelectImage.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(UpdateProductActivity.this,
                                    "Cannot access this image. Please select another.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
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
        checkPermissions();
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
        btnSelectImage = findViewById(R.id.btnSelectImage);
        rgIsSaled = findViewById(R.id.rgIsSaled);
        rgIsAvailable = findViewById(R.id.rgIsAvailable);
        edtPrice = findViewById(R.id.edtPrice);
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

            btnSelectImage.setVisibility(View.VISIBLE);
            if (product.getIMAGE_URL() != null && !product.getIMAGE_URL().isEmpty()) {
                imageUri = Uri.parse(product.getIMAGE_URL());


                try {

                    Picasso.get()
                            .load(imageUri)
                            .placeholder(R.drawable.avatar)
                            .error(R.drawable.error)
                            .into(imgProduct);

                    //btnSelectImage.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    btnSelectImage.setVisibility(View.VISIBLE);
                    // Let the user select a new image if loading fails
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
        btnSelectImage.setOnClickListener(v -> openImageChooser());

        btnUpdate.setOnClickListener(v -> {
            if (validateInput()) {
                updateProduct();
            }
        });

    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image/png"});
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        imagePickerLauncher.launch(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imgProduct.setImageBitmap(bitmap);
                //btnSelectImage.setVisibility(View.GONE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        if (imageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
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
        String imageUrl = imageUri.toString();
        boolean isSaled = rgIsSaled.getCheckedRadioButtonId() == R.id.rbSaledTrue;
        boolean isAvailable = rgIsAvailable.getCheckedRadioButtonId() == R.id.rbAvailableTrue;

        Product updatedProduct = new Product(name, description, price, imageUrl,
                LocalDateTime.now().toString(), LocalDateTime.now().toString(), isAvailable, categoryID, quantity, isSaled);
        updatedProduct.setProductID(productId);

        productRepository.updateProduct(updatedProduct);
        Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show();
        //finish();
    }
    private void checkPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        PERMISSION_REQUEST_CODE);
         }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Đóng Activity và quay lại màn hình trước đó
        return true;
    }
}