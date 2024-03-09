package com.example.androidfacebook.adapters;

import static com.example.androidfacebook.login.Login.ServerIP;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfacebook.R;
import com.example.androidfacebook.api.UserAPI;
import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.entities.DataHolder;
import com.example.androidfacebook.friends.ProfilePage;

import java.util.List;
import java.util.Stack;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.FriendViewHolder> {
    // Set the image view with the bytes array
    public void setImageViewWithBytes(ImageView imageView, byte[] imageBytes) {
        if (imageBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            imageView.setImageBitmap(bitmap);
        } else {
            // Set a default image or leave it empty
            imageView.setImageDrawable(null);
        }
    }
    public byte[] convertBase64ToByteArray(String base64Image) {
        if (base64Image != null && base64Image.startsWith("data:image/jpeg;base64,")) {
            String base64EncodedImage = base64Image.substring("data:image/jpeg;base64,".length());
            return Base64.decode(base64EncodedImage, Base64.DEFAULT);
        }
        return null;
    }
    class FriendViewHolder extends RecyclerView.ViewHolder {
        private final ImageView iconUser;
        private final TextView tvAuthor;

        // this is the constructor of the class
        private FriendViewHolder(View itemView){
            super(itemView);
            tvAuthor=itemView.findViewById(R.id.tvAuthor);
            iconUser=itemView.findViewById(R.id.iconUser);
        }
    }

    private final LayoutInflater mInflater;
    private List<ClientUser> friends;
    private ClientUser userLoggedIn;

    private ClientUser viewedFriend;
    public FriendsListAdapter(Context context){mInflater=LayoutInflater.from(context);}

    // this method is used to create the view holder for the friends
    @Override
    public FriendsListAdapter.FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.friend_item,parent,false);
        return new FriendsListAdapter.FriendViewHolder(itemView);
    }
    // this method is used to bind the view holder with the friends
    public void onBindViewHolder(FriendsListAdapter.FriendViewHolder holder, int position) {
        if (friends != null) {
            final ClientUser current = friends.get(position);
            holder.tvAuthor.setText(current.getDisplayName());
            String imgNotif = current.getPhoto();
            byte[] iconBytes = convertBase64ToByteArray(current.getPhoto());
            setImageViewWithBytes(holder.iconUser, iconBytes);
            holder.iconUser.setOnClickListener(view -> {
                Context context = view.getContext();
                //DataHolder.getInstance().setFriendProfileId(current.getIdUserName());
                Stack<String> s = DataHolder.getInstance().getStackOfIDs();
                s.push(current.getId());
                DataHolder.getInstance().setStackOfIDs(s);
                Intent intent = new Intent(context, ProfilePage.class);
                context.startActivity(intent);
            });

        }
    }


    public void setFriends(List<ClientUser> s, ClientUser viewedFriend,ClientUser userLoggedIn){
        this.userLoggedIn = userLoggedIn;
        this.viewedFriend = viewedFriend;
        this.friends = s;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount(){
        if(friends!=null){
            return friends.size();
        }
        else{
            return 0;
        }
    }
    public List<ClientUser> getFriends() {return friends;}
}

