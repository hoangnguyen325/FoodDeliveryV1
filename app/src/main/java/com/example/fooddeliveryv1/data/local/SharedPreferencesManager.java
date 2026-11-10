package com.example.fooddeliveryv1.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.fooddeliveryv1.data.model.User;
import com.google.gson.Gson;

public class SharedPreferencesManager {

    private static final String PREF_NAME = "FoodDeliveryPrefs";
    private static final String KEY_TOKEN = "KEY_AUTH_TOKEN";
    private static final String KEY_USER = "KEY_USER_DATA";

    private static volatile SharedPreferencesManager instance;
    private final SharedPreferences prefs;
    private final Gson gson;

    private SharedPreferencesManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static SharedPreferencesManager getInstance(Context context) {
        if (instance == null) {
            synchronized (SharedPreferencesManager.class) {
                if (instance == null) {
                    instance = new SharedPreferencesManager(context);
                }
            }
        }
        return instance;
    }

    public void saveAuthToken(String token) {
        prefs.edit().putString(KEY_TOKEN, token).apply();
    }

    public String getAuthToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public void saveUser(User user) {
        if (user != null) {
            String json = gson.toJson(user);
            prefs.edit().putString(KEY_USER, json).apply();
        }
    }

    public User getUser() {
        String json = prefs.getString(KEY_USER, null);
        if (json != null) {
            return gson.fromJson(json, User.class);
        }
        // Fallback default user if null
        return new User("user_101", "Nguyễn Văn A", "0901234567", "123 Nguyễn Huệ, Q.1, TP.HCM", 10.7769, 106.7009);
    }

    public boolean isLoggedIn() {
        return getAuthToken() != null;
    }

    public void clearSession() {
        prefs.edit().clear().apply();
    }
}
