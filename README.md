#  AppQuanLiChiTieuCaNhan


## 🚀 Giai đoạn 1: Xây Dựng Giao Diện Gốc

### I. Giao Diện Màn Hình Trang Chủ
Màn hình trung tâm giúp người dùng có cái nhìn tổng quan về tình hình tài chính cá nhân ngay khi mở ứng dụng.

<p align="center">
  <img src="https://github.com/user-attachments/assets/f03a0c8f-4b4c-47cf-b8bb-2f454a6149a6" alt="Giao diện trang chủ" width="360" />
</p>

* **1.1 Khu vực chào hỏi (`Header`):** Hiển thị lời chào cá nhân hóa theo tài khoản người dùng. Góc phải tích hợp biểu tượng hồ sơ (Profile) giúp nhận diện tài khoản đang đăng nhập.
* **1.2 Thẻ báo cáo tài chính tổng quan :**
    * **Tổng số dư khả dụng:** Hiển thị với font chữ lớn nhất, đại diện cho số tiền thực tế còn lại. Hệ thống tự động tính toán theo công thức: 
        $$\text{Số dư khả dụng} = \text{Tổng Thu} - \text{Tổng Chi}$$
    * **Cột Thu nhập:** Màu xanh lá kèm mũi tên hướng xuống (⬇️), thể hiện tổng số tiền đã kiếm được.
    * **Cột Chi tiêu:** Màu đỏ kèm mũi tên hướng lên (⬆️), thể hiện tổng số tiền đã tiêu xài.
* **1.3 Lịch sử giao dịch trực quan (`RecyclerView`):** Danh sách cuộn hiển thị các khoản thu/chi sắp xếp theo thời gian mới nhất.
    * 🟢 **Thu nhập:** Icon dấu cộng (+), số tiền hiển thị màu xanh lá (Ví dụ: `+20.000 đ`).
    * 🔴 **Chi tiêu:** Icon dấu trừ (-), số tiền hiển thị màu đỏ (Ví dụ: `-20.000 đ`).
    * *Ý nghĩa:* Giúp người dùng phân biệt nhanh chóng các khoản thu chi mà không bị nhầm lẫn.
* **1.4 Thanh điều hướng & Nút thêm nhanh:**
    * **Nút hành động nổi (`+`):** Nằm ở góc phải dưới, chạm nhẹ để mở ngay màn hình nhập liệu.
    * **Thanh điều hướng dưới (Bottom Navigation Bar):** Gồm 4 tab chức năng: *Trang chủ*, *Thống kê biểu đồ*, *Hạn mức ngân sách*, và *Quản lý tài khoản*.

---

### II. Giao Diện Màn Hình Ghi Chép Thu/Chi 
Giao diện nhập liệu thủ công xuất hiện khi người dùng ấn vào nút `+` tại Trang chủ.

<p align="center">
  <img src="https://github.com/user-attachments/assets/73ebf8b3-a974-469a-9358-e8800e6abf9a" alt="Màn hình ghi chép" width="360" />
</p>

* **2.1 Phân loại dòng tiền:** Gồm 2 nút tùy chọn độc duy nhất: **Khoản CHI (-)** và **Khoản THU (+)**, giúp hệ thống phân loại chính xác để thực hiện phép toán cộng/trừ vào ví.
* **2.2 Nhập số tiền phát sinh:** Ô nhập liệu số để người dùng điền số tiền thực tế của giao dịch.
* **2.3 Danh mục phân loại :** Menu lựa chọn nhanh với các danh mục mẫu (Ăn uống, Học tập, Đi lại, Lương, Thưởng...), tự động thả xuống khi chạm.
* **2.4 Ngày tháng phát sinh:** Tích hợp hộp thoại lịch chọn ngày (`DatePickerDialog`). Hệ thống tự động định dạng và điền theo chuẩn `Năm-Tháng-Ngày` (`YYYY-MM-DD`) để lưu trữ chính xác vào SQLite.
* **2.5 Ghi chú thêm:** Ô nhập văn bản tự do cho phép người dùng ghi chú thông tin chi tiết về khoản thu/chi.


## 🔄 Giai đoạn 2: Tính Năng Sửa & Xóa Giao Dịch

### I. Chức Năng Sửa Giao Dịch
Hỗ trợ người dùng thay đổi thông tin khi lỡ nhập sai dữ liệu trước đó.

<p align="center">
  <img src="https://github.com/user-attachments/assets/68598c1f-050d-4881-a8b5-1ee6dc3fd854" alt="Chức năng sửa" width="360" />
</p>

* **1.1 Cơ chế kích hoạt:** Khi người dùng **Chạm nhẹ (Click)** vào một dòng bất kỳ trên danh sách Lịch sử giao dịch, ứng dụng sẽ bật lên một hộp thoại hệ thống `AlertDialog` mang tên **"Chi tiết & Chỉnh sửa"**.
* **1.2 Cơ chế xử lý nút bấm:**
    * `HỦY BỎ`: Đóng hộp thoại ngay lập tức, giữ nguyên dữ liệu gốc.
    * `CẬP NHẬT`: Lấy thông tin mới nhất ghi đè (`UPDATE`) trực tiếp vào dòng dữ liệu có `id` tương ứng trong bảng `thuchi` của SQLite.

---

### II. Chức Năng Xóa Giao Dịch 
Giúp tối ưu hóa dữ liệu và loại bỏ các khoản ghi chép không cần thiết hoặc bị trùng lặp.

<p align="center">
  <img src="https://github.com/user-attachments/assets/faf28139-13db-40f0-98f5-f7a88c0fabac" alt="Chức năng xóa" width="360" />
</p>

