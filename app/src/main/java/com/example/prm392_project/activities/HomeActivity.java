package com.example.prm392_project.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_project.adapter.HomepageAdapter;
import com.example.prm392_project.adapter.ProductAdapter;
import com.example.prm392_project.bean.Category;
import com.example.prm392_project.bean.Product;
import com.example.prm392_project.bean.User;
import com.example.prm392_project.R;
import com.example.prm392_project.repositories.CategoryRepository;
import com.example.prm392_project.repositories.ProductRepository;
import com.example.prm392_project.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {
    private RecyclerView recyclerCategories, recyclerProducts;
    private HomepageAdapter homepageAdapter;
    private ProductAdapter productAdapter;
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;
    private EditText edtSearch;
    private Button btnSearch;
    private Button btnAddProduct;
    private ImageView imgSaleBanner;
    private List<Product> allProducts = new ArrayList<>();
    private List<Product> currentFilteredProducts = new ArrayList<>();
    private int currentCategoryId = 0; // 0 means "All Products"
    private String currentSearchQuery = "";
    private static final int PAGE_SIZE = 6;
    private boolean isLoading = false;
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // Initialize repositories
        categoryRepository = new CategoryRepository(this);
        productRepository = new ProductRepository(this);
        userRepository = new UserRepository(this);

        // Initialize UI components
        initViews();
        setupRecyclerViews();
        checkAdminStatus();

        // Load data
        currentPage = 0;
        loadCategories();
        loadProducts(currentPage);

        // Setup listeners
        setupListeners();

        setupBottomNavigation();
    }

    private void initViews() {
        recyclerCategories = findViewById(R.id.recyclerCategories);
        recyclerProducts = findViewById(R.id.recyclerProducts);
        edtSearch = findViewById(R.id.edtSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        ImageView imgSaleBanner = findViewById(R.id.imgSaleBanner);
        imgSaleBanner.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SaleActivity.class);
            startActivity(intent);
        });
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
        // Allow the Load More button to span full width
        productLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position == productAdapter.getItemCount() - 1 &&
                        productAdapter.getItemViewType(position) == 1) ? 2 : 1;
            }
        });
        recyclerProducts.setLayoutManager(productLayoutManager);

        productAdapter = new ProductAdapter(this, new ArrayList<>(), product -> {
            // Handle product click
            Toast.makeText(this, "Selected: " + product.getProductName(), Toast.LENGTH_SHORT).show();
            // Chuyển sang ProductDetailActivity
            Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
            SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
            int userId = sharedPreferences.getInt("userId", -1);
            intent.putExtra("userId", userId);
            intent.putExtra("product_id", product.getProductID());
            //Can userid chuyen sang
            //intent.putExtra("user_id", );
            startActivity(intent);
        });

        productAdapter.setLoadMoreListener(() -> {
            currentPage++;
            isLoading = true;

            if (currentCategoryId == 0 && currentSearchQuery.isEmpty()) {
                loadProducts(currentPage);
            } else {
                loadFilteredProducts(currentPage);
            }
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

    private void loadProducts(int page) {
        int offset = page * PAGE_SIZE;
        List<Product> newProducts = productRepository.getProducts(PAGE_SIZE, offset);

        if (newProducts != null && !newProducts.isEmpty()) {
            allProducts.addAll(newProducts);
            productAdapter.setProductList(allProducts);
            productAdapter.setShowLoadingButton(newProducts.size() == PAGE_SIZE);
        } else {
            productAdapter.setShowLoadingButton(false);
        }

        isLoading = false;
    }

    private void loadFilteredProducts(int page) {
        isLoading = true;
        int offset = page * PAGE_SIZE;

        // If this is the first page, clear existing items
        if (page == 0) {
            allProducts.clear();
        }

        // Use repository methods directly instead of filtering with streams
        List<Product> filteredProducts;

        if (currentCategoryId == 0 && currentSearchQuery.isEmpty()) {
            // No filters applied, get all products
            filteredProducts = productRepository.getProducts(PAGE_SIZE, offset);
        } else if (currentCategoryId != 0 && currentSearchQuery.isEmpty()) {
            // Only filter by category
            filteredProducts = productRepository.getProductsByCategory(currentCategoryId, PAGE_SIZE, offset);
        } else if (currentCategoryId == 0 && !currentSearchQuery.isEmpty()) {
            // Only filter by search query
            filteredProducts = productRepository.searchProducts(currentSearchQuery, PAGE_SIZE, offset);
        } else {
            // Filter by both category and search query
            filteredProducts = productRepository.searchProductsByCategory(currentSearchQuery, currentCategoryId, PAGE_SIZE, offset);
        }

        if (filteredProducts != null && !filteredProducts.isEmpty()) {
            allProducts.addAll(filteredProducts);
            productAdapter.setProductList(allProducts);

            // Show load more button if we got a full page
            productAdapter.setShowLoadingButton(filteredProducts.size() == PAGE_SIZE);

            if (page == 0 && filteredProducts.isEmpty()) {
                Toast.makeText(HomeActivity.this, "No products found", Toast.LENGTH_SHORT).show();
            }
        } else {
            productAdapter.setShowLoadingButton(false);
        }

        isLoading = false;
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
        // Clear current products and reset pagination when applying new filters
        allProducts.clear();
        currentPage = 0;

        // Reset adapter with empty list
        productAdapter.setProductList(new ArrayList<>());

        // Load the first page with filters
        loadFilteredProducts(currentPage);
    }

    private void checkAdminStatus(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", 2);
        User user = new User();
        if(userId != -1){user = userRepository.getUserByID(String.valueOf(userId));}

        if(user.getIsAdmin()){
            btnAddProduct.setVisibility(View.VISIBLE);
            btnAddProduct.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, CreateProductActivity.class);
                startActivity(intent);
            });
        }
        else{
            btnAddProduct.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAdminStatus();
        currentPage = 0;
        allProducts.clear();
        loadProducts(currentPage);
    }
}