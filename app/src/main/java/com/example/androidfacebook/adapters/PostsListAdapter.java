package com.example.androidfacebook.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfacebook.R;
import com.example.androidfacebook.entities.Post;

import java.util.List;
public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostViewHolder>{
    class PostViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvAuthor;
        private final TextView tvContent;
        private final ImageView ivPic;
        private final ImageButton btnLike;
        private final ImageButton btnShare;

        private PostViewHolder(View itemView){
            super(itemView);
            tvAuthor=itemView.findViewById(R.id.tvAuthor);
            tvContent=itemView.findViewById(R.id.tvContent);
            ivPic=itemView.findViewById(R.id.ivPic);
            btnLike = itemView.findViewById(R.id.likeButton);
            btnShare = itemView.findViewById(R.id.shareButton);
        }
        private void showPopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.getMenuInflater().inflate(R.menu.share_menu, popupMenu.getMenu());


            // Show the popup menu
            popupMenu.show();
        }
    }
    private final LayoutInflater mInflater;
    private List<Post> posts;

    public PostsListAdapter(Context context){mInflater=LayoutInflater.from(context);}

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View itemView = mInflater.inflate(R.layout.post_item,parent,false);
        return new PostViewHolder(itemView);
    }
    public void onBindViewHolder(PostViewHolder holder,int position){
        if(posts!=null){
            final Post current = posts.get(position);
            holder.tvAuthor.setText(current.getUser().getDisplayName());
            holder.tvContent.setText(current.getInitialText());
            holder.ivPic.setImageResource(current.getPictures());

            holder.btnLike.setOnClickListener(v -> {
                if (current.isLiked()) {
                // Change the icon to icon1
                    holder.btnLike.setImageResource(R.drawable.like_svgrepo_com);
                // Set liked to false
                current.setLiked(false);
//                 Decrease the number of likes
//                post.setLikes(post.getLikes() - 1);
            } else {
                // Change the icon to icon2
                    holder.btnLike.setImageResource(R.drawable.like_icon);
                // Set liked to true
                current.setLiked(true);
//                 Increase the number of likes
//                post.setLikes(post.getLikes() + 1);
            }
            });
            holder.btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Show the popup menu when the share button is clicked
                    holder.showPopupMenu(view);
                }
            });
        }
    }
    public void setPosts(List<Post> s){
        posts = s;
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
