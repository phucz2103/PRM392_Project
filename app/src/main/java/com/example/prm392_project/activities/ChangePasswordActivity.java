package com.example.prm392_project.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import org.mindrot.jbcrypt.BCrypt;
import com.example.prm392_project.bean.User;
import com.example.prm392_project.helpers.Validation;
import com.example.prm392_project.R;
import com.example.prm392_project.repositories.UserRepository;

public class ChangePasswordActivity extends AppCompatActivity {
    private UserRepository userRepository;
    private EditText etCurrentPassword;
    private EditText etNewPassword;
    private EditText etConfirmPassword;
    private Button btnChangePassword;
    private ImageView ivCurrentPassword, ivNewPassword, ivConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = findViewById(R.id.toolbarChangePassword);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userRepository = new UserRepository(this);
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmNewPassword);
        ivCurrentPassword = findViewById(R.id.ivCurrentPassword);
        ivNewPassword = findViewById(R.id.ivNewPassword);
        ivConfirmPassword = findViewById(R.id.ivConfirmNewPassword);

        // Gọi hàm xử lý sự kiện click
        togglePasswordVisibility(ivCurrentPassword, etCurrentPassword);
        togglePasswordVisibility(ivNewPassword, etNewPassword);
        togglePasswordVisibility(ivConfirmPassword, etConfirmPassword);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("UserID");
        User user = userRepository.getUserByID(userID);
        btnChangePassword = findViewById(R.id.btnSaveChangePassword);
        btnChangePassword.setOnClickListener(v -> {
                if (etCurrentPassword.getText().toString().isEmpty()) {
                    etCurrentPassword.setError("Please enter your current password");
                    etCurrentPassword.requestFocus();
                    return;
                }
                if (etNewPassword.getText().toString().isEmpty()) {
                    etNewPassword.setError("Please enter your new password");
                    etNewPassword.requestFocus();
                    return;
                }
                if (etConfirmPassword.getText().toString().isEmpty()) {
                    etConfirmPassword.setError("Please enter your confirm password");
                    etConfirmPassword.requestFocus();
                    return;
                }

                if(!BCrypt.checkpw(etCurrentPassword.getText().toString(), user.getPassword())){
                    etCurrentPassword.setError("Current password is incorrect");
                    etCurrentPassword.requestFocus();
                    return;
                }

                if(!Validation.isValidPassword(etNewPassword.getText().toString())){
                etNewPassword.setError("Password must contain at least 8 characters, including uppercase, lowercase, number, and special character");
                etNewPassword.requestFocus();
                return;
                }
                if(!etNewPassword.getText().toString().equals(etConfirmPassword.getText().toString())){
                    etConfirmPassword.setError("Confirm password is incorrect");
                    etConfirmPassword.requestFocus();
                    return;
                }
                String hashpassword = BCrypt.hashpw(etNewPassword.getText().toString(), BCrypt.gensalt());
                user.setPassword(hashpassword);
                userRepository.updateUser(user);
                Toast.makeText(ChangePasswordActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                Intent intent1 = new Intent();
                intent1.putExtra("UserID", String.valueOf(user.getUserID()));
                setResult(RESULT_OK, intent1);
                finish();
        });
    }

    private void togglePasswordVisibility(ImageView imageView, EditText editText) {
        imageView.setOnClickListener(v -> {
            if (editText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                // Hiển thị mật khẩu
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                imageView.setImageResource(R.drawable.ic_open_eye); // Cập nhật icon
            } else {
                // Ẩn mật khẩu
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                imageView.setImageResource(R.drawable.ic_closed_eye);
            }
            editText.setSelection(editText.getText().length()); // Giữ con trỏ ở cuối văn bản
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Exit")
                .setMessage("Are you sure you want to leave? Unsaved changes will be lost.")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Lấy dữ liệu từ Intent gốc
                    Intent intent = getIntent();
                    if (intent != null && intent.hasExtra("UserID")) {
                        String userId = intent.getStringExtra("UserID");
                        User user = userRepository.getUserByID(userId);

                        // Tạo intent mới để trả kết quả
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("UserID", String.valueOf(user.getUserID()));
                        setResult(RESULT_OK, resultIntent);
                    }
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss()) // Ở lại nếu chọn No
                .show();
        return true;
    }


}