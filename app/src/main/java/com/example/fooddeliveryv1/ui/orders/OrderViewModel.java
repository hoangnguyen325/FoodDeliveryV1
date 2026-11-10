package com.example.fooddeliveryv1.ui.orders;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fooddeliveryv1.data.model.Order;
import com.example.fooddeliveryv1.data.remote.ApiClient;
import com.example.fooddeliveryv1.data.remote.ApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OrderViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Order>> ordersLiveData = new MutableLiveData<>();
    private final ApiService apiService;
    private ScheduledExecutorService scheduler;

    public OrderViewModel(@NonNull Application application) {
        super(application);
        apiService = ApiClient.getApiService();
        loadMockOrInitialOrders();
        startPollingOrderStatus();
    }

    public LiveData<List<Order>> getOrdersLiveData() {
        return ordersLiveData;
    }

    private void loadMockOrInitialOrders() {
        List<Order> mockOrders = new ArrayList<>();
        mockOrders.add(new Order("ORD_170912345", "user_101", "res_1", 135000, "DELIVERING", 10.7745, 106.6970));
        mockOrders.add(new Order("ORD_170911222", "user_101", "res_2", 95000, "PREPARING", 10.7738, 106.6983));
        mockOrders.add(new Order("ORD_170910000", "user_101", "res_3", 210000, "COMPLETED", 10.7825, 106.6981));
        ordersLiveData.setValue(mockOrders);
    }

    private void startPollingOrderStatus() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleWithFixedDelay(() -> {
            // Periodic polling every 10 seconds to auto-update order status
            List<Order> current = ordersLiveData.getValue();
            if (current != null && !current.isEmpty()) {
                // Simulate status progression for demo
                for (Order o : current) {
                    if ("PREPARING".equals(o.getStatus())) {
                        o.setStatus("DELIVERING");
                    } else if ("DELIVERING".equals(o.getStatus())) {
                        // simulate driver movement
                        o.setDriverLat(o.getDriverLat() + 0.001);
                        o.setDriverLng(o.getDriverLng() + 0.001);
                    }
                }
                ordersLiveData.postValue(current);
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}
