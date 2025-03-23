package com.example.prm392_project;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392_project.Bean.Order;
import com.example.prm392_project.Bean.OrderDetail;
import com.example.prm392_project.Bean.Product;
import com.example.prm392_project.Bean.User;
import com.example.prm392_project.DAO.CategoryDao;
import com.example.prm392_project.DAO.OrderDao;
import com.example.prm392_project.DAO.ProductDao;
import com.example.prm392_project.DAO.UserDao;
import com.example.prm392_project.Database.AppDatabase;
import com.example.prm392_project.Repositories.CategoryRepository;
import com.example.prm392_project.Repositories.OrderDetailRepository;
import com.example.prm392_project.Repositories.OrderRepository;
import com.example.prm392_project.Repositories.ProductRepository;
import com.example.prm392_project.Repositories.UserRepository;

public class MainActivity extends AppCompatActivity {

    private UserRepository userRepository;
    private OrderRepository orderRepository;
    private OrderDetailRepository orderDetailRepository;
    private ProductRepository productRepository;

    private CategoryRepository categoryRepository ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userRepository = new UserRepository(this);
        categoryRepository = new CategoryRepository(this);
        orderRepository = new OrderRepository(this);
        orderDetailRepository = new OrderDetailRepository(this);
        productRepository = new ProductRepository(this);

//        orderRepository.insertOrder(new Order("2024-03-03",1500,1, 3));
//        orderRepository.insertOrder(new Order("2024-03-03",1000,1, 2));
//        orderRepository.insertOrder(new Order("2024-01-01",500,1, 2));
//
//
//        orderDetailRepository.insertOrderDetail(new OrderDetail(1,500.00, 3, 1));
//        orderDetailRepository.insertOrderDetail(new OrderDetail(1,500.00, 3, 2));
//        orderDetailRepository.insertOrderDetail(new OrderDetail(1,500.00, 3, 3));
//        orderDetailRepository.insertOrderDetail(new OrderDetail(2,500.00, 2, 1));
//        orderDetailRepository.insertOrderDetail(new OrderDetail(1,500.00, 2, 2));
//        orderDetailRepository.insertOrderDetail(new OrderDetail(1,500.00, 2, 1));



//        userRepository.insertUser(new User("Cao Duy Quan", "0987654322",
//                "user3@gmail.com", "Huyen Thach That, Xa Thach Hoa",
//                "Male","Abc123456@",true,false), "Abc123456@" );
//        userRepository.insertUser(new User("Nguyen Van A", "0987654329",
//                "user2@gmail.com", "Huyen Thach That, Xa Thach Hoa",
//                "Male","Abc123456@",true,true),"Abc123456@");


//        categoryRepository.AddCategory("Foods",true);
//        categoryRepository.AddCategory("Drinks",true);
//        categoryRepository.AddCategory("Toys",true);
//        categoryRepository.AddCategory("Healthcare",true);
//
//        Product[] products = new Product[] {
//                // Foods (Category ID: 1)
//                new Product(
//                        "Pandan Tube Cake",
//                        "A traditional cake made from pandan leaves, sugar, and coconut milk with a distinctive aroma.",
//                        25000,
//                        "https://tiki.vn/blog/wp-content/uploads/2023/07/thumb-12.jpg",
//                        "2025-03-15",
//                        "2025-03-15",
//                        true,
//                        1,
//                        100,
//                        true
//                ),
//                new Product(
//                        "Pho Bo",
//                        "Vietnamese traditional beef noodle soup with aromatic broth, rice noodles, and tender slices of beef.",
//                        45000,
//                        "https://tiki.vn/blog/wp-content/uploads/2023/07/thumb-12.jpg",
//                        "2025-03-15",
//                        "2025-03-15",
//                        true,
//                        1,
//                        80,
//                        false
//                ),
//                new Product(
//                        "Banh Mi",
//                        "Vietnamese sandwich with crispy bread crust, filled with savory meats, fresh vegetables and herbs.",
//                        20000,
//                        "https://tiki.vn/blog/wp-content/uploads/2023/07/thumb-12.jpg",
//                        "2025-03-15",
//                        "2025-03-15",
//                        true,
//                        1,
//                        120,
//                        true
//                ),
//
//                // Drinks (Category ID: 2)
//                new Product(
//                        "Vietnamese Coffee",
//                        "Strong and flavorful coffee brewed with a traditional Vietnamese coffee filter, served with condensed milk.",
//                        35000,
//                        "https://tiki.vn/blog/wp-content/uploads/2023/07/thumb-12.jpg",
//                        "2025-03-15",
//                        "2025-03-15",
//                        true,
//                        2,
//                        90,
//                        true
//                ),
//                new Product(
//                        "Fresh Coconut Water",
//                        "Natural coconut water served directly from young green coconuts, rich in electrolytes.",
//                        30000,
//                        "https://tiki.vn/blog/wp-content/uploads/2023/07/thumb-12.jpg",
//                        "2025-03-15",
//                        "2025-03-15",
//                        true,
//                        2,
//                        70,
//                        false
//                ),
//                new Product(
//                        "Bubble Tea",
//                        "Sweet tea-based drink with chewy tapioca pearls, available in various flavors.",
//                        40000,
//                        "https://tiki.vn/blog/wp-content/uploads/2023/07/thumb-12.jpg",
//                        "2025-03-15",
//                        "2025-03-15",
//                        true,
//                        2,
//                        60,
//                        true
//                ),
//
//                // Toys (Category ID: 3)
//                new Product(
//                        "Wooden Puzzle",
//                        "Educational wooden puzzle for children to develop problem-solving skills and hand-eye coordination.",
//                        120000,
//                        "https://tiki.vn/blog/wp-content/uploads/2023/07/thumb-12.jpg",
//                        "2025-03-15",
//                        "2025-03-15",
//                        true,
//                        3,
//                        40,
//                        false
//                ),
//                new Product(
//                        "Plush Teddy Bear",
//                        "Soft and cuddly teddy bear made with high-quality plush material, perfect for hugging.",
//                        150000,
//                        "https://tiki.vn/blog/wp-content/uploads/2023/07/thumb-12.jpg",
//                        "2025-03-15",
//                        "2025-03-15",
//                        true,
//                        3,
//                        50,
//                        true
//                ),
//                new Product(
//                        "Remote Control Car",
//                        "Battery-operated RC car with responsive controls and durable design for hours of fun.",
//                        250000,
//                        "https://tiki.vn/blog/wp-content/uploads/2023/07/thumb-12.jpg",
//                        "2025-03-15",
//                        "2025-03-15",
//                        true,
//                        3,
//                        30,
//                        false
//                ),
//
//                // Healthcare (Category ID: 4)
//                new Product(
//                        "Vitamin C Supplement",
//                        "Daily vitamin C supplement to boost immune system and promote overall health.",
//                        180000,
//                        "https://tiki.vn/blog/wp-content/uploads/2023/07/thumb-12.jpg",
//                        "2025-03-15",
//                        "2025-03-15",
//                        true,
//                        4,
//                        100,
//                        true
//                ),
//                new Product(
//                        "Digital Thermometer",
//                        "Accurate digital thermometer for measuring body temperature with quick results.",
//                        95000,
//                        "https://tiki.vn/blog/wp-content/uploads/2023/07/thumb-12.jpg",
//                        "2025-03-15",
//                        "2025-03-15",
//                        true,
//                        4,
//                        60,
//                        false
//                ),
//                new Product(
//                        "First Aid Kit",
//                        "Comprehensive first aid kit containing essential supplies for emergency situations.",
//                        220000,
//                        "https://tiki.vn/blog/wp-content/uploads/2023/07/thumb-12.jpg",
//                        "2025-03-15",
//                        "2025-03-15",
//                        true,
//                        4,
//                        45,
//                        true
//                )
//        };
//
// Insert all products
//        for (Product product : products) {
//            productRepository.insertProduct(product);
//        }
    }
}