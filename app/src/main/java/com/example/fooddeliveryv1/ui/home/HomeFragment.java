package com.example.fooddeliveryv1.ui.home;

import android.content.Intent;
import android.os.Bundle;
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

    private HomeViewModel homeViewModel;
    private RestaurantAdapter restaurantAdapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        RecyclerView rvCategories = view.findViewById(R.id.rvCategories);
        RecyclerView rvRestaurants = view.findViewById(R.id.rvRestaurants);

        // Categories setup
        rvCategories.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        List<String> categories = Arrays.asList("Tất cả", "Cơm", "Phở & Mì", "Đồ uống", "Fast Food");
        CategoryAdapter categoryAdapter = new CategoryAdapter(categories, category -> {
            Toast.makeText(requireContext(), "Đã chọn: " + category, Toast.LENGTH_SHORT).show();
        });
        rvCategories.setAdapter(categoryAdapter);

        // Restaurants setup
        rvRestaurants.setLayoutManager(new LinearLayoutManager(requireContext()));
        restaurantAdapter = new RestaurantAdapter(restaurant -> {
            Intent intent = new Intent(requireContext(), RestaurantDetailActivity.class);
            intent.putExtra("EXTRA_RESTAURANT", restaurant);
            startActivity(intent);
        });
        rvRestaurants.setAdapter(restaurantAdapter);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getRestaurants().observe(getViewLifecycleOwner(), restaurants -> {
            progressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
            if (restaurants != null) {
                restaurantAdapter.setRestaurantList(restaurants);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Trigger reload data
            homeViewModel.getRestaurants();
        });
    }
}
