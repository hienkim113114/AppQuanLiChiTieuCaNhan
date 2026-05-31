package KimHien.appquanlychitieucanhan;

public class NganSachModel {
    private String thangNam;
    private double hanMuc;
    private double daChi;

    public NganSachModel(String thangNam, double hanMuc, double daChi) {
        this.thangNam = thangNam;
        this.hanMuc = hanMuc;
        this.daChi = daChi;
    }

    public String getThangNam() { return thangNam; }
    public double getHanMuc() { return hanMuc; }
    public double getDaChi() { return daChi; }
    public double getConLai() { return hanMuc - daChi; }
    public double getTienDo() {
        if (hanMuc <= 0) return 0;
        return (daChi / hanMuc) * 100;
    }
}