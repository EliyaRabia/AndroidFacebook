package com.example.androidfacebook.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.pid.Pid;
import com.example.androidfacebook.signup.SignUp;
import com.example.androidfacebook.R;
import com.example.androidfacebook.entities.User;

import java.io.Serializable;
import java.util.*;

public class Login extends AppCompatActivity {
    @SuppressWarnings("unchecked")


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent mainIntent = getIntent();
        List<User>userList=(List<User>) mainIntent.getSerializableExtra("LIST");
        if(userList==null){
            finish();
            return;
        }


        userList.add(new User("user1","pass1",
                "Eliya Rabia",null));
        userList.add(new User("user2","pass2",
                "Or Shmuel",null));
        userList.add(new User("user3","pass3",
                "Ofek Yemini",null));


        EditText emailOrPhoneEditText = findViewById(R.id.editText);
        EditText passwordEditText = findViewById(R.id.editText2);
        Button btnLogin = findViewById(R.id.button);
        Button btnSignUp = findViewById(R.id.button2);

        btnLogin.setOnClickListener(v -> {
            String emailOrPhone = emailOrPhoneEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            boolean isAuthenticated = false;
            ClientUser u=null;
            for (User user : userList) {
                if (user.getUsername().equals(emailOrPhone)
                        && user.getPassword().equals(password)) {
                    isAuthenticated = true;
                    u=new ClientUser(user.getUsername(),user.getDisplayName(),user.getPhoto()); /* save the user details in u*/
                    break;
                }
            }

            if (isAuthenticated) {
                // Successful login, need to change to Pid
                Intent i = new Intent(this, Pid.class);

                i.putExtra("USER", u);
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
            i.putExtra("LIST", (Serializable) userList);
            startActivity(i);
        });
    }
}