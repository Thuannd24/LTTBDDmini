# TÀI LIỆU GIẢI THÍCH TOÀN BỘ DỰ ÁN BTmini

## 1) Thông tin chung
- **Tên dự án:** BTmini
- **Loại dự án:** Ứng dụng Android native (Java)
- **Mục tiêu chính:** Quản lý danh sách phòng trọ (thêm/sửa/xem trạng thái thuê)
- **Package:** `com.example.btmini`
- **Màn hình chính:** `MainActivity`

---

## 2) Mục tiêu nghiệp vụ
Ứng dụng phục vụ bài tập quản lý phòng trọ ở mức cơ bản:
1. Hiển thị danh sách phòng.
2. Mỗi phòng có thông tin: mã phòng, tên phòng, giá, trạng thái thuê, tên người thuê, số điện thoại.
3. Thêm phòng mới bằng hộp thoại nhập liệu.
4. Sửa thông tin phòng hiện có.
5. Phân biệt trực quan phòng còn trống / đã thuê trên danh sách.

---

## 3) Công nghệ và môi trường
### 3.1 Công nghệ sử dụng
- **Ngôn ngữ:** Java 11
- **Android SDK:**
  - `minSdk = 24`
  - `targetSdk = 36`
  - `compileSdk = 36`
- **UI toolkit:** XML + Material Components
- **Build system:** Gradle Kotlin DSL (`.kts`)

### 3.2 Thư viện chính
(Theo `gradle/libs.versions.toml`)
- Android Gradle Plugin: `9.0.1`
- `androidx.appcompat:appcompat:1.7.1`
- `com.google.android.material:material:1.13.0`
- `androidx.activity:activity:1.12.4`
- `androidx.constraintlayout:constraintlayout:2.2.1`
- Test:
  - JUnit `4.13.2`
  - AndroidX Test JUnit `1.3.0`
  - Espresso `3.7.0`

---

## 4) Cấu trúc thư mục và vai trò
```text
LTTBDDmini/
 ├─ app/
 │   ├─ src/main/java/com/example/btmini/
 │   │   ├─ MainActivity.java                # Điều phối UI + nghiệp vụ chính
 │   │   ├─ adapter/RoomAdapter.java         # Adapter cho RecyclerView
 │   │   └─ model/Room.java                  # Model dữ liệu phòng
 │   ├─ src/main/res/layout/
 │   │   ├─ activity_main.xml                # Màn hình danh sách phòng
 │   │   ├─ item_room.xml                    # 1 item phòng
 │   │   └─ dialog_room.xml                  # Form thêm/sửa phòng
 │   ├─ src/main/res/values/
 │   │   ├─ colors.xml                       # Bảng màu
 │   │   ├─ themes.xml                       # Theme sáng
 │   │   └─ strings.xml                      # String resource
 │   ├─ src/main/res/values-night/themes.xml # Theme tối
 │   └─ src/main/AndroidManifest.xml         # Cấu hình app + activity launcher
 ├─ gradle/libs.versions.toml                # Version catalog
 ├─ build.gradle.kts                         # Top-level Gradle
 ├─ settings.gradle.kts                      # Khai báo module
 └─ ...
```

---

## 5) Kiến trúc hiện tại
Dự án đang ở mức kiến trúc **đơn giản theo Activity + Adapter + Model**:
- **Model (`Room`)**: chứa dữ liệu phòng.
- **View (XML layouts)**: mô tả giao diện danh sách, item, dialog.
- **Controller/Logic (`MainActivity`)**:
  - khởi tạo dữ liệu mẫu,
  - xử lý sự kiện thêm/sửa,
  - cập nhật RecyclerView thông qua Adapter.

> Chưa tách tầng Repository, ViewModel hoặc database. Dữ liệu đang nằm trong RAM (`List<Room>`).

---

## 6) Luồng hoạt động chi tiết
### 6.1 Khởi động ứng dụng
1. `AndroidManifest.xml` khai báo `MainActivity` là launcher.
2. Trong `onCreate()`:
   - `setContentView(activity_main)`
   - tạo `roomList` (dữ liệu mẫu)
   - gắn `RoomAdapter` vào `RecyclerView`
   - gắn sự kiện cho FAB để mở dialog thêm phòng.

### 6.2 Hiển thị danh sách phòng
- `RoomAdapter.onBindViewHolder()` nhận từng `Room` và bind:
  - tên phòng,
  - giá (format VNĐ),
  - trạng thái (đã thuê/còn trống),
  - thông tin người thuê (chỉ hiện khi đã thuê).

### 6.3 Thêm phòng
1. Người dùng nhấn FAB.
2. `showRoomDialog(null, -1)` mở dialog.
3. Nhấn nút “Thêm”:
   - đọc dữ liệu từ form,
   - `validateInput()` kiểm tra rỗng,
   - tạo `Room` mới và `add` vào list,
   - gọi `notifyItemInserted()` để cập nhật UI,
   - hiển thị `Toast` xác nhận.

### 6.4 Sửa phòng
1. Người dùng bấm nút “Sửa” (hoặc chạm item).
2. `showRoomDialog(room, position)` mở dialog với dữ liệu có sẵn.
3. Nhấn “Cập nhật”:
   - cập nhật các field trong object `Room`,
   - gọi `notifyItemChanged(position)`,
   - hiện `Toast`.

### 6.5 Xóa phòng
- Interface đã có `onDelete(Room room, int position)`.
- Nút xóa đã có trên item.
- **Nhưng `onDelete` trong `MainActivity` chưa triển khai** (đang để trống).

---

## 7) Mô tả từng lớp chính
## 7.1 `Room.java`
- Thuộc tính:
  - `id`, `name`, `price`, `isRented`, `tenantName`, `phoneNumber`
- Có constructor, getter, setter đầy đủ.
- Dùng `Serializable`.

## 7.2 `RoomAdapter.java`
- Kế thừa `RecyclerView.Adapter<RoomViewHolder>`.
- Quản lý hiển thị danh sách phòng.
- Interface callback `OnRoomActionListener` để Activity xử lý `onEdit`, `onDelete`.
- Dùng `DecimalFormat("###,###,### VNĐ")` cho giá.
- Đổi màu trạng thái thuê bằng `ColorStateList` + màu trong resources.

## 7.3 `MainActivity.java`
- Chịu trách nhiệm điều phối toàn bộ màn hình.
- Khởi tạo dữ liệu mẫu và RecyclerView.
- Mở dialog thêm/sửa phòng.
- Validate dữ liệu cơ bản (không để trống 3 trường chính).
- Cập nhật danh sách bằng notify theo vị trí.

---

## 8) Mô tả giao diện XML
### 8.1 `activity_main.xml`
- Root: `ConstraintLayout`
- Thành phần chính:
  - Header tiêu đề,
  - `RecyclerView` hiển thị danh sách phòng,
  - `FloatingActionButton` để thêm phòng.

### 8.2 `item_room.xml`
- Mỗi item là `MaterialCardView`.
- Hiển thị:
  - tên phòng,
  - trạng thái thuê (chip màu),
  - giá thuê,
  - người thuê,
  - nút “Sửa”, “Xóa”.

### 8.3 `dialog_room.xml`
- Dùng `ScrollView` + `TextInputLayout/TextInputEditText`.
- Trường nhập:
  - mã phòng,
  - tên phòng,
  - giá,
  - checkbox đã thuê,
  - tên người thuê,
  - số điện thoại.

---

## 9) Điểm mạnh hiện tại
1. Cấu trúc đơn giản, dễ hiểu cho đồ án/bài tập.
2. Luồng thêm/sửa hoạt động rõ ràng.
3. UI đã dùng Material components cơ bản.
4. Code ngắn gọn, dễ demo và giải thích.

---

## 10) Hạn chế hiện tại (quan trọng khi thuyết trình)
1. **Chưa lưu dữ liệu bền vững**: thoát app sẽ mất dữ liệu mới thêm/sửa.
2. **Xóa phòng chưa implement** trong `MainActivity`.
3. **Validate còn đơn giản**:
   - chưa kiểm tra trùng mã phòng,
   - chưa kiểm tra định dạng số điện thoại,
   - chưa bắt lỗi số âm/giá trị không hợp lệ chi tiết.
4. Chưa có phân lớp kiến trúc hiện đại (MVVM/Repository).
5. Unit/UI test đang là mẫu mặc định, chưa test nghiệp vụ thực tế.

---

## 11) Đề xuất hướng phát triển tiếp theo
1. Tích hợp Room Database để lưu dữ liệu cục bộ.
2. Hoàn thiện chức năng xóa kèm dialog xác nhận.
3. Tách kiến trúc sang MVVM (ViewModel + LiveData/StateFlow).
4. Bổ sung validate nâng cao và thông báo lỗi theo từng field.
5. Thêm tìm kiếm/lọc theo trạng thái thuê.
6. Viết test cho Adapter và luồng thêm/sửa/xóa.

---

## 12) Hướng dẫn chạy dự án
### 12.1 Yêu cầu
- Android Studio phiên bản mới.
- Android SDK tương thích compileSdk 36.
- JDK 11.

### 12.2 Các bước
1. Mở thư mục dự án `LTTBDDmini` bằng Android Studio.
2. Chờ Gradle sync hoàn tất.
3. Đảm bảo file `local.properties` có dòng:
   - `sdk.dir=/duong_dan_den_Android/Sdk`
4. Chọn thiết bị/emulator.
5. Run app module `app`.

### 12.3 Lỗi thường gặp
- Nếu build báo `SDK location not found`:
  - thêm/cập nhật `local.properties` với `sdk.dir`, hoặc
  - đặt biến môi trường `ANDROID_HOME`.

---

## 13) Kịch bản demo gợi ý khi cô hỏi
1. Mở app, giới thiệu màn hình danh sách và dữ liệu mẫu.
2. Thêm một phòng mới bằng nút `+`.
3. Sửa một phòng đã có để chứng minh cập nhật theo vị trí.
4. Chỉ ra khác biệt trạng thái “Còn trống/Đã thuê”.
5. Nêu rõ hạn chế hiện tại (chưa lưu DB, chưa xóa) và kế hoạch nâng cấp.

---

## 14) Tóm tắt 30 giây
"Đây là ứng dụng Android Java quản lý phòng trọ ở mức CRUD cơ bản (hiện có thêm/sửa/xem, xóa chưa hoàn tất), dùng RecyclerView + Adapter để hiển thị danh sách, dữ liệu hiện lưu trong RAM. Dự án phù hợp mục tiêu học phần vì thể hiện được model, adapter, xử lý sự kiện UI, validate đầu vào và tổ chức tài nguyên Android chuẩn."
