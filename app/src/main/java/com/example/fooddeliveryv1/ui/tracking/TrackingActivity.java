package com.example.fooddeliveryv1.ui.tracking;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddeliveryv1.R;
import com.example.fooddeliveryv1.data.model.Order;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;

public class TrackingActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String EXTRA_ORDER = "EXTRA_ORDER";

    private GoogleMap googleMap;
    private Marker driverMarker;
    private Marker destinationMarker;

    private LatLng currentDriverPosition;
    private final LatLng destinationPosition = new LatLng(10.7769, 106.7009); // Default: Q1 TP.HCM

    private ValueAnimator markerAnimator;
    private Handler simulationHandler;
    private Runnable simulationRunnable;
    private int simulationStep = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        Order order = (Order) getIntent().getSerializableExtra(EXTRA_ORDER);
        if (order != null && order.getDriverLat() != 0 && order.getDriverLng() != 0) {
            currentDriverPosition = new LatLng(order.getDriverLat(), order.getDriverLng());
        } else {
            currentDriverPosition = new LatLng(10.7720, 106.6940); // Initial start point
        }

        ImageButton btnBack = findViewById(R.id.btnTrackingBack);
        btnBack.setOnClickListener(v -> finish());

        MaterialButton btnSimulate = findViewById(R.id.btnSimulateDriver);
        btnSimulate.setOnClickListener(v -> startDriverSimulation());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        this.googleMap = map;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);

        // Add Destination Marker
        destinationMarker = googleMap.addMarker(new MarkerOptions()
                .position(destinationPosition)
                .title("Điểm giao hàng")
                .snippet("123 Nguyễn Huệ, Quận 1")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        // Add Driver Marker
        driverMarker = googleMap.addMarker(new MarkerOptions()
                .position(currentDriverPosition)
                .title("Tài xế (Nguyễn Văn Tài)")
                .snippet("Đang di chuyển đến bạn...")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        // Adjust camera to view both points
        adjustCameraBounds();
    }

    /**
     * Smoothly moves the driver marker and camera to new (lat, lng) coordinates
     */
    public void updateDriverLocation(double newLat, double newLng) {
        if (googleMap == null || driverMarker == null) return;

        LatLng startPosition = driverMarker.getPosition();
        LatLng endPosition = new LatLng(newLat, newLng);

        if (markerAnimator != null && markerAnimator.isRunning()) {
            markerAnimator.cancel();
        }

        markerAnimator = ValueAnimator.ofFloat(0f, 1f);
        markerAnimator.setDuration(1500); // 1.5s smooth transition
        markerAnimator.setInterpolator(new LinearInterpolator());
        markerAnimator.addUpdateListener(animation -> {
            float v = animation.getAnimatedFraction();
            double lat = v * endPosition.latitude + (1 - v) * startPosition.latitude;
            double lng = v * endPosition.longitude + (1 - v) * startPosition.longitude;
            LatLng current = new LatLng(lat, lng);

            driverMarker.setPosition(current);

            // Compute bearing angle for marker rotation
            float bearing = calculateBearing(startPosition, endPosition);
            if (bearing != 0) {
                driverMarker.setRotation(bearing);
            }

            // Animate camera to follow driver smoothly
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(current));
        });

        markerAnimator.start();
        currentDriverPosition = endPosition;
    }

    private void adjustCameraBounds() {
        if (googleMap == null) return;

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(currentDriverPosition);
        builder.include(destinationPosition);

        LatLngBounds bounds = builder.build();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 120));
    }

    private float calculateBearing(LatLng start, LatLng end) {
        double lat1 = Math.toRadians(start.latitude);
        double lng1 = Math.toRadians(start.longitude);
        double lat2 = Math.toRadians(end.latitude);
        double lng2 = Math.toRadians(end.longitude);

        double dLng = lng2 - lng1;
        double y = Math.sin(dLng) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLng);

        double bearing = Math.atan2(y, x);
        return (float) (Math.toDegrees(bearing) + 360) % 360;
    }

    private void startDriverSimulation() {
        Toast.makeText(this, "Bắt đầu mô phỏng tài xế di chuyển...", Toast.LENGTH_SHORT).show();
        simulationStep = 0;
        simulationHandler = new Handler(Looper.getMainLooper());

        final LatLng[] routePoints = new LatLng[]{
                new LatLng(10.7720, 106.6940),
                new LatLng(10.7732, 106.6955),
                new LatLng(10.7745, 106.6970),
                new LatLng(10.7758, 106.6988),
                new LatLng(10.7769, 106.7009) // Arrived at destination
        };

        simulationRunnable = new Runnable() {
            @Override
            public void run() {
                if (simulationStep < routePoints.length) {
                    LatLng point = routePoints[simulationStep];
                    updateDriverLocation(point.latitude, point.longitude);
                    simulationStep++;
                    if (simulationStep < routePoints.length) {
                        simulationHandler.postDelayed(this, 2000);
                    } else {
                        Toast.makeText(TrackingActivity.this, "Tài xế đã đến điểm giao hàng!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        };

        simulationHandler.post(simulationRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (simulationHandler != null && simulationRunnable != null) {
            simulationHandler.removeCallbacks(simulationRunnable);
        }
        if (markerAnimator != null && markerAnimator.isRunning()) {
            markerAnimator.cancel();
        }
    }
}
