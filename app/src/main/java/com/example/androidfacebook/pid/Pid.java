package com.example.androidfacebook.pid;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfacebook.R;
import com.example.androidfacebook.adapters.PostsListAdapter;
import com.example.androidfacebook.entities.AddPost;
import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.entities.Post;

import java.util.ArrayList;
import java.util.List;

public class Pid extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pid);
        ClientUser user = (ClientUser)getIntent().getSerializableExtra("USER");
        if(user==null){
            return;
        }
        //the user that is in the pid now

        RecyclerView lstPosts = findViewById(R.id.lstPosts);
        final PostsListAdapter adapter = new PostsListAdapter(this);
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));
        Post p1 = new Post(1,"user1",null,"Hello There","12.2.2024",R.drawable.picture1);
        Post p2 = new Post(2,"user2",null,"Hey Facebook","13.2.2024",R.drawable.picture1);
        Post p3 = new Post(2,user.getDisplayName(), user.getPhoto(), "Hey Facebook","13.2.2024",R.drawable.picture1);
        List <Post> posts;
        posts = new ArrayList<>();
        posts.add(p1);
        posts.add(p2);
        posts.add(p3);
        adapter.setPosts(posts);

        Button btnAddPost = findViewById(R.id.btnAddPost);
        btnAddPost.setOnClickListener(v->{
            Intent i = new Intent(this, AddPost.class);
            i.putExtra("USER", user);
            startActivity(i);

        });

    }
}