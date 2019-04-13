package com.example.tuan6_bai4;

public class MatHang {
    private String id;
    private String name;

    public MatHang() {}

    public MatHang(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.id + "-" + this.name;
    }
}
