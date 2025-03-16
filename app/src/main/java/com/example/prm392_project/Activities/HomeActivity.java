package com.example.prm392_project.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_project.Adapter.CategoryAdapter;
import com.example.prm392_project.Adapter.HomepageAdapter;
import com.example.prm392_project.Adapter.ProductAdapter;
import com.example.prm392_project.Bean.Category;
import com.example.prm392_project.Bean.Product;
import com.example.prm392_project.R;
import com.example.prm392_project.Repositories.CategoryRepository;
import com.example.prm392_project.Repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerCategories, recyclerProducts;
    private HomepageAdapter homepageAdapter;
    private ProductAdapter productAdapter;
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;
    private EditText edtSearch;
    private Button btnSearch;
    private List<Product> allProducts = new ArrayList<>();
    private List<Product> currentFilteredProducts = new ArrayList<>();
    private int currentCategoryId = 0; // 0 means "All Products"
    private String currentSearchQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize repositories
        categoryRepository = new CategoryRepository(this);
        productRepository = new ProductRepository(this);

        // Initialize UI components
        initViews();
        setupRecyclerViews();

        // Load data
        loadCategories();
        loadProducts();

        // Setup listeners
        setupListeners();
    }

    private void initViews() {
        recyclerCategories = findViewById(R.id.recyclerCategories);
        recyclerProducts = findViewById(R.id.recyclerProducts);
        edtSearch = findViewById(R.id.edtSearch);
        btnSearch = findViewById(R.id.btnSearch);
    }

    private void setupRecyclerViews() {
        // Setup Category RecyclerView
        LinearLayoutManager categoryLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);
        recyclerCategories.setLayoutManager(categoryLayoutManager);

        homepageAdapter = new HomepageAdapter(this, new ArrayList<>(), category -> {
            // Handle category click
            filterProductsByCategory(category.getCategoryID());
        });
        recyclerCategories.setAdapter(homepageAdapter);

        // Setup Product RecyclerView (Grid with 2 columns)
        GridLayoutManager productLayoutManager = new GridLayoutManager(this, 2);
        recyclerProducts.setLayoutManager(productLayoutManager);

        productAdapter = new ProductAdapter(this, new ArrayList<>(), product -> {
            // Chuyển sang ProductDetailActivity
            Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
            int userId = getIntent().getIntExtra("userId", -1);
            intent.putExtra("userId", userId);
            intent.putExtra("product_id", product.getProductID());
            //Can userid chuyen sang
            //intent.putExtra("user_id", );
            startActivity(intent);
        });

        recyclerProducts.setAdapter(productAdapter);
    }

    private void loadCategories() {
        List<Category> categories = categoryRepository.getAllCategories();
        homepageAdapter.setCategoryList(categories);

        // Add "All" category at the beginning
        Category allCategory = new Category("All Products", true);
        allCategory.setCategoryID(0); // Special ID for "All"

        List<Category> categoriesWithAll = new ArrayList<>();
        categoriesWithAll.add(allCategory);
        categoriesWithAll.addAll(categories);

        homepageAdapter.setCategoryList(categoriesWithAll);
    }

    private void loadProducts() {
        allProducts = productRepository.getAllProducts();

        if (allProducts == null) {
            allProducts = new ArrayList<>();
        }

        // Reset filters
        currentCategoryId = 0;
        currentSearchQuery = "";
        currentFilteredProducts = new ArrayList<>(allProducts);

        productAdapter.setProductList(allProducts);

        if (allProducts.isEmpty()) {
            Toast.makeText(this, "No products available", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupListeners() {
        btnSearch.setOnClickListener(v -> performSearch());

        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });
    }

    private void filterProductsByCategory(int categoryId) {
        currentCategoryId = categoryId;
        applyFilters();
    }

    private void performSearch() {
        currentSearchQuery = edtSearch.getText().toString().trim().toLowerCase();
        applyFilters();
    }

    private void applyFilters() {

        List<Product> filteredByCategory;
        if (currentCategoryId == 0) {
            filteredByCategory = new ArrayList<>(allProducts);
        } else {
            filteredByCategory = allProducts.stream()
                    .filter(product -> product.getCategoryID() == currentCategoryId)
                    .collect(Collectors.toList());
        }

        List<Product> finalFilteredProducts;
        if (currentSearchQuery.isEmpty()) {
            finalFilteredProducts = filteredByCategory;
        } else {
            finalFilteredProducts = filteredByCategory.stream()
                    .filter(product ->
                            product.getProductName().toLowerCase().contains(currentSearchQuery) ||
                                    (product.getDescription() != null &&
                                            product.getDescription().toLowerCase().contains(currentSearchQuery))
                    )
                    .collect(Collectors.toList());
        }

        currentFilteredProducts = finalFilteredProducts;
        productAdapter.setProductList(currentFilteredProducts);

        if (finalFilteredProducts.isEmpty()) {
            Toast.makeText(HomeActivity.this, "No products found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadProducts();
    }
}