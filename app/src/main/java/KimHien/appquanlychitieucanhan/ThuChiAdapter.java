package KimHien.appquanlychitieucanhan;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.DecimalFormat;
import java.util.List;

public class ThuChiAdapter extends RecyclerView.Adapter<ThuChiAdapter.ThuChiViewHolder> {
    private Context context;
    private List<ThuChiModel> danhSachThuChi;
    public ThuChiAdapter(Context context, List<ThuChiModel> danhSachThuChi) {
        this.context = context;
        this.danhSachThuChi = danhSachThuChi;
    }

    @NonNull
    @Override
    public ThuChiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Nạp giao diện item_thuchi.xml vào Adapter
        View view = LayoutInflater.from(context).inflate(R.layout.item_thuchi, parent, false);
        return new ThuChiViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ThuChiViewHolder holder, int position) {
        ThuChiModel item = danhSachThuChi.get(position);
        // Gán dữ liệu chữ lên giao diện
        holder.txtTenDanhMuc.setText(item.getTenDanhMuc());
        holder.txtGhiChu.setText(item.getGhiChu());
        holder.txtNgay.setText(item.getNgay());

        // Định dạng hiển thị số tiền
        DecimalFormat formatter = new DecimalFormat("#,###");
        String bieuDienSoTien = formatter.format(item.getSoTien()) + " đ";

        // Logic kiểm tra Loại hình để đổi màu sắc tiền
        if (item.getLoai().equalsIgnoreCase("THU")) {
            holder.txtSoTien.setText("+" + bieuDienSoTien);
            holder.txtSoTien.setTextColor(Color.parseColor("#10B981"));
            holder.imgBieuTuong.setImageResource(android.R.drawable.ic_input_add); //icon thu
        } else {
            holder.txtSoTien.setText("-" + bieuDienSoTien);
            holder.txtSoTien.setTextColor(Color.parseColor("#EF4444"));
            holder.imgBieuTuong.setImageResource(android.R.drawable.ic_delete); // icon chi
        }
    }

    @Override
    public int getItemCount() {
        return danhSachThuChi != null ? danhSachThuChi.size() : 0;
    }
    // Lớp ViewHolder đe quản lý các thành phần UI trong file XML
    public static class ThuChiViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBieuTuong;
        TextView txtTenDanhMuc, txtGhiChu, txtNgay, txtSoTien;
        public ThuChiViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBieuTuong = itemView.findViewById(R.id.img_item_bieutuong);
            txtTenDanhMuc = itemView.findViewById(R.id.txt_item_tendanhmuc);
            txtGhiChu = itemView.findViewById(R.id.txt_item_ghichu);
            txtNgay = itemView.findViewById(R.id.txt_item_ngay);
            txtSoTien = itemView.findViewById(R.id.txt_item_sotien);
        }
    }
}