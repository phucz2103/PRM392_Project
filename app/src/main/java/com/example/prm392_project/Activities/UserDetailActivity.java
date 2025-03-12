package com.example.prm392_project.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392_project.Bean.User;
import com.example.prm392_project.R;
import com.example.prm392_project.Repositories.UserRepository;

public class UserDetailActivity extends AppCompatActivity {

    private UserRepository userRepository;
    private EditText etFullNameUserDetail;
    private EditText etMobileUserDetail;
    private EditText etEmailUserDetail;
    private EditText etAddressUserDetail;
    private RadioButton rbMaleUserDetail;
    private RadioButton rbFemaleUserDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_detail);
        Toolbar toolbar = findViewById(R.id.toolbarUserDetail);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etFullNameUserDetail = findViewById(R.id.edtFullNameUserDetail);
        etMobileUserDetail = findViewById(R.id.edtMobileUserDetail);
        etEmailUserDetail = findViewById(R.id.edtEmailUserDetail);
        etAddressUserDetail = findViewById(R.id.edtAddressUserDetail);
        rbMaleUserDetail = findViewById(R.id.rbMaleUserDetail);
        rbFemaleUserDetail = findViewById(R.id.rbFemaleUserDetail);
        userRepository = new UserRepository(this);
        Intent intent = getIntent();
        User user = userRepository.getUserByID(intent.getStringExtra("UserID"));
        if (user != null) {
            etFullNameUserDetail.setText(user.getFullName());
            etMobileUserDetail.setText(user.getMobile());
            etEmailUserDetail.setText(user.getEmail());
            etAddressUserDetail.setText(user.getAddress());
            if (user.getGender().equals("Male")) {
                rbMaleUserDetail.setChecked(true);
            } else {
                rbFemaleUserDetail.setChecked(true);
            }
        } else {
            Log.e("UserDetailActivity", "User not found");
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Đóng Activity và quay lại màn hình trước đó
        return true;
    }
}