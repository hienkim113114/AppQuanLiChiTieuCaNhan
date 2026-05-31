package KimHien.appquanlychitieucanhan;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NganSachFragment extends Fragment {

    private TextView txtThangNam, txtHanMucHienTai, txtTongChiThang, txtConLai, txtThongBaoCanhBao;
    private ProgressBar prgTienDo;
    private Button btnThietLapHanMuc;

    private RecyclerView rcvNganSach;
    private NganSachAdapter nganSachAdapter;
    private List<NganSachModel> danhSachNganSach;
    private DatabaseHelper dbHelper;
    private String maNguoiDung = "offline_user";
    private String thangNamHienTai;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ngan_sach, container, false);

        dbHelper = new DatabaseHelper(getContext());
        // Ánh xạ các thành phần giao diện
        txtThangNam = view.findViewById(R.id.txt_ngansach_thangnam);
        txtHanMucHienTai = view.findViewById(R.id.txt_ngansach_hanmuc);
        txtTongChiThang = view.findViewById(R.id.txt_ngansach_tongchi);
        txtConLai = view.findViewById(R.id.txt_ngansach_conlai);
        txtThongBaoCanhBao = view.findViewById(R.id.txt_ngansach_canhbao);
        prgTienDo = view.findViewById(R.id.prg_ngansach_tiendo);
        btnThietLapHanMuc = view.findViewById(R.id.btn_thiet_lap_han_muc);

        // Cấu hình danh sách so sánh các tháng
        rcvNganSach = view.findViewById(R.id.rcv_danh_sach_ngan_sach);
        rcvNganSach.setLayoutManager(new LinearLayoutManager(getContext()));
        danhSachNganSach = new ArrayList<>();
        nganSachAdapter = new NganSachAdapter(danhSachNganSach);
        rcvNganSach.setAdapter(nganSachAdapter);

        // Lấy tháng năm hiện tại tự động theo định dạng YYYY-MM
        Calendar calendar = Calendar.getInstance();
        int nam = calendar.get(Calendar.YEAR);
        int thang = calendar.get(Calendar.MONTH) + 1;
        thangNamHienTai = String.format("%d-%02d", nam, thang);

        txtThangNam.setText("Ngân sách Tháng " + thang + "/" + nam);

        // Bắt sự kiện nút bấm thiết lập hạn mức ngân sách
        btnThietLapHanMuc.setOnClickListener(v -> hienThiHopThoaiDatHanMuc());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Nhận mã người dùng thật từ MainActivity gửi xuống
        if (getArguments() != null) {
            maNguoiDung = getArguments().getString("USER_ID", "offline_user");
        }
        chuyenDoiVaTinhToanNganSach();
        taiDanhSachNganSachCacThang();
    }

    // Hộp thoại cho user nhập hạn mức ví tiền mong muốn
    private void hienThiHopThoaiDatHanMuc() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Đặt hạn mức chi tiêu");

        final EditText edtHanMucMoi = new EditText(getContext());
        edtHanMucMoi.setHint("Nhập số tiền hạn mức tối đa (đ)");
        edtHanMucMoi.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

        double hanMucCu = dbHelper.getBudgetAmount(maNguoiDung, thangNamHienTai);
        if (hanMucCu > 0) {
            edtHanMucMoi.setText(String.valueOf((int) hanMucCu));
        }

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);
        layout.addView(edtHanMucMoi);
        builder.setView(layout);

        builder.setPositiveButton("LƯU", (dialog, which) -> {
            String chuoiHanMuc = edtHanMucMoi.getText().toString().trim();
            if (chuoiHanMuc.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập số tiền hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }

            double soTienHanMuc = Double.parseDouble(chuoiHanMuc);

            dbHelper.saveOrUpdateBudget(maNguoiDung, thangNamHienTai, soTienHanMuc);
            Toast.makeText(getContext(), "Đã thiết lập hạn mức chi tiêu thành công!", Toast.LENGTH_SHORT).show();

            chuyenDoiVaTinhToanNganSach();
            taiDanhSachNganSachCacThang();
        });

        builder.setNegativeButton("HỦY", null);
        builder.show();
    }

    // SO SÁNH TỔNG CHI VỚI HẠN MỨC ĐỂ KÍCH HOẠT CẢNH BÁO
    private void chuyenDoiVaTinhToanNganSach() {
        DecimalFormat formatter = new DecimalFormat("#,###");

        //Đọc hạn mức ngân sách tháng này từ SQLite
        double hanMuc = dbHelper.getBudgetAmount(maNguoiDung, thangNamHienTai);

        //Tính tổng số tiền đã "CHI" trong tháng
        double tongChi = dbHelper.getTotalAmountByType(maNguoiDung, "CHI", thangNamHienTai);

        //Hiển thị con số lên giao diện
        txtHanMucHienTai.setText(formatter.format(hanMuc) + " đ");
        txtTongChiThang.setText(formatter.format(tongChi) + " đ");

        if (hanMuc <= 0) {
            txtConLai.setText("0 đ");
            prgTienDo.setProgress(0);
            txtThongBaoCanhBao.setText("Hãy thiết lập hạn mức chi tiêu cho tháng này!");
            txtThongBaoCanhBao.setTextColor(Color.GRAY);
            return;
        }

        //Tính số tiền còn lại có thể tiêu
        double conLai = hanMuc - tongChi;
        if (conLai >= 0) {
            txtConLai.setText(formatter.format(conLai) + " đ");
            txtConLai.setTextColor(Color.parseColor("#10B981"));
        } else {
            txtConLai.setText("-" + formatter.format(Math.abs(conLai)) + " đ");
            txtConLai.setTextColor(Color.RED);
        }

        //Tính toán phần trăm tiến độ để vẽ lên ProgressBar
        int phanTramTienDo = (int) ((tongChi / hanMuc) * 100);
        prgTienDo.setProgress(Math.min(phanTramTienDo, 100));

        // Cảnh báo chi tiu
        if (tongChi > hanMuc) {
            //VƯỢT CHI (TỔNG CHI > HẠN MỨC) -> KÍCH HOẠT CẢNH BÁO NGUY HIỂM
            txtThongBaoCanhBao.setText("⚠️ NGUY HIỂM: Bạn đã chi tiêu vượt hạn mức cho phép " + formatter.format(Math.abs(conLai)) + " đ! Hãy thắt chặt chi tiêu ngay!");
            txtThongBaoCanhBao.setTextColor(Color.RED);
            prgTienDo.setProgressDrawable(getContext().getResources().getDrawable(android.R.drawable.progress_horizontal));
        } else if (tongChi >= (0.9 * hanMuc)) {
            //CHI TIÊU ĐẠT TỪ 90% ĐẾN 100% HẠN MỨC -> KÍCH HOẠT CẢNH BÁO CHÚ Ý
            txtThongBaoCanhBao.setText("🍊 CHÚ Ý: Quỹ chi tiêu của bạn đã dùng hết " + phanTramTienDo + "%. Bạn sắp chạm mức giới hạn cho phép!");
            txtThongBaoCanhBao.setTextColor(Color.parseColor("#F59E0B"));
        } else {
            //CHI TIÊU DƯỚI 90% HẠN MỨC -> TRẠNG THÁI ỔN ĐỊNH
            txtThongBaoCanhBao.setText("✅ AN TOÀN: Tình hình tài chính tháng này của bạn rất tốt. Tiếp tục duy trì nhé!");
            txtThongBaoCanhBao.setTextColor(Color.parseColor("#10B981"));
        }
    }

    //Quét database để tìm ngân sách và chi tiêu thực tế của các tháng cũ đưa vào danh sách so sánh
    private void taiDanhSachNganSachCacThang() {
        danhSachNganSach.clear();

        // Lấy danh sách 4 tháng gần nhất tự động để lập bảng so sánh
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < 4; i++) {
            int nam = cal.get(Calendar.YEAR);
            int thang = cal.get(Calendar.MONTH) + 1;
            String chuoiThangNam = String.format("%d-%02d", nam, thang);

            double hanMucThang = dbHelper.getBudgetAmount(maNguoiDung, chuoiThangNam);
            double daChiThang = dbHelper.getTotalAmountByType(maNguoiDung, "CHI", chuoiThangNam);

            // Chỉ thêm vào bảng hiển thị nếu tháng đó có thiết lập hạn mức hoặc có phát sinh giao dịch chi tiêu
            if (hanMucThang > 0 || daChiThang > 0) {
                danhSachNganSach.add(new NganSachModel(chuoiThangNam, hanMucThang, daChiThang));
            }
            cal.add(Calendar.MONTH, -1); // Lùi về 1 tháng trước đó để lấy dữ liệu lịch sử
        }
        nganSachAdapter.notifyDataSetChanged();
    }
}