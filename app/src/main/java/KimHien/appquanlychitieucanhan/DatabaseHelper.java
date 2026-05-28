package KimHien.appquanlychitieucanhan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ExpenseManager.db";
    private static final int DATABASE_VERSION = 6;

    public static final String TABLE_THUCHI = "thuchi";
    public static final String TABLE_DANHMUC = "danhmuc";
    public static final String TABLE_NGANSACH = "ngansach";

    public static final String TABLE_USERS = "users";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_HOTEN = "ho_ten";
    public static final String COL_SDT = "so_dien_thoai";
    // Các trường chung
    public static final String ID = "id";
    public static final String USER_ID = "user_id";

    // Trường của bảng THUCHI (Khoản thu / chi)
    public static final String LOAI = "loai_hinh";       // "THU" hoặc "CHI"
    public static final String SO_TIEN = "so_tien";       // Số tiền thực hiện
    public static final String MA_DANH_MUC = "ma_danh_muc";
    public static final String NGAY = "ngay_thang";       // Định dạng: YYYY-MM-DD
    public static final String GHI_CHU = "ghi_chu";

    // Trường của bảng DANHMUC (Danh mục chi tiêu)
    public static final String TEN = "ten_danh_muc";      // Vd: Ăn uống, Học tập,giải trí..
    public static final String BIEU_TUONG = "bieu_tuong";  // icon cho dễ nhìn

    // Trường của bảng NGANSACH (Hạn mức ngân sách tháng)
    public static final String THANG_NAM = "thang_nam";   // Định dạng: MM-YYYY
    public static final String HAN_MUC = "so_tien_han_muc"; // Hạn mức chi tiêu tối đa

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //TẠO CÁC BẢNG DỮ LIỆU
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Bảng Người dùng
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + USER_ID + " TEXT PRIMARY KEY," // Dùng làm khóa chính liên kết các bảng khác
                + COL_EMAIL + " TEXT,"
                + COL_PASSWORD + " TEXT,"
                + COL_HOTEN + " TEXT,"
                + COL_SDT + " TEXT" + ")";

        // Tạo bảng Danh mục
        String CREATE_DANHMUC_TABLE = "CREATE TABLE " + TABLE_DANHMUC + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + USER_ID + " TEXT,"
                + TEN + " TEXT,"
                + BIEU_TUONG + " TEXT" + ")";

        // Tạo bảng ThuChi
        String CREATE_THUCHI_TABLE = "CREATE TABLE " + TABLE_THUCHI + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + USER_ID + " TEXT,"
                + LOAI + " TEXT,"
                + SO_TIEN + " REAL,"
                + MA_DANH_MUC + " INTEGER,"
                + NGAY + " TEXT,"
                + GHI_CHU + " TEXT,"
                + "FOREIGN KEY(" + MA_DANH_MUC + ") REFERENCES " + TABLE_DANHMUC + "(" + ID + ")" + ")";

        // Tạo bảng Ngân sách
        String CREATE_NGANSACH_TABLE = "CREATE TABLE " + TABLE_NGANSACH + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + USER_ID + " TEXT,"
                + THANG_NAM + " TEXT,"
                + HAN_MUC + " REAL" + ")";


         db.execSQL(CREATE_USERS_TABLE);

        db.execSQL(CREATE_DANHMUC_TABLE);
        db.execSQL(CREATE_THUCHI_TABLE);
        db.execSQL(CREATE_NGANSACH_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_THUCHI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DANHMUC);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NGANSACH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }
    //  HÀM XỬ LÝ BẢNG THUCHI
    public long addThuChi(String userId, String loai, double soTien, int maDanhMuc, String ngay, String ghiChu) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_ID, userId);
        values.put(LOAI, loai);
        values.put(SO_TIEN, soTien);
        values.put(MA_DANH_MUC, maDanhMuc);
        values.put(NGAY, ngay);
        values.put(GHI_CHU, ghiChu);

        long id = db.insert(TABLE_THUCHI, null, values);
        db.close();
        return id;
    }

    public Cursor getAllThuChiByUser(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_THUCHI
                + " WHERE " + USER_ID + " = ?"
                + " ORDER BY " + NGAY + " DESC";

        return db.rawQuery(query, new String[]{userId});
    }

    public int updateThuChi(int id, String loai, double soTien, int maDanhMuc, String ngay, String ghiChu) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LOAI, loai);
        values.put(SO_TIEN, soTien);
        values.put(MA_DANH_MUC, maDanhMuc);
        values.put(NGAY, ngay);
        values.put(GHI_CHU, ghiChu);

        return db.update(TABLE_THUCHI, values, ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void deleteThuChi(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_THUCHI, ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public double getTotalAmountByType(String userId, String loai, String thangNam) {
        SQLiteDatabase db = this.getReadableDatabase();
        double total = 0;

        String query = "SELECT SUM(" + SO_TIEN + ") FROM " + TABLE_THUCHI
                + " WHERE " + USER_ID + " = ? AND " + LOAI + " = ? AND " + NGAY + " LIKE ?";

        Cursor cursor = db.rawQuery(query, new String[]{userId, loai, thangNam + "%"});
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        return total;
    }
    //  HÀM XỬ LÝ BẢNG DANHMUC
    public long addCategory(String userId, String tenDanhMuc, String bieuTuong) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_ID, userId);
        values.put(TEN, tenDanhMuc);
        values.put(BIEU_TUONG, bieuTuong);
        long id = db.insert(TABLE_DANHMUC, null, values);
        db.close();
        return id;
    }

    public Cursor getCategoriesByUser(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_DANHMUC + " WHERE " + USER_ID + " = ?";
        return db.rawQuery(query, new String[]{userId});
    }

    //HÀM XỬ LÝ BẢNG NGANSACH

    public long saveOrUpdateBudget(String userId, String thangNam, double soTienHanMuc) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_NGANSACH + " WHERE " + USER_ID + " = ? AND " + THANG_NAM + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{userId, thangNam});

        ContentValues values = new ContentValues();
        values.put(HAN_MUC, soTienHanMuc);

        long result;
        if (cursor.getCount() > 0) {
            result = db.update(TABLE_NGANSACH, values, USER_ID + " = ? AND " + THANG_NAM + " = ?", new String[]{userId, thangNam});
        } else {
            values.put(USER_ID, userId);
            values.put(THANG_NAM, thangNam);
            result = db.insert(TABLE_NGANSACH, null, values);
        }
        cursor.close();
        db.close();
        return result;
    }

    public double getBudgetAmount(String userId, String thangNam) {
        SQLiteDatabase db = this.getReadableDatabase();
        double limit = 0;
        String query = "SELECT " + HAN_MUC + " FROM " + TABLE_NGANSACH + " WHERE " + USER_ID + " = ? AND " + THANG_NAM + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{userId, thangNam});
        if (cursor.moveToFirst()) {
            limit = cursor.getDouble(0);
        }
        cursor.close();
        return limit;
    }
    // Hàm thống kê tổng chi tiêu theo từng danh mục của một tháng cụ thể
    public Cursor getThongKeChiTieuTheoDanhMuc(String userId, String thangNam) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Câu lệnh SQL truy vấn nhóm dữ liệu
        String query = "SELECT " + MA_DANH_MUC + ", SUM(" + SO_TIEN + ") AS TongTien " +
                "FROM " + TABLE_THUCHI + " " +
                "WHERE " + USER_ID + " = ? " +
                "AND " + LOAI + " = 'CHI' " +
                "AND " + NGAY + " LIKE ? " +
                "GROUP BY " + MA_DANH_MUC;

        return db.rawQuery(query, new String[]{userId, thangNam + "%"});
    }

    // Hàm xu lý đăng ký tài khoản
    public boolean registerUser(String hoTen, String email, String sdt, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Dùng email làm USER_ID duy nhất
        values.put(USER_ID, email);
        values.put(COL_HOTEN, hoTen);
        values.put(COL_EMAIL, email);
        values.put(COL_SDT, sdt);
        values.put(COL_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }
    //Hàm xử lý đăng nhập
    public boolean checkLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COL_EMAIL + " = ? AND " + COL_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }
    // Hàm lấy thông tin  của người dùng dựa vào Email/ID đăng nhập
    public Cursor getThongTinNguoiDung(String emailOrId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + USER_ID + " = ?";
        return db.rawQuery(query, new String[]{emailOrId});
    }
}