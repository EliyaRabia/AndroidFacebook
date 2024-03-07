package com.example.androidfacebook.api;

import com.example.androidfacebook.entities.ClientPost;
import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.entities.Post;
import com.example.androidfacebook.entities.UpdateUser;
import com.example.androidfacebook.entities.User;
import com.example.androidfacebook.entities.UserNameAndPass;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface WebServiceAPI {

    @POST("tokens")
    Call<ResponseBody> login(@Body UserNameAndPass user);
    @POST("users")
    Call<ResponseBody> createUser(@Body User user);

    @POST("users/{id}/posts")
    Call<Post> createPost(@Header("Authorization") String token, @Body ClientPost post, @Path("id") String id);

    @GET("users/{id}")
    Call<ClientUser> getUserById(@Header("Authorization") String token, @Path("id") String id);

    @PUT("users/{id}")
    Call<ResponseBody> updateUser(@Header("Authorization") String token, @Body UpdateUser user, @Path("id") String id);
    @DELETE("users/{id}")
    Call<ResponseBody> deleteUser(@Header("Authorization") String token, @Path("id") String id);
    @GET("posts")
    Call<List<Post>> getAllPosts(@Header("Authorization") String token);








}
