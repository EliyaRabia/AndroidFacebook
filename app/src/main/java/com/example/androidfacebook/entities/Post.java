package com.example.androidfacebook.entities;

public class Post {
    private int id;

    private User user;
    private String initialText;
    private String time;
    private int likes;
    private boolean liked;
    private int commentsNumber;
    private int pictures;
    private Comment[] comments;

    public Post(int id,User user,String initialText,String time,int pictures){
        this.id=id;
        this.user=user;
        this.initialText=initialText;
        this.time=time;
        this.pictures=pictures;
        this.likes=0;
        this.liked = false;
        this.commentsNumber=0;
        this.comments=null;

    }
    /* A constructor when there is no pic in the post*/
    public Post(int id,User user,String initialText,String time){
        this.id=id;
        this.user=user;
        this.initialText=initialText;
        this.time=time;
        this.pictures=0;
        this.likes=0;
        this.liked = false;
        this.commentsNumber=0;
        this.comments=null;

    }
    public int getId(){
        return this.id;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public int getPictures() {
        return pictures;
    }

    public void setPictures(int pictures) {
        this.pictures = pictures;
    }

    public int getCommentsNumber() {
        return commentsNumber;
    }

    public void setCommentsNumber(int commentsNumber) {
        this.commentsNumber = commentsNumber;
    }

    public Comment[] getComments() {
        return comments;
    }

    public void setComments(Comment[] comments) {
        this.comments = comments;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

}

