package com.example.cuoiki.item;

public class Item {
    private int id;
    private int id_task;
    private String title;
    private boolean check;
    private boolean kt;
    private boolean edit;
    private boolean del;

    public Item(int id, int id_task, String title, boolean check) {
        this.id = id;
        this.id_task = id_task;
        this.title = title;
        this.check = check;
    }

    public Item(int id_task, String title, boolean check) {
        this.id_task = id_task;
        this.title = title;
        this.check = check;
    }

    public Item() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_task() {
        return id_task;
    }

    public void setId_task(int id_task) {
        this.id_task = id_task;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean isKt() {
        return kt;
    }

    public void setKt(boolean kt) {
        this.kt = kt;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public boolean isDel() {
        return del;
    }

    public void setDel(boolean del) {
        this.del = del;
    }
}
