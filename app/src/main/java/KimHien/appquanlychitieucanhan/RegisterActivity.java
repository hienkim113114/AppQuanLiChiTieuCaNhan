package KimHien.appquanlychitieucanhan;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText edtHoTen, edtEmail, edtSdt, edtPassword, edtRePassword;
    private Button btnDangKy;
    private TextView txtChuyenDangNhap;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);
        // Ánh xạ giao diện
        edtHoTen = findViewById(R.id.edt_reg_hoten);
        edtEmail = findViewById(R.id.edt_reg_email);
        edtSdt = findViewById(R.id.edt_reg_sdt);
        edtPassword = findViewById(R.id.edt_reg_password);
        edtRePassword = findViewById(R.id.edt_reg_repassword);
        btnDangKy = findViewById(R.id.btn_dang_ky);
        txtChuyenDangNhap = findViewById(R.id.txt_chuyen_dang_nhap);

        // Nút quay lại
        findViewById(R.id.btn_back_to_login).setOnClickListener(v -> finish());

        // Bấm chữ "Đăng nhập"đóng màn hình để quay lại Login
        txtChuyenDangNhap.setOnClickListener(v -> finish());
        // Xử lý sự kiện bấm nút đăng Ký
        btnDangKy.setOnClickListener(v -> {
            String hoTen = edtHoTen.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String sdt = edtSdt.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String rePassword = edtRePassword.getText().toString().trim();

            //Không được để trống
            if (hoTen.isEmpty() || email.isEmpty() || sdt.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            //Nhập mk khong trùng khớp
            if (!password.equals(rePassword)) {
                Toast.makeText(this, "Mật khẩu nhập lại không trùng khớp!", Toast.LENGTH_SHORT).show();
                return;
            }

            //Lưu vào SQLite
            boolean canRegister = dbHelper.registerUser(hoTen, email, sdt, password);
            if (canRegister) {
                Toast.makeText(this, "Đăng ký tài khoản thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Đăng ký thất bại! Email có thể đã tồn tại.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}