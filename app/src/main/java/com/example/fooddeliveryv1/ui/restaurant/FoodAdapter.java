package com.example.fooddeliveryv1.ui.restaurant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryv1.R;
import com.example.fooddeliveryv1.data.model.FoodItem;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    public interface OnAddToCartClickListener {
        void onAddToCart(FoodItem foodItem);
    }

    private List<FoodItem> foodList = new ArrayList<>();
    private final OnAddToCartClickListener listener;

    public FoodAdapter(OnAddToCartClickListener listener) {
        this.listener = listener;
    }

    public void setFoodList(List<FoodItem> foodList) {
        this.foodList = foodList != null ? foodList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem foodItem = foodList.get(position);
        holder.bind(foodItem, listener);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgFood;
        private final TextView tvFoodName;
        private final TextView tvFoodDescription;
        private final TextView tvFoodPrice;
        private final MaterialButton btnAddToCart;
        private final DecimalFormat formatter = new DecimalFormat("#,### đ");

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imgFood);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            tvFoodDescription = itemView.findViewById(R.id.tvFoodDescription);
            tvFoodPrice = itemView.findViewById(R.id.tvFoodPrice);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }

        public void bind(FoodItem foodItem, OnAddToCartClickListener listener) {
            tvFoodName.setText(foodItem.getName());
            tvFoodDescription.setText(foodItem.getDescription());
            tvFoodPrice.setText(formatter.format(foodItem.getPrice()));

            Glide.with(itemView.getContext())
                    .load(foodItem.getImageUrl())
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .into(imgFood);

            btnAddToCart.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAddToCart(foodItem);
                }
            });
        }
    }
}
