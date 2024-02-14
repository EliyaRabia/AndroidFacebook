package com.example.androidfacebook.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
public class Login extends AppCompatActivity {
    private String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = getAssets().open("db.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    @SuppressWarnings("unchecked")
    public class PostDeserializer implements JsonDeserializer<Post> {

        @Override
        public Post deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();

            int id = jsonObject.get("id").getAsInt();
            String fullname = jsonObject.get("fullname").getAsString();
            byte[] icon = null;
            if (jsonObject.get("icon") != null && !jsonObject.get("icon").isJsonNull()) {
                icon = convertImageToBytes(jsonObject.get("icon").getAsString());
            }
            String initialText = jsonObject.get("initialText").getAsString();
            byte[] pictures = null;
            if (jsonObject.get("pictures") != null && !jsonObject.get("pictures").isJsonNull()) {
                pictures = convertImageToBytes(jsonObject.get("pictures").getAsString());
            }
            String time = jsonObject.get("time").getAsString();
            int likes = jsonObject.get("likes").getAsInt();
            int commentsNumber = jsonObject.get("commentsNumber").getAsInt();
            Comment[] comments = null;

            return new Post(id, fullname, icon, initialText, pictures, time, likes, commentsNumber, comments);
        }

        private byte[] convertImageToBytes(String imagePath) {
            try {
                InputStream is = getAssets().open(imagePath);
                byte[] bytes = new byte[is.available()];
                is.read(bytes);
                is.close();
                return bytes;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }


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
        String dbJson = loadJSONFromAsset();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Post.class, new PostDeserializer());
        Gson gson = gsonBuilder.create();


        Type type = new TypeToken<List<Post>>(){}.getType();
        List<Post> postList = gson.fromJson(dbJson, type);
        Log.d("Login", "postList: " + postList);

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
                DataHolder.getInstance().setPostList(postList);
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