package KimHien.appquanlychitieucanhan;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class TaiKhoanFragment extends Fragment {
    private TextView txtProfileName, txtProfileEmail;
    private LinearLayout layoutChangePassword, layoutUserInfo;
    private Button btnLogout;
    private ImageView imgProfileAvatar;
    private TextView txtChangeAvatar;

    private static final int PICK_IMAGE_REQUEST = 100;

    private DatabaseHelper dbHelper;
    private String maNguoiDungHienTai;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tai_khoan, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        txtProfileName = view.findViewById(R.id.txt_profile_name);
        txtProfileEmail = view.findViewById(R.id.txt_profile_email);
        layoutChangePassword = view.findViewById(R.id.layout_change_password);
        layoutUserInfo = view.findViewById(R.id.layout_user_info);
        btnLogout = view.findViewById(R.id.btn_logout);
        imgProfileAvatar = view.findViewById(R.id.img_profile_avatar);
        txtChangeAvatar = view.findViewById(R.id.txt_change_avatar);

        if (getArguments() != null) {
            maNguoiDungHienTai =
                    getArguments().getString("USER_ID");
        }

        if (maNguoiDungHienTai == null) {
            maNguoiDungHienTai = "offline_user";
        }
        loadAvatar();
          txtChangeAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");

            startActivityForResult(intent, PICK_IMAGE_REQUEST);

        });

        // Nhận mã USER_ID từ MainActivity truyền sang
        if (getArguments() != null) {
            maNguoiDungHienTai = getArguments().getString("USER_ID");
        }

        if (maNguoiDungHienTai == null) {
            maNguoiDungHienTai = "offline_user";
        }

        // Hiển thị thông tin ban đầu lên giao diện chính của Tab
        if (!maNguoiDungHienTai.equals("offline_user")) {
            txtProfileEmail.setText(maNguoiDungHienTai);

            // Đọc tên thật từ SQLite hiển thị lên màn hình
            Cursor cursor = dbHelper.getThongTinNguoiDung(maNguoiDungHienTai);
            if (cursor != null && cursor.moveToFirst()) {
                int indexHoTen = cursor.getColumnIndex(DatabaseHelper.COL_HOTEN);
                if (indexHoTen >= 0) {
                    String tenThat = cursor.getString(indexHoTen);
                    txtProfileName.setText(tenThat);
                }
            } else {
                txtProfileName.setText("Xin chào!");
            }
            if (cursor != null) cursor.close();
        } else {
            txtProfileEmail.setText("chua_dang_nhap@email.com");
            txtProfileName.setText("Khách");
        }

        // Bắt sự kiện đổi mật khẩu
        layoutChangePassword.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Chức năng đổi mật khẩu đang phát triển!", Toast.LENGTH_SHORT).show();
        });

        // XỬ LÝ SỰ KIỆN: Xem thông tin chi tiết tài khoản đã đăng ký trước đó
        layoutUserInfo.setOnClickListener(v -> {
            //Tạo sẵn các biến chứa chữ mặc định
            String hoTen = "Chưa cập nhật";
            String soDienThoai = "Chưa cập nhật";
            String email = maNguoiDungHienTai;

            // 2. Vào thẳng SQLite bê dữ liệu thật ra đè lên
            Cursor cursor = dbHelper.getThongTinNguoiDung(maNguoiDungHienTai);
            if (cursor != null && cursor.moveToFirst()) {

                // Tìm vị trí của từng cột xem có tồn tại không
                int idxHoTen = cursor.getColumnIndex(DatabaseHelper.COL_HOTEN);
                int idxSdt = cursor.getColumnIndex(DatabaseHelper.COL_SDT);
                int idxEmail = cursor.getColumnIndex(DatabaseHelper.COL_EMAIL);

                // Nếu vị trí hợp lệ (lớn hơn hoặc bằng 0) thì mới lấy chữ ra
                if (idxHoTen >= 0) hoTen = cursor.getString(idxHoTen);
                if (idxSdt >= 0) soDienThoai = cursor.getString(idxSdt);
                if (idxEmail >= 0) email = cursor.getString(idxEmail);

                cursor.close();
            }
            //Hiện bảng thông báo lên màn hình
            new AlertDialog.Builder(requireContext())
                    .setTitle("THÔNG TIN ĐĂNG KÝ")
                    .setMessage("👤 Họ và tên: " + hoTen + "\n\n" +
                            "✉️ Email: " + email + "\n\n" +
                            "📞 Số điện thoại: " + soDienThoai + "\n\n" +
                            "🛡️ Trạng thái: Thành viên tích cực")
                    .setPositiveButton("ĐÓNG", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        });

        // Xử lý sự kiện ĐĂNG XUẤT
        btnLogout.setOnClickListener(v -> {
            requireContext().getSharedPreferences("USER_DATA",0)
                    .edit()
                    .remove("CURRENT_USER")
                    .apply();

            Toast.makeText(requireContext(),"Đã đăng xuất thành công!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent( requireContext(), LoginActivity.class );

            intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TASK
            );
            startActivity(intent);
        });

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();

        loadAvatar();
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,  @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST&& resultCode == getActivity().RESULT_OK && data != null) {
            Uri imageUri = data.getData();

            String avatarPath =saveImageToInternalStorage(imageUri);

            if (!avatarPath.isEmpty()) {
                imgProfileAvatar.setImageURI(
                        Uri.fromFile(new java.io.File(avatarPath))
                );
                requireContext().getSharedPreferences("USER_DATA", 0)
                        .edit()
                        .putString("avatar_" + maNguoiDungHienTai, avatarPath)
                        .apply();

                Toast.makeText(
                        getContext(),
                        "Đổi ảnh thành công",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }
    private String saveImageToInternalStorage(Uri imageUri) {

        try {
            InputStream inputStream = requireContext().getContentResolver() .openInputStream(imageUri);

            Bitmap bitmap =BitmapFactory.decodeStream(inputStream);

            String fileName ="avatar_" + maNguoiDungHienTai + ".jpg";

            File file = new File(requireContext().getFilesDir(),  fileName);
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    90,
                    fos
            );
            fos.flush();
            fos.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    private void loadAvatar() {
        String avatarPath = requireContext()
                .getSharedPreferences("USER_DATA",0)
                .getString("avatar_" + maNguoiDungHienTai,"" );
        if (!avatarPath.isEmpty()) {
            imgProfileAvatar.setImageURI(
                    Uri.fromFile(new java.io.File(avatarPath))
            );

        } else {

            imgProfileAvatar.setImageResource(R.drawable.img_2);
        }
    }
}