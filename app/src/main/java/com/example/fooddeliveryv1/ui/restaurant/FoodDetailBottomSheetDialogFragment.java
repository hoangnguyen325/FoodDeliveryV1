package com.example.fooddeliveryv1.ui.restaurant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryv1.R;
import com.example.fooddeliveryv1.data.local.CartItem;
import com.example.fooddeliveryv1.data.local.CartRepository;
import com.example.fooddeliveryv1.data.model.FoodItem;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;

public class FoodDetailBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private static final String ARG_FOOD_ITEM = "ARG_FOOD_ITEM";

    private FoodItem foodItem;
    private int quantity = 1;
    private final DecimalFormat formatter = new DecimalFormat("#,### đ");

    private TextView tvSheetQuantity;
    private MaterialButton btnAddToCartSheet;
    private TextInputEditText etNotes;

    public static FoodDetailBottomSheetDialogFragment newInstance(FoodItem foodItem) {
        FoodDetailBottomSheetDialogFragment fragment = new FoodDetailBottomSheetDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_FOOD_ITEM, foodItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_food_detail_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            foodItem = (FoodItem) getArguments().getSerializable(ARG_FOOD_ITEM);
        }

        if (foodItem == null) {
            dismiss();
            return;
        }

        ImageView imgSheetFood = view.findViewById(R.id.imgSheetFood);
        TextView tvSheetFoodName = view.findViewById(R.id.tvSheetFoodName);
        TextView tvSheetFoodDescription = view.findViewById(R.id.tvSheetFoodDescription);
        TextView tvSheetFoodPrice = view.findViewById(R.id.tvSheetFoodPrice);
        MaterialButton btnSheetMinus = view.findViewById(R.id.btnSheetMinus);
        MaterialButton btnSheetPlus = view.findViewById(R.id.btnSheetPlus);
        tvSheetQuantity = view.findViewById(R.id.tvSheetQuantity);
        btnAddToCartSheet = view.findViewById(R.id.btnAddToCartSheet);
        etNotes = view.findViewById(R.id.etNotes);

        tvSheetFoodName.setText(foodItem.getName());
        tvSheetFoodDescription.setText(foodItem.getDescription());
        tvSheetFoodPrice.setText(formatter.format(foodItem.getPrice()));

        Glide.with(this)
                .load(foodItem.getImageUrl())
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .into(imgSheetFood);

        updateQuantityAndButton();

        btnSheetMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updateQuantityAndButton();
            }
        });

        btnSheetPlus.setOnClickListener(v -> {
            quantity++;
            updateQuantityAndButton();
        });

        btnAddToCartSheet.setOnClickListener(v -> {
            CartRepository repository = new CartRepository(requireActivity().getApplication());
            repository.checkCartRestaurant(existingRestaurantId -> {
                if (existingRestaurantId != null && !existingRestaurantId.equals(foodItem.getRestaurantId())) {
                    if (isAdded() && getActivity() != null) {
                        requireActivity().runOnUiThread(() -> {
                            new AlertDialog.Builder(requireContext())
                                    .setTitle("Xóa giỏ hàng cũ?")
                                    .setMessage("Giỏ hàng của bạn chứa món ăn từ nhà hàng khác. Bạn có muốn xóa giỏ hàng cũ để thêm món này không?")
                                    .setPositiveButton("Đồng ý", (dialog, which) -> {
                                        repository.clearCart();
                                        addCartItem(repository);
                                    })
                                    .setNegativeButton("Hủy", null)
                                    .show();
                        });
                    }
                } else {
                    if (isAdded() && getActivity() != null) {
                        requireActivity().runOnUiThread(() -> addCartItem(repository));
                    }
                }
            });
        });
    }

    private void addCartItem(CartRepository repository) {
        CartItem cartItem = new CartItem(
                foodItem.getId(),
                foodItem.getName(),
                foodItem.getPrice(),
                quantity,
                foodItem.getRestaurantId()
        );
        repository.insert(cartItem);
        Toast.makeText(requireContext(), "Đã thêm " + foodItem.getName() + " vào giỏ hàng", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    private void updateQuantityAndButton() {
        tvSheetQuantity.setText(String.valueOf(quantity));
        double total = foodItem.getPrice() * quantity;
        btnAddToCartSheet.setText("Thêm vào giỏ hàng • " + formatter.format(total));
    }
}
