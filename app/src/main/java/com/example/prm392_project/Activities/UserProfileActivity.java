package com.example.prm392_project.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392_project.Bean.User;
import com.example.prm392_project.R;
import com.example.prm392_project.Repositories.UserRepository;

public class UserProfileActivity extends AppCompatActivity {
    private UserRepository userRepository;
    private EditText edtFullName;
    private EditText edtMobile;
    private EditText edtEmail;
    private EditText edtAddress;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private Button btnEditUserProfile;
    private Button btnChangePassword;

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

        Toolbar toolbar = findViewById(R.id.toolbarUserProfile);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
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

        userRepository = new UserRepository(this);
        User user;
        if (getIntent() != null && getIntent().hasExtra("UserID")) {
        String userId = getIntent().getStringExtra("UserID");
        user = userRepository.getUserByID(userId);
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
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}