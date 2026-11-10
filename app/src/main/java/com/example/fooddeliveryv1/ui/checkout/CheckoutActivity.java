package com.example.fooddeliveryv1.ui.checkout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddeliveryv1.R;
import com.example.fooddeliveryv1.data.model.Order;
import com.example.fooddeliveryv1.ui.tracking.TrackingActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.text.DecimalFormat;

public class CheckoutActivity extends AppCompatActivity {

    private double subtotal = 120000;
    private double shippingFee = 15000;
    private double discount = 0;
    private double total = 135000;

    private TextView tvSubtotalCheckout;
    private TextView tvShippingCheckout;
    private TextView tvDiscountCheckout;
    private TextView tvTotalCheckout;
    private TextView tvAppliedPromo;

    private final DecimalFormat formatter = new DecimalFormat("#,### đ");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        ImageButton btnCheckoutBack = findViewById(R.id.btnCheckoutBack);
        btnCheckoutBack.setOnClickListener(v -> finish());

        tvSubtotalCheckout = findViewById(R.id.tvSubtotalCheckout);
        tvShippingCheckout = findViewById(R.id.tvShippingCheckout);
        tvDiscountCheckout = findViewById(R.id.tvDiscountCheckout);
        tvTotalCheckout = findViewById(R.id.tvTotalCheckout);
        tvAppliedPromo = findViewById(R.id.tvAppliedPromo);
        MaterialCardView cardPromo = findViewById(R.id.cardPromo);
        MaterialButton btnConfirmOrder = findViewById(R.id.btnConfirmOrder);
        RadioGroup rgPaymentMethod = findViewById(R.id.rgPaymentMethod);

        if (getIntent().hasExtra("EXTRA_SUBTOTAL")) {
            subtotal = getIntent().getDoubleExtra("EXTRA_SUBTOTAL", 120000);
            total = subtotal + shippingFee;
        }

        updatePriceUI();

        cardPromo.setOnClickListener(v -> {
            PromoCodeBottomSheet promoSheet = new PromoCodeBottomSheet();
            promoSheet.setOnPromoAppliedListener((code, discountAmount) -> {
                discount = discountAmount;
                total = subtotal + shippingFee - discount;
                if (total < 0) total = 0;
                tvAppliedPromo.setText(code + " (-" + formatter.format(discount) + ")");
                updatePriceUI();
            });
            promoSheet.show(getSupportFragmentManager(), promoSheet.getTag());
        });

        btnConfirmOrder.setOnClickListener(v -> {
            OrderSuccessBottomSheet successSheet = new OrderSuccessBottomSheet();
            successSheet.setOnTrackOrderListener(() -> {
                Order order = new Order(
                        "ORD_" + System.currentTimeMillis(),
                        "user_101",
                        "res_1",
                        total,
                        "PREPARING",
                        10.7769,
                        106.7009
                );
                Intent intent = new Intent(CheckoutActivity.this, TrackingActivity.class);
                intent.putExtra("EXTRA_ORDER", order);
                startActivity(intent);
                finish();
            });
            successSheet.show(getSupportFragmentManager(), successSheet.getTag());
        });
    }

    private void updatePriceUI() {
        tvSubtotalCheckout.setText(formatter.format(subtotal));
        tvShippingCheckout.setText(formatter.format(shippingFee));
        tvDiscountCheckout.setText("-" + formatter.format(discount));
        tvTotalCheckout.setText(formatter.format(total));
    }
}
