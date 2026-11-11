package com.example.fooddeliveryv1;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.fooddeliveryv1.data.repository.AuthRepository;
import com.example.fooddeliveryv1.ui.cart.CartActivity;
import com.example.fooddeliveryv1.ui.home.HomeFragment;
import com.example.fooddeliveryv1.ui.orders.OrderHistoryFragment;
import com.example.fooddeliveryv1.ui.profile.ProfileFragment;
import com.example.fooddeliveryv1.ui.search.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AuthRepository authRepository = new AuthRepository(this);
        authRepository.autoLoginDemoUser();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                replaceFragment(new HomeFragment());
                return true;
            } else if (itemId == R.id.nav_search) {
                replaceFragment(new SearchFragment());
                return true;
            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
                return false;
            } else if (itemId == R.id.nav_orders) {
                replaceFragment(new OrderHistoryFragment());
                return true;
            } else if (itemId == R.id.nav_profile) {
                replaceFragment(new ProfileFragment());
                return true;
            }
            return false;
        });

        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment());
        }
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
