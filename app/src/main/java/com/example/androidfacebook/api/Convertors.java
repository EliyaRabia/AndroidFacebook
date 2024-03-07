package com.example.androidfacebook.api;

import androidx.room.TypeConverter;

import com.example.androidfacebook.entities.ClientUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Convertors {
    // From User to string
    @TypeConverter
    public String fromUser(ClientUser user) {
        if (user == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.toJson(user);
    }

    // Back to User
    @TypeConverter
    public ClientUser toUser(String userString) {
        if (userString == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(userString, ClientUser.class);
    }
    @TypeConverter
    public static List<String> fromString(String value) {
        Type listType = new TypeToken<List<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<String> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

}
