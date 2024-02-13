package com.example.androidfacebook.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidfacebook.R;
import com.example.androidfacebook.entities.User;
import com.example.androidfacebook.login.Login;

import java.io.Serializable;
import java.util.List;

public class SignUp extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private EditText confirmPassword;
    private EditText displayName;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Intent LoginIntent = getIntent();
        List<User> userList=(List<User>) LoginIntent.getSerializableExtra("LIST");
        if(userList==null){
            return;
        }
        /*now you have users in userlist*/

        username = findViewById(R.id.textView3);
        password = findViewById(R.id.editTextTextPassword2);
        confirmPassword = findViewById(R.id.editTextTextPassword4);
        displayName = findViewById(R.id.editTextText2);
        Button btnGoBack = findViewById(R.id.btnGoBack);
        Button btnSignUp = findViewById(R.id.btnSignUp);
        Button btnUploadImage = findViewById(R.id.btnSelectPhoto);
        btnGoBack.setOnClickListener(v -> {
            // go to the sign up page.
            Intent i = new Intent(this, Login.class);
            startActivity(i);
        });
        btnSignUp.setOnClickListener(v -> {
            // sign up the user.
            String usernameStr = username.getText().toString();
            String passwordStr = password.getText().toString();
            String confirmPasswordStr = confirmPassword.getText().toString();
            String displayNameStr = displayName.getText().toString();
            if (usernameStr.isEmpty() || passwordStr.isEmpty() || confirmPasswordStr.isEmpty() || displayNameStr.isEmpty()) {
                // show error message.
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }
            // Check if the password meets the criteria
            String passwordPattern = "^(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
            if (!passwordStr.matches(passwordPattern)) {
                Toast.makeText(this, "Password must be at least 8 characters long," +
                        " include a capital letter and a special character",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            // Check if the passwords and confirm password match
            if (!passwordStr.equals(confirmPasswordStr)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            }


            // sign up the user.
            User newU = new User(usernameStr,passwordStr,displayNameStr,R.drawable.picture1);
            userList.add(newU);
            Intent i = new Intent(this, Login.class);
            i.putExtra("LIST", (Serializable) userList); /*adding the list to the intent*/
            startActivity(i);

        });
    }
}