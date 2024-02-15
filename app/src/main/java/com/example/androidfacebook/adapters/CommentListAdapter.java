package com.example.androidfacebook.adapters;

import android.content.Context;
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

import com.example.androidfacebook.R;
import com.example.androidfacebook.entities.Comment;

import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.CommentViewHolder> {
    public void setImageViewWithBytes(ImageView imageView, byte[] imageBytes) {
        if (imageBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            imageView.setImageBitmap(bitmap);
        } else {
            // Set a default image or leave it empty
            imageView.setImageDrawable(null);
        }
    }
    class CommentViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton CommentButtonOption;
        private final ImageView iconUser;
        private final TextView tvAuthor;
        private final TextView tvContent;

        private CommentViewHolder(View itemView){
            super(itemView);
            tvAuthor=itemView.findViewById(R.id.tvAuthor);
            tvContent=itemView.findViewById(R.id.tvContent);
            iconUser=itemView.findViewById(R.id.iconUser);
            CommentButtonOption=itemView.findViewById(R.id.CommentButtonOption);
        }

        private void showPopupOptionMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.getMenuInflater().inflate(R.menu.option_menu, popupMenu.getMenu());
            popupMenu.show();
        }

    }

    private final LayoutInflater mInflater;
    private List<Comment> comments;
    public CommentListAdapter(Context context){mInflater=LayoutInflater.from(context);}

    @Override
    public CommentListAdapter.CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.comment_item,parent,false);
        return new CommentListAdapter.CommentViewHolder(itemView);
    }

    public void onBindViewHolder(CommentListAdapter.CommentViewHolder holder, int position){
        if(comments!=null){
            final Comment current = comments.get(position);
            holder.tvAuthor.setText(current.getFullname());
            holder.tvContent.setText(current.getText());
            byte[] iconBytes= current.getIcon();
            setImageViewWithBytes(holder.iconUser,iconBytes);


            holder.CommentButtonOption.setOnClickListener(view -> {
                holder.showPopupOptionMenu(view);
            });
        }
    }

    public void setComments(List<Comment> s){
        comments = s;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount(){
        if(comments!=null){
            return comments.size();
        }
        else{
            return 0;
        }
    }
    public List<Comment> getComments() {return comments;}

}