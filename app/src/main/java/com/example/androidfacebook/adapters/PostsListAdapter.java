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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfacebook.addspages.AddPost;
import com.example.androidfacebook.addspages.EditPost;
import com.example.androidfacebook.R;
import com.example.androidfacebook.api.UserAPI;
import com.example.androidfacebook.comments.CommentPage;
import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.entities.DataHolder;
import com.example.androidfacebook.entities.Post;
import com.example.androidfacebook.pid.Pid;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostViewHolder>{
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

    // Create the view holder
    class PostViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvAuthor;
        private final TextView tvContent;
        private final TextView tvDate;
        private final ImageView ivPic;
        private final ImageButton likeButton;
        private final ImageButton btnShare;
        private final TextView tvNumLike;
        private final TextView tvNumComment;
        private final ImageButton dotsButton;
        private final ImageView iconUser;
        private final ImageButton commentButton;
        private PostViewHolder(View itemView){
            super(itemView);
            tvAuthor=itemView.findViewById(R.id.tvAuthor);
            tvContent=itemView.findViewById(R.id.tvContent);
            ivPic=itemView.findViewById(R.id.ivPic);
            likeButton = itemView.findViewById(R.id.likeButton);
            btnShare = itemView.findViewById(R.id.shareButton);
            iconUser=itemView.findViewById(R.id.iconUser);
            tvDate=itemView.findViewById(R.id.tvDate);
            tvNumLike=itemView.findViewById(R.id.tvNumLike);
            tvNumComment=itemView.findViewById(R.id.tvNumComment);
            dotsButton=itemView.findViewById(R.id.dotsButton);
            commentButton=itemView.findViewById(R.id.commentButton);
        }
        // Show the popup menu when the share button is clicked
        private void showPopupShareMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.getMenuInflater().inflate(R.menu.share_menu, popupMenu.getMenu());
            // Show the popup menu
            popupMenu.show();
        }
        // Show the popup menu when the option button is clicked
        private void showPopupOptionMenu(View view,Post current ) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.getMenuInflater().inflate(R.menu.option_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                // Handle edit post action
                if (id == R.id.action_edit_post) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, EditPost.class);
                    // Set the post list and the current post to the DataHolder
                    DataHolder.getInstance().setPostList(posts);
                    DataHolder.getInstance().setEditposter(current);
                    intent.putExtra("USER", user);
                    context.startActivity(intent);

                    return true;
                }
                // Handle delete post action
                if(id == R.id.action_delete_post){
                    Context context = view.getContext();
                    String token = DataHolder.getInstance().getToken();
                    UserAPI userAPI = new UserAPI(ServerIP);
                    userAPI.deletePost(token, user.getId(), current.getId(), new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.isSuccessful()) {
                                posts.remove(current);
                                Toast.makeText(context, "Post deleted successfully", Toast.LENGTH_LONG).show();
                                notifyDataSetChanged();

                            }
                            else{
                                Toast.makeText(context, "Can't delete this post", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(context, "Something got wrong!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    return true;
                }
                return false;
            });
            // Show the popup menu
            popupMenu.show();
        }

    }
    private final LayoutInflater mInflater;
    private List<Post> posts;
    private ClientUser user;

    public PostsListAdapter(Context context){mInflater=LayoutInflater.from(context);}

    @Override
    // Create the view holder
    public PostViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View itemView = mInflater.inflate(R.layout.post_item,parent,false);
        return new PostViewHolder(itemView);
    }
    public void onBindViewHolder(PostViewHolder holder,int position){
        // Bind the data to the view holder
        if(posts!=null){
            final Post current = posts.get(position);
            holder.tvNumComment.setText("comments: "+String.valueOf(current.getComments().size()));
            holder.tvNumLike.setText(String.valueOf(current.getLikes()));
            holder.tvAuthor.setText(current.getFullname());
//            holder.tvDate.setText(current.getTime());
            holder.tvContent.setText(current.getInitialText());
            holder.tvNumLike.setText(String.valueOf(current.getLikes().size()));
            // Set the image view with the bytes array
            byte[] pictureBytes = convertBase64ToByteArray(current.getPictures());
//            byte[] pictureBytes = current.getPictures();
            setImageViewWithBytes(holder.ivPic, pictureBytes);
            // Set the image view with the bytes array
            byte[] iconBytes = convertBase64ToByteArray(current.getIcon());
//            byte[] iconBytes= current.getIcon();
            setImageViewWithBytes(holder.iconUser,iconBytes);

            if(current.getLikes().contains(user.getId())){
                holder.likeButton.setImageResource(R.drawable.like_icon);

            }
            else{
                holder.likeButton.setImageResource(R.drawable.like_svgrepo_com);
            }
            // Set the onClickListener for the comment button
            holder.commentButton.setOnClickListener(view -> {
                Context context = view.getContext();
                Intent intent = new Intent(context, CommentPage.class);
//                DataHolder.getInstance().setComments(current.getComments());
                DataHolder.getInstance().setPostList(this.getPosts());
                DataHolder.getInstance().setCurrentPost(current);
                intent.putExtra("USER", user);
                context.startActivity(intent);
            });
            holder.likeButton.setOnClickListener(view -> {
                String token = DataHolder.getInstance().getToken();
                UserAPI userAPI = new UserAPI(ServerIP);
                userAPI.addOrRemoveLike(token, user.getId(), current.getId(), new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call, Response<Post> response) {
                        Context context = view.getContext();
                        if(response.isSuccessful()){
                            Post likedP = response.body();
                            int numOriginal = current.getLikes().size();
                            current.setLikes(likedP.getLikes());
                            int numNew = current.getLikes().size();
                            if(numOriginal<numNew){
                                holder.likeButton.setImageResource(R.drawable.like_icon);
                            }
                            else{
                                holder.likeButton.setImageResource(R.drawable.like_svgrepo_com);
                            }
                            notifyDataSetChanged();

                            //need to change on local DB
                        }
                        else{
                            Toast.makeText(context, "something wrong with this like", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {
                        Context context = view.getContext();
                        Toast.makeText(context, "something wrong with this like", Toast.LENGTH_SHORT).show();

                    }
                });

            });

            // Set the onClickListener for the share button
            holder.btnShare.setOnClickListener(view -> {
                // Show the popup menu when the share button is clicked
                holder.showPopupShareMenu(view);
            });

            if (current.getIdUserName().equals(user.getId())) {
                holder.dotsButton.setVisibility(View.VISIBLE);
                // Set the onClickListener for the option button
                holder.dotsButton.setOnClickListener(view -> {
                    // Show the popup menu when the option button is clicked
                    holder.showPopupOptionMenu(view, current);
                });
            } else {
                holder.dotsButton.setVisibility(View.GONE);
            }

        }
    }
    // Set the posts and the user
    public void setPosts(List<Post> s, ClientUser u){
        posts = s;
        user = u;
        notifyDataSetChanged();
    }
    @Override
    // Return the number of posts
    public int getItemCount(){
        if(posts!=null){
            return posts.size();
        }
        else{
            return 0;
        }
    }
    public List<Post> getPosts() {return posts;}


}
