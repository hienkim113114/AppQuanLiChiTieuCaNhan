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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ các thành phần trên giao diện XML
        bottomNavigation = findViewById(R.id.bottom_navigation);
        fabAdd = findViewById(R.id.fab_add);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new TrangChuFragment())
                    .commit();
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
                    selectedFragment = new Fragment();
                } else if (id == R.id.nav_budget) {
                    selectedFragment = new Fragment();
                } else if (id == R.id.nav_profile) {
                    selectedFragment = new Fragment();
                }

                // Thực hiện chuyển đổi Fragment trên màn hình nếu selectedFragment không bị rỗng
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                }
                return true;
            }
        });

        //Xử lý sự kiện bấm vào nút Nổi để mở màn hình Ghi Chép Thu Chi
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GhiChepThuChiActivity.class);
            startActivity(intent);
        });
    }
}