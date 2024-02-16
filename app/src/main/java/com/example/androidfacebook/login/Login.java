package com.example.androidfacebook.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.androidfacebook.entities.DataHolder;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.pid.Pid;
import com.example.androidfacebook.signup.SignUp;
import com.example.androidfacebook.R;
import com.example.androidfacebook.entities.User;
import com.example.androidfacebook.entities.Post;
import com.example.androidfacebook.entities.Comment;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
//import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
/*
    This class is the main class for the login page.
 */
public class Login extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        List<User>userList=DataHolder.getInstance().getUserList();

        // get what the user has entered in the email and password fields
        EditText emailOrPhoneEditText = findViewById(R.id.editText);
        EditText passwordEditText = findViewById(R.id.editText2);
        Button btnLogin = findViewById(R.id.button);
        Button btnSignUp = findViewById(R.id.button2);
        // when the user clicks the login button
        btnLogin.setOnClickListener(v -> {
            String emailOrPhone = emailOrPhoneEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            // check if the user has entered the correct email and password
            boolean isAuthenticated = false;
            ClientUser u=null;
            // loop through the list of users to check if the user has entered the correct username
            // and password
            for (User user : userList) {
                if (user.getUsername().equals(emailOrPhone)
                        && user.getPassword().equals(password)) {
                    // if the user has entered the correct username and password
                    isAuthenticated = true;
                    // save the user details in u
                    u=new ClientUser(user.getUsername(),user.getDisplayName(),user.getPhoto());
                    break;
                }
            }
            // if the user has entered the correct username and password
            if (isAuthenticated) {
                // Successful login, need to change to Pid
                Intent i = new Intent(this, Pid.class);
                i.putExtra("USER", u);
                startActivity(i);
            } else {
                // Invalid inputs
                Toast.makeText(Login.this,
                        "Invalid email or password",
                        Toast.LENGTH_SHORT).show();
            }
        });
        // when the user clicks the sign up button
        btnSignUp.setOnClickListener(v -> {
            // go to the sign up page.
            Intent i = new Intent(this, SignUp.class);
            startActivity(i);
        });
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
    }
}