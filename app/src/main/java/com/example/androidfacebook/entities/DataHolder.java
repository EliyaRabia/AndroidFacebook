package com.example.androidfacebook.entities;

import java.util.List;

public class DataHolder {
    private static DataHolder instance;
    private List<Post> postList;

    private DataHolder() {}

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
}