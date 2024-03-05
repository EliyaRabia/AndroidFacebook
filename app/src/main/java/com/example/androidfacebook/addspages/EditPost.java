package com.example.androidfacebook.addspages;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
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

public class EditPost extends AppCompatActivity {

    private byte[] selectedImageByteArray;
    private ImageView selectedImageView;
    private Button btnDeletePhoto;
    private byte[] pic;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    // ActivityResultLauncher for selecting an image from the gallery
    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            // The callback is called when the user has selected an image
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
    // Handle the image
    private void handleImage(Bitmap bitmap) {
        // Convert the bitmap to a byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        selectedImageByteArray = stream.toByteArray();
        selectedImageView.setImageBitmap(bitmap);
        btnDeletePhoto.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        // Get the user object from the intent and check if it is null
        // also get the post from the Dataholder
        List<Post> posts= DataHolder.getInstance().getPostList();
        ClientUser user = (ClientUser)getIntent().getSerializableExtra("USER");
        Post p = DataHolder.getInstance().getEditposter();
        if(user==null){
            return;
        }
        // Get the views and set button click listeners
        Button btnDeleteEditPost = findViewById(R.id.btnDeleteEditPost);
        Button btnPostEditPost = findViewById(R.id.btnPostEdit);
        selectedImageView= findViewById(R.id.selectedImageEditPost);
        EditText TextShare = findViewById(R.id.editTextShareEditPost);
        setImageViewWithBytes(selectedImageView, p.getPictures());
        TextShare.setText(p.getInitialText());
        pic = p.getPictures();
        btnDeletePhoto = findViewById(R.id.btnPhotoDelEditPost);
        // Check if the post has a picture
        if(p.getPictures()==null){
            btnDeletePhoto.setVisibility(View.GONE);
        }
        else{
            btnDeletePhoto.setVisibility(View.VISIBLE);
        }
        // Set the button click listeners
        btnDeletePhoto.setOnClickListener(v -> {
            // Delete the photo
            selectedImageByteArray = null;
            selectedImageView.setImageBitmap(null);
            btnDeletePhoto.setVisibility(View.GONE);
            pic=null;
        });
        btnDeleteEditPost.setOnClickListener(v -> {
            // remain the post and go back to the previous activity
            Intent intent = new Intent(this, Pid.class);
            intent.putExtra("USER", user);
            startActivity(intent);
        });
        // Set the button click listener
        btnPostEditPost.setOnClickListener(v -> {
            String textString = TextShare.getText().toString();
            // Check if the text is empty
            if(textString.length()==0){
                Toast.makeText(this, "You have to write something to get it post!", Toast.LENGTH_SHORT).show();
                return;
            }
            // Update the post and go back to the previous activity
            Post t;
            if(selectedImageByteArray==null){
                t = new Post(posts.size()+1,user.getDisplayName(),null,textString,pic,p.getTime(),p.getLikes(),p.getCommentsNumber(),p.getComments());
            }
            else{
                t = new Post(posts.size()+1,user.getDisplayName(),null,textString,selectedImageByteArray,p.getTime(),p.getLikes(),p.getCommentsNumber(),p.getComments());
            }
            posts.set(posts.indexOf(p),t);
            Intent inte = new Intent(this, Pid.class);
            inte.putExtra("USER", user);
            DataHolder.getInstance().setPostList(posts);
            startActivity(inte);
        });

    }
    // Handle the permission request result
    public void onAddPicToPostClickEditPost(View view) {
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
    // Set the image view with the byte array
    public void setImageViewWithBytes(ImageView imageView, byte[] imageBytes) {
        if (imageBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            imageView.setImageBitmap(bitmap);
        } else {
            // Set a default image or leave it empty
            imageView.setImageDrawable(null);
        }
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
    }

}