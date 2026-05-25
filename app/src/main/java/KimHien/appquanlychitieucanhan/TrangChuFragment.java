package KimHien.appquanlychitieucanhan;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TrangChuFragment extends Fragment {

    private TextView txtTongSoDu, txtTongThu, txtTongChi;
    private RecyclerView rcvLichSu;
    private DatabaseHelper dbHelper;
    private ThuChiAdapter adapter;
    private List<ThuChiModel> danhSachThuChi;
    private String maNguoiDung = "offline_user";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trang_chu, container, false);

        dbHelper = new DatabaseHelper(getContext());
        txtTongSoDu = view.findViewById(R.id.txt_tong_so_du);
        txtTongThu = view.findViewById(R.id.txt_tong_thu);
        txtTongChi = view.findViewById(R.id.txt_tong_chi);
        rcvLichSu = view.findViewById(R.id.rcv_lich_su);

        rcvLichSu.setLayoutManager(new LinearLayoutManager(getContext()));

        danhSachThuChi = new ArrayList<>();
        adapter = new ThuChiAdapter(getContext(), danhSachThuChi);
        rcvLichSu.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Mỗi khi quay lại tab Trang Chủ, tải lại dữ liệu mới nhất
        docDuLieuTuSQLite();
    }

    private void docDuLieuTuSQLite() {
        danhSachThuChi.clear();
        double tongThu = 0;
        double tongChi = 0;

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

                // Tính toán tổng số dư
                if (loai.equalsIgnoreCase("THU")) {
                    tongThu += soTien;
                } else {
                    tongChi += soTien;
                }

                danhSachThuChi.add(new ThuChiModel(id, maNguoiDung, loai, soTien, maDanhMuc, ngay, ghiChu, tenDanhMuc));

            } while (cursor.moveToNext());
            cursor.close();
        }

        // Cập nhật số dư lên giao diện

        double soDu = tongThu - tongChi;
        DecimalFormat formatter = new DecimalFormat("#,###");

        txtTongSoDu.setText(formatter.format(soDu) + " đ");
        txtTongThu.setText(formatter.format(tongThu) + " đ");
        txtTongChi.setText(formatter.format(tongChi) + " đ");

        adapter.notifyDataSetChanged();
        txtTongSoDu.setText(formatter.format(soDu) + " đ");

        // Thông báo cho Adapter biết dữ liệu đã thay đổi để vẽ lại danh sách trên màn hình
        adapter.notifyDataSetChanged();
    }
}