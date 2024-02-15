package com.example.androidfacebook.pid;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfacebook.R;
import com.example.androidfacebook.adapters.PostsListAdapter;
import com.example.androidfacebook.entities.AddPost;
import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.entities.DataHolder;
import com.example.androidfacebook.entities.Post;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class Pid extends AppCompatActivity {


    @SuppressLint({"MissingInflatedId", "WrongThread"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pid);
        ClientUser user = (ClientUser)getIntent().getSerializableExtra("USER");
        if(user==null){
            return;
        }
        List<Post> postList = DataHolder.getInstance().getPostList();
        //the user that is in the pid now

        RecyclerView lstPosts = findViewById(R.id.lstPosts);
        final PostsListAdapter adapter = new PostsListAdapter(this);
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.picture1);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        adapter.setPosts(postList, user);

        Button btnAddPost = findViewById(R.id.btnAddPost);
        btnAddPost.setOnClickListener(v->{
            Intent i = new Intent(this, AddPost.class);
            i.putExtra("USER", user);
            DataHolder.getInstance().setPostList(postList);
            startActivity(i);

        });

    }
}