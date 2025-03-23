package com.example.prm392_project.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392_project.R;
import com.example.prm392_project.repositories.UserRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText etEmail;
    private EditText etOtp;
    private Button btnSendOtp;
    private Button btnConfirmOtp;
    private TextView tvCountdown;
    private UserRepository userRepository;
    private Runnable countdownRunnable;
    private Handler handler = new Handler();
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = findViewById(R.id.toolbarForgotPassword);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        executorService = Executors.newSingleThreadExecutor();
        userRepository = new UserRepository(this);
        etEmail = findViewById(R.id.fpEmail);
        etOtp = findViewById(R.id.fgOtp);
        tvCountdown = findViewById(R.id.tvCountdown);
        btnSendOtp = findViewById(R.id.btnReceiveOtp);
        btnConfirmOtp = findViewById(R.id.btnVerify);
        btnSendOtp.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (email.isEmpty()) {
                etEmail.setError("Email is required");
                return;
            }
            executorService.execute(() -> {
                String error = userRepository.receiveOTP(email);
                runOnUiThread(() -> {
                    if (error.isEmpty()) {
                        Toast.makeText(ForgotPasswordActivity.this, "Your OTP has been sent to your email", Toast.LENGTH_SHORT).show();
                        startCountdown();
                    } else {
                        new AlertDialog.Builder(ForgotPasswordActivity.this)
                                .setTitle("Error")
                                .setMessage(error)
                                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                                .setIcon(R.drawable.error)
                                .show();
                    }
                });
            });
        });

        btnConfirmOtp.setOnClickListener(v -> {
            String otp = etOtp.getText().toString();
            if (otp.isEmpty()) {
                etOtp.setError("OTP is required");
                return;
            }
            if (userRepository.checkOTP(otp)) {
                Toast.makeText(this, "OTP is correct", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ResetPasswordActivity.class);
                intent.putExtra("email", etEmail.getText().toString());
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "OTP is incorrect", Toast.LENGTH_SHORT).show();
            }
        });
    }
        private void startCountdown() {
            final long endTime = userRepository.getOtpGeneratedTime() + 60 * 1000;
            countdownRunnable = new Runnable() {
                @Override
                public void run() {
                    long remainingTime = endTime - System.currentTimeMillis();
                    if (remainingTime > 0) {
                        tvCountdown.setText("Time remaining: " + (remainingTime / 1000) + " seconds");
                        handler.postDelayed(this, 1000);
                    } else {
                        tvCountdown.setText("OTP has expired");
                    }
                }
            };
            handler.post(countdownRunnable);
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            if (countdownRunnable != null) {
                handler.removeCallbacks(countdownRunnable);
            }
        }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}