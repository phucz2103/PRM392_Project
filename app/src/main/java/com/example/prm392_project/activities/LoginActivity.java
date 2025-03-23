package com.example.prm392_project.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392_project.bean.User;
import com.example.prm392_project.R;
import com.example.prm392_project.repositories.UserRepository;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvSignUp;
    private TextView tvForgetPassword;
    private ImageView ivLoginPassword;
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
        ivLoginPassword = findViewById(R.id.ivLoginPassword);
        etEmail = findViewById(R.id.lgEmail);
        etPassword = findViewById(R.id.lgPassword);
        tvSignUp = findViewById(R.id.lgSignUp);
        tvForgetPassword = findViewById(R.id.lgForgetPassword);
        btnLogin = findViewById(R.id.btnSignIn);
        togglePasswordVisibility(ivLoginPassword, etPassword);
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            if(validateInput()) {
                User user = userRepository.login(email, password);
                if (user != null) {
                    SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("userId", user.getUserID());
                    editor.putBoolean("isAdmin", user.getIsAdmin());
                    editor.apply();
                    if(!user.getIsActive()){
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("Error")
                                .setMessage("Your account have been ban by Admin, contact 0963133593 to support!")
                                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                                .setIcon(R.drawable.error)
                                .show();
                        return;
                    }
                    if (user.getIsAdmin()) {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        //chuyen huong sang admin
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
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

    private void togglePasswordVisibility(ImageView imageView, EditText editText) {
        imageView.setOnClickListener(v -> {
            if (editText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                imageView.setImageResource(R.drawable.ic_open_eye);
            } else {
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                imageView.setImageResource(R.drawable.ic_closed_eye);
            }
            editText.setSelection(editText.getText().length());
        });
    }
}