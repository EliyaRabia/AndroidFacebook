package com.example.androidfacebook.entities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidfacebook.R;
import com.example.androidfacebook.pid.Pid;

public class AddPost extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        Intent addPostIntent = getIntent();

        Button btnDelete = findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(v -> {
        // Navigate to addPost activity
        Intent intent = new Intent(this, Pid.class);
        startActivity(intent);
        });
    }

}