package com.example.prm392_project.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392_project.Bean.User;
import com.example.prm392_project.R;
import com.example.prm392_project.Repositories.UserRepository;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvSignUp;
    private TextView tvForgetPassword;
    private UserRepository userRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userRepository = new UserRepository(this);

        etEmail = findViewById(R.id.lgEmail);
        etPassword = findViewById(R.id.lgPassword);
        tvSignUp = findViewById(R.id.lgSignUp);
        tvForgetPassword = findViewById(R.id.lgForgetPassword);
        btnLogin = findViewById(R.id.btnSignIn);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            if(validateInput()) {
                User user = userRepository.login(email, password);
                if (user != null) {
                    SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("userId", user.getUserID());
                    editor.apply();
                    if (user.getIsAdmin()) {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        //chuyen huong sang admin
                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        intent.putExtra("userId", user.getUserID());
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        //chuyen huong sang user
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.putExtra("userId", user.getUserID());
                        startActivity(intent);
                        finish();
                    }
                } else {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Error")
                            .setMessage("Email and password incorrect!")
                            .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                            .setIcon(R.drawable.error)
                            .show();
                }
            }
        });

        tvForgetPassword.setOnClickListener(v ->{
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        tvSignUp.setOnClickListener(v ->{
            Intent intent = new Intent(LoginActivity.this, RegisterActivity .class);
            startActivity(intent);
        });
    }
    private boolean validateInput() {
        if (etEmail.getText().toString().trim().isEmpty()) {
            etEmail.setError("Email is required");
            return false;
        }
        if (etPassword.getText().toString().trim().isEmpty()) {
            etPassword.setError("Password is required");
            return false;
        }
        return true;
    }
}