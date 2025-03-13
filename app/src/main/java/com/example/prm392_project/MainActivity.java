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
    private CategoryRepository categoryRepository;


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
//        orderRepository = new OrderRepository(this);
//        orderDetailRepository = new OrderDetailRepository(this);
//        productRepository = new ProductRepository(this);

//        orderRepository.insertOrder(new Order("2024-03-03",1500,1, 3));
//        orderRepository.insertOrder(new Order("2024-03-03",1000,1, 2));
//        orderRepository.insertOrder(new Order("2024-01-01",500,1, 2));
        

//        orderDetailRepository.insertOrderDetail(new OrderDetail(1,500.00, 3, 1,1));
//        orderDetailRepository.insertOrderDetail(new OrderDetail(1,500.00, 3, 2,1));
//        orderDetailRepository.insertOrderDetail(new OrderDetail(1,500.00, 3, 3,1));
//        orderDetailRepository.insertOrderDetail(new OrderDetail(2,500.00, 2, 101,2));
//        orderDetailRepository.insertOrderDetail(new OrderDetail(1,500.00, 2, 102,2));
//        orderDetailRepository.insertOrderDetail(new OrderDetail(1,500.00, 2, 101,3));


        userRepository.insertUser(new User("Cao Duy Quan", "0987654321",
                "user1@gmail.com", "Huyen Thach That, Xa Thach Hoa",
                "Male","Abc123456@",true,false), "Abc123456@" );
        userRepository.insertUser(new User("Nguyen Van A", "0987654322",
                "user2@gmail.com", "Huyen Thach That, Xa Thach Hoa",
                "Male","Abc123456@",true,false),"Abc123456@");


    }
}