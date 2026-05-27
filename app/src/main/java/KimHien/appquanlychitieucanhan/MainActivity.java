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

        // Ánh xạ các thành phần trên giao diện
        bottomNavigation = findViewById(R.id.bottom_navigation);

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
                    selectedFragment = new ThongKeFragment();
                } else if (id == R.id.nav_budget) {
                    selectedFragment = new NganSachFragment();
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

    }
}