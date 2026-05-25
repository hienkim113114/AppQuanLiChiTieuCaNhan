package KimHien.appquanlychitieucanhan;

public class ThuChiModel {
    private int id;
    private String userId;
    private String loai;
    private double soTien;
    private int maDanhMuc;
    private String ngay;
    private String ghiChu;
    private String tenDanhMuc; //  Trường này để hiển thị tên danh mục lên giao diện

    // Hàm Constructor
    public ThuChiModel(int id, String userId, String loai, double soTien, int maDanhMuc, String ngay, String ghiChu, String tenDanhMuc) {
        this.id = id;
        this.userId = userId;
        this.loai = loai;
        this.soTien = soTien;
        this.maDanhMuc = maDanhMuc;
        this.ngay = ngay;
        this.ghiChu = ghiChu;
        this.tenDanhMuc = tenDanhMuc;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getLoai() { return loai; }
    public void setLoai(String loai) { this.loai = loai; }

    public double getSoTien() { return soTien; }
    public void setSoTien(double soTien) { this.soTien = soTien; }

    public int getMaDanhMuc() { return maDanhMuc; }
    public void setMaDanhMuc(int maDanhMuc) { this.maDanhMuc = maDanhMuc; }

    public String getNgay() { return ngay; }
    public void setNgay(String ngay) { this.ngay = ngay; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public String getTenDanhMuc() { return tenDanhMuc; }
    public void setTenDanhMuc(String tenDanhMuc) { this.tenDanhMuc = tenDanhMuc; }
}