package com.example.androidfacebook.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidfacebook.pid.Pid;
import com.example.androidfacebook.signup.SignUp;
import com.example.androidfacebook.R;
/*import com.example.androidfacebook.users.User;*/
import com.example.androidfacebook.entities.User;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {
    private List<User> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_login);

        userList = new ArrayList<>();

        /* eliya's code before ofek change the user class:
        userList.add(new User("user1@example.com",
                "password1","Eliya Rabia"));
        userList.add(new User("1234567890",
                "password2", "Or Shmuel"));
        userList.add(new User("ofekyemini87@gmail.com",
                "password3", "Ofek Yemini")); */

        userList.add(new User("user1","pass1",
                "Eliya Rabia",R.drawable.picture1));
        userList.add(new User("user2","pass2",
                "Or Shmuel",R.drawable.picture1));
        userList.add(new User("user3","pass3",
                "Ofek Yemini",R.drawable.picture1));


        EditText emailOrPhoneEditText = findViewById(R.id.editText);
        EditText passwordEditText = findViewById(R.id.editText2);
        Button btnLogin = findViewById(R.id.button);
        Button btnSignUp = findViewById(R.id.button2);

        btnLogin.setOnClickListener(v -> {
            String emailOrPhone = emailOrPhoneEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            boolean isAuthenticated = false;
            for (User user : userList) {
                if (user.getUsername().equals(emailOrPhone)
                        && user.getPassword().equals(password)) {
                    isAuthenticated = true;
                    break;
                }
            }

            if (isAuthenticated) {
                // Successful login, need to change to Pid
                Intent i = new Intent(this, Pid.class);
                startActivity(i);
                // Add your logic to proceed after successful login
            } else {
                // Invalid inputs
                Toast.makeText(Login.this,
                        "Invalid email or password",
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnSignUp.setOnClickListener(v -> {
            // go to the sign up page.
            Intent i = new Intent(this, SignUp.class);
            startActivity(i);
        });
    }
}