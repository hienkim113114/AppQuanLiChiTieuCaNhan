package KimHien.appquanlychitieucanhan;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TrangChuFragment extends Fragment {

    private TextView txtTongSoDu, txtTongThu, txtTongChi;
    private TextView txtTenNguoiDung;
    private RecyclerView rcvLichSu;
    private DatabaseHelper dbHelper;
    private ThuChiAdapter adapter;
    private List<ThuChiModel> danhSachThuChi;
    private String maNguoiDung = "offline_user";
    private ShapeableImageView imgAvatar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trang_chu, container, false);

        dbHelper = new DatabaseHelper(getContext());
        txtTongSoDu = view.findViewById(R.id.txt_tong_so_du);
        txtTongThu = view.findViewById(R.id.txt_tong_thu);
        txtTongChi = view.findViewById(R.id.txt_tong_chi);
        rcvLichSu = view.findViewById(R.id.rcv_lich_su);
        txtTenNguoiDung = view.findViewById(R.id.txt_ten_nguoi_dung);


        rcvLichSu.setLayoutManager(new LinearLayoutManager(getContext()));

        danhSachThuChi = new ArrayList<>();
        adapter = new ThuChiAdapter(getContext(), danhSachThuChi);
        rcvLichSu.setAdapter(adapter);

        imgAvatar =view.findViewById(R.id.img_avatar);


        com.google.android.material.floatingactionbutton.FloatingActionButton fabAdd = view.findViewById(R.id.fab_trangchu_add);

        fabAdd.setOnClickListener(v -> {
            android.content.Intent intent = new Intent(getContext(), GhiChepThuChiActivity.class);

            //Lấy trực tiếp ID từ arguments ngay khi click
            String idThat = "offline_user";
            if (getArguments() != null) {
                idThat = getArguments().getString("USER_ID", "offline_user");
            }

            intent.putExtra("USER_ID", idThat);
            startActivity(intent);
        });
        // Đón nhận sự kiện từ Adapter
        adapter.setOnItemClickListener(new ThuChiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ThuChiModel item, int position) {
                hienThiHopThoaiXemVaSua(item);
            }

            @Override
            public void onItemLongClick(ThuChiModel item, int position) {
                xacNhanXoa(item);
            }
        });
        return view;
    }
    private void loadAvatar() {

        String avatarPath = requireContext()
                .getSharedPreferences("USER_DATA",0)
                .getString(
                        "avatar_" + maNguoiDung,
                        ""
                );

        if (!avatarPath.isEmpty()) {

            imgAvatar.setImageURI(
                    Uri.fromFile(new java.io.File(avatarPath))
            );

        } else {

            imgAvatar.setImageResource(R.drawable.img_2);
        }
    }
    private void loadThongTinNguoiDung() {

        if (maNguoiDung.equals("offline_user")) {
            txtTenNguoiDung.setText("Khách");
            return;
        }

        Cursor cursor = dbHelper.getThongTinNguoiDung(maNguoiDung);

        if (cursor != null && cursor.moveToFirst()) {

            String hoTen = cursor.getString(  cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOTEN) );
            txtTenNguoiDung.setText(hoTen);
            cursor.close();
        }
    }
    // Hộp thoại xem và sửa trực tiếp
    private void hienThiHopThoaiXemVaSua(ThuChiModel item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Chi tiết & Chỉnh sửa");

        // Các ô nhập liệu và điền sẵn dữ liệu cũ để người dùng vừa xem vừa sửa
        final EditText edtSuaSoTien = new EditText(getContext());
        edtSuaSoTien.setHint("Số tiền");
        edtSuaSoTien.setText(String.valueOf((int) item.getSoTien()));
        edtSuaSoTien.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

        final EditText edtSuaGhiChu = new EditText(getContext());
        edtSuaGhiChu.setHint("Ghi chú");
        edtSuaGhiChu.setText(item.getGhiChu());

        // Hiện thêm thông tin Danh mục và Ngày
        TextView txtThongTinPhu = new TextView(getContext());
        txtThongTinPhu.setText("Danh mục: " + item.getTenDanhMuc() + " | Ngày: " + item.getNgay());
        txtThongTinPhu.setPadding(10, 0, 10, 20);
        txtThongTinPhu.setTextSize(14);

        LinearLayout layoutPopup = new LinearLayout(getContext());
        layoutPopup.setOrientation(LinearLayout.VERTICAL);
        layoutPopup.setPadding(50, 40, 50, 30);
        layoutPopup.addView(txtThongTinPhu);
        layoutPopup.addView(edtSuaSoTien);
        layoutPopup.addView(edtSuaGhiChu);
        builder.setView(layoutPopup);

        // Bấm nút cập nhật sẽ lưu lại các thay đổi
        builder.setPositiveButton("CẬP NHẬT", (dialog, which) -> {
            String chuoiSoTien = edtSuaSoTien.getText().toString().trim();
            String ghiChuMoi = edtSuaGhiChu.getText().toString().trim();

            if (chuoiSoTien.isEmpty()) {
                Toast.makeText(getContext(), "Số tiền không được bỏ trống!", Toast.LENGTH_SHORT).show();
                return;
            }

            double soTienMoi = Double.parseDouble(chuoiSoTien);

            // Ghi đè dữ liệu mới xuống SQLite
            dbHelper.updateThuChi(item.getId(), item.getLoai(), soTienMoi, item.getMaDanhMuc(), item.getNgay(), ghiChuMoi);
            Toast.makeText(getContext(), "Đã cập nhật thay đổi!", Toast.LENGTH_SHORT).show();

            docDuLieuTuSQLite(); // Làm mới giao diện Trang Chủ
        });

        builder.setNegativeButton("HỦY BỎ", null);
        builder.show();
    }
    //HỘP THOẠI XÁC NHẬN XÓA
    private void xacNhanXoa(ThuChiModel item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xác nhận xóa lịch sử");
        builder.setMessage("Bạn có chắc chắn muốn xóa giao dịch '" + item.getTenDanhMuc() + "' này không?");

        builder.setPositiveButton("XÓA", (dialog, which) -> {
            dbHelper.deleteThuChi(item.getId());
            Toast.makeText(getContext(), "Đã xóa khoản ghi thành công!", Toast.LENGTH_SHORT).show();
            docDuLieuTuSQLite();
        });

        builder.setNegativeButton("HỦY", null);
        builder.show();
    }
    @Override
    public void onResume() {
        super.onResume();
        //Nhận mã người dùng thật từ MainActivity gửi xuống
        if (getArguments() != null) {
            maNguoiDung = getArguments().getString("USER_ID", "offline_user");
        }

        loadThongTinNguoiDung();
        loadAvatar();
        // Mỗi khi quay lại tab Trang Chủ, tải lại dữ liệu mới nhất
        docDuLieuTuSQLite();
    }

    private void docDuLieuTuSQLite() {
        danhSachThuChi.clear();
        double tongThu = 0;
        double tongChi = 0;

        //  Lấy tháng và năm hiện tại để lọc dữ liệu
        java.util.Calendar lichHeThong = java.util.Calendar.getInstance();
        int namHienTai = lichHeThong.get(java.util.Calendar.YEAR);
        int thangHienTai = lichHeThong.get(java.util.Calendar.MONTH) + 1;

        String chuoiThangNamHienTai = String.format("%d-%02d", namHienTai, thangHienTai);

        // Gọi hàm lấy toàn bộ khoản thu chi của user từ DatabaseHelper
        Cursor cursor = dbHelper.getAllThuChiByUser(maNguoiDung);

        if (cursor != null && cursor.moveToFirst()) {
            do {

                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.ID));
                String loai = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.LOAI));
                double soTien = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.SO_TIEN));
                int maDanhMuc = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.MA_DANH_MUC));
                String ngay = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.NGAY));
                String ghiChu = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.GHI_CHU));

                // Lấy tên danh mục mẫu theo ID
                String[] danhMucMau = {"Ăn uống", "Học tập", "Đi lại", "Giải trí", "Tiền nhà", "Lương", "Thưởng"};
                String tenDanhMuc = (maDanhMuc > 0 && maDanhMuc <= danhMucMau.length) ? danhMucMau[maDanhMuc - 1] : "Khác";

                //  Chỉ cộng vào tổng Thu/Chi ở widget nếu giao dịch thuộc tháng hiện tại
                if (ngay != null && ngay.startsWith(chuoiThangNamHienTai)) {
                    if (loai.equalsIgnoreCase("THU")) {
                        tongThu += soTien;
                    } else {
                        tongChi += soTien;
                    }
                }

                danhSachThuChi.add( new ThuChiModel(id, maNguoiDung, loai, soTien, maDanhMuc, ngay, ghiChu, tenDanhMuc));

            } while (cursor.moveToNext());
            cursor.close();
        }
         // lấy tổng số dư dựa theo tất cả danh sách đang có
        double soDuCacGiaoDich = 0;
        for (ThuChiModel model : danhSachThuChi) {
            if (model.getLoai().equalsIgnoreCase("THU")) {
                soDuCacGiaoDich += model.getSoTien();
            } else {
                soDuCacGiaoDich -= model.getSoTien();
            }
        }
        DecimalFormat formatter = new DecimalFormat("#,###");

        // Cập nhật các con số tổng lên giao diện
        txtTongSoDu.setText(formatter.format(soDuCacGiaoDich) + " đ"); // Số dư khả dụng thực tế trong ví
        txtTongThu.setText(formatter.format(tongThu) + " đ");          // Tổng thu nhập của riêng tháng này
        txtTongChi.setText(formatter.format(tongChi) + " đ");

        Toast.makeText(
                getContext(),
                "Tong giao dich: " + danhSachThuChi.size(),
                Toast.LENGTH_LONG
        ).show();

        adapter.notifyDataSetChanged();


    }
}