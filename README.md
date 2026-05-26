# AppQuanLiChiTieuCaNhan
Giai đoạn 1
<img width="720" height="1612" alt="image" src="https://github.com/user-attachments/assets/f03a0c8f-4b4c-47cf-b8bb-2f454a6149a6" />
I.Giao diện màn hình trang chủ 
 1.1 Khu vực chào hỏi hiển thịn lời chào với tài khoản người dùng , góc phhair có biểu tượng hồ sơ cá nhân.Ý nghĩa Giúp người dùng nhận diện ngay tài khoản đang đăng nhập.
1.2 Thẻ báo cáo tài chính tổng quan
  Tổng số dư khả dụng: Con số lớn nhất (20.000 đ) thể hiện số tiền thực tế còn lại mà người       dùng  đang có (được tính toán tự động bằng: Tổng Thu - Tổng Chi).
  Phía dưới chia làm 2 cột rõ rệt:
    Cột Thu nhập (Màu xanh lá, có mũi tên hướng xuống) thể hiện tổng số tiền đã kiếm được.
    Cột Chi tiêu (Màu đỏ, có mũi tên hướng lên) thể hiện tổng số tiền đã tiêu xài.
1.3.Lịch sử giao dịch trực quan
  Danh sách cuộn (RecyclerView): Hiển thị các khoản thu/chi phát sinh được sắp xếp theo thời       gian mới nhất (Ví dụ: Khoản được thưởng ngày 2026-05-25).
  Thiết kế phân loại:
    Thu nhập:icon dấu cộng (+) màu xanh lá và hiển thị số tiền cộng (+20.000 đ) .
    Chi tiêu:icon dấu trừ (-) màu đỏ và hiển thị số tiền trừ(-20.000 đ) .
    ý nghĩa: giúp người dùng phân biệt chớp nhoáng với các khoản chi tiêu/thu nhập một cách dễ         dàng mà không bị nhầm lẫn.
1.4.Thanh điều hướng và nút thêm giao dịch 
  Nút bấm nổi (+):Nằm ở góc phải,chỉ cần chạm nhẹ, người dùng sẽ mở ngay màn hình nhập liệu để thêm nhanh một khoản Thu hoặc Chi mới.
  Thanh điều hướng dưới (Bottom Navigation Bar): Gồm 4 Tab chức năng để chuyển đổi nhanh qua các phân hệ khác của app: Trang chủ, Thống kê biểu đồ, Hạn mức ngân sách, và Quản lý tài khoản.
  

II.Giao diện màn Hình ghi chép thu/chi
  Đây là giao diện nhập liệu thủ công (Form) khi người dùng bấm vào nút hành động nổi (+) ở Trang chủ
<img width="720" height="1612" alt="image" src="https://github.com/user-attachments/assets/73ebf8b3-a974-469a-9358-e8800e6abf9a" />

2.1.Phân loại dòng tiền 
  Gồm hai nút tùy chọn độc nhất: Khoản CHI (-) và Khoản THU (+).Giúp hệ thống xác định bản ghi thuộc loại hình nào để thực hiện phép toán cộng/trừ vào tổng số dư của ví.
2.2.Nhập số tiền phát sinh
  Người dùng tự nhập số tiền thực tế của giao dịch
2.3.Danh mục phân loại
  Menu lựa chọn nhanh,khi chạm vào, một danh sách các danh mục mẫu (Ăn uống, Học tập, Đi lại,     Lương, Thưởng...) sẽ thả xuống để người dùng chọn nhanh.
2.4.Ngày tháng phát sinh giao dịch
  Khi người dùng chạm vào ô này, ứng dụng sẽ không bắt họ gõ lịch mà tự động mở ra một Hộp thoại lịch đểchọn ngày. Hệ thống sau đó tự động định dạng và điền vào ô theo chuẩn Năm-Tháng-Ngày để lưu chính xác vào SQLite.
2.5.Ghi chú thêm
  Cho phép người dùng nhập nội dung cụ thể về khoản thu/chi
Giai đoạn 2 :
I.chức năng sửa giao dịch
<img width="720" height="1612" alt="image" src="https://github.com/user-attachments/assets/68598c1f-050d-4881-a8b5-1ee6dc3fd854" />

1.1.Cơ chế kích hoạt chức năng
Khi người dùng thực hiện hành vi Chạm nhẹ vào một dòng bất kỳ trên danh sách Lịch sử giao dịch, ứng dụng sẽ  khởi tạo và bật lên một hộp thoại hệ thống (AlertDialog) mang tên "Chi tiết & Chỉnh sửa".Hộp thoại này đóng vai trò hiển thị và chỉnh sửa , ở đây người dùng có thể chỉnh sửa lại thông tin giao dịch nếu lỡ nhập sai thông tin trước đó .
1.2.Cơ chế xử lý của hai nút hành động
Nút "HỦY BỎ": Đóng hộp thoại ngay lập tức, giữ nguyên toàn bộ dữ liệu ban đầu và không thực hiện bất kỳ thay đổi nào dưới cơ sở dữ liệu.
Nút "CẬP NHẬT": hệ thống sẽ lấy thoonng tin mới nhất mà người dùng vừa sửa ghi đè (Update) trực tiếp vào dòng dữ liệu có id tương ứng trong bảng thuchi của SQLite.

II.chức năng xóa giao dịch
<img width="720" height="1612" alt="image" src="https://github.com/user-attachments/assets/faf28139-13db-40f0-98f5-f7a88c0fabac" />
2.1.Cơ chế kích hoạt chức năng
Khi người dùng Ấn giữ lâu (Long Click) vào một dòng bất kỳ trên danh sách Lịch sử giao dịch, ứng dụng sẽ lập tức kích hoạt và hiển thị Hộp thoại AlertDialog mang tên "Xác nhận xóa lịch sử".Hệ thống tự động bốc tên danh mục của khoản ghi đó và hỏi xác nhận người dùng có thực sự muốn xóa không. Việc này giúp người dùng kiểm tra lại chính xác mục mình định xóa, tránh tình trạng ấn nhầm.
2.2.Cơ chế xử lý của hai nút hành động
Nút "HỦY": Đóng hộp thoại, giữ nguyên giao dịch và không làm thay đổi dữ liệu.
Nút "XÓA": Hệ thống thực thi lệnh dbHelper.deleteThuChi(id) để xóa vĩnh viễn bản ghi khỏi SQLite. Ngay sau đó, app tự động tính toán lại dòng tiền: số dư khả dụng, tổng thu/chi trên thẻ báo cáo , và dòng lịch sử đó cũng biến mất khỏi danh sách.



