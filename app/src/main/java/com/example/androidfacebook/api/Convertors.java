package com.example.androidfacebook.api;

import androidx.room.TypeConverter;

import com.example.androidfacebook.entities.ClientUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Date;
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
    static Gson gson = new Gson();
    @TypeConverter
    public static String fromList(List<String> list) {
        return gson.toJson(list);
    }

    @TypeConverter
    public static List<String> stringToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<String>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String fromDateToJson(Date date) {
        if (date == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.toJson(date);
    }

    @TypeConverter
    public static Date fromJsonToDate(String json) {
        if (json == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(json, Date.class);
    }

}