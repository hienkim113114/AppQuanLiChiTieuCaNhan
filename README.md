#  AppQuanLiChiTieuCaNhan


## 🚀 Giai đoạn 1: Xây Dựng Giao Diện Gốc & Bộ Lọc Nhập Liệu

### I. Giao Diện Màn Hình Trang Chủ (Home Screen)
Màn hình trung tâm giúp người dùng có cái nhìn tổng quan về tình hình tài chính cá nhân ngay khi mở ứng dụng.

<p align="center">
  <img src="https://github.com/user-attachments/assets/f03a0c8f-4b4c-47cf-b8bb-2f454a6149a6" alt="Giao diện trang chủ" width="360" />
</p>

* **1.1 Khu vực chào hỏi (`Header`):** Hiển thị lời chào cá nhân hóa theo tài khoản người dùng. Góc phải tích hợp biểu tượng hồ sơ (Profile) giúp nhận diện tài khoản đang đăng nhập.
* **1.2 Thẻ báo cáo tài chính tổng quan (`Dashboard Card`):**
    * **Tổng số dư khả dụng:** Hiển thị với font chữ lớn nhất, đại diện cho số tiền thực tế còn lại. Hệ thống tự động tính toán theo công thức: 
        $$\text{Số dư khả dụng} = \text{Tổng Thu} - \text{Tổng Chi}$$
    * **Cột Thu nhập:** Màu xanh lá kèm mũi tên hướng xuống (⬇️), thể hiện tổng số tiền đã kiếm được.
    * **Cột Chi tiêu:** Màu đỏ kèm mũi tên hướng lên (⬆️), thể hiện tổng số tiền đã tiêu xài.
* **1.3 Lịch sử giao dịch trực quan (`RecyclerView`):** Danh sách cuộn hiển thị các khoản thu/chi sắp xếp theo thời gian mới nhất.
    * 🟢 **Thu nhập:** Icon dấu cộng (+), số tiền hiển thị màu xanh lá (Ví dụ: `+20.000 đ`).
    * 🔴 **Chi tiêu:** Icon dấu trừ (-), số tiền hiển thị màu đỏ (Ví dụ: `-20.000 đ`).
    * *Ý nghĩa:* Giúp người dùng phân biệt nhanh chóng các khoản thu chi mà không bị nhầm lẫn.
* **1.4 Thanh điều hướng & Nút thêm nhanh:**
    * **Nút hành động nổi (FAB - Floating Action Button `+`):** Nằm ở góc phải dưới, chạm nhẹ để mở ngay màn hình nhập liệu.
    * **Thanh điều hướng dưới (Bottom Navigation Bar):** Gồm 4 tab chức năng: *Trang chủ*, *Thống kê biểu đồ*, *Hạn mức ngân sách*, và *Quản lý tài khoản*.

---

### II. Giao Diện Màn Hình Ghi Chép Thu/Chi (Transaction Form)
Giao diện nhập liệu thủ công xuất hiện khi người dùng ấn vào nút `+` tại Trang chủ.

<p align="center">
  <img src="https://github.com/user-attachments/assets/73ebf8b3-a974-469a-9358-e8800e6abf9a" alt="Màn hình ghi chép" width="360" />
</p>

* **2.1 Phân loại dòng tiền:** Gồm 2 nút tùy chọn độc duy nhất: **Khoản CHI (-)** và **Khoản THU (+)**, giúp hệ thống phân loại chính xác để thực hiện phép toán cộng/trừ vào ví.
* **2.2 Nhập số tiền phát sinh:** Ô nhập liệu số (Numeric Input) để người dùng điền số tiền thực tế của giao dịch.
* **2.3 Danh mục phân loại (Category Dropdown):** Menu lựa chọn nhanh với các danh mục mẫu (Ăn uống, Học tập, Đi lại, Lương, Thưởng...), tự động thả xuống khi chạm.
* **2.4 Ngày tháng phát sinh:** Tích hợp hộp thoại lịch chọn ngày (`DatePickerDialog`). Hệ thống tự động định dạng và điền theo chuẩn `Năm-Tháng-Ngày` (`YYYY-MM-DD`) để lưu trữ chính xác vào SQLite.
* **2.5 Ghi chú thêm:** Ô nhập văn bản tự do cho phép người dùng ghi chú thông tin chi tiết về khoản thu/chi.


## 🔄 Giai đoạn 2: Tính Năng Nâng Cao (Sửa & Xóa Giao Dịch)

### I. Chức Năng Sửa Giao Dịch (Edit Transaction)
Hỗ trợ người dùng thay đổi thông tin khi lỡ nhập sai dữ liệu trước đó.

<p align="center">
  <img src="https://github.com/user-attachments/assets/68598c1f-050d-4881-a8b5-1ee6dc3fd854" alt="Chức năng sửa" width="360" />
</p>

* **1.1 Cơ chế kích hoạt:** Khi người dùng **Chạm nhẹ (Click)** vào một dòng bất kỳ trên danh sách Lịch sử giao dịch, ứng dụng sẽ bật lên một hộp thoại hệ thống `AlertDialog` mang tên **"Chi tiết & Chỉnh sửa"**.
* **1.2 Cơ chế xử lý nút bấm:**
    * `HỦY BỎ`: Đóng hộp thoại ngay lập tức, giữ nguyên dữ liệu gốc.
    * `CẬP NHẬT`: Lấy thông tin mới nhất ghi đè (`UPDATE`) trực tiếp vào dòng dữ liệu có `id` tương ứng trong bảng `thuchi` của SQLite.

---

### II. Chức Năng Xóa Giao Dịch (Delete Transaction)
Giúp tối ưu hóa dữ liệu và loại bỏ các khoản ghi chép không cần thiết hoặc bị trùng lặp.

<p align="center">
  <img src="https://github.com/user-attachments/assets/faf28139-13db-40f0-98f5-f7a88c0fabac" alt="Chức năng xóa" width="360" />
</p>

* **2.1 Cơ chế kích hoạt:** Khi người dùng **Ấn giữ lâu (Long Click)** vào một dòng giao dịch, hộp thoại `AlertDialog` **"Xác nhận xóa lịch sử"** sẽ xuất hiện. Hệ thống tự động trích xuất tên danh mục của khoản ghi đó để hỏi xác nhận, tránh tình trạng ấn nhầm.
* **2.2 Cơ chế xử lý nút bấm:**
    * `HỦY`: Đóng hộp thoại, an toàn giữ nguyên giao dịch.
    * `XÓA`: Thực thi lệnh `dbHelper.deleteThuChi(id)` xóa vĩnh viễn bản ghi khỏi SQLite.
> 💡 **Hành động sau khi xóa:** Ứng dụng sẽ tự động tính toán lại dòng tiền (Số dư khả dụng, Tổng Thu, Tổng Chi) trên thẻ báo cáo và cập nhật lại giao diện `RecyclerView` ngay lập tức.


