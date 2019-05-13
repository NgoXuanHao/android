package com.example.quanlytour.phieu;

import java.util.Date;

public class Phieu {
    private int soPhieu;
    private String ngayDk;
    private String maKH_phieu;
    private String maTour_phieu;
    private int soNguoi;

    public Phieu() {
    }

    public Phieu(int soPhieu, String ngayDk, String maKH_phieu, String maTour_phieu, int soNguoi) {
        this.soPhieu = soPhieu;
        this.ngayDk = ngayDk;
        this.maKH_phieu = maKH_phieu;
        this.maTour_phieu = maTour_phieu;
        this.soNguoi = soNguoi;
    }

    public int getSoPhieu() {
        return soPhieu;
    }

    public void setSoPhieu(int soPhieu) {
        this.soPhieu = soPhieu;
    }

    public String getNgayDk() {
        return ngayDk;
    }

    public void setNgayDk(String ngayDk) {
        this.ngayDk = ngayDk;
    }

    public String getMaKH_phieu() {
        return maKH_phieu;
    }

    public void setMaKH_phieu(String maKH_phieu) {
        this.maKH_phieu = maKH_phieu;
    }

    public String getMaTour_phieu() {
        return maTour_phieu;
    }

    public void setMaTour_phieu(String maTour_phieu) {
        this.maTour_phieu = maTour_phieu;
    }

    public int getSoNguoi() {
        return soNguoi;
    }

    public void setSoNguoi(int soNguoi) {
        this.soNguoi = soNguoi;
    }
}
