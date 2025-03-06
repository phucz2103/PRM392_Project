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
//        userRepository.insertUser(new User("Cao Duy Quan", "0987654321",
//                "user@gmail.com", "Huyen Thach That, Xa Thach Hoa",
//                "Male","abc",true,false));
//        userRepository.insertUser(new User("Nguyen Van A", "0987654321",
//                "user@gmail.com", "Huyen Thach That, Xa Thach Hoa",
//                "Male","abc",true,false));
//        userRepository.insertUser(new User("Nguyen Van B", "0987654321",
//                "user@gmail.com", "Huyen Thach That, Xa Thach Hoa",
//                "Male","abc",true,false));
//        userRepository.insertUser(new User("Nguyen Van C", "0987654321",
//                "user@gmail.com", "Huyen Thach That, Xa Thach Hoa",
//                "Male","abc",true,false));

    }
}