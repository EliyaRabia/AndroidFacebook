package com.example.androidfacebook.api;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.androidfacebook.entities.ClientUser;

@Database(entities = {ClientUser.class}, version = 1)
@TypeConverters(Convertors.class)
public abstract class AppDB extends RoomDatabase {
    public abstract UserDao userDao();
    public void clearAllTables() {
        // Implement the logic to clear all tables here
        // For each DAO in your database, call their respective delete or truncate methods
        // For example:
        userDao().delete(); // Assuming you have a deleteAll method in your UserDao
        // Add similar calls for other DAOs if you have more than one
    }

}
