package com.example.androidfacebook;

import android.app.Activity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfacebook.Pid;
import com.example.androidfacebook.adapters.PostsListAdapter;
import com.example.androidfacebook.entities.Post;
import com.example.androidfacebook.entities.User;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }
}
