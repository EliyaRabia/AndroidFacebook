package com.example.androidfacebook.entities;

import java.util.List;

public class DataHolder {
    private static DataHolder instance;
    private List<Post> postList;
    private List<Comment> comments;
    private Post currentPost;
    private List<User> userList;

    private DataHolder() {
    }

    public static synchronized DataHolder getInstance() {
        if (instance == null) {
            instance = new DataHolder();
        }
        return instance;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

    public List<Post> getPostList() {
        return postList;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    public List<Comment> getComments() {
        return comments;
    }

    public Post getCurrentPost() {
        return currentPost;
    }

    public void setCurrentPost(Post currentPost) {
        this.currentPost = currentPost;
    }
    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
    public List<User> getUserList() {
        return userList;
    }
}