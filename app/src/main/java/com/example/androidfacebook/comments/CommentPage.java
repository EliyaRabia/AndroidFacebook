package com.example.androidfacebook.comments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfacebook.R;
import com.example.androidfacebook.adapters.CommentListAdapter;
import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.entities.Comment;
import com.example.androidfacebook.entities.DataHolder;
import com.example.androidfacebook.entities.Post;
import com.example.androidfacebook.pid.Pid;

import java.util.List;

public class CommentPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_page);
        List<Comment> CommentList = DataHolder.getInstance().getComments();
        List<Post> postList = DataHolder.getInstance().getPostList();
        ClientUser user = (ClientUser)getIntent().getSerializableExtra("USER");
        Post currentPost = DataHolder.getInstance().getCurrentPost();

        RecyclerView lstComments = findViewById(R.id.lstComments);
        final CommentListAdapter adapter = new CommentListAdapter(this);
        lstComments.setAdapter(adapter);
        lstComments.setLayoutManager(new LinearLayoutManager(this));
        adapter.setComments(CommentList);

        Button btnGoBackToPid = findViewById(R.id.btnGoBackToPid);
        btnGoBackToPid.setOnClickListener(v ->{
            Intent i = new Intent(this, Pid.class);
            int postIndex = postList.indexOf(currentPost);
            postList.get(postIndex).setComments(CommentList);
            DataHolder.getInstance().setPostList(postList);
            i.putExtra("USER", user);
            startActivity(i);
        });
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
    }
}