package com.example.androidfacebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;
import java.util.*;

import com.example.androidfacebook.entities.User;
import com.example.androidfacebook.login.Login;


public class MainActivity extends Activity {
    public List<User> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userList = new ArrayList<>(); /*initialize the array of users*/
        Intent i = new Intent(this, Login.class);
        i.putExtra("LIST", (Serializable) userList); /*adding the list to the intent*/
        startActivity(i);
    }
}
