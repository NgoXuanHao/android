package com.example.quanlytour.tour;

import java.io.Serializable;

public class Tour  implements Serializable {
    private String maTour;
    private String lotrinh;
    private int hanhtrinhTour;
    private int giaTour;
    public Tour(){}

    public Tour(String maTour, String lotrinh, int hanhtrinhTour, int giaTour){
        this.maTour = maTour;
        this.lotrinh = lotrinh;
        this.hanhtrinhTour = hanhtrinhTour;
        this.giaTour = giaTour;

    }
    public String getMaTour() {
        return maTour;
    }

    public void setMaTour(String maTour) {
        this.maTour = maTour;
    }

    public String getLotrinh() {
        return lotrinh;
    }

    public void setLotrinh(String lotrinh) {
        this.lotrinh = lotrinh;
    }


    public int getHanhtrinhTour() {
        return hanhtrinhTour;
    }

    public void setHanhtrinhTour(int hanhtrinhTour) {
        this.hanhtrinhTour = hanhtrinhTour;
    }

    public int getGiaTour() {
        return giaTour;
    }

    public void setGiaTour(int giaTour) {
        this.giaTour = giaTour;
    }
}