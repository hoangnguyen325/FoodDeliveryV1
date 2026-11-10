package com.example.fooddeliveryv1.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddeliveryv1.R;
import com.example.fooddeliveryv1.data.local.CartItem;
import com.example.fooddeliveryv1.data.model.Order;
import com.example.fooddeliveryv1.ui.checkout.CheckoutActivity;
import com.example.fooddeliveryv1.ui.tracking.TrackingActivity;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private CartViewModel cartViewModel;
    private CartAdapter cartAdapter;

    private RecyclerView rvCartItems;
    private LinearLayout layoutEmptyCart;
    private View cardCheckoutSummary;
    private TextView tvSubtotal;
    private TextView tvTotalPrice;
    private MaterialButton btnPlaceOrder;
    private MaterialButton btnClearCart;
    private ImageButton btnCartBack;

    private List<CartItem> currentCartItems = new ArrayList<>();
    private double currentTotalAmount = 0;
    private static final double DELIVERY_FEE = 15000;
    private final DecimalFormat formatter = new DecimalFormat("#,### đ");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        rvCartItems = findViewById(R.id.rvCartItems);
        layoutEmptyCart = findViewById(R.id.layoutEmptyCart);
        cardCheckoutSummary = findViewById(R.id.cardCheckoutSummary);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        btnClearCart = findViewById(R.id.btnClearCart);
        btnCartBack = findViewById(R.id.btnCartBack);

        btnCartBack.setOnClickListener(v -> finish());

        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(new CartAdapter.OnCartItemChangeListener() {
            @Override
            public void onQuantityChanged(CartItem item, int newQuantity) {
                cartViewModel.updateQuantity(item.getId(), newQuantity);
            }

            @Override
            public void onDeleteClicked(CartItem item) {
                cartViewModel.deleteItem(item);
            }
        });
        rvCartItems.setAdapter(cartAdapter);

        btnClearCart.setOnClickListener(v -> {
            if (currentCartItems != null && !currentCartItems.isEmpty()) {
                cartViewModel.clearCart();
                Toast.makeText(CartActivity.this, "Đã xóa toàn bộ giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });

        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        cartViewModel.getCartItems().observe(this, cartItems -> {
            currentCartItems = cartItems != null ? cartItems : new ArrayList<>();
            if (currentCartItems.isEmpty()) {
                rvCartItems.setVisibility(View.GONE);
                cardCheckoutSummary.setVisibility(View.GONE);
                layoutEmptyCart.setVisibility(View.VISIBLE);
                btnClearCart.setVisibility(View.GONE);
            } else {
                rvCartItems.setVisibility(View.VISIBLE);
                cardCheckoutSummary.setVisibility(View.VISIBLE);
                layoutEmptyCart.setVisibility(View.GONE);
                btnClearCart.setVisibility(View.VISIBLE);

                cartAdapter.setCartItems(currentCartItems);
                double subtotal = cartViewModel.calculateSubtotal(currentCartItems);
                currentTotalAmount = subtotal + DELIVERY_FEE;

                tvSubtotal.setText(formatter.format(subtotal));
                tvTotalPrice.setText(formatter.format(currentTotalAmount));
            }
        });

        btnPlaceOrder.setOnClickListener(v -> {
            if (currentCartItems.isEmpty()) {
                Toast.makeText(this, "Giỏ hàng của bạn đang trống", Toast.LENGTH_SHORT).show();
                return;
            }

            double subtotal = cartViewModel.calculateSubtotal(currentCartItems);
            Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
            intent.putExtra("EXTRA_SUBTOTAL", subtotal);
            startActivity(intent);
        });
    }
}
