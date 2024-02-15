package com.example.androidfacebook.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfacebook.comments.CommentPage;
import com.example.androidfacebook.R;
import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.entities.DataHolder;
import com.example.androidfacebook.entities.Post;
import com.example.androidfacebook.pid.Pid;

import java.util.List;
public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostViewHolder>{
    public void setImageViewWithBytes(ImageView imageView, byte[] imageBytes) {
        if (imageBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            imageView.setImageBitmap(bitmap);
        } else {
            // Set a default image or leave it empty
            imageView.setImageDrawable(null);
        }
    }
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
            //btnOption= itemView.findViewById(R.id.optionButton);
            iconUser=itemView.findViewById(R.id.iconUser);
            tvDate=itemView.findViewById(R.id.tvDate);
            tvNumLike=itemView.findViewById(R.id.tvNumLike);
            tvNumComment=itemView.findViewById(R.id.tvNumComment);
            dotsButton=itemView.findViewById(R.id.dotsButton);
            commentButton=itemView.findViewById(R.id.commentButton);
        }
        private void showPopupShareMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.getMenuInflater().inflate(R.menu.share_menu, popupMenu.getMenu());
            // Show the popup menu
            popupMenu.show();
        }
        private void showPopupOptionMenu(View view,Post current ) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.getMenuInflater().inflate(R.menu.option_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                // Handle delete post action
                // Implement the logic to delete the post here

                if (id == R.id.action_edit_post) {
                    // Handle edit post action
                    // Implement the logic to edit the post here

                    return true;
                }
                if(id == R.id.action_delete_post){
                    // Handle delete post action
                    // Implement the logic to delete the post here
                    Context context = view.getContext();
                    Intent intent = new Intent(context, Pid.class);
                    posts.remove(current);
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
    public PostViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View itemView = mInflater.inflate(R.layout.test_item,parent,false);
        return new PostViewHolder(itemView);
    }
    public void onBindViewHolder(PostViewHolder holder,int position){
        if(posts!=null){
            final Post current = posts.get(position);
            holder.tvNumComment.setText("comments: "+String.valueOf(current.getCommentsNumber()));
            holder.tvNumLike.setText(String.valueOf(current.getLikes()));
            holder.tvAuthor.setText(current.getFullname());
            holder.tvDate.setText(current.getTime());
            holder.tvContent.setText(current.getInitialText());
            byte[] pictureBytes = current.getPictures();
            setImageViewWithBytes(holder.ivPic, pictureBytes);
            byte[] iconBytes= current.getIcon();
            setImageViewWithBytes(holder.iconUser,iconBytes);

            holder.commentButton.setOnClickListener(view -> {
                Context context = view.getContext();
                Intent intent = new Intent(context, CommentPage.class);
                DataHolder.getInstance().setComments(current.getComments());
                DataHolder.getInstance().setPostList(this.getPosts());
                DataHolder.getInstance().setCurrentPost(current);
                intent.putExtra("USER", user);
                context.startActivity(intent);
            });


            holder.likeButton.setOnClickListener(view -> {
                if(current.isLiked()){
                    // Decrease the number of likes by 1
                    current.setLikes(current.getLikes() - 1);
                    // Update the number of likes in the TextView
                    holder.tvNumLike.setText(String.valueOf(current.getLikes()));
                    // Set the liked status of the post to false
                    current.setLiked(false);
                    // Change the image of the like button to the default one
                    holder.likeButton.setImageResource(R.drawable.like_svgrepo_com);
                } else {
                    // Increase the number of likes by 1
                    current.setLikes(current.getLikes() + 1);
                    // Update the number of likes in the TextView
                    holder.tvNumLike.setText(String.valueOf(current.getLikes()));
                    // Set the liked status of the post to true
                    current.setLiked(true);
                    // Change the image of the like button to the liked one
                    holder.likeButton.setImageResource(R.drawable.like_icon);
                }
            });
            holder.btnShare.setOnClickListener(view -> {
                // Show the popup menu when the share button is clicked
                holder.showPopupShareMenu(view);
            });

            holder.dotsButton.setOnClickListener(view -> {
                // Show the popup menu when the option button is clicked
                holder.showPopupOptionMenu(view, current);
            });
        }
    }
    public void setPosts(List<Post> s, ClientUser u){
        posts = s;
        user = u;
        notifyDataSetChanged();
    }
    @Override
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
