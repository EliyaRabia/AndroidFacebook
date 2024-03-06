package com.example.androidfacebook.api;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.androidfacebook.entities.ClientUser;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users WHERE _id LIKE :id")
    ClientUser getUserById(String id);

    @Query("SELECT * FROM users LIMIT 1")
    ClientUser getUser();

    @Insert
    void insert(ClientUser... user);

    @Delete
    void delete(ClientUser... user);
}
