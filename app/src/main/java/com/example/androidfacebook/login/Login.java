package com.example.androidfacebook.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidfacebook.api.UserAPI;
import com.example.androidfacebook.entities.DataHolder;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.pid.Pid;
import com.example.androidfacebook.signup.SignUp;
import com.example.androidfacebook.R;
import com.example.androidfacebook.entities.User;
import com.example.androidfacebook.entities.Post;
import com.example.androidfacebook.entities.Comment;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
//import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
    This class is the main class for the login page.
 */
public class Login extends AppCompatActivity {
    public static String ServerIP = "10.0.2.2";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        List<User>userList=DataHolder.getInstance().getUserList();

        // get what the user has entered in the email and password fields
        EditText emailOrPhoneEditText = findViewById(R.id.editText);
        EditText passwordEditText = findViewById(R.id.editText2);
        Button btnLogin = findViewById(R.id.button);
        Button btnSignUp = findViewById(R.id.button2);
        // when the user clicks the login button
        btnLogin.setOnClickListener(v -> {
            String emailOrPhone = emailOrPhoneEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            UserAPI usersApi = new UserAPI(ServerIP);
            Intent intent2 = new Intent(this, Pid.class);
            usersApi.loginServer(emailOrPhone, password, new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        String status = String.valueOf(response.code());
                        String userID="";
                        String token="";
                        try {
                            String responseBody = response.body().string();
                            JSONObject jsonResponse = new JSONObject(responseBody);
                            userID = jsonResponse.getString("userId");
                            token = jsonResponse.getString("token");
                            // Now you have the user ID, you can use it as needed
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            // Handle JSON parsing exception or IO exception
                        }
                            //String token = response.body().string();
                            if (status.equals("200")) {

                                String finalToken = token;
                                usersApi.getUserData(token,userID, new Callback<ClientUser>() {
                                    @Override
                                    public void onResponse(Call<ClientUser> call, Response<ClientUser> response) {
                                        if(response.isSuccessful()){
                                            ClientUser currectUser = response.body();
                                            DataHolder.getInstance().setUserLoggedIn(currectUser);
                                            intent2.putExtra("token", finalToken);
                                            startActivity(intent2);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ClientUser> call, Throwable t) {
                                        Toast.makeText(Login.this,
                                                "failed to load this user",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                            else {
                                // Handle the case when the status is not 200
                                showCustomToast("Login request failed 404");
                            }

                    } else {
                        // Handle unsuccessful response
                        showCustomToast("invalid username or password");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    showCustomToast("Invalid server call!");
                }
            });
            // check if the user has entered the correct email and password
            /*
            boolean isAuthenticated = false;
            ClientUser u=null;
            // loop through the list of users to check if the user has entered the correct username
            // and password
            for (User user : userList) {
                if (user.getUsername().equals(emailOrPhone)
                        && user.getPassword().equals(password)) {
                    // if the user has entered the correct username and password
                    isAuthenticated = true;
                    // save the user details in u
                    u=new ClientUser(user.getUsername(),user.getDisplayName(),user.getPhoto());
                    break;
                }
            }
            // if the user has entered the correct username and password
            if (isAuthenticated) {
                // Successful login, need to change to Pid
                Intent i = new Intent(this, Pid.class);
                i.putExtra("USER", u);
                startActivity(i);
            } else {
                // Invalid inputs
                Toast.makeText(Login.this,
                        "Invalid email or password",
                        Toast.LENGTH_SHORT).show();
            }*/
        });
        // when the user clicks the sign up button
        btnSignUp.setOnClickListener(v -> {
            // go to the sign up page.
            Intent i = new Intent(this, SignUp.class);
            startActivity(i);
        });
    }
    public void showCustomToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_warning,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        TextView text = layout.findViewById(R.id.toast_text);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP, 0, 32);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
    }
}