package KimHien.appquanlychitieucanhan;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Calendar;

public class GhiChepThuChiActivity extends AppCompatActivity {
    private RadioGroup rgLoai;
    private RadioButton rbChi, rbThu;
    private EditText edtSoTien, edtNgay, edtGhiChu;
    private Spinner spnDanhMuc;
    private Button btnLuu;

    private DatabaseHelper dbHelper;
    private String maNguoiDungHienTai = "user_mac_dinh";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghi_chep_thu_chi);

        dbHelper = new DatabaseHelper(this);
        //  Ánh xạ UI
        rgLoai = findViewById(R.id.rg_loai);
        rbChi = findViewById(R.id.rb_chi);
        rbThu = findViewById(R.id.rb_thu);
        edtSoTien = findViewById(R.id.edt_sotien);
        edtNgay = findViewById(R.id.edt_ngay);
        edtGhiChu = findViewById(R.id.edt_ghichu);
        spnDanhMuc = findViewById(R.id.spn_danhmuc);
        btnLuu = findViewById(R.id.btn_luu);

        maNguoiDungHienTai = "offline_user";

        // Danh mục mẫu
        String[] danhMucMau = {"Ăn uống", "Học tập", "Đi lại", "Giải trí", "Tiền nhà", "Lương", "Thưởng"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, danhMucMau);
        spnDanhMuc.setAdapter(adapter);

        // 4. Bắt sự kiện Click vào ô Ngày để hiển thị hộp thoại chọn ngày Picker
        edtNgay.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int nam = calendar.get(Calendar.YEAR);
            int thang = calendar.get(Calendar.MONTH);
            int ngay = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(GhiChepThuChiActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        // Định dạng chuỗi ngày chuẩn YYYY-MM-DD để dễ lưu vào SQLite
                        String thangDinhDang = String.format("%02d", (month + 1));
                        String ngayDinhDang = String.format("%02d", dayOfMonth);
                        edtNgay.setText(year + "-" + thangDinhDang + "-" + ngayDinhDang);
                    }, nam, thang, ngay);
            datePickerDialog.show();
        });

        // Bắt sự kiện nút HOÀN TẤT VÀ LƯU
        btnLuu.setOnClickListener(v -> xuLyLuuGiaoDich());
    }

    private void xuLyLuuGiaoDich() {
        String chuoiSoTien = edtSoTien.getText().toString().trim();
        String ngay = edtNgay.getText().toString().trim();
        String ghiChu = edtGhiChu.getText().toString().trim();

        // Kiểm tra dữ liệu đầu vào bắt buộc
        if (chuoiSoTien.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số tiền!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ngay.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ngày giao dịch!", Toast.LENGTH_SHORT).show();
            return;
        }

        double soTien = Double.parseDouble(chuoiSoTien);

        // Xác định loại hình là THU hay CHI từ Radio Button
        String loaiGiaoDich = "CHI";
        if (rgLoai.getCheckedRadioButtonId() == R.id.rb_thu) {
            loaiGiaoDich = "THU";
        }

        int maDanhMuc = spnDanhMuc.getSelectedItemPosition() + 1;

        // Gọi hàm của DatabaseHelper để ghi dữ liệu trực tiếp vào SQLite
        long ketQua = dbHelper.addThuChi(maNguoiDungHienTai, loaiGiaoDich, soTien, maDanhMuc, ngay, ghiChu);

        if (ketQua != -1) {
            Toast.makeText(this, "Đã lưu khoản " + loaiGiaoDich + " thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Lưu thất bại, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
        }
    }
}