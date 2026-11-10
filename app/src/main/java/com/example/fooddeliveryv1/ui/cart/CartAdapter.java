package com.example.fooddeliveryv1.ui.cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddeliveryv1.R;
import com.example.fooddeliveryv1.data.local.CartItem;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public interface OnCartItemChangeListener {
        void onQuantityChanged(CartItem item, int newQuantity);
        void onDeleteClicked(CartItem item);
    }

    private List<CartItem> cartItems = new ArrayList<>();
    private final OnCartItemChangeListener listener;

    public CartAdapter(OnCartItemChangeListener listener) {
        this.listener = listener;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems != null ? cartItems : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCartFoodName;
        private final TextView tvCartFoodPrice;
        private final TextView tvQuantity;
        private final MaterialButton btnMinus;
        private final MaterialButton btnPlus;
        private final ImageButton btnDelete;
        private final DecimalFormat formatter = new DecimalFormat("#,### đ");

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCartFoodName = itemView.findViewById(R.id.tvCartFoodName);
            tvCartFoodPrice = itemView.findViewById(R.id.tvCartFoodPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(CartItem item, OnCartItemChangeListener listener) {
            tvCartFoodName.setText(item.getFoodName());
            double subtotal = item.getPrice() * item.getQuantity();
            tvCartFoodPrice.setText(formatter.format(subtotal));
            tvQuantity.setText(String.valueOf(item.getQuantity()));

            btnMinus.setOnClickListener(v -> {
                if (listener != null) {
                    int newQty = item.getQuantity() - 1;
                    if (newQty <= 0) {
                        listener.onDeleteClicked(item);
                    } else {
                        listener.onQuantityChanged(item, newQty);
                    }
                }
            });

            btnPlus.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onQuantityChanged(item, item.getQuantity() + 1);
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClicked(item);
                }
            });
        }
    }
}
