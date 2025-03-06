package com.example.prm392_project.Activities;

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
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        tvSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

    }
}