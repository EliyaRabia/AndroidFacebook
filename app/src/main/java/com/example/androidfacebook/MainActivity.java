package com.example.androidfacebook;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.*;

import com.example.androidfacebook.api.AppDB;
import com.example.androidfacebook.entities.Comment;
import com.example.androidfacebook.entities.DataHolder;
import com.example.androidfacebook.entities.Post;
import com.example.androidfacebook.entities.User;
import com.example.androidfacebook.login.Login;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;


public class MainActivity extends Activity {
    public List<User> userList;
    // Method to load the JSON file from the assets folder
    private String loadJSONFromAsset() {
        String json;
        // Load the JSON file from the assets folder
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
    // Custom deserializer for the Post class
    public class PostDeserializer implements JsonDeserializer<Post> {

        @Override
        public Post deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            // Deserialize the JSON object into a Post object
            int id = jsonObject.get("id").getAsInt();
            String fullname = jsonObject.get("fullname").getAsString();
            byte[] icon = null;
            // Convert the image to bytes
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
            // Deserialize the comments
            int commentsNumber = jsonObject.get("commentsNumber").getAsInt();
            JsonArray commentsj = jsonObject.getAsJsonArray("comments");
            List<Comment> comments = new ArrayList<>();
            // this loop is used to deserialize the comments
            for (JsonElement c:commentsj){
                // Deserialize the JSON object into a Comment object
                JsonObject co = c.getAsJsonObject();
                int commentId = co.get("id").getAsInt();
                String commentFullname = co.get("fullname").getAsString();
                String commentText = co.get("text").getAsString();
                byte[] commentIcon = null;
                if (co.get("icon") != null && !co.get("icon").isJsonNull()) {
                    commentIcon = convertImageToBytes(co.get("icon").getAsString());
                }
                // Add the comment to the list of comments
                comments.add(new Comment(commentId,commentFullname,commentText,commentIcon));
            }
            // Return the Post object
            return new Post(id, fullname, icon, initialText, pictures, time, likes, commentsNumber,comments);
        }
        // Method to convert the image to bytes
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
        setContentView(R.layout.activity_main);
        // Create a new intent to start the Login activity
        userList = new ArrayList<>();
        Intent i = new Intent(this, Login.class);
        // set the new user list to the DataHolder
        DataHolder.getInstance().setUserList(userList);
        // Load the JSON file from the assets folder
        String dbJson = loadJSONFromAsset();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Post.class, new PostDeserializer());
        Gson gson = gsonBuilder.create();
        // Deserialize the JSON array into a list of Post objects
        Type type = new TypeToken<List<Post>>(){}.getType();
        List<Post> postList = gson.fromJson(dbJson, type);
        // Set the post list to the DataHolder
        DataHolder.getInstance().setPostList(postList);
        startActivity(i);
    }
}
