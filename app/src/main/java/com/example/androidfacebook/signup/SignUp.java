package com.example.androidfacebook.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.androidfacebook.R;
import com.example.androidfacebook.login.Login;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Button btnGoBack = findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(v -> {
            // go to the sign up page.
            Intent i = new Intent(this, Login.class);
            startActivity(i);
        });
    }
}