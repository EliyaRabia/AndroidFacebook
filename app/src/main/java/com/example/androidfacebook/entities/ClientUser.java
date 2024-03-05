package com.example.androidfacebook.entities;

import java.io.Serializable;

public class ClientUser implements Serializable {
    private String username;
    private String displayName;
    private String photo;
    private String[] postList;
    private String[] friendsList;
    private String[] friendRequests;
    private String[] friendRequestsSent;
    private String[] likes;
    private String[] comments;

    /*
    this class is used to store the user's information
     */
    public ClientUser(String username, String displayName, String photo,
                      String[] postList, String[] friendsList, String[] friendRequests,
                      String[] friendRequestsSent, String[] likes, String[] comments){
        this.username = username;
        this.displayName = displayName;
        this.photo = photo;
        this.postList = postList;
        this.friendsList = friendsList;
        this.friendRequests = friendRequests;
        this.friendRequestsSent = friendRequestsSent;
        this.likes = likes;
        this.comments = comments;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String[] getPostList() {
        return postList;
    }

    public void setPostList(String[] postList) {
        this.postList = postList;
    }

    public String[] getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(String[] friendsList) {
        this.friendsList = friendsList;
    }

    public String[] getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(String[] friendRequests) {
        this.friendRequests = friendRequests;
    }

    public String[] getFriendRequestsSent() {
        return friendRequestsSent;
    }

    public void setFriendRequestsSent(String[] friendRequestsSent) {
        this.friendRequestsSent = friendRequestsSent;
    }

    public String[] getLikes() {
        return likes;
    }

    public void setLikes(String[] likes) {
        this.likes = likes;
    }

    public String[] getComments() {
        return comments;
    }

    public void setComments(String[] comments) {
        this.comments = comments;
    }
}
