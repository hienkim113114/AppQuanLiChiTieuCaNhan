package KimHien.appquanlychitieucanhan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText edtEmail, edtPassword;
    private Button btnDangNhap;
    private TextView txtChuyenDangKy;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);

        // Ánh xạ giao diện
        edtEmail = findViewById(R.id.edt_login_email);
        edtPassword = findViewById(R.id.edt_login_password);
        btnDangNhap = findViewById(R.id.btn_dang_nhap);
        txtChuyenDangKy = findViewById(R.id.txt_chuyen_dang_ky);

        // Bấm "Đăng ký mới" để mở màn hình đăng ký
        txtChuyenDangKy.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Xử lý sự kiện bấm nút đăng nhập
        btnDangNhap.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tài khoản và mật khẩu!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Gọi hàm checkLogin trong SQLite
            if (dbHelper.checkLogin(email, password)) {
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                // Chuyển sang màn hình chính MainActivity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("USER_ID", email); // Truyền email làm mã người dùng sang MainActivity
                startActivity(intent);

                finish();
            } else {
                Toast.makeText(this, "Tài khoản hoặc mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}