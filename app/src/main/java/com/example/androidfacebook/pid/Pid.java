package com.example.androidfacebook.pid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfacebook.R;
import com.example.androidfacebook.entities.Post;
import com.example.androidfacebook.entities.User;

import android.os.Bundle;

import com.example.androidfacebook.adapters.PostsListAdapter;

import java.util.*;

public class Pid extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pid);
        User user = (User)getIntent().getSerializableExtra("USER");
        //the user that is in the pid now





        RecyclerView lstPosts = findViewById(R.id.lstPosts);
        final PostsListAdapter adapter = new PostsListAdapter(this);
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));
        User a = new User("foobar","foo123","foo bar",null);
        Post p1 = new Post(1,a,"Hello There","12.2.2024",R.drawable.picture1);
        Post p2 = new Post(2,a,"Hey Facebook","13.2.2024",R.drawable.picture1);
        Post p3 = new Post(2,user,"Hey Facebook","13.2.2024",R.drawable.picture1);
        List <Post> posts;
        posts = new ArrayList<>();
        posts.add(p1);
        posts.add(p2);
        posts.add(p3);
        adapter.setPosts(posts);

    }
}