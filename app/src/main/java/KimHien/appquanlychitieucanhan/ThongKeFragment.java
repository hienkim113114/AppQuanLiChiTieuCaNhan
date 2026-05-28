package KimHien.appquanlychitieucanhan;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ThongKeFragment extends Fragment {

    private PieChart pieChart;
    private TextView txtHienThiThang;
    private Button btnThangTruoc, btnThangSau;
    private DatabaseHelper dbHelper;
    private String maNguoiDung = "offline_user";
    // lịch hệ thống để theo dõi tháng/năm chọn
    private Calendar boLichHienTai;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thong_ke, container, false);

        dbHelper = new DatabaseHelper(getContext());
        pieChart = view.findViewById(R.id.pie_chart_thongke);
        txtHienThiThang = view.findViewById(R.id.txt_hien_thi_thang);
        btnThangTruoc = view.findViewById(R.id.btn_thang_truoc);
        btnThangSau = view.findViewById(R.id.btn_thang_sau);

        // Khởi tạo thời gian mặc định là ngày/tháng/năm hiện tại
        boLichHienTai = Calendar.getInstance();
        // Xử lý sự kiện bấm nút lùi về tháng trước
        btnThangTruoc.setOnClickListener(v -> {
            boLichHienTai.add(Calendar.MONTH, -1); // Trừ đi 1 tháng
            capNhatThongKe();
        });
        // Xử lý sự kiện bấm nút tiến tới tháng sau
        btnThangSau.setOnClickListener(v -> {
            boLichHienTai.add(Calendar.MONTH, 1); // Cộng thêm 1 tháng
            capNhatThongKe();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Nhận mã người dùng thật từ MainActivity gửi xuống
        if (getArguments() != null) {
            maNguoiDung = getArguments().getString("USER_ID", "offline_user");
        }
        capNhatThongKe();
    }

    private void capNhatThongKe() {
        //Lấy thông tin tháng và năm từ bộ lịch đang chọn
        int nam = boLichHienTai.get(Calendar.YEAR);
        int thang = boLichHienTai.get(Calendar.MONTH) + 1; // Vì tháng trong Java chạy từ 0-11 nên phải +1

        txtHienThiThang.setText(String.format("Tháng %02d/%d", thang, nam));

        // Tạo chuỗi định dạng "YYYY-MM" để truyền vào câu lệnh SQLite
        String chuoiThangNamQuery = String.format("%d-%02d", nam, thang);

        //Vẽ lại biểu đồ với điều kiện tháng năm vừa tính được
        veBieuDoTheoThang(chuoiThangNamQuery);
    }

    private void veBieuDoTheoThang(String chuoiThangNam) {
        List<PieEntry> danhSachManh = new ArrayList<>();
        double tongTienCaThang = 0;
        // Gọi câu lệnh SQL và truyền biến chuoiThangNam động thay vì cố định "2026-05"
        Cursor cursor = dbHelper.getThongKeChiTieuTheoDanhMuc(maNguoiDung, chuoiThangNam);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int maDanhMuc = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.MA_DANH_MUC));
                double tongTien = cursor.getDouble(cursor.getColumnIndexOrThrow("TongTien"));

                // Cộng dồn vào tổng tiền cả tháng
                tongTienCaThang += tongTien;

                String[] danhMucMau = {"Ăn uống", "Học tập", "Đi lại", "Giải trí", "Tiền nhà", "Lương", "Thưởng"};
                String tenDanhMuc = (maDanhMuc > 0 && maDanhMuc <= danhMucMau.length) ? danhMucMau[maDanhMuc - 1] : "Khác";

                danhSachManh.add(new PieEntry((float) tongTien, tenDanhMuc));
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (danhSachManh.isEmpty()) {
            pieChart.clear();
            pieChart.setNoDataText("Tháng này chưa có dữ liệu chi tiêu!");
            pieChart.invalidate();
            return;
        }

        PieDataSet dataSet = new PieDataSet(danhSachManh, "");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(14f);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);

        pieChart.setDrawHoleEnabled(true); // Đục lỗ giữa
        pieChart.setHoleRadius(60f);       // Độ rộng của lỗ khuyết
        pieChart.setHoleColor(android.graphics.Color.parseColor("#F1F5F9"));

        pieChart.setCenterText("TỔNG CHI\n" + (int)tongTienCaThang + " đ");

        pieChart.setCenterTextColor(android.graphics.Color.parseColor("#0F172A"));
        pieChart.setCenterTextSize(15f);
        pieChart.invalidate();
    }
}