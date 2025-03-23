package com.example.prm392_project.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392_project.bean.User;
import com.example.prm392_project.R;
import com.example.prm392_project.repositories.UserRepository;

public class UserProfileActivity extends BaseActivity {
    private UserRepository userRepository;
    private EditText edtFullName;
    private EditText edtMobile;
    private EditText edtEmail;
    private EditText edtAddress;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private Button btnEditUserProfile;
    private Button btnChangePassword;
    private Button btnLogout;
    private ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                @Override
                public void onActivityResult(ActivityResult activityResult) {
                    Intent intent = activityResult.getData();
                    User user = userRepository.getUserByID(intent.getStringExtra("UserID"));
                    edtFullName.setText(user.getFullName());
                    edtMobile.setText(user.getMobile());
                    edtEmail.setText(user.getEmail());
                    edtAddress.setText(user.getAddress());
                    if(user.getGender().equals("Male")){
                        rbMale.setChecked(true);
                    }else{
                        rbFemale.setChecked(true);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);

        setupBottomNavigation();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        edtFullName = findViewById(R.id.etFullName);
        edtMobile = findViewById(R.id.etMobile);
        edtEmail = findViewById(R.id.etEmail);
        edtAddress = findViewById(R.id.etAddress);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        btnEditUserProfile = findViewById(R.id.btnEditUserProfile);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnLogout = findViewById(R.id.btnLogout);

        userRepository = new UserRepository(this);
        User user;
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);
        if (userId != -1) {
        user = userRepository.getUserByID(String.valueOf(userId));
        } else {
            user = userRepository.login("user3@gmail.com", "Abcd123456@@");
        }

        edtFullName.setText(user.getFullName());
        edtMobile.setText(user.getMobile());
        edtEmail.setText(user.getEmail());
        edtAddress.setText(user.getAddress());
        if(user.getGender().equals("Male")){
            rbMale.setChecked(true);
        }else{
            rbFemale.setChecked(true);
        }

        btnEditUserProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditProfileActivity.class);
            intent.putExtra("UserID", String.valueOf(user.getUserID()));
            startForResult.launch(intent);
        });

        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            intent.putExtra("UserID", String.valueOf(user.getUserID()));
            startForResult.launch(intent);
        });

        btnLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();

                        Intent intent = new Intent(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss()) // Ở lại nếu chọn No
                    .show();
        });


    }



}