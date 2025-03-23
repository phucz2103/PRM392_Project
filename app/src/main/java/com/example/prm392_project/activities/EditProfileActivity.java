package com.example.prm392_project.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392_project.bean.User;
import com.example.prm392_project.helpers.Validation;
import com.example.prm392_project.R;
import com.example.prm392_project.repositories.UserRepository;
import android.app.AlertDialog;
import android.widget.Toast;


public class EditProfileActivity extends AppCompatActivity {
    private UserRepository userRepository;
    private EditText etFullNameEditProfile;
    private EditText etMobileEditProfile;
    private EditText etEmailEditProfile;
    private EditText etAddressEditProfile;
    private RadioButton rgMaleEditProfile;
    private RadioButton rgFemaleEditProfile;
    private Button btnSaveUserProfile;
    Validation validation = new Validation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        // nut back lai
        Toolbar toolbar = findViewById(R.id.toolbarEditProfile);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etFullNameEditProfile = findViewById(R.id.etFullNameEditProfile);
        etMobileEditProfile = findViewById(R.id.etMobileEditProfile);
        etEmailEditProfile = findViewById(R.id.etEmailEditProfile);
        etAddressEditProfile = findViewById(R.id.etAddressEditProfile);
        rgMaleEditProfile = findViewById(R.id.rbMaleEditProfile);
        rgFemaleEditProfile = findViewById(R.id.rbFemaleEditProfile);
        btnSaveUserProfile = findViewById(R.id.btnSaveUserProfile);

        userRepository = new UserRepository(this);
        Intent intent = getIntent();
        User user = userRepository.getUserByID(intent.getStringExtra("UserID"));
        if (user != null) {
            etFullNameEditProfile.setText(user.getFullName());
            etMobileEditProfile.setText(user.getMobile());
            etEmailEditProfile.setText(user.getEmail());
            etAddressEditProfile.setText(user.getAddress());
            if (user.getGender().equals("Male")) {
                rgMaleEditProfile.setChecked(true);
            } else {
                rgFemaleEditProfile.setChecked(true);
            }
        }

        btnSaveUserProfile.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(etFullNameEditProfile.getText().toString().trim().isEmpty()){
                etFullNameEditProfile.setError("Please enter your full name");
                etFullNameEditProfile.requestFocus();
                return;
            }
            if(!Validation.isValidPhone(etMobileEditProfile.getText().toString().trim())){
                etMobileEditProfile.setError("Mobile number must be 10 digits and start with 0");
                etMobileEditProfile.requestFocus();
                return;
            }
            if(etAddressEditProfile.getText().toString().trim().isEmpty()){
                etAddressEditProfile.setError("Please enter your address");
                etAddressEditProfile.requestFocus();
                return;
            }

            user.setFullName(etFullNameEditProfile.getText().toString());
            user.setMobile(etMobileEditProfile.getText().toString());
            user.setAddress(etAddressEditProfile.getText().toString());
            if(rgMaleEditProfile.isChecked()){
                user.setGender("Male");
            }else{
                user.setGender("Female");
            }

            userRepository.updateUser(user);
            Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("UserID", String.valueOf(user.getUserID()));
            setResult(RESULT_OK, intent);
            finish();
        }

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