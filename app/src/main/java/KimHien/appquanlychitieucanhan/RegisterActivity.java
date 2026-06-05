package KimHien.appquanlychitieucanhan;

import android.content.Intent;
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
            //email phải ddun định dạng
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(
                        this,
                        "Email không đúng định dạng!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }
            //Số điện thoại phải gồm 10 số
            if (!sdt.matches("^0\\d{9}$")) {
                Toast.makeText(
                        this,
                        "Số điện thoại phải gồm 10 số!",

                        Toast.LENGTH_SHORT
                ).show();
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
                hienThiDialogThanhCong();
            } else {
                Toast.makeText(this, "Đăng ký thất bại! Email có thể đã tồn tại.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void hienThiDialogThanhCong() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RegisterActivity.this);
        // Nạp file giao diện dialog_success.xml vào code
        android.view.LayoutInflater inflater = getLayoutInflater();
        android.view.View dialogView = inflater.inflate(R.layout.dialog_success, null);
        builder.setView(dialogView);

        android.app.AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        dialog.setCancelable(false);
        dialog.show();

        // Xử lý sự kiện khi bấm nút "Go To Login"
        Button btnGoToLogin = dialogView.findViewById(R.id.btn_go_to_login);
        btnGoToLogin.setOnClickListener(v -> {
            dialog.dismiss(); // Tắt cửa sổ popup

            // Chuyển hướng về màn hình Login
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

}