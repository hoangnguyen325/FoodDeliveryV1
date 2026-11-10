package com.example.fooddeliveryv1.data.repository;

import android.content.Context;

import com.example.fooddeliveryv1.data.local.SharedPreferencesManager;
import com.example.fooddeliveryv1.data.model.User;

public class AuthRepository {

    public interface AuthCallback {
        void onSuccess(User user, String token);
        void onError(String message);
    }

    private final SharedPreferencesManager prefManager;

    public AuthRepository(Context context) {
        this.prefManager = SharedPreferencesManager.getInstance(context);
    }

    public void login(String phone, String password, AuthCallback callback) {
        if (phone.isEmpty() || password.isEmpty()) {
            if (callback != null) callback.onError("Vui lòng nhập đầy đủ thông tin đăng nhập");
            return;
        }

        // Simulated login request / JWT Token generation
        String fakeToken = "JWT_TOKEN_" + System.currentTimeMillis();
        User loggedUser = new User("user_101", "Nguyễn Văn A", phone, "123 Nguyễn Huệ, Quận 1, TP.HCM", 10.7769, 106.7009);

        prefManager.saveAuthToken(fakeToken);
        prefManager.saveUser(loggedUser);

        if (callback != null) {
            callback.onSuccess(loggedUser, fakeToken);
        }
    }

    public void register(String name, String phone, String address, String password, AuthCallback callback) {
        if (name.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            if (callback != null) callback.onError("Vui lòng điền đầy đủ các thông tin bắt buộc");
            return;
        }

        String fakeToken = "JWT_TOKEN_" + System.currentTimeMillis();
        User newUser = new User("user_" + System.currentTimeMillis(), name, phone, address, 10.7769, 106.7009);

        prefManager.saveAuthToken(fakeToken);
        prefManager.saveUser(newUser);

        if (callback != null) {
            callback.onSuccess(newUser, fakeToken);
        }
    }

    public User getCurrentUser() {
        return prefManager.getUser();
    }

    public boolean isLoggedIn() {
        return prefManager.isLoggedIn();
    }

    public void logout() {
        prefManager.clearSession();
    }

    public void autoLoginDemoUser() {
        if (!isLoggedIn()) {
            String fakeToken = "JWT_TOKEN_DEMO";
            User demoUser = new User("user_101", "Nguyễn Văn A", "0901234567", "123 Nguyễn Huệ, Quận 1, TP.HCM", 10.7769, 106.7009);
            prefManager.saveAuthToken(fakeToken);
            prefManager.saveUser(demoUser);
        }
    }
}
