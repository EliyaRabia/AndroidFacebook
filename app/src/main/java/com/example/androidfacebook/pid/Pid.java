package com.example.androidfacebook.pid;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfacebook.R;
import com.example.androidfacebook.adapters.PostsListAdapter;
import com.example.androidfacebook.addspages.AddPost;
import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.entities.DataHolder;
import com.example.androidfacebook.entities.Post;
import com.example.androidfacebook.entities.User;
import com.example.androidfacebook.login.Login;

import java.io.ByteArrayOutputStream;
import java.util.List;
/*
    Pid class is the main class for the user to see posts and add new posts
 */
public class Pid extends AppCompatActivity {
    @SuppressLint({"MissingInflatedId", "WrongThread"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pid);
        // Get the user that is in the pid now
        ClientUser user = (ClientUser)getIntent().getSerializableExtra("USER");
        if(user==null){
            return;
        }
        // Get the posts from the data holder
        List<Post> postList = DataHolder.getInstance().getPostList();
        RecyclerView lstPosts = findViewById(R.id.lstPosts);
        // Set the adapter for the recycler view
        final PostsListAdapter adapter = new PostsListAdapter(this);
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));
        // Set the posts and the user to the adapter
        adapter.setPosts(postList, user);

        Button btnAddPost = findViewById(R.id.btnAddPost);
        // When the user clicks on the add post button,
        // the user will be redirected to the add post page
        btnAddPost.setOnClickListener(v->{
            Intent i = new Intent(this, AddPost.class);
            i.putExtra("USER", user);
            DataHolder.getInstance().setPostList(postList);
            startActivity(i);
        });
        ImageButton menuIcon = findViewById(R.id.menuIcon);
        menuIcon.setOnClickListener(v -> showPopupMenu(v));



    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
    }
    // Show the popup menu
    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.navi_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if(id == R.id.action_darkMode) {
                // Handle dark mode action
                int nightMode = AppCompatDelegate.getDefaultNightMode();
                if(nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    // Change to dark mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                return true;
            }
            if(id == R.id.action_logOut) {
                // Handle logout action
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
        popupMenu.show();
    }
}