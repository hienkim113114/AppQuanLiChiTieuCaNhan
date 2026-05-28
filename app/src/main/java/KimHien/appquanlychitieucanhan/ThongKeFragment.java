package KimHien.appquanlychitieucanhan;

import android.database.Cursor;
import android.graphics.Color;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ThongKeFragment extends Fragment {

    private PieChart pieChart;
    private TextView txtHienThiThang, txtTongQuanThuChi;
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
        txtTongQuanThuChi = view.findViewById(R.id.txt_tong_quan_thu_chi);
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

        // Tạo chuỗi định dạng "YYYY-MM"
        String chuoiThangNamQuery = String.format("%d-%02d", nam, thang);

        // Tính toán và hiển thị chuỗi tổng quan số tiền Thu/Chi thực tế
        DecimalFormat dinhDangSo = new DecimalFormat("#,###");
        double tongChiThang = dbHelper.getTongChiThang(maNguoiDung, chuoiThangNamQuery);
        double tongThuThang = dbHelper.getTongThuThang(maNguoiDung, chuoiThangNamQuery);

        // HIỂN THỊ KHOẢN THU
        txtTongQuanThuChi.setText("Tháng " + thang + "/" + nam + ": Chi Tiêu " + dinhDangSo.format(tongChiThang) + " đ, Thu Nhập " + dinhDangSo.format(tongThuThang) + " đ");
        //Vẽ lại biểu đồ với điều kiện tháng năm vừa tính được
        veBieuDoTheoThang(chuoiThangNamQuery);
    }

    private void veBieuDoTheoThang(String chuoiThangNam) {
        // Mảng tên danh mục chuẩn để đối chiếu tên từ mã danh mục
        String[] danhMucMau = {"Ăn uống", "Học tập", "Đi lại", "Giải trí", "Tiền nhà", "Lương", "Thưởng"};


        List<PieEntry> danhSachVongTrong = new ArrayList<>();
        Cursor cursorChi = dbHelper.getThongKeChiTieuTheoDanhMuc(maNguoiDung, chuoiThangNam);

        if (cursorChi != null) {
            // Lấy dữ liệu theo vị trí cột cố định 0 và 1
            if (cursorChi.moveToFirst()) {
                do {
                    int maDanhMuc = cursorChi.getInt(0);
                    double tongTien = cursorChi.getDouble(1);

                    String tenDanhMuc = (maDanhMuc > 0 && maDanhMuc <= danhMucMau.length) ? danhMucMau[maDanhMuc - 1] : "Khác";
                    danhSachVongTrong.add(new PieEntry((float) tongTien, tenDanhMuc));
                } while (cursorChi.moveToNext());
            }
            cursorChi.close();
        }

        // Nếu không có dữ liệu Chi thì báo trống
        if (danhSachVongTrong.isEmpty()) {
            pieChart.clear();
            pieChart.setNoDataText("Tháng này chưa có dữ liệu chi tiêu!");
            pieChart.setNoDataTextColor(Color.parseColor("#475569"));
            pieChart.invalidate();
            return;
        }

        // Tạo bộ dữ liệu Chi tiêu
        PieDataSet dataSetVongTrong = new PieDataSet(danhSachVongTrong, "");
        dataSetVongTrong.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSetVongTrong.setValueTextSize(13f);
        dataSetVongTrong.setValueTextColor(Color.BLACK);

        // Đưa dữ liệu vào biểu đồ
        PieData data = new PieData(dataSetVongTrong);
        pieChart.setData(data);

        // Cấu hình hiển thị
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(true);
        pieChart.getLegend().setWordWrapEnabled(true);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(55f); // Độ rộng của lỗ khuyết
        pieChart.setHoleColor(Color.parseColor("#F1F5F9")); // Màu lỗ trùng với màu nền

        pieChart.setCenterText("");


        pieChart.invalidate();
    }
}