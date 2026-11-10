package com.example.fooddeliveryv1.ui.search;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fooddeliveryv1.data.model.Restaurant;
import com.example.fooddeliveryv1.data.repository.RestaurantRepository;

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends AndroidViewModel {

    private final RestaurantRepository repository;
    private final MutableLiveData<List<Restaurant>> searchResults = new MutableLiveData<>();
    private List<Restaurant> allRestaurants = new ArrayList<>();

    public SearchViewModel(@NonNull Application application) {
        super(application);
        repository = new RestaurantRepository();
        loadAllRestaurants();
    }

    private void loadAllRestaurants() {
        repository.getRestaurants().observeForever(restaurants -> {
            if (restaurants != null) {
                allRestaurants = restaurants;
                searchResults.setValue(restaurants);
            }
        });
    }

    public LiveData<List<Restaurant>> getSearchResults() {
        return searchResults;
    }

    public void search(String query) {
        if (query == null || query.trim().isEmpty()) {
            searchResults.setValue(allRestaurants);
            return;
        }

        String lowerQuery = query.toLowerCase().trim();
        List<Restaurant> filtered = new ArrayList<>();
        for (Restaurant r : allRestaurants) {
            if (r.getName().toLowerCase().contains(lowerQuery) || r.getAddress().toLowerCase().contains(lowerQuery)) {
                filtered.add(r);
            }
        }
        searchResults.setValue(filtered);
    }
}
