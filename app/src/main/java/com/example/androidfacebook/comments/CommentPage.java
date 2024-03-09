package com.example.androidfacebook.comments;

import static com.example.androidfacebook.login.Login.ServerIP;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.androidfacebook.R;
import com.example.androidfacebook.adapters.CommentListAdapter;
import com.example.androidfacebook.adapters.PostsListAdapter;
import com.example.androidfacebook.addspages.AddPost;
import com.example.androidfacebook.api.AppDB;
import com.example.androidfacebook.api.CommentDao;
import com.example.androidfacebook.api.PostAPI;
import com.example.androidfacebook.api.UserAPI;
import com.example.androidfacebook.api.UserDao;
import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.entities.Comment;
import com.example.androidfacebook.entities.CommentNoID;
import com.example.androidfacebook.entities.DataHolder;
import com.example.androidfacebook.entities.Post;
import com.example.androidfacebook.models.CommentsViewModel;
import com.example.androidfacebook.models.PostsViewModel;
import com.example.androidfacebook.pid.Pid;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentPage extends AppCompatActivity {
    private String token;
    private AppDB appDB;
    private UserDao userDao;
    private ClientUser userLoggedIn;
    private ClientUser postUser;
    private List<Comment> comments;

    private CommentDao commentDao;

    private CommentsViewModel viewModel;
    private Post currentPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_page);
        token = DataHolder.getInstance().getToken();
        currentPost = DataHolder.getInstance().getCurrentPost();
        viewModel= new ViewModelProvider(this).get(CommentsViewModel.class);

        String userLoggedInId = DataHolder.getInstance().getUserLoggedInID();
        token = DataHolder.getInstance().getToken();
        appDB = Room.databaseBuilder(getApplicationContext(), AppDB.class, "facebookDB")
                .fallbackToDestructiveMigration()
                .build();
        userDao = appDB.userDao();
        final ClientUser[] currentUser = new ClientUser[1];
        CountDownLatch latch = new CountDownLatch(1); // Create a CountDownLatch with a count of 1

        new Thread(() -> {
            currentUser[0] = appDB.userDao().getUserById(userLoggedInId);
            latch.countDown(); // Decrease the count
        }).start();

        try {
            latch.await(); // Main thread waits here until count reaches zero
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        userLoggedIn = currentUser[0];
        UserAPI usersApi = new UserAPI(ServerIP);
        String userPostId = currentPost.getIdUserName();

        usersApi.getUserData(token,userPostId, new Callback<ClientUser>() {
            @Override
            public void onResponse(Call<ClientUser> call, Response<ClientUser> response) {
                if (response.isSuccessful()) {
                    ClientUser currectUser = response.body();

                    postUser = currectUser;
                }
            }
            @Override
            public void onFailure(Call<ClientUser> call, Throwable t) {
                Toast.makeText(CommentPage.this,
                        "Invalid Call from server",
                        Toast.LENGTH_SHORT).show();
            }
        });


        Button btnGoBackToPid = findViewById(R.id.btnGoBackToPid);
        Button btnAddComment = findViewById(R.id.addCommentButton);
        // Set the onClickListeners for the buttons
        btnGoBackToPid.setOnClickListener(v ->{
            DataHolder.getInstance().setCurrentPost(currentPost);
            finish();

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
            UserAPI userAPI = new UserAPI(ServerIP);
            CommentNoID cid = new CommentNoID(userLoggedInId,userLoggedIn.getDisplayName(),userLoggedIn.getPhoto(),currentPost.getId(),s);
            userAPI.createComment(token, cid, userLoggedInId, new Callback<Comment>() {
                @Override
                public void onResponse(Call<Comment> call, Response<Comment> response) {
                    if(response.isSuccessful()){
                        Comment c = response.body();

                        Toast.makeText(CommentPage.this, "Comment added successfully", Toast.LENGTH_SHORT).show();
                        new Thread(() -> {
                            appDB.commentDao().insert(c);
                        }).start();
                        List <String> s = currentPost.getComments();
                        s.add(c.get_id());
                        currentPost.setComments(s);
                        e.setText("");
                        doTheRender();

                    }


                }

                @Override
                public void onFailure(Call<Comment> call, Throwable t) {
                    Toast.makeText(CommentPage.this, "Invalid Server Call", Toast.LENGTH_SHORT).show();
                }
            });

        });

    }
    protected void onResume() {
        super.onResume();
        doTheRender();
        UserAPI usersApi = new UserAPI(ServerIP);
        String userID = DataHolder.getInstance().getUserLoggedInID();
        currentPost=DataHolder.getInstance().getCurrentPost();

        usersApi.getUserData(token,userID, new Callback<ClientUser>() {
            @Override
            public void onResponse(Call<ClientUser> call, Response<ClientUser> response) {
                if (response.isSuccessful()) {
                    ClientUser currectUser = response.body();
                    appDB = Room.databaseBuilder(getApplicationContext(), AppDB.class, "facebookDB")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                    userDao = appDB.userDao();
                    new Thread(() -> {
                        userDao.insert(currectUser);

                    }).start();
                    userLoggedIn = currectUser;


                }
            }

            @Override
            public void onFailure(Call<ClientUser> call, Throwable t) {
                Toast.makeText(CommentPage.this,
                        "failed to load this page",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void doTheRender(){
        commentDao = appDB.commentDao();
        token = DataHolder.getInstance().getToken();
        PostAPI postsApi = new PostAPI(ServerIP);
        new Thread(() -> {
            commentDao.deleteAllComments();
        }).start();
        postsApi.getAllComments(token,currentPost.getId(), new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                comments = response.body();
                commentDao = appDB.commentDao();
                for (Comment c : comments) {
                    new Thread(() -> {
                        commentDao.insert(c);
                    }).start();
                }
                final CommentListAdapter adapter = new CommentListAdapter(CommentPage.this);
                RecyclerView lstComments = findViewById(R.id.lstComments);
                lstComments.setAdapter(adapter);
                lstComments.setLayoutManager(new LinearLayoutManager(CommentPage.this));
                viewModel.setComments(comments);
                viewModel.get().observe(CommentPage.this, come -> {
                    adapter.setComments(come, currentPost,postUser,userLoggedIn);
                });
            }


            @Override
            public void onFailure(retrofit2.Call<List<Comment>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
    }
}