package com.example.androidfacebook.api;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.androidfacebook.entities.Post;

import java.util.List;


@Dao
public interface PostDao {

    @Query("SELECT * FROM Post")
    List<Post> getAllPosts();

    @Insert
    void insert(Post... post);

    @Query("DELETE FROM Post")
    void deleteAllPosts();
}
