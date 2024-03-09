package com.example.androidfacebook.entities;

import java.util.List;
import java.util.Stack;

/*
this class is used to store the data that is used in the application
like the posts, comments, and the current post and the user list
 */
public class DataHolder {
    private static DataHolder instance;
    private String friendProfileId;
    private List<Post> postList;
    private List<Comment> comments;

    private Stack<String> stackOfIDs;
    private Post currentPost;
    private Post editposter;
    private String token;

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private ClientUser userLoggedIn;

    private String userLoggedInID;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserLoggedInID() {
        return userLoggedInID;
    }

    public void setUserLoggedInID(String userLoggedInID) {
        this.userLoggedInID = userLoggedInID;
    }


    private List<User> userList;

    private DataHolder() {
    }

    public static synchronized DataHolder getInstance() {
        if (instance == null) {
            instance = new DataHolder();
        }
        return instance;
    }

    public ClientUser getUserLoggedIn() {
        return userLoggedIn;
    }

    public void setUserLoggedIn(ClientUser userLoggedIn) {
        this.userLoggedIn = userLoggedIn;
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

    public Post getEditposter() {
        return editposter;
    }

    public void setEditposter(Post editposter) {
        this.editposter = editposter;
    }
    public String getFriendProfileId() {
        return friendProfileId;
    }
    public void setFriendProfileId(String friendProfileId) {
        this.friendProfileId = friendProfileId;
    }
    public Stack<String> getStackOfIDs() {
        return stackOfIDs;
    }

    public void setStackOfIDs(Stack<String> stackOfIDs) {
        this.stackOfIDs = stackOfIDs;
    }
}