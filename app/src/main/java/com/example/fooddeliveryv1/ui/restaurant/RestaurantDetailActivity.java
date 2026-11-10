package com.example.fooddeliveryv1.ui.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryv1.R;
import com.example.fooddeliveryv1.data.model.Restaurant;
import com.example.fooddeliveryv1.ui.cart.CartActivity;

public class RestaurantDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RESTAURANT = "EXTRA_RESTAURANT";

    private RestaurantDetailViewModel viewModel;
    private FoodAdapter foodAdapter;
    private ProgressBar progressBarMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        Restaurant restaurant = (Restaurant) getIntent().getSerializableExtra(EXTRA_RESTAURANT);

        ImageView imgBanner = findViewById(R.id.imgRestaurantBanner);
        ImageButton btnBack = findViewById(R.id.btnBack);
        TextView tvName = findViewById(R.id.tvRestaurantDetailName);
        TextView tvRating = findViewById(R.id.tvDetailRating);
        TextView tvAddress = findViewById(R.id.tvDetailAddress);
        progressBarMenu = findViewById(R.id.progressBarMenu);
        RecyclerView rvFoodMenu = findViewById(R.id.rvFoodMenu);

        btnBack.setOnClickListener(v -> finish());

        if (restaurant != null) {
            tvName.setText(restaurant.getName());
            tvRating.setText(String.valueOf(restaurant.getRating()));
            tvAddress.setText(restaurant.getAddress());

            Glide.with(this)
                    .load(restaurant.getImageUrl())
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .into(imgBanner);
        }

        rvFoodMenu.setLayoutManager(new LinearLayoutManager(this));
        foodAdapter = new FoodAdapter(foodItem -> {
            if (foodItem != null) {
                FoodDetailBottomSheetDialogFragment bottomSheet = FoodDetailBottomSheetDialogFragment.newInstance(foodItem);
                bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
            }
        });
        rvFoodMenu.setAdapter(foodAdapter);

        findViewById(R.id.fabCart).setOnClickListener(v -> {
            Intent intent = new Intent(RestaurantDetailActivity.this, CartActivity.class);
            startActivity(intent);
        });

        viewModel = new ViewModelProvider(this).get(RestaurantDetailViewModel.class);

        if (restaurant != null) {
            viewModel.getMenu(restaurant.getId()).observe(this, foodItems -> {
                progressBarMenu.setVisibility(View.GONE);
                if (foodItems != null) {
                    foodAdapter.setFoodList(foodItems);
                }
            });
        } else {
            progressBarMenu.setVisibility(View.GONE);
        }
    }
}
