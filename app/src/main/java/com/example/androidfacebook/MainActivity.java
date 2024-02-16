package com.example.androidfacebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;
import java.util.*;

import com.example.androidfacebook.entities.DataHolder;
import com.example.androidfacebook.entities.User;
import com.example.androidfacebook.login.Login;


public class MainActivity extends Activity {
    public List<User> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userList = new ArrayList<>(); /*initialize the array of users*/
        userList.add(new User("user1","pass1",
                "Eliya Rabia",null));
        userList.add(new User("user2","pass2",
                "Or Shmuel",null));
        userList.add(new User("user3","pass3",
                "Ofek Yemini",null));
        Intent i = new Intent(this, Login.class);
        DataHolder.getInstance().setUserList(userList);
        startActivity(i);
    }
}
