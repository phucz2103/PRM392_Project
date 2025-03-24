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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {
    // Các thành phần UI
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

    // Dữ liệu và trạng thái
    private List<Product> allProducts = new ArrayList<>();
    private int currentCategoryId = 0; // 0 nghĩa là "Tất cả sản phẩm"
    private String currentSearchQuery = "";
    private static final int PAGE_SIZE = 6;
    private boolean isLoading = false;
    private int currentPage = 0;
    private User user;
    boolean isAdmin = false;

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

        // Khởi tạo các repository
        categoryRepository = new CategoryRepository(this);
        productRepository = new ProductRepository(this);
        userRepository = new UserRepository(this);

        // Khởi tạo giao diện
        initViews();
        setupRecyclerViews();
        checkAdminStatus();

        // Tải dữ liệu
        currentPage = 0;
        loadCategories();
        loadProducts(currentPage);

        // Thiết lập listeners
        setupListeners();
        setupBottomNavigation();
    }

    // Khởi tạo các thành phần UI
    private void initViews() {
        recyclerCategories = findViewById(R.id.recyclerCategories);
        recyclerProducts = findViewById(R.id.recyclerProducts);
        edtSearch = findViewById(R.id.edtSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        imgSaleBanner = findViewById(R.id.imgSaleBanner);

        imgSaleBanner.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SaleActivity.class);
            startActivity(intent);
        });
    }

    // Thiết lập RecyclerViews
    private void setupRecyclerViews() {
        // Thiết lập RecyclerView cho danh mục
        LinearLayoutManager categoryLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);
        recyclerCategories.setLayoutManager(categoryLayoutManager);

        homepageAdapter = new HomepageAdapter(this, new ArrayList<>(), category -> {
            filterProductsByCategory(category.getCategoryID());
        });
        recyclerCategories.setAdapter(homepageAdapter);

        // Thiết lập RecyclerView cho sản phẩm (dạng lưới 2 cột)
        GridLayoutManager productLayoutManager = new GridLayoutManager(this, 2);
        productLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position == productAdapter.getItemCount() - 1 &&
                        productAdapter.getItemViewType(position) == 1) ? 2 : 1;
            }
        });
        recyclerProducts.setLayoutManager(productLayoutManager);

        productAdapter = new ProductAdapter(this, new ArrayList<>(), product -> {
            Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
            SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
            int userId = sharedPreferences.getInt("userId", -1);
            intent.putExtra("userId", userId);
            intent.putExtra("product_id", product.getProductID());
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

    // Tải danh sách danh mục
    private void loadCategories() {
        List<Category> categories = categoryRepository.getAllCategories();

        // Thêm danh mục "Tất cả" vào đầu danh sách
        Category allCategory = new Category("All Categories", true);
        allCategory.setCategoryID(0);

        List<Category> categoriesWithAll = new ArrayList<>();
        categoriesWithAll.add(allCategory);
        categoriesWithAll.addAll(categories);

        homepageAdapter.setCategoryList(categoriesWithAll);
    }

    // Tải danh sách sản phẩm theo trang
    private void loadProducts(int page) {
        int offset = page * PAGE_SIZE;
        // Yêu cầu PAGE_SIZE + 1 sản phẩm để kiểm tra xem có còn sản phẩm nữa không
        List<Product> newProducts = productRepository.getFilteredProducts(0,
                "", isAdmin ? 0 : 1, 0, PAGE_SIZE + 1, offset);

        if (newProducts != null && !newProducts.isEmpty()) {
            // Kiểm tra xem còn sản phẩm để tải hay không
            boolean hasMoreItems = newProducts.size() > PAGE_SIZE;

            // Chỉ hiển thị PAGE_SIZE sản phẩm
            if (hasMoreItems) {
                newProducts = newProducts.subList(0, PAGE_SIZE);
            }

            allProducts.addAll(newProducts);
            productAdapter.setProductList(allProducts);
            productAdapter.setShowLoadingButton(hasMoreItems);
        } else {
            productAdapter.setShowLoadingButton(false);
        }

        isLoading = false;
    }

    // Tải danh sách sản phẩm đã lọc theo trang
    private void loadFilteredProducts(int page) {
        isLoading = true;
        int offset = page * PAGE_SIZE;

        // Nếu là trang đầu tiên, xóa các mục hiện có
        if (page == 0) {
            allProducts.clear();
        }

        // Yêu cầu PAGE_SIZE + 1 sản phẩm để kiểm tra xem có còn sản phẩm nữa không
        List<Product> filteredProducts = productRepository.getFilteredProducts(currentCategoryId,
                currentSearchQuery, isAdmin ? 0 : 1, 0, PAGE_SIZE + 1, offset);

        if (filteredProducts != null && !filteredProducts.isEmpty()) {
            // Kiểm tra xem còn sản phẩm để tải hay không
            boolean hasMoreItems = filteredProducts.size() > PAGE_SIZE;

            // Chỉ hiển thị PAGE_SIZE sản phẩm
            if (hasMoreItems) {
                filteredProducts = filteredProducts.subList(0, PAGE_SIZE);
            }

            allProducts.addAll(filteredProducts);
            productAdapter.setProductList(allProducts);
            productAdapter.setShowLoadingButton(hasMoreItems);

            if (page == 0 && filteredProducts.isEmpty()) {
                Toast.makeText(HomeActivity.this, "Cannot found product", Toast.LENGTH_SHORT).show();
            }
        } else {
            productAdapter.setShowLoadingButton(false);
        }

        isLoading = false;
    }

    // Thiết lập các sự kiện
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

    // Lọc sản phẩm theo danh mục
    private void filterProductsByCategory(int categoryId) {
        currentCategoryId = categoryId;
        applyFilters();
    }

    // Thực hiện tìm kiếm
    private void performSearch() {
        currentSearchQuery = edtSearch.getText().toString().trim().toLowerCase();
        applyFilters();
    }

    // Áp dụng bộ lọc
    private void applyFilters() {
        allProducts.clear();
        currentPage = 0;
        productAdapter.setProductList(new ArrayList<>());
        loadFilteredProducts(currentPage);
    }

    // Kiểm tra quyền admin
    private void checkAdminStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);
        user = new User();
        if (userId != -1) {
            user = userRepository.getUserByID(String.valueOf(userId));
            isAdmin = user.getIsAdmin();
        }

        if (user.getIsAdmin()) {
            btnAddProduct.setVisibility(View.VISIBLE);
            btnAddProduct.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, CreateProductActivity.class);
                startActivity(intent);
            });
        } else {
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