package com.example.cuoiki.note;

import java.io.Serializable;

public class Notes implements Serializable {
    private int id; // default value
    private String noteContent;
    private String noteTitle;
    private long noteDate;

    private boolean checked = false;

    public Notes() {
    }

    public Notes(int id, String noteTitle, String noteContent, long noteDate) {
        this.id = id;
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.noteDate = noteDate;
    }

    public Notes(String noteTitle, String noteContent, long noteDate) {
        this.noteContent = noteContent;
        this.noteTitle = noteTitle;
        this.noteDate = noteDate;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public long getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(long noteDate) {
        this.noteDate = noteDate;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "Notes{" +
                "id=" + id +
                ", noteContent='" + noteContent + '\'' +
                ", noteTitle='" + noteTitle + '\'' +
                ", noteDate=" + noteDate +
                '}';
    }
}