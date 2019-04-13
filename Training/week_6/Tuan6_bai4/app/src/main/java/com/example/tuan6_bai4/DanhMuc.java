package com.example.tuan6_bai4;

import java.util.ArrayList;

public class DanhMuc extends MatHang {
    private ArrayList<SanPham> listSp = null;

    public DanhMuc(String ma, String name) {
        super(ma, name);
        this.listSp = new ArrayList<SanPham>();
    }

    public DanhMuc() {
        super();
        this.listSp = new ArrayList<SanPham>();
    }

    public boolean isDuplicate(SanPham p) {
        for (SanPham p1 : listSp) {
            if (p1.getId().trim().equalsIgnoreCase(p.getId().trim()))
                return true;
        }
        return false;
    }

    public boolean addProduct(SanPham p) {
        boolean isDup = isDuplicate(p);
        if (!isDup) {
            p.setDmuc(this);
            return listSp.add(p);
        }
        return !isDup;
    }

    public ArrayList<SanPham> getListProduct() {
        return this.listSp;
    }

    public int size() {
        return listSp.size();
    }

    public SanPham get(int i) {
        return listSp.get(i);
    }
}