package com.example.androidfacebook.signup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.Manifest;
import com.example.androidfacebook.R;
import com.example.androidfacebook.entities.User;
import com.example.androidfacebook.login.Login;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class SignUp extends AppCompatActivity {
    private EditText username;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    private EditText password;
    private EditText confirmPassword;
    private EditText displayName;
    private byte[] selectedImageByteArray; // Variable to hold the selected image's byte array
    // Declare two ActivityResultLaunchers for picking from gallery and capturing from camera

    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    handleImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

    private final ActivityResultLauncher<Void> mCaptureImage = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(),
            result -> {
                if (result != null) {
                    handleImage(result);
                }
            });

    private void handleImage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        selectedImageByteArray = stream.toByteArray();
    }


    @SuppressLint("WrongThread")
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
            i.putExtra("LIST", (Serializable) userList);
            startActivity(i);
        });
        btnUploadImage.setOnClickListener(v -> {
            // Check for camera permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            } else {
                // Permission has already been granted
                new AlertDialog.Builder(this)
                        .setTitle("Select Image")
                        .setItems(new String[]{"From Gallery", "From Camera"}, (dialog, which) -> {
                            if (which == 0) {
                                // From Gallery
                                mGetContent.launch("image/*");
                            } else {
                                // From Camera
                                mCaptureImage.launch(null);
                            }
                        })
                        .show();
            }
        });


        btnSignUp.setOnClickListener(v -> {
            // sign up the user.
            String usernameStr = username.getText().toString();
            String passwordStr = password.getText().toString();
            String confirmPasswordStr = confirmPassword.getText().toString();
            String displayNameStr = displayName.getText().toString();
            if (usernameStr.isEmpty() || passwordStr.isEmpty() || confirmPasswordStr.isEmpty() || displayNameStr.isEmpty() || selectedImageByteArray == null) {
                // show error message.
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            /* check if user already exists in the list*/
            for (User user : userList){
                if(usernameStr.equals(user.getUsername())){
                    Toast.makeText(this,"User already exists",Toast.LENGTH_SHORT).show();
                    return;
                }
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
                return;
            }


            // sign up the user.
            User newU = new User(usernameStr,passwordStr,displayNameStr,selectedImageByteArray);
            userList.add(newU);
            Intent i = new Intent(this, Login.class);
            i.putExtra("LIST", (Serializable) userList); /*adding the list to the intent*/
            Toast.makeText(this, "User created successfully", Toast.LENGTH_SHORT).show();
            startActivity(i);

        });
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
    }
}