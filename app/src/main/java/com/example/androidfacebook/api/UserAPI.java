package com.example.androidfacebook.api;

import com.example.androidfacebook.entities.ClientPost;
import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.entities.Post;
import com.example.androidfacebook.entities.User;
import com.example.androidfacebook.entities.UserNameAndPass;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAPI {
    private String ServerurlUser;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    public UserAPI(String url) {
        this.setServerUrl(url);
        retrofit = new Retrofit.Builder()
                .baseUrl(this.ServerurlUser)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public WebServiceAPI getWebServiceAPI() {
        return webServiceAPI;
    }

    public void setServerUrl(String serverIp) {
        String url = "http://" + serverIp + ":8080/api/";
        this.ServerurlUser = url;
    }

    public void registerServer(User user, Callback<ResponseBody> callback){
        Call<ResponseBody> call = webServiceAPI.createUser(user);
        call.enqueue(callback);
    }
    public void loginServer(String username, String password, Callback<ResponseBody> callback){
        UserNameAndPass data = new UserNameAndPass(username, password);
        Call<ResponseBody> call = webServiceAPI.login(data);
        call.enqueue(callback);
    }

    public void getUserData(String token,String id, Callback<ClientUser> callback){
        if (token.startsWith("\"") && token.endsWith("\"")) {
            token = token.substring(1, token.length() - 1);
        }
        //UserName userStr = new UserName(username);
        Call<ClientUser> call = webServiceAPI.getUserById(token,id);
        call.enqueue(callback);
    }

    public void createPost(String token, ClientPost post, String id, Callback<Post> callback){
        if (token.startsWith("\"") && token.endsWith("\"")) {
            token = token.substring(1, token.length() - 1);
        }
        Call<Post> call = webServiceAPI.createPost(token, post, id);
        call.enqueue(callback);
    }

}
