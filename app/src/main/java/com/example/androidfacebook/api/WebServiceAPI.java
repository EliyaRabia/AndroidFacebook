package com.example.androidfacebook.api;

import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.entities.User;
import com.example.androidfacebook.entities.UserNameAndPass;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WebServiceAPI {

    @POST("tokens")
    Call<ResponseBody> login(@Body UserNameAndPass user);
    @POST("users")
    Call<ResponseBody> createUser(@Body User user);

    @GET("users/{id}")
    Call<ClientUser> getUserById(@Header("Authorization") String token, @Path("id") String id);








}