* **2.1 Cơ chế kích hoạt:** Khi người dùng **Ấn giữ lâu (Long Click)** vào một dòng giao dịch, hộp thoại `AlertDialog` **"Xác nhận xóa lịch sử"** sẽ xuất hiện. Hệ thống tự động trích xuất tên danh mục của khoản ghi đó để hỏi xác nhận, tránh tình trạng ấn nhầm.
* **2.2 Cơ chế xử lý nút bấm:**
    * `HỦY`: Đóng hộp thoại, an toàn giữ nguyên giao dịch.
    * `XÓA`: Thực thi lệnh `dbHelper.deleteThuChi(id)` xóa vĩnh viễn bản ghi khỏi SQLite.
**Hành động sau khi xóa:** Ứng dụng sẽ tự động tính toán lại dòng tiền (Số dư khả dụng, Tổng Thu, Tổng Chi) trên thẻ báo cáo và cập nhật lại giao diện `RecyclerView`.
---

## 🔄 Giai đoạn 3: Giao diện Ngân Sách Theo Tháng Và Các Chức Năng Của Ngân Sách

### I. Giao diện Ngân Sách

<p align="center">
  <img src="https://github.com/user-attachments/assets/7b24a711-7228-4d92-ae23-e41a32dc0bc2" width="320" alt="Giao diện Ngân Sách">
</p>

* **1.1 Tiêu đề hiển thị thời gian**
  * **Nội dung:** Ngân sách Tháng 5/2026.
  * **Cơ chế:** Được cập nhật tự động bằng cách bốc dữ liệu thời gian thực từ hệ thống giúp phân tách các gói ngân sách theo từng tháng riêng biệt.

* **1.2 Thẻ báo cáo Ngân Sách**
  * **Hạn mức chi tiêu tối đa:** Số tiền mà người dùng đặt ra trước đó để giới hạn bản thân.
  * **📉 Đã chi tiêu:** Tổng tất cả các khoản ghi thuộc loại "CHI" phát sinh trong tháng 5/2026, được truy vấn từ SQLite.
  * **💰 Ví còn lại:** Kết quả của phép tính: `Hạn mức` - `Đã chi tiêu`.

* **1.3 Thanh tiến độ**
  * **Cơ chế:** Giúp người dùng hình dung nhanh tốc độ "đốt tiền" của mình trong tháng, tránh trường hợp người dùng phải suy nghĩ phân tích lâu.

* **1.4 Thông điệp và Cảnh báo chi tiêu**
  * **Nội dung hiển thị theo các trường hợp:**
  
    > 🔴 **Trường hợp Tổng chi > Hạn Mức (Kích hoạt cảnh báo nguy hiểm):**
    > *"⚠️ NGUY HIỂM: Bạn đã chi tiêu vượt hạn mức cho phép.. đ! Hãy thắt chặt chi tiêu ngay!"*
    
    > 🟠 **Trường hợp Tổng chi đạt từ 90% đến 100% Hạn Mức (Kích hoạt cảnh báo chú ý):**
    > *"🍊 CHÚ Ý: Quỹ chi tiêu của bạn đã dùng hết [phanTramTienDo]%. Bạn sắp chạm mức giới hạn cho phép!"*
    
    > 🟢 **Trường hợp Tổng chi dưới 90% Hạn Mức (Kích hoạt cảnh báo an toàn):**
    > *"✅ AN TOÀN: Tình hình tài chính tháng này của bạn rất tốt. Tiếp tục duy trì nhé!"*

* **1.5 Nút bấm "THIẾT LẬP HẠN MỨC"**
  * **Cơ chế:** Nút này cho phép người dùng thay đổi lại con số về hạn mức tháng mong muốn (ví dụ nâng từ 150.000 đ lên con số khác tùy ý).

---

### II. Chức Năng Đặt Hạn Mức Chi Tiêu

<p align="center">
  <img src="https://github.com/user-attachments/assets/d4e90162-ccff-4653-afd3-cfb083e98571" width="320" alt="Chức năng đặt hạn mức">
</p>

* **Cơ chế hoạt động:**
  * Khi nhấn vào nút **"THIẾT LẬP HẠN MỨC"** tại màn hình quản lý ngân sách thì sẽ xuất hiện hộp thoại Thiết lập Hạn mức Ngân sách Tháng. 
  * Tại đây, hệ thống sẽ tự động hiển thị số hạn mức cũ đã lưu gần nhất để người dùng dễ điều chỉnh. 
  * Khi nhấn **"LƯU"**, hệ thống sẽ cập nhật bảng ngân sách trong SQLite theo mốc thời gian Tháng/Năm hiện tại, từ đó cập nhật lại tỷ lệ % trên thanh tiến độ.

-----
## 🔄 Giai đoạn 4: Giao diện Đăng Nhập và Đăng Ký

<p align="center">
<img width="720" height="1612" alt="image" src="https://github.com/user-attachments/assets/eabfa340-f366-4d99-a96f-fb65c1ae9a05" />
</p>

-----

<p align="center">
  <img width="720" height="1612" alt="image" src="https://github.com/user-attachments/assets/e8098889-469e-460d-b028-f4da7d075209" />

</p>

----
## 🔄 Giai đoạn 5: Chỉnh sửa lại giao diện
<p align="center">
 <img width="720" height="1612" alt="image" src="https://github.com/user-attachments/assets/8cf78f3a-7dd3-425b-999c-dafdfa43d2ff" />
</p>

<p align="center">
<img width="720" height="1612" alt="image" src="https://github.com/user-attachments/assets/5a30e6f7-4c96-4d00-87e5-a4af1cde5c27" />

</p>
