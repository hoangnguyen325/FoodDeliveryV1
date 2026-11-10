package com.example.fooddeliveryv1.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fooddeliveryv1.R;
import com.example.fooddeliveryv1.data.model.User;
import com.example.fooddeliveryv1.data.repository.AuthRepository;
import com.example.fooddeliveryv1.ui.auth.LoginActivity;
import com.google.android.material.button.MaterialButton;

public class ProfileFragment extends Fragment {

    private AuthRepository authRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        authRepository = new AuthRepository(requireContext());

        TextView tvProfileName = view.findViewById(R.id.tvProfileName);
        TextView tvProfilePhone = view.findViewById(R.id.tvProfilePhone);
        TextView tvProfileAddress = view.findViewById(R.id.tvProfileAddress);
        MaterialButton btnLogout = view.findViewById(R.id.btnLogout);

        User currentUser = authRepository.getCurrentUser();
        if (currentUser != null) {
            tvProfileName.setText(currentUser.getName());
            tvProfilePhone.setText(currentUser.getPhone());
            tvProfileAddress.setText(currentUser.getAddress());
        }

        btnLogout.setOnClickListener(v -> {
            authRepository.logout();
            Toast.makeText(requireContext(), "Đã đăng xuất", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}
