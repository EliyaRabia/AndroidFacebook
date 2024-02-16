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

public class Pid extends AppCompatActivity {
    private List<User> userList;



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
        ImageButton menuIcon = findViewById(R.id.menuIcon);
        menuIcon.setOnClickListener(v -> showPopupMenu(v));



    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
    }

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