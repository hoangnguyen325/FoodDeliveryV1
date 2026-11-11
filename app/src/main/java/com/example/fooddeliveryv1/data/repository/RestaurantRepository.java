package com.example.fooddeliveryv1.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fooddeliveryv1.data.model.FoodItem;
import com.example.fooddeliveryv1.data.model.Restaurant;
import com.example.fooddeliveryv1.data.remote.ApiClient;
import com.example.fooddeliveryv1.data.remote.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRepository {

    private final ApiService apiService;

    public RestaurantRepository() {
        this.apiService = ApiClient.getApiService();
    }

    public LiveData<List<Restaurant>> getRestaurants() {
        MutableLiveData<List<Restaurant>> data = new MutableLiveData<>(getMockRestaurants());
        apiService.getRestaurants().enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(@NonNull Call<List<Restaurant>> call, @NonNull Response<List<Restaurant>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Restaurant>> call, @NonNull Throwable t) {
                // Keep mock data
            }
        });
        return data;
    }

    public LiveData<List<FoodItem>> getMenu(String restaurantId) {
        MutableLiveData<List<FoodItem>> data = new MutableLiveData<>(getMockMenu(restaurantId));
        apiService.getMenu(restaurantId).enqueue(new Callback<List<FoodItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<FoodItem>> call, @NonNull Response<List<FoodItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<FoodItem>> call, @NonNull Throwable t) {
                // Keep mock data
            }
        });
        return data;
    }

    private List<Restaurant> getMockRestaurants() {
        List<Restaurant> list = new ArrayList<>();
        list.add(new Restaurant("res_1", "Phở Bò Gia Truyền", "https://images.unsplash.com/photo-1582878826629-29b7ad1cdc43?w=500", 4.8, 1.2, "123 Nguyễn Huệ, Q.1, TP.HCM", 10.7769, 106.7009));
        list.add(new Restaurant("res_2", "Bún Chả Hà Nội", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500", 4.6, 2.5, "45 Lê Lợi, Q.1, TP.HCM", 10.7738, 106.6983));
        list.add(new Restaurant("res_3", "Pizza Hut Premium", "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=500", 4.5, 3.0, "88 Hai Bà Trưng, Q.3, TP.HCM", 10.7825, 106.6981));
        list.add(new Restaurant("res_4", "Cơm Tấm Sài Gòn", "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=500", 4.9, 0.8, "12 Điện Biên Phủ, Q.Bình Thạnh, TP.HCM", 10.7932, 106.7092));
        return list;
    }

    private List<FoodItem> getMockMenu(String restaurantId) {
        List<FoodItem> list = new ArrayList<>();
        list.add(new FoodItem("food_1", restaurantId, "Phở Bò Đặc Biệt", "Tái, nạm, gầu, gân, bò viên thơm ngon nóng hổi", 65000, "https://images.unsplash.com/photo-1582878826629-29b7ad1cdc43?w=500", true));
        list.add(new FoodItem("food_2", restaurantId, "Phở Bò Tái Nạm", "Bò tái mềm kết hợp với nạm giòn ngon", 55000, "https://images.unsplash.com/photo-1569718212165-3a8278d5f624?w=500", true));
        list.add(new FoodItem("food_3", restaurantId, "Bánh Mì Thịt Nướng", "Bánh mì giòn rụm với thịt nướng đậm đà", 30000, "https://images.unsplash.com/photo-1626777552726-4a6b54c97e46?w=500", true));
        list.add(new FoodItem("food_4", restaurantId, "Trà Sữa Thái Xanh", "Trà sữa vị thái xanh giải nhiệt thơm mát", 25000, "https://images.unsplash.com/photo-1558857563-b371033873b8?w=500", true));
        return list;
    }
}
