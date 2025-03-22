package com.example.prm392_project.Activities;

import android.content.Intent;
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

import com.example.prm392_project.R;
import com.example.prm392_project.Repositories.UserRepository;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText etNewPassword;
    private EditText etConfirmPassword;
    private Button btnResetPassword;
    private UserRepository userRepository;
    private ImageView ivResetNewPassword,ivResetConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userRepository = new UserRepository(this);
        etNewPassword = findViewById(R.id.rsNewPassword);
        etConfirmPassword = findViewById(R.id.rsConfirmPassword);
        btnResetPassword = findViewById(R.id.btnConfirmNewPass);
        ivResetNewPassword = findViewById(R.id.ivResetNewPassword);
        ivResetConfirmPassword = findViewById(R.id.ivResetConfirmPassword);
        togglePasswordVisibility(ivResetNewPassword, etNewPassword);
        togglePasswordVisibility(ivResetConfirmPassword, etConfirmPassword);
        btnResetPassword.setOnClickListener(v -> {
            String newPassword = etNewPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();
            if(!validateInput()){
                return;
            }
            String email = getIntent().getStringExtra("email");
            String error = userRepository.resetPassword(email, newPassword, confirmPassword);
            if(error.isEmpty()){
                Toast.makeText(this, "Reset Password Success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }else{
                new AlertDialog.Builder(ResetPasswordActivity.this)
                        .setTitle("Error")
                        .setMessage(error)
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .setIcon(R.drawable.error)
                        .show();
            }
        });
    }
    private boolean validateInput() {
        if (etNewPassword.getText().toString().trim().isEmpty()) {
            etNewPassword.setError("NewPassword is required");
            return false;
        }
        if (etConfirmPassword.getText().toString().trim().isEmpty()) {
            etConfirmPassword.setError("ConfirmPassword is required");
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