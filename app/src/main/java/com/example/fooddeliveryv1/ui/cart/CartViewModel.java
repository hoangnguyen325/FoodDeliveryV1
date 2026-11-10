package com.example.fooddeliveryv1.ui.cart;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fooddeliveryv1.data.local.CartItem;
import com.example.fooddeliveryv1.data.local.CartRepository;
import com.example.fooddeliveryv1.data.model.Order;
import com.example.fooddeliveryv1.data.remote.ApiClient;
import com.example.fooddeliveryv1.data.remote.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartViewModel extends AndroidViewModel {

    public interface OnOrderPlacedListener {
        void onSuccess(Order order);
        void onError(String message);
    }

    private final CartRepository cartRepository;
    private final ApiService apiService;

    public CartViewModel(@NonNull Application application) {
        super(application);
        cartRepository = new CartRepository(application);
        apiService = ApiClient.getApiService();
    }

    public LiveData<List<CartItem>> getCartItems() {
        return cartRepository.getAllCartItems();
    }

    public void updateQuantity(int id, int newQuantity) {
        cartRepository.updateQuantity(id, newQuantity);
    }

    public void deleteItem(CartItem cartItem) {
        cartRepository.deleteItem(cartItem);
    }

    public void clearCart() {
        cartRepository.clearCart();
    }

    public double calculateSubtotal(List<CartItem> items) {
        double subtotal = 0;
        if (items != null) {
            for (CartItem item : items) {
                subtotal += item.getPrice() * item.getQuantity();
            }
        }
        return subtotal;
    }

    public void placeOrder(List<CartItem> items, double totalAmount, OnOrderPlacedListener listener) {
        if (items == null || items.isEmpty()) {
            if (listener != null) listener.onError("Giỏ hàng của bạn đang trống");
            return;
        }

        String restaurantId = items.get(0).getRestaurantId();
        Order newOrder = new Order(
                "ORD_" + System.currentTimeMillis(),
                "user_101",
                restaurantId,
                totalAmount,
                "PREPARING",
                10.7769,
                106.7009
        );

        apiService.createOrder(newOrder).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(@NonNull Call<Order> call, @NonNull Response<Order> response) {
                clearCart();
                if (listener != null) {
                    listener.onSuccess(newOrder);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Order> call, @NonNull Throwable t) {
                // Fallback for offline/demo: proceed with local order creation
                clearCart();
                if (listener != null) {
                    listener.onSuccess(newOrder);
                }
            }
        });
    }
}
