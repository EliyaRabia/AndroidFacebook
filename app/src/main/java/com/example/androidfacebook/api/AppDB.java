package com.example.androidfacebook.api;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.entities.Post;

@Database(entities = {ClientUser.class, Post.class},version =2)
@TypeConverters(Convertors.class)
public abstract class AppDB extends RoomDatabase {
    public abstract PostDao postDao();
    public abstract UserDao userDao();
    public void clearAllTables() {
        // Implement the logic to clear all tables here
        // For each DAO in your database, call their respective delete or truncate methods
        // For example:
        // Add similar calls for other DAOs if you have more than one
    }

}
