package com.example.fooddeliveryv1.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.fooddeliveryv1.R;
import com.example.fooddeliveryv1.ui.restaurant.RestaurantDetailActivity;

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "FOOD_DELIVERY_APP";

    private HomeViewModel homeViewModel;
    private RestaurantAdapter restaurantAdapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "HomeFragment: onCreateView() called");
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "HomeFragment: onViewCreated() called");

        progressBar = view.findViewById(R.id.progressBar);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        RecyclerView rvCategories = view.findViewById(R.id.rvCategories);
        RecyclerView rvRestaurants = view.findViewById(R.id.rvRestaurants);

        // Categories setup
        if (rvCategories != null) {
            rvCategories.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
            List<String> categories = Arrays.asList("Tất cả", "Cơm", "Phở & Mì", "Đồ uống", "Fast Food");
            CategoryAdapter categoryAdapter = new CategoryAdapter(categories, category -> {
                Toast.makeText(requireContext(), "Đã chọn: " + category, Toast.LENGTH_SHORT).show();
            });
            rvCategories.setAdapter(categoryAdapter);
            Log.d(TAG, "HomeFragment: Categories adapter attached successfully");
        }

        // Restaurants setup
        if (rvRestaurants != null) {
            rvRestaurants.setLayoutManager(new LinearLayoutManager(requireContext()));
            restaurantAdapter = new RestaurantAdapter(restaurant -> {
                Intent intent = new Intent(requireContext(), RestaurantDetailActivity.class);
                intent.putExtra("EXTRA_RESTAURANT", restaurant);
                startActivity(intent);
            });
            rvRestaurants.setAdapter(restaurantAdapter);
            Log.d(TAG, "HomeFragment: Restaurant adapter attached successfully");
        }

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getRestaurants().observe(getViewLifecycleOwner(), restaurants -> {
            Log.d(TAG, "HomeFragment: Observed restaurants data count: " + (restaurants != null ? restaurants.size() : 0));
            if (progressBar != null) progressBar.setVisibility(View.GONE);
            if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
            if (restaurants != null && restaurantAdapter != null) {
                restaurantAdapter.setRestaurantList(restaurants);
            }
        });

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(() -> {
                Log.d(TAG, "HomeFragment: SwipeRefreshLayout triggered");
                homeViewModel.getRestaurants();
            });
        }
    }
}
