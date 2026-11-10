package com.example.fooddeliveryv1.ui.restaurant;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fooddeliveryv1.data.local.CartItem;
import com.example.fooddeliveryv1.data.local.CartRepository;
import com.example.fooddeliveryv1.data.model.FoodItem;
import com.example.fooddeliveryv1.data.repository.RestaurantRepository;

import java.util.List;

public class RestaurantDetailViewModel extends AndroidViewModel {

    private final RestaurantRepository restaurantRepository;
    private final CartRepository cartRepository;
    private LiveData<List<FoodItem>> menuLiveData;

    public RestaurantDetailViewModel(@NonNull Application application) {
        super(application);
        restaurantRepository = new RestaurantRepository();
        cartRepository = new CartRepository(application);
    }

    public LiveData<List<FoodItem>> getMenu(String restaurantId) {
        if (menuLiveData == null) {
            menuLiveData = restaurantRepository.getMenu(restaurantId);
        }
        return menuLiveData;
    }

    public void addToCart(FoodItem foodItem) {
        if (foodItem == null) return;
        CartItem cartItem = new CartItem(
                foodItem.getId(),
                foodItem.getName(),
                foodItem.getPrice(),
                1,
                foodItem.getRestaurantId()
        );
        cartRepository.insert(cartItem);
    }
}
