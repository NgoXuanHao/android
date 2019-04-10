package com.example.tuan6_bai2;

public class Player {
    public String name;
    public String club;
    public String star;
    public int img;

    public Player(int img, String name, String star, String club){
        this.name = name;
        this.club = club;
        this.star = star;
        this.img = img;

    }
    public void Player(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
