package com.example.androidfacebook.entities;

import static com.example.androidfacebook.login.Login.ServerIP;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.androidfacebook.R;
import com.example.androidfacebook.adapters.PostsListAdapter;
import com.example.androidfacebook.api.AppDB;
import com.example.androidfacebook.api.PostDao;
import com.example.androidfacebook.api.UserAPI;
import com.example.androidfacebook.api.UserDao;
import com.example.androidfacebook.models.PostsViewModel;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilePage extends AppCompatActivity {
    private AppDB appDB;
    private UserDao userDao;
    private ClientUser friendUser;
    private List<Post> FriendPostList;
    private PostDao postDao;
    private String token;

    private PostsViewModel viewModel;
    private ClientUser user;
    private TextView editTextName;
    private TextView editTextFriendsCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        viewModel= new ViewModelProvider(this).get(PostsViewModel.class);
        // Get the user that is in the pid now
        editTextName=findViewById(R.id.editText1);
        editTextFriendsCount=findViewById(R.id.editText2);
//        editTextName.setText(friendUser.getDisplayName());
//        editTextFriendsCount.setText("Friends " + String.valueOf(friendUser.getFriendsList().size()));

        String userId = DataHolder.getInstance().getUserLoggedInID();
        token = DataHolder.getInstance().getToken();
        appDB = Room.databaseBuilder(getApplicationContext(), AppDB.class, "facebookDB")
                .fallbackToDestructiveMigration()
                .build();
        userDao = appDB.userDao();
        final ClientUser[] currentUser = new ClientUser[1];
        CountDownLatch latch = new CountDownLatch(1); // Create a CountDownLatch with a count of 1

        new Thread(() -> {
            currentUser[0] = appDB.userDao().getUserById(userId);
            latch.countDown(); // Decrease the count
        }).start();

        try {
            latch.await(); // Main thread waits here until count reaches zero
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        user = currentUser[0];
    }



    @SuppressLint("SetTextI18n")
    protected void onResume() {
        super.onResume();
        postDao = appDB.postDao();
        new Thread(() -> {
            postDao.deleteAllPosts();
        }).start();
        UserAPI userAPI = new UserAPI(ServerIP);
        token = DataHolder.getInstance().getToken();
        String friendUserId = DataHolder.getInstance().getFriendProfileId();


        userAPI.getUserData(token, friendUserId, new Callback<ClientUser>() {
            @Override
            public void onResponse(Call<ClientUser> call, Response<ClientUser> response) {
                if (response.isSuccessful()) {
                    friendUser = response.body();
                }
            }

            @Override
            public void onFailure(Call<ClientUser> call, Throwable t) {
                t.printStackTrace();
            }
        });

        userAPI.getPostsByUser(token, friendUserId, new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    FriendPostList = response.body();
                    if( FriendPostList != null){
                    for(Post p:FriendPostList){
                        new Thread(() -> {
                            postDao.insert(p);
                        }).start();
                    }
                        final PostsListAdapter adapter = new PostsListAdapter(ProfilePage.this);
                        RecyclerView lstPosts = findViewById(R.id.lstPosts);
                        lstPosts.setAdapter(adapter);
                        lstPosts.setLayoutManager(new LinearLayoutManager(ProfilePage.this));
                        viewModel.setPosts(FriendPostList);
                        viewModel.get().observe(ProfilePage.this, posts -> {
                            adapter.setPosts(posts, user);
                        });
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void goToPid(View view) {
        finish();
    }
}