package com.example.fooddeliveryv1.data.remote;

import com.example.fooddeliveryv1.data.model.FoodItem;
import com.example.fooddeliveryv1.data.model.Order;
import com.example.fooddeliveryv1.data.model.Restaurant;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @GET("restaurants")
    Call<List<Restaurant>> getRestaurants();

    @GET("restaurants/{id}/menu")
    Call<List<FoodItem>> getMenu(@Path("id") String restaurantId);

    @POST("orders")
    Call<Order> createOrder(@Body Order order);

    @GET("orders/{id}")
    Call<Order> getOrderStatus(@Path("id") String orderId);
}
