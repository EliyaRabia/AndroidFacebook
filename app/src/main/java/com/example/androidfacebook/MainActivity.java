package com.example.androidfacebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.androidfacebook.login.Login;


public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = new Intent(this, Login.class);
        startActivity(i);

    }
}
