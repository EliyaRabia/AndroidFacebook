package com.example.androidfacebook.users;

public class User {
    private final String emailOrPhone;
    private final String password;
    private final String displayName;
    public User(String emailOrPhone, String password, String displayName) {
        this.emailOrPhone = emailOrPhone;
        this.password = password;
        this.displayName = displayName;
    }

    public String getEmailOrPhone() {
        return emailOrPhone;
    }

    public String getPassword() {
        return password;
    }

    public String getDisplayName(){
        return displayName;
    }
}

