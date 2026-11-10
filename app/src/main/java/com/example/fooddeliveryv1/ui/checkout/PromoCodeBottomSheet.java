package com.example.fooddeliveryv1.ui.checkout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fooddeliveryv1.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class PromoCodeBottomSheet extends BottomSheetDialogFragment {

    public interface OnPromoAppliedListener {
        void onPromoApplied(String code, double discountAmount);
    }

    private OnPromoAppliedListener listener;

    public void setOnPromoAppliedListener(OnPromoAppliedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_promo_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextInputEditText etPromoCode = view.findViewById(R.id.etPromoCode);
        MaterialButton btnApplyPromo = view.findViewById(R.id.btnApplyPromo);

        btnApplyPromo.setOnClickListener(v -> {
            String code = etPromoCode.getText() != null ? etPromoCode.getText().toString().trim().toUpperCase() : "";
            double discount = 0;

            if ("FREESHIP".equals(code)) {
                discount = 15000;
            } else if ("GIAMGIA20K".equals(code)) {
                discount = 20000;
            } else {
                Toast.makeText(requireContext(), "Mã giảm giá không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(requireContext(), "Áp dụng mã thành công!", Toast.LENGTH_SHORT).show();
            if (listener != null) {
                listener.onPromoApplied(code, discount);
            }
            dismiss();
        });
    }
}
