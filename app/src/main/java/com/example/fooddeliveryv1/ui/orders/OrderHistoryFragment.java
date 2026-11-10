package com.example.fooddeliveryv1.ui.orders;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddeliveryv1.R;
import com.example.fooddeliveryv1.ui.tracking.TrackingActivity;

public class OrderHistoryFragment extends Fragment {

    private OrderAdapter orderAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvOrders = view.findViewById(R.id.rvOrders);
        rvOrders.setLayoutManager(new LinearLayoutManager(requireContext()));

        orderAdapter = new OrderAdapter(order -> {
            if ("COMPLETED".equals(order.getStatus())) {
                Toast.makeText(requireContext(), "Đặt lại đơn hàng #" + order.getId() + " thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(requireContext(), TrackingActivity.class);
                intent.putExtra("EXTRA_ORDER", order);
                startActivity(intent);
            }
        });
        rvOrders.setAdapter(orderAdapter);

        OrderViewModel orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        orderViewModel.getOrdersLiveData().observe(getViewLifecycleOwner(), orders -> {
            if (orders != null) {
                orderAdapter.setOrderList(orders);
            }
        });
    }
}
