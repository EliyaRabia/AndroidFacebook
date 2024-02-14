package com.example.androidfacebook.pid;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfacebook.R;
import com.example.androidfacebook.adapters.PostsListAdapter;
import com.example.androidfacebook.entities.Post;
import com.example.androidfacebook.entities.User;

import java.util.ArrayList;
import java.util.List;

public class Pid extends AppCompatActivity {


    @SuppressLint("MissingInflatedId")
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



//        ImageButton likeButton;
//        likeButton = findViewById(R.id.likeButton);
//        likeButton.setOnClickListener(v -> {
//            // Get the position of the post associated with this like button
//            int position = lstPosts.getChildAdapterPosition((View) v.getParent().getParent());
//            // Get the post at the given position
//            Post post = adapter.getPosts().get(position);
//
//            // Check if the post is liked
//            if (post.isLiked()) {
//                // Change the icon to icon1
//                likeButton.setImageResource(R.drawable.like_svgrepo_com);
//                // Set liked to false
//                post.setLiked(false);
////                 Decrease the number of likes
////                post.setLikes(post.getLikes() - 1);
//            } else {
//                // Change the icon to icon2
//                likeButton.setImageResource(R.drawable.like_icon);
//                // Set liked to true
//                post.setLiked(true);
////                 Increase the number of likes
////                post.setLikes(post.getLikes() + 1);
//            }
//        });
    }
}