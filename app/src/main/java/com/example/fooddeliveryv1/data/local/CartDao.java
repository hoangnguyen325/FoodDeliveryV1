package com.example.fooddeliveryv1.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CartItem cartItem);

    @Update
    void update(CartItem cartItem);

    @Query("UPDATE cart_items SET quantity = :quantity WHERE id = :id")
    void updateQuantity(int id, int quantity);

    @Delete
    void deleteItem(CartItem cartItem);

    @Query("DELETE FROM cart_items WHERE id = :id")
    void deleteItemById(int id);

    @Query("DELETE FROM cart_items")
    void clearCart();

    @Query("SELECT * FROM cart_items")
    LiveData<List<CartItem>> getAllCartItems();

    @Query("SELECT * FROM cart_items")
    List<CartItem> getAllCartItemsSync();

    @Query("SELECT restaurantId FROM cart_items LIMIT 1")
    String getCartRestaurantId();

    @Query("SELECT * FROM cart_items WHERE foodId = :foodId LIMIT 1")
    CartItem getCartItemByFoodId(String foodId);
}
