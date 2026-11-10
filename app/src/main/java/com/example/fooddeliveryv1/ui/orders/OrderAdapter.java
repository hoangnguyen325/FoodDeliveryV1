package com.example.fooddeliveryv1.ui.orders;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddeliveryv1.R;
import com.example.fooddeliveryv1.data.model.Order;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    public interface OnOrderActionListener {
        void onOrderActionClick(Order order);
    }

    private List<Order> orderList = new ArrayList<>();
    private final OnOrderActionListener listener;

    public OrderAdapter(OnOrderActionListener listener) {
        this.listener = listener;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList != null ? orderList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bind(order, listener);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvOrderId;
        private final TextView tvOrderStatus;
        private final TextView tvOrderTotal;
        private final MaterialButton btnOrderAction;
        private final DecimalFormat formatter = new DecimalFormat("#,### đ");

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);
            btnOrderAction = itemView.findViewById(R.id.btnOrderAction);
        }

        public void bind(Order order, OnOrderActionListener listener) {
            tvOrderId.setText("Đơn hàng #" + order.getId());
            tvOrderTotal.setText("Tổng tiền: " + formatter.format(order.getTotalAmount()));

            String status = order.getStatus() != null ? order.getStatus() : "PREPARING";
            switch (status) {
                case "PENDING":
                    tvOrderStatus.setText("Chờ xác nhận");
                    tvOrderStatus.setBackgroundColor(Color.parseColor("#FFF3E0"));
                    tvOrderStatus.setTextColor(Color.parseColor("#F57C00"));
                    btnOrderAction.setText("Theo dõi");
                    break;
                case "PREPARING":
                    tvOrderStatus.setText("Đang chuẩn bị");
                    tvOrderStatus.setBackgroundColor(Color.parseColor("#E3F2FD"));
                    tvOrderStatus.setTextColor(Color.parseColor("#1976D2"));
                    btnOrderAction.setText("Theo dõi");
                    break;
                case "DELIVERING":
                    tvOrderStatus.setText("Đang giao hàng");
                    tvOrderStatus.setBackgroundColor(Color.parseColor("#F3E5F5"));
                    tvOrderStatus.setTextColor(Color.parseColor("#7B1FA2"));
                    btnOrderAction.setText("Theo dõi đơn");
                    break;
                case "COMPLETED":
                default:
                    tvOrderStatus.setText("Đã hoàn thành");
                    tvOrderStatus.setBackgroundColor(Color.parseColor("#E8F5E9"));
                    tvOrderStatus.setTextColor(Color.parseColor("#388E3C"));
                    btnOrderAction.setText("Đặt lại");
                    break;
            }

            btnOrderAction.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onOrderActionClick(order);
                }
            });
        }
    }
}
