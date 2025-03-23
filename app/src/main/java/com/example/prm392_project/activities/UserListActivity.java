package com.example.prm392_project.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_project.adapter.CustomerAdapter;
import com.example.prm392_project.bean.User;
import com.example.prm392_project.R;
import com.example.prm392_project.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {
    private UserRepository userRepository;
    private RecyclerView recyclerViewUsers;
    private CustomerAdapter customerAdapter;
    private Spinner spinner_filter, spinner_sort;
    private EditText search_bar_user;
    private List<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_list);
        Toolbar toolbar = findViewById(R.id.toolbarUserList);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        // Chỉnh padding cho system bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userRepository = new UserRepository(this);
        search_bar_user = findViewById(R.id.search_bar_user);
        spinner_filter = findViewById(R.id.spinner_filter);
        spinner_sort = findViewById(R.id.spinner_sort);
        // Thiết lập RecyclerView
        recyclerViewUsers = findViewById(R.id.recycler_view);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        userList = userRepository.getAllUsers();
        // Khởi tạo Adapter và gán vào RecyclerView
        customerAdapter = new CustomerAdapter(this, userList);
        customerAdapter.setUserList(userList);
        recyclerViewUsers.setAdapter(customerAdapter);

        spinner_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateUserList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinner_sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateUserList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        search_bar_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateUserList();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this,DashboardActivity.class);
        startActivity(intent);
        finish();
        return true;
    }
    private void updateUserList() {
        String searchQuery = search_bar_user.getText().toString().trim();
        String selectedGender = spinner_filter.getSelectedItem().toString();
        String selectedSort = spinner_sort.getSelectedItem().toString();

        if (searchQuery.trim().isEmpty()) {
            searchQuery = "%";
        } else {
            searchQuery = "%" + searchQuery + "%";
        }

        if (selectedGender.equals("All")) {
            selectedGender = "%";
        }

        if (selectedSort.equals("ASC")) {
            userList = userRepository.searchUserAsc(searchQuery, selectedGender);
        } else {
            userList = userRepository.searchUserDesc(searchQuery, selectedGender);
        }
        // Cập nhật danh sách trong Adapter
        customerAdapter.setUserList(userList);
        customerAdapter.notifyDataSetChanged();
    }
}
