package KimHien.appquanlychitieucanhan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.DecimalFormat;
import java.util.List;

public class NganSachAdapter extends RecyclerView.Adapter<NganSachAdapter.ViewHolder> {

    private List<NganSachModel> list;
    private DecimalFormat formatter = new DecimalFormat("#,###");

    public NganSachAdapter(List<NganSachModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ngan_sach, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NganSachModel model = list.get(position);

        // Chuyển đổi định dạng chuỗi YYYY-MM sang MM/YYYY để hiển thị đẹp hơn
        String[] parts = model.getThangNam().split("-");
        if (parts.length == 2) {
            holder.txtThang.setText(parts[1] + "/" + parts[0]);
        } else {
            holder.txtThang.setText(model.getThangNam());
        }

        holder.txtHanMuc.setText(formatter.format(model.getHanMuc()) + " đ");
        holder.txtDaChi.setText(formatter.format(model.getDaChi()) + " đ");

        double conLai = model.getConLai();
        if (conLai >= 0) {
            holder.txtConLai.setText(formatter.format(conLai) + " đ");
            holder.txtConLai.setTextColor(android.graphics.Color.parseColor("#10B981"));
        } else {
            holder.txtConLai.setText("-" + formatter.format(Math.abs(conLai)) + " đ");
            holder.txtConLai.setTextColor(android.graphics.Color.RED);
        }

        int phanTram = (int) model.getTienDo();
        holder.txtTienDoSo.setText(phanTram + "%");
        holder.prgTienDo.setProgress(Math.min(phanTram, 100));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtThang, txtHanMuc, txtDaChi, txtConLai, txtTienDoSo;
        ProgressBar prgTienDo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtThang = itemView.findViewById(R.id.item_txt_thang);
            txtHanMuc = itemView.findViewById(R.id.item_txt_hanmuc);
            txtDaChi = itemView.findViewById(R.id.item_txt_dachi);
            txtConLai = itemView.findViewById(R.id.item_txt_conlai);
            txtTienDoSo = itemView.findViewById(R.id.item_txt_tiendo_so);
            prgTienDo = itemView.findViewById(R.id.item_prg_tiendo);
        }
    }
}