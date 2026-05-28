package KimHien.appquanlychitieucanhan;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private FloatingActionButton fabAdd;
    private String maNguoiDung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    //Hứng mã người dùng đăng nhập
        maNguoiDung = getIntent().getStringExtra("USER_ID");
        if (maNguoiDung == null) {
            maNguoiDung = "offline_user"; // Phòng hờ nếu có lỗi xảy ra
        }

        // Ánh xạ giao diện
        bottomNavigation = findViewById(R.id.bottom_navigation);
        //Chuyển vào màn hình trang chủ
        if (savedInstanceState == null) {
            chuyenFragment(new TrangChuFragment());
        }

        // Xử lý sự kiện khi người dùng click đổi các Tab dưới Bottom Navigation
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    selectedFragment = new TrangChuFragment();
                } else if (id == R.id.nav_chart) {
                    selectedFragment = new ThongKeFragment();
                } else if (id == R.id.nav_budget) {
                    selectedFragment = new NganSachFragment();
                } else if (id == R.id.nav_profile) {
                    selectedFragment = new TaiKhoanFragment();
                }

                if (selectedFragment != null) {
                    chuyenFragment(selectedFragment);
                }
                return true;
            }
        });

    }
    private void chuyenFragment(Fragment fragment) {
        // Dữ liệu maNguoiDung gửi vào Fragment nhận
        Bundle args = new Bundle();
        args.putString("USER_ID", maNguoiDung);
        fragment.setArguments(args);

        // Thực hiện lệnh chuyển màn hình
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}