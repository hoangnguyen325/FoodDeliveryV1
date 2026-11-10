package com.example.fooddeliveryv1.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddeliveryv1.MainActivity;
import com.example.fooddeliveryv1.R;
import com.example.fooddeliveryv1.data.model.User;
import com.example.fooddeliveryv1.data.repository.AuthRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etRegName;
    private TextInputEditText etRegPhone;
    private TextInputEditText etRegAddress;
    private TextInputEditText etRegPassword;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        authRepository = new AuthRepository(this);

        etRegName = findViewById(R.id.etRegName);
        etRegPhone = findViewById(R.id.etRegPhone);
        etRegAddress = findViewById(R.id.etRegAddress);
        etRegPassword = findViewById(R.id.etRegPassword);

        MaterialButton btnRegister = findViewById(R.id.btnRegister);
        TextView tvLoginLink = findViewById(R.id.tvLoginLink);

        btnRegister.setOnClickListener(v -> handleRegister());
        tvLoginLink.setOnClickListener(v -> finish());
    }

    private void handleRegister() {
        String name = etRegName.getText() != null ? etRegName.getText().toString().trim() : "";
        String phone = etRegPhone.getText() != null ? etRegPhone.getText().toString().trim() : "";
        String address = etRegAddress.getText() != null ? etRegAddress.getText().toString().trim() : "";
        String password = etRegPassword.getText() != null ? etRegPassword.getText().toString().trim() : "";

        authRepository.register(name, phone, address, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(User user, String token) {
                Toast.makeText(RegisterActivity.this, "Đăng ký thành công! Chào " + user.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
