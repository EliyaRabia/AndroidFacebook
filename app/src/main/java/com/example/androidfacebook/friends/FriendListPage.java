package com.example.androidfacebook.friends;

import static com.example.androidfacebook.login.Login.ServerIP;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.androidfacebook.R;
import com.example.androidfacebook.adapters.FriendsListAdapter;
import com.example.androidfacebook.api.AppDB;
import com.example.androidfacebook.api.UserAPI;
import com.example.androidfacebook.api.UserDao;
import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.entities.DataHolder;
import com.example.androidfacebook.models.UsersViewModel;
import com.example.androidfacebook.notification.NotificationPage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendListPage extends AppCompatActivity {
    private String token;
    private AppDB appDB;
    private UserDao userDao;
    private TextView nameOfFriendTextView;
    private ClientUser userLoggedIn;
    private ClientUser viewedFriendUser;
    private UsersViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list_page);
        viewModel= new ViewModelProvider(this).get(UsersViewModel.class);
        nameOfFriendTextView = findViewById(R.id.nameOfFriend);

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
        userLoggedIn = currentUser[0];
        Button goBack = findViewById(R.id.goBackButton);
        goBack.setOnClickListener(view -> {
            finish();
        });
    }
    @SuppressLint("SetTextI18n")
    protected void onResume() {
        super.onResume();
        getUser();
    }
    public void getUser(){
        UserAPI userAPI = new UserAPI(ServerIP);
        token = DataHolder.getInstance().getToken();
        String viewedFriendUserId = DataHolder.getInstance().getFriendProfileId();


        userAPI.getUserData(token, viewedFriendUserId, new Callback<ClientUser>() {
            @Override
            public void onResponse(Call<ClientUser> call, Response<ClientUser> response) {
                if (response.isSuccessful()) {
                    viewedFriendUser = response.body();
                    nameOfFriendTextView.setText(viewedFriendUser.getDisplayName() + "'s Friends");
                    List<String> lif = viewedFriendUser.getFriendsList();
                    List<ClientUser> friends = new ArrayList<>();
                    AtomicInteger requestCount = new AtomicInteger(lif.size());
                    for (String f : lif) {
                        userAPI.getUserData(token, f, new Callback<ClientUser>() {
                            @Override
                            public void onResponse(Call<ClientUser> call, Response<ClientUser> response) {
                                if (response.isSuccessful()) {
                                    ClientUser currentUser = response.body();
                                    friends.add(currentUser);
                                } else {
                                    Toast.makeText(FriendListPage.this,
                                            "failed to load this friend",
                                            Toast.LENGTH_SHORT).show();
                                }
                                if (requestCount.decrementAndGet() == 0) {
                                    // All requests are completed, update UI
                                    final FriendsListAdapter adapter = new FriendsListAdapter(FriendListPage.this);
                                    RecyclerView lstFriends = findViewById(R.id.lstFriends);
                                    lstFriends.setAdapter(adapter);
                                    lstFriends.setLayoutManager(new LinearLayoutManager(FriendListPage.this));
                                    viewModel.setUsers(friends);
                                    viewModel.get().observe(FriendListPage.this, frie -> {
                                        adapter.setFriends(frie,userLoggedIn,viewedFriendUser);
                                    });
                                }
                            }

                            @Override
                            public void onFailure(Call<ClientUser> call, Throwable t) {
                                Toast.makeText(FriendListPage.this,
                                        "Invalid call from server",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<ClientUser> call, Throwable t) {
                t.printStackTrace();
            }

        });
    }
}