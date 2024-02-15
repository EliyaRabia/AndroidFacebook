package com.example.androidfacebook.entities;
import java.util.*;
public class Comment {
    private int id;
    private String fullname;
    private String text;
    private byte[] icon;

    public Comment(int id,String fullname,String text,byte[] icon){
        this.id=id;
        this.fullname=fullname;
        this.text=text;
        this.icon=icon;

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
}
