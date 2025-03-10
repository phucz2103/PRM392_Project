package com.example.prm392_project;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392_project.Bean.User;
import com.example.prm392_project.DAO.ProductDao;
import com.example.prm392_project.DAO.UserDao;
import com.example.prm392_project.Database.AppDatabase;
import com.example.prm392_project.Repositories.UserRepository;

public class MainActivity extends AppCompatActivity {

    private UserRepository userRepository;
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
        userRepository.insertUser(new User("Nguyen Van A", "0912345678",
                "nguyenvana@gmail.com", "Quan Ba Dinh, Ha Noi",
                "Male", "password123", true, false));

        userRepository.insertUser(new User("Tran Thi B", "0981122334",
                "tranthib@gmail.com", "Quan Hoan Kiem, Ha Noi",
                "Female", "securePass", false, true));

        userRepository.insertUser(new User("Le Van C", "0977564321",
                "levanc@gmail.com", "Thanh Pho Hai Phong",
                "Male", "myPassword", true, true));

        userRepository.insertUser(new User("Pham Minh D", "0909876543",
                "phamminhd@gmail.com", "Quan Hai Ba Trung, Ha Noi",
                "Male", "helloWorld", false, false));

        userRepository.insertUser(new User("Hoang Thu E", "0933456789",
                "hoangthue@gmail.com", "Thanh Pho Da Nang",
                "Female", "thuEPass", true, false));

        userRepository.insertUser(new User("Bui Tien F", "0922233445",
                "buitienf@gmail.com", "Huyen Chuong My, Ha Noi",
                "Male", "pass1234", false, true));

        userRepository.insertUser(new User("Dang Lan G", "0966778899",
                "danglang@gmail.com", "Quan Cau Giay, Ha Noi",
                "Female", "lanGSecret", true, true));

        userRepository.insertUser(new User("Ngo Hoang H", "0911223344",
                "ngohoangh@gmail.com", "Thanh Pho Ho Chi Minh",
                "Male", "hoangHpass", false, false));

        userRepository.insertUser(new User("Vu Thi I", "0955667788",
                "vuthii@gmail.com", "Quan Thanh Xuan, Ha Noi",
                "Female", "thiIPass", true, false));

        userRepository.insertUser(new User("Dinh Quoc K", "0944332211",
                "dinhquock@gmail.com", "Thanh Pho Hue",
                "Male", "quocKPass", false, true));


    }
}