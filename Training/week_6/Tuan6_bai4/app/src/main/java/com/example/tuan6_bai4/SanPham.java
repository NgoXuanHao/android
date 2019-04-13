package com.example.tuan6_bai4;

public class SanPham extends MatHang{
    //Lấy tham chiếu để lập trình cho lẹ
    private DanhMuc Dmuc;

    public DanhMuc getDmuc() {
        return Dmuc;
    }

    public void setDmuc(DanhMuc dmuc) {
        Dmuc = dmuc;
    }

    public SanPham(String ma, String name, DanhMuc dmuc) {
        super(ma, name);
        Dmuc = dmuc;
    }

    public SanPham(String ma, String name) {
        super(ma, name);
    }

    public SanPham() {
        super();
    }
}
