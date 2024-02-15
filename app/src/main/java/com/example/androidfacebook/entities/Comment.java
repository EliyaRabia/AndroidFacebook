package com.example.androidfacebook.entities;

public class Comment {
    private int id;
    private String fullname;
    private String text;
    private byte[] icon;
    private boolean editMode;


    public Comment(int id,String fullname,String text,byte[] icon){
        this.id=id;
        this.fullname=fullname;
        this.text=text;
        this.icon=icon;
        this.editMode=false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }
    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }
}
