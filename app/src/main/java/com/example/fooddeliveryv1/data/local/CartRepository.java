package com.example.fooddeliveryv1.data.local;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartRepository {

    private final CartDao cartDao;
    private final LiveData<List<CartItem>> allCartItems;
    private final ExecutorService executorService;

    public CartRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        cartDao = database.cartDao();
        allCartItems = cartDao.getAllCartItems();
        executorService = Executors.newSingleThreadExecutor();
    }

    public CartRepository(CartDao cartDao) {
        this.cartDao = cartDao;
        this.allCartItems = cartDao.getAllCartItems();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<CartItem>> getAllCartItems() {
        return allCartItems;
    }

    public void insert(CartItem cartItem) {
        executorService.execute(() -> cartDao.insert(cartItem));
    }

    public void update(CartItem cartItem) {
        executorService.execute(() -> cartDao.update(cartItem));
    }

    public void updateQuantity(int id, int quantity) {
        executorService.execute(() -> cartDao.updateQuantity(id, quantity));
    }

    public void deleteItem(CartItem cartItem) {
        executorService.execute(() -> cartDao.deleteItem(cartItem));
    }

    public void deleteItemById(int id) {
        executorService.execute(() -> cartDao.deleteItemById(id));
    }

    public void clearCart() {
        executorService.execute(cartDao::clearCart);
    }

    public interface RestaurantCheckCallback {
        void onChecked(String existingRestaurantId);
    }

    public void checkCartRestaurant(RestaurantCheckCallback callback) {
        executorService.execute(() -> {
            String resId = cartDao.getCartRestaurantId();
            if (callback != null) {
                callback.onChecked(resId);
            }
        });
    }
}
