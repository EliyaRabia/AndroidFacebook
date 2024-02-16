package com.example.androidfacebook.comments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfacebook.R;
import com.example.androidfacebook.adapters.CommentListAdapter;
import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.entities.Comment;
import com.example.androidfacebook.entities.DataHolder;
import com.example.androidfacebook.entities.Post;
import com.example.androidfacebook.login.Login;
import com.example.androidfacebook.pid.Pid;

import java.util.List;

public class CommentPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_page);
        // Get the current user and the current post from the intent and the DataHolder
        List<Comment> CommentList = DataHolder.getInstance().getComments();
        List<Post> postList = DataHolder.getInstance().getPostList();
        ClientUser user = (ClientUser)getIntent().getSerializableExtra("USER");
        Post currentPost = DataHolder.getInstance().getCurrentPost();

        RecyclerView lstComments = findViewById(R.id.lstComments);
        final CommentListAdapter adapter = new CommentListAdapter(this);
        lstComments.setAdapter(adapter);
        lstComments.setLayoutManager(new LinearLayoutManager(this));
        adapter.setComments(CommentList, user, currentPost, postList);

        Button btnGoBackToPid = findViewById(R.id.btnGoBackToPid);
        Button btnAddComment = findViewById(R.id.addCommentButton);
        // Set the onClickListeners for the buttons
        btnGoBackToPid.setOnClickListener(v ->{
            Intent i = new Intent(this, Pid.class);
            // Set the user and the post in the DataHolder and start the activity
            int postIndex = postList.indexOf(currentPost);
            postList.get(postIndex).setComments(CommentList);
            DataHolder.getInstance().setPostList(postList);
            i.putExtra("USER", user);
            startActivity(i);
        });
        // Set the onClickListener for the add comment button
        btnAddComment.setOnClickListener(v->{
            EditText e = findViewById(R.id.commentTextView);
            String s = e.getText().toString();
            // Check if the comment is empty
            if(s.length()==0){
                Toast.makeText(CommentPage.this,
                        "You can't add a blank comment!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            // Add the comment to the list and start the activity
            Comment newC= new Comment(CommentList.size()+1,user.getDisplayName(),s,user.getPhoto());
            CommentList.add(newC);
            postList.get(postList.indexOf(currentPost)).setComments(CommentList);
            postList.get(postList.indexOf(currentPost)).setCommentsNumber(postList.get(postList.indexOf(currentPost)).getCommentsNumber()+1);
            Intent i = new Intent(this,CommentPage.class);
            DataHolder.getInstance().setComments(CommentList);
            DataHolder.getInstance().setPostList(postList);
            DataHolder.getInstance().setCurrentPost(currentPost);
            i.putExtra("USER", user);
            startActivity(i);
        });

    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
    }
}