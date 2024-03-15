public class SanPham {
    private int Id;
    private String tenSanPham;
    private double giaBan;

    public SanPham(int id, String tenSanPham, double giaBan) {
        Id = id;
        this.tenSanPham = tenSanPham;
        this.giaBan = giaBan;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }
}
