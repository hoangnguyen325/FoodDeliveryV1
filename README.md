# 🍔 Food Delivery Android Application (FoodDeliveryV1)

Ứng dụng di động Android đặt đồ ăn trực tuyến (**Food Delivery**) được xây dựng hoàn toàn bằng **100% Java**, áp dụng mô hình kiến trúc **MVVM (Model - View - ViewModel)** kết hợp **Clean Architecture** cơ bản và các thư viện hiện đại của Android Jetpack.

---

## 🛠 1. Công nghệ & Quy tắc kỹ thuật bắt buộc
- **Ngôn ngữ:** 100% Java (Không sử dụng Kotlin).
- **Kiến trúc:** MVVM (Model - View - ViewModel) + Repository Pattern + LiveData.
- **UI/UX:** Material Design 3, ConstraintLayout, RecyclerView, BottomNavigationView, BottomSheetDialogFragment, SwipeRefreshLayout.
- **Local Database:** Room Database (quản lý giỏ hàng cục bộ và đồng bộ dữ liệu đa luồng bằng ExecutorService).
- **Networking:** Retrofit 2 + Gson + OkHttp (HttpLoggingInterceptor).
- **Bản đồ & Định vị:** Google Maps Android SDK (Tích hợp Marker động, xoay hướng di chuyển mượt mà, mô phỏng thời gian thực).
- **Xác thực & Bảo mật:** SharedPreferencesManager lưu trữ phiên đăng nhập và JWT Token giả lập.

---

## 📁 2. Cấu trúc Package mẫu
```text
com.example.fooddeliveryv1
│
├── data
│   ├── local          # Room Database (AppDatabase, CartDao, CartItem, SharedPreferencesManager)
│   ├── model          # POJO classes (User, Restaurant, FoodItem, Order)
│   ├── remote         # Retrofit API Service & ApiClient singleton
│   └── repository     # RestaurantRepository & AuthRepository
│
└── ui
    ├── auth           # LoginActivity & RegisterActivity
    ├── cart           # CartActivity & CartAdapter, CartViewModel
    ├── checkout       # CheckoutActivity, PromoCodeBottomSheet, OrderSuccessBottomSheet
    ├── home           # HomeFragment, RestaurantAdapter, CategoryAdapter, HomeViewModel
    ├── orders         # OrderHistoryFragment, OrderAdapter, OrderViewModel
    ├── profile        # ProfileFragment
    ├── restaurant     # RestaurantDetailActivity, FoodAdapter, FoodDetailBottomSheetDialogFragment
    ├── search         # SearchFragment, SearchViewModel
    └── tracking       # TrackingActivity (Google Maps integration)
```

---

## ✨ 3. Các tính năng cốt lõi đã triển khai

### 🔐 3.1. Xác thực & Hồ sơ cá nhân (Auth & Profile)
- **LoginActivity & RegisterActivity:** Giao diện đăng nhập/đăng ký hiện đại sử dụng Material `TextInputLayout` với tính năng ẩn/hiện mật khẩu.
- **AuthRepository & SharedPreferencesManager:** Lưu trữ phiên đăng nhập (`JWT Token`) và thông tin người dùng an toàn. Tự động chuyển hướng sang màn hình Đăng nhập nếu chưa xác thực, hoặc tự động đăng nhập tài khoản Demo khi mở ứng dụng để trải nghiệm mượt mà.
- **ProfileFragment:** Hiển thị thông tin cá nhân, địa chỉ giao hàng mặc định và nút **Đăng xuất**.

### 🏠 3.2. Trang chủ & Khám phá nhà hàng (Home & Categories)
- **HomeFragment & HomeViewModel:** Hiển thị danh sách nhà hàng nổi bật kèm hình ảnh tải qua thư viện **Glide**.
- **Horizontal Categories:** Thanh danh mục ngang dạng Chip (`Tất cả`, `Cơm`, `Phở & Mì`, `Đồ uống`, `Fast Food`).
- **SwipeRefreshLayout:** Hỗ trợ kéo xuống để làm mới dữ liệu (`Pull-to-Refresh`).

### 🔍 3.3. Tìm kiếm & Lọc thời gian thực (Search)
- **SearchFragment & SearchViewModel:** Tích hợp `SearchView` cho phép tìm kiếm và lọc tức thì danh sách nhà hàng và món ăn theo tên hoặc địa chỉ.

### 🍜 3.4. Chi tiết nhà hàng & Thực đơn (Restaurant Detail & Cart Validation)
- **RestaurantDetailActivity & FoodAdapter:** Hiển thị thông tin nhà hàng và thực đơn món ăn phong phú.
- **FoodDetailBottomSheetDialogFragment:** BottomSheet chọn số lượng món ăn (`+`/`-`) và nhập ghi chú riêng cho nhà hàng.
- **Cross-Restaurant Cart Validation:** Tự động kiểm tra giỏ hàng; nếu thêm món từ nhà hàng khác, hiển thị `AlertDialog` cảnh báo người dùng xác nhận xóa giỏ hàng cũ để tránh xung đột dữ liệu.

### 🛒 3.5. Giỏ hàng & Thanh toán (Cart & Checkout UX)
- **CartActivity:** Quản lý danh sách món trong giỏ hàng, cập nhật số lượng trực tiếp, tính toán tổng tiền tạm tính và phí giao hàng (15.000đ).
- **CheckoutActivity:** Lựa chọn phương thức thanh toán (COD, MoMo E-Wallet, Thẻ ngân hàng).
- **PromoCodeBottomSheet:** Áp dụng mã giảm giá ưu đãi (`FREESHIP`, `GIAMGIA20K`) và tự động khấu trừ vào tổng tiền thanh toán.
- **OrderSuccessBottomSheet:** Thông báo đặt hàng thành công với hiệu ứng trực quan trước khi chuyển đến màn hình theo dõi đơn hàng.

### 🗺 3.6. Theo dõi đơn hàng thời gian thực (Google Maps Tracking)
- **TrackingActivity:** Tích hợp `SupportMapFragment` hiển thị bản đồ Google Maps.
- **Live Driver Simulation & Animation:** Định vị điểm giao hàng và vị trí tài xế. Cung cấp hàm `updateDriverLocation(lat, lng)` kết hợp `ValueAnimator` và `LinearInterpolator` giúp Marker tài xế trượt mượt mà trên bản đồ kèm xoay hướng xe (`Bearing`), cùng tính năng **Mô phỏng di chuyển** trực tiếp.

### 📋 3.7. Lịch sử & Trạng thái đơn hàng (Order History & Polling)
- **OrderHistoryFragment & OrderAdapter:** Quản lý danh sách đơn hàng active và lịch sử quá khứ với các Badge trạng thái trực quan (*Chờ xác nhận, Đang chuẩn bị, Đang giao hàng, Đã hoàn thành*).
- **Real-time Polling:** Sử dụng `ScheduledExecutorService` định kỳ cập nhật trạng thái đơn hàng và mô phỏng lộ trình giao hàng ngầm.

---

## 🚀 4. Hướng dẫn cài đặt & Chạy ứng dụng
1. Clone hoặc tải mã nguồn dự án về máy.
2. Mở dự án bằng **Android Studio**.
3. Cập nhật Google Maps API Key của bạn trong tệp [AndroidManifest.xml](file:///D:/FoodDeliveryV1/app/src/main/AndroidManifest.xml):
   ```xml
   <meta-data
       android:name="com.google.android.geo.API_KEY"
       android:value="YOUR_GOOGLE_MAPS_API_KEY" />
   ```
4. Thực hiện **Gradle Sync** và chạy ứng dụng trên máy ảo (Emulator) hoặc thiết bị Android thực tế.
