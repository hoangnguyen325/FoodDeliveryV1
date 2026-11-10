package com.example.fooddeliveryv1.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.fooddeliveryv1.data.model.Restaurant;
import com.example.fooddeliveryv1.data.repository.RestaurantRepository;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private final RestaurantRepository restaurantRepository;
    private LiveData<List<Restaurant>> restaurantsLiveData;

    public HomeViewModel() {
        restaurantRepository = new RestaurantRepository();
    }

    public LiveData<List<Restaurant>> getRestaurants() {
        if (restaurantsLiveData == null) {
            restaurantsLiveData = restaurantRepository.getRestaurants();
        }
        return restaurantsLiveData;
    }
}
