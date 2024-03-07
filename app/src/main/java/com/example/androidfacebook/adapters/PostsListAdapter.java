package com.example.androidfacebook.adapters;

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

import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfacebook.addspages.EditPost;
import com.example.androidfacebook.R;
import com.example.androidfacebook.comments.CommentPage;
import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.entities.DataHolder;
import com.example.androidfacebook.entities.Post;
import com.example.androidfacebook.pid.Pid;

import java.util.List;
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
                    Intent intent = new Intent(context, Pid.class);
                    posts.remove(current);
                    // Set the updated post list to the DataHolder
                    DataHolder.getInstance().setPostList(posts);
                    intent.putExtra("USER", user);
                    context.startActivity(intent);
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
            holder.tvNumComment.setText("comments: "+String.valueOf(current.getCommentsNumber()));
            holder.tvNumLike.setText(String.valueOf(current.getLikes()));
            holder.tvAuthor.setText(current.getFullname());
//            holder.tvDate.setText(current.getTime());
            holder.tvContent.setText(current.getInitialText());
            // Set the image view with the bytes array
            byte[] pictureBytes = convertBase64ToByteArray(current.getPictures());
//            byte[] pictureBytes = current.getPictures();
            setImageViewWithBytes(holder.ivPic, pictureBytes);
            // Set the image view with the bytes array
            byte[] iconBytes = convertBase64ToByteArray(current.getIcon());
//            byte[] iconBytes= current.getIcon();
            setImageViewWithBytes(holder.iconUser,iconBytes);
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
            // Set the onClickListener for the like button
//            holder.likeButton.setOnClickListener(view -> {
//                if(current.isLiked()){
//                    // Decrease the number of likes by 1
//                    current.setLikes(current.getLikes() - 1);
//                    // Update the number of likes in the TextView
//                    holder.tvNumLike.setText(String.valueOf(current.getLikes()));
//                    // Set the liked status of the post to false
//                    current.setLiked(false);
//                    // Change the image of the like button to the default one
//                    holder.likeButton.setImageResource(R.drawable.like_svgrepo_com);
//                    // Set the liked status of the post to false
//                } else {
//                    // Increase the number of likes by 1
//                    current.setLikes(current.getLikes() + 1);
//                    // Update the number of likes in the TextView
//                    holder.tvNumLike.setText(String.valueOf(current.getLikes()));
//                    // Set the liked status of the post to true
//                    current.setLiked(true);
//                    // Change the image of the like button to the liked one
//                    holder.likeButton.setImageResource(R.drawable.like_icon);
//                }
//            });
            // Set the onClickListener for the share button
            holder.btnShare.setOnClickListener(view -> {
                // Show the popup menu when the share button is clicked
                holder.showPopupShareMenu(view);
            });
            // Set the onClickListener for the option button
            holder.dotsButton.setOnClickListener(view -> {
                // Show the popup menu when the option button is clicked
                holder.showPopupOptionMenu(view, current);
            });
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
