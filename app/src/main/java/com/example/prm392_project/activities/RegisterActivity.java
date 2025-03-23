package com.example.prm392_project.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392_project.Bean.User;
import com.example.prm392_project.R;
import com.example.prm392_project.Repositories.UserRepository;

public class RegisterActivity extends AppCompatActivity {
    private EditText regisFullName;
    private EditText regisPhoneNumber;
    private EditText regisEmail;
    private EditText regisAddress;
    private EditText regisPassword;
    private EditText regisConfirmPassword;
    private TextView tvSignIn;
    private RadioButton radioMale;
    private Button btnSignUp;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbarUserRegister);
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

        regisFullName = findViewById(R.id.regisFULLNAME);
        regisPhoneNumber = findViewById(R.id.regisPhoneNumber);
        regisEmail = findViewById(R.id.regisEMAIL);
        regisAddress = findViewById(R.id.regisADDRESS);
        regisPassword = findViewById(R.id.regisPASSWORD);
        regisConfirmPassword = findViewById(R.id.etConfirmPassword);
        radioMale = findViewById(R.id.rbMale);
        btnSignUp = findViewById(R.id.btnRegister);
        tvSignIn = findViewById(R.id.regisSignIn);

        btnSignUp.setOnClickListener(v -> {
            String fullName = regisFullName.getText().toString();
            String phoneNumber = regisPhoneNumber.getText().toString();
            String email = regisEmail.getText().toString();
            String address = regisAddress.getText().toString();
            String password = regisPassword.getText().toString();
            String confirmPassword = regisConfirmPassword.getText().toString();
            String isMale = radioMale.isChecked()? "MALE" : "FEMALE";
            if(!validateInput()) return;
            User user = new User(fullName, phoneNumber, email, address, isMale, password, true,false);
            String checkRegister = userRepository.insertUser(user,confirmPassword);

            if(checkRegister.equals("success")){
                Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }else{
                new AlertDialog.Builder(RegisterActivity.this)
                        .setTitle("Error")
                        .setMessage(checkRegister)
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .setIcon(R.drawable.error)
                        .show();
            }
        });

        tvSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
    private boolean validateInput() {
        if (regisFullName.getText().toString().trim().isEmpty()) {
            regisFullName.setError("Email is required");
            return false;
        }
        if (regisPhoneNumber.getText().toString().trim().isEmpty()) {
            regisPhoneNumber.setError("Phone is required");
            return false;
        }
        if (regisEmail.getText().toString().trim().isEmpty()) {
            regisEmail.setError("Email is required");
            return false;
        }
        if (regisAddress.getText().toString().trim().isEmpty()) {
            regisAddress.setError("Address is required");
            return false;
        }
        if (regisPassword.getText().toString().trim().isEmpty()) {
            regisPassword.setError("Password is required");
            return false;
        }
        if (regisConfirmPassword.getText().toString().trim().isEmpty()) {
            regisConfirmPassword.setError("ConfirmPassword is required");
            return false;
        }
        return true;
    }
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}