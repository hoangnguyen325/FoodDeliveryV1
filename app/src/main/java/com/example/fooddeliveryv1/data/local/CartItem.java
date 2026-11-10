package com.example.fooddeliveryv1.data.local;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "cart_items")
public class CartItem implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String foodId;
    private String foodName;
    private double price;
    private int quantity;
    private String restaurantId;

    public CartItem() {
    }

    @Ignore
    public CartItem(String foodId, String foodName, double price, int quantity, String restaurantId) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.price = price;
        this.quantity = quantity;
        this.restaurantId = restaurantId;
    }

    public CartItem(int id, String foodId, String foodName, double price, int quantity, String restaurantId) {
        this.id = id;
        this.foodId = foodId;
        this.foodName = foodName;
        this.price = price;
        this.quantity = quantity;
        this.restaurantId = restaurantId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }
}
