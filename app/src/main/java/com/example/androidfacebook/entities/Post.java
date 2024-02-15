package com.example.androidfacebook.entities;
import java.io.Serializable;
import java.util.List;

public class Post implements Serializable {
    private int id;

    private String fullname;
    private byte[] icon;
    private String initialText;
    private String time;
    private int likes;
    private int commentsNumber;
    private byte[] pictures;
    private List<Comment> comments;
    private boolean liked;

    public Post(int id, String fullname, byte[] icon, String initialText, byte[] pictures, String time, int likes, int commentsNumber, List<Comment> comments) {
        this.id = id;
        this.fullname = fullname;
        this.icon = icon;
        this.initialText = initialText;
        this.time = time;
        this.pictures = pictures;
        this.likes = likes;
        this.commentsNumber = commentsNumber;
        this.comments = comments;
        this.liked = false;
    }

    public Post(int id, String fullname, byte[] icon, String initialText, String time, int likes, int commentsNumber, List<Comment> comments) {
        this.id = id;
        this.fullname = fullname;
        this.icon = icon;
        this.initialText = initialText;
        this.time = time;
        this.pictures = null;
        this.likes = likes;
        this.commentsNumber = commentsNumber;
        this.comments = comments;
        this.liked = false;
    }

    public int getId() {
        return this.id;
    }

    public String getFullname() {
        return fullname;
    }

    public byte[] getIcon() {
        return icon;
    }


    public String getInitialText() {
        return initialText;
    }

    public void setInitialText(String initialText) {
        this.initialText = initialText;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getTime() {
        return time;
    }

    public byte[] getPictures() {
        return pictures;
    }

    public void setPictures(byte[] pictures) {
        this.pictures = pictures;
    }

    public int getCommentsNumber() {
        return commentsNumber;
    }

    public void setCommentsNumber(int commentsNumber) {
        this.commentsNumber = commentsNumber;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public boolean isLiked() {
        return liked;

    }

    public void setLiked(boolean liked) {
        this.liked = liked;

    }
}
