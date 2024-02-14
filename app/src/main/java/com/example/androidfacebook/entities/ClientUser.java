package com.example.androidfacebook.entities;

import java.io.Serializable;

public class ClientUser implements Serializable {
    private String username;
    private String displayName;
    private byte[] photo;



    public ClientUser(String username, String displayName, byte[] photo){
        this.username=username;
        this.displayName=displayName;
        this.photo=photo;
    }
    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
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
}
