package com.example.prm392_project.Activities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
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
import com.example.prm392_project.Adapter.ProductAdapter;
import com.example.prm392_project.Bean.Category;
import com.example.prm392_project.Bean.Product;
import com.example.prm392_project.R;
import com.example.prm392_project.Repositories.CategoryRepository;
import com.example.prm392_project.Repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;

public class SaleActivity extends BaseActivity {
    private RecyclerView recyclerCategories, recyclerProducts;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;
    private EditText edtSearch;
    private Button btnSearch;
    private List<Product> allProducts = new ArrayList<>();
    private int currentCategoryId = 0; // 0 means "All Products"
    private String currentSearchQuery = "";
    private static final int PAGE_SIZE = 6;
    private boolean isLoading = false;
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sale);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0); // Set bottom padding to 0
            return insets;
        });

        // Initialize repositories
        categoryRepository = new CategoryRepository(this);
        productRepository = new ProductRepository(this);

        // Initialize UI components
        initViews();
        setupRecyclerViews();

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
    }

    private void setupRecyclerViews() {
        // Setup Category RecyclerView
        LinearLayoutManager categoryLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);
        recyclerCategories.setLayoutManager(categoryLayoutManager);

        categoryAdapter = new CategoryAdapter(this, new ArrayList<>(), category -> {
            // Handle category click
            filterProductsByCategory(category.getCategoryID());
        });
        recyclerCategories.setAdapter(categoryAdapter);

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
            // TODO: Navigate to product details activity
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
        categoryAdapter.setCategoryList(categories);

        // Add "All" category at the beginning
        Category allCategory = new Category("All Sale Products", true);
        allCategory.setCategoryID(0); // Special ID for "All"

        List<Category> categoriesWithAll = new ArrayList<>();
        categoriesWithAll.add(allCategory);
        categoriesWithAll.addAll(categories);

        categoryAdapter.setCategoryList(categoriesWithAll);
    }

    private void loadProducts(int page) {
        int offset = page * PAGE_SIZE;
        // Use getSaleProducts instead of getProducts
        List<Product> newProducts = productRepository.getSaleProducts(PAGE_SIZE, offset);

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
            // No filters applied, get all sale products
            filteredProducts = productRepository.getSaleProducts(PAGE_SIZE, offset);
        } else if (currentCategoryId != 0 && currentSearchQuery.isEmpty()) {
            // Only filter by category
            filteredProducts = productRepository.getSaleProductsByCategory(currentCategoryId, PAGE_SIZE, offset);
        } else if (currentCategoryId == 0 && !currentSearchQuery.isEmpty()) {
            // Only filter by search query
            filteredProducts = productRepository.searchSaleProducts(currentSearchQuery, PAGE_SIZE, offset);
        } else {
            // Filter by both category and search query
            filteredProducts = productRepository.searchSaleProductsByCategory(currentSearchQuery, currentCategoryId, PAGE_SIZE, offset);
        }

        if (filteredProducts != null && !filteredProducts.isEmpty()) {
            allProducts.addAll(filteredProducts);
            productAdapter.setProductList(allProducts);

            // Show load more button if we got a full page
            productAdapter.setShowLoadingButton(filteredProducts.size() == PAGE_SIZE);

            if (page == 0 && filteredProducts.isEmpty()) {
                Toast.makeText(SaleActivity.this, "No sale products found", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        currentPage = 0;
        allProducts.clear();
        loadProducts(currentPage);
    }
}