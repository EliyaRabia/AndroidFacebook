package com.example.androidfacebook.addspages;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.androidfacebook.R;
import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.entities.Comment;
import com.example.androidfacebook.entities.DataHolder;
import com.example.androidfacebook.entities.Post;
import com.example.androidfacebook.pid.Pid;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class AddPost extends AppCompatActivity {
    private byte[] selectedImageByteArray;
    private ImageView selectedImageView;
    private Button btnDeletePhoto;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    // ActivityResultLauncher for selecting an image from the gallery
    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    handleImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
    // ActivityResultLauncher for capturing an image from the camera
    private final ActivityResultLauncher<Void> mCaptureImage = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(),
            result -> {
                if (result != null) {
                    handleImage(result);
                }
            });
    // Handle the selected image
    private void handleImage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        selectedImageByteArray = stream.toByteArray();
        selectedImageView.setImageBitmap(bitmap);
        btnDeletePhoto.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // create the activity and get the user and post list
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        ClientUser user = DataHolder.getInstance().getUserLoggedIn();
        List<Post> postList = DataHolder.getInstance().getPostList();
        if(user==null){
            return;
        }
        // get the views and set the listeners
        Button btnDelete = findViewById(R.id.btnDelete);
        Button btnPost = findViewById(R.id.btnPost);
        selectedImageView= findViewById(R.id.selectedImage);
        EditText TextShare = findViewById(R.id.editTextShare);
        // set the hint for the text view to include the user's name
        String hint = user.getDisplayName() + ", " + getString(R.string.add_post_edit_text);
        TextShare.setHint(hint);
        btnDeletePhoto = findViewById(R.id.btnPhotoDel);
        // set the listeners
        btnDeletePhoto.setOnClickListener(v -> {
            // Delete the photo
            selectedImageByteArray = null;
            selectedImageView.setImageBitmap(null);
            btnDeletePhoto.setVisibility(View.GONE);
        });
        // set the btnDelete listener to navigate to the pid activity
        btnDelete.setOnClickListener(v -> {
        // Navigate to addPost activity
        Intent intent = new Intent(this, Pid.class);
        startActivity(intent);
        });
        // set the btnPost listener to post the post
        btnPost.setOnClickListener(v -> {
            String textString = TextShare.getText().toString();
            if(textString.length()==0){
                Toast.makeText(this, "You have to write something to get it post!", Toast.LENGTH_SHORT).show();
                return;
            }
            // get the current date and time
            TimeZone israelTimeZone = TimeZone.getTimeZone("Israel");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            dateFormat.setTimeZone(israelTimeZone);
            String currentDateTime = dateFormat.format(new Date());
            // create the post and navigate to the pid activity
            List<Comment> l = new ArrayList<>();
            Post p = new Post(postList.size()+1,user.getDisplayName(),null,textString,currentDateTime,0,0,l);
            if(selectedImageByteArray!=null){
                p.setPictures(selectedImageByteArray);
            }
            postList.add(0,p);
            Intent inte = new Intent(this, Pid.class);
            DataHolder.getInstance().setPostList(postList);
            startActivity(inte);
        });

    }
    // Handle the click event of the "Add Picture" button
    public void onAddPicToPostClick(View view) {
        // Handle the click event here
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
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
    }





}