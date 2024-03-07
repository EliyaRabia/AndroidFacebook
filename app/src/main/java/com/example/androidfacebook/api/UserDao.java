package com.example.androidfacebook.api;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.androidfacebook.entities.ClientUser;

@Dao
public interface UserDao {
    @Query("SELECT * FROM ClientUser WHERE _id = :id")
    ClientUser getUserById(String id);

    @Query("SELECT * FROM ClientUser LIMIT 1")
    ClientUser getUser();

    @Insert
    void insert(ClientUser... user);

    @Query("DELETE FROM ClientUser")
    void deleteAllUsers();
}