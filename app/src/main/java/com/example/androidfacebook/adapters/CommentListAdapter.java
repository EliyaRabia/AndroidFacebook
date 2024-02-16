package com.example.androidfacebook.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfacebook.R;
import com.example.androidfacebook.comments.CommentPage;
import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.entities.Comment;
import com.example.androidfacebook.entities.DataHolder;
import com.example.androidfacebook.entities.Post;

import java.util.List;
/*
this class is the adapter for the comments list
 */
public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.CommentViewHolder> {
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
    class CommentViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton CommentButtonOption;
        private final ImageView iconUser;
        private final TextView tvAuthor;
        private final TextView tvContent;
        private final TextView editCommentTextView;
        private final Button btnSaveComment;
        private final Button btnCancelEdit;

        // this is the constructor of the class
        private CommentViewHolder(View itemView){
            super(itemView);
            tvAuthor=itemView.findViewById(R.id.tvAuthor);
            tvContent=itemView.findViewById(R.id.tvContent);
            iconUser=itemView.findViewById(R.id.iconUser);
            CommentButtonOption=itemView.findViewById(R.id.CommentButtonOption);
            editCommentTextView=itemView.findViewById(R.id.editCommentTextView);
            btnSaveComment = itemView.findViewById(R.id.btnSaveComment);
            btnCancelEdit = itemView.findViewById(R.id.btnCancelEdit);
        }

        // this method is used to show the popup menu for the comments
        private void showPopupOptionMenu(View view, Comment current ) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.getMenuInflater().inflate(R.menu.option_menu_comments, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                // Handle delete post action
                // Implement the logic to delete the post here
                if (id == R.id.action_edit_comment) {
                    // Toggle between view modes (read-only and edit)
                    if (tvContent.getVisibility() == View.VISIBLE) {
                        tvContent.setVisibility(View.GONE);
                        editCommentTextView.setVisibility(View.VISIBLE);
                        editCommentTextView.setText(tvContent.getText());
                        btnCancelEdit.setVisibility(View.VISIBLE);
                        btnSaveComment.setVisibility(View.VISIBLE);
                        current.setEditMode(true);
                    } else {
                        tvContent.setVisibility(View.VISIBLE);
                        editCommentTextView.setVisibility(View.GONE);
                        // Save edited comment here
                        current.setEditMode(false);
                    }
                    return true;
                }
                // Handle delete post action
                if(id == R.id.action_delete_comment){
                    // Handle delete post action
                    // Implement the logic to delete the post here
                    Context context = view.getContext();
                    Intent intent = new Intent(context, CommentPage.class);
                    postList.get(postList.indexOf(currentPost)).setCommentsNumber(postList.get(postList.indexOf(currentPost)).getCommentsNumber()-1);
                    comments.remove(current);
                    // Set the updated comments list to the DataHolder
                    DataHolder.getInstance().setComments(comments);
                    DataHolder.getInstance().setPostList(postList);
                    DataHolder.getInstance().setCurrentPost(currentPost);
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
    private List<Comment> comments;
    private ClientUser user;
    private Post currentPost;
    private List<Post> postList;
    public CommentListAdapter(Context context){mInflater=LayoutInflater.from(context);}

    // this method is used to create the view holder for the comments
    @Override
    public CommentListAdapter.CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.comment_item,parent,false);
        return new CommentListAdapter.CommentViewHolder(itemView);
    }
    // this method is used to bind the view holder with the comments
    public void onBindViewHolder(CommentListAdapter.CommentViewHolder holder, int position) {
        if (comments != null) {
            final Comment current = comments.get(position);
            holder.tvAuthor.setText(current.getFullname());
            holder.tvContent.setText(current.getText());
            byte[] iconBytes = current.getIcon();
            setImageViewWithBytes(holder.iconUser, iconBytes);
            // Set OnClickListener for the option button
            holder.CommentButtonOption.setOnClickListener(view -> {
                holder.showPopupOptionMenu(view, current);
            });
            // Set OnClickListener for save button
            holder.btnSaveComment.setOnClickListener(v -> {
                String editedComment = holder.editCommentTextView.getText().toString();
                if(editedComment.length()==0){
                    Toast.makeText(v.getContext(), "Comment can't be blank!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Save the edited comment
                current.setText(editedComment);
                current.setEditMode(false);
                comments.set(comments.indexOf(current),current);
                Context context = v.getContext();
                Intent intent = new Intent(context, CommentPage.class);
                // Set the updated comments list to the DataHolder
                DataHolder.getInstance().setComments(comments);
                DataHolder.getInstance().setPostList(postList);
                DataHolder.getInstance().setCurrentPost(currentPost);
                intent.putExtra("USER", user);
                // Hide the EditText and show the TextView
                holder.btnCancelEdit.setVisibility(View.GONE);
                holder.btnSaveComment.setVisibility(View.GONE);
                context.startActivity(intent);
            });
            // Set OnClickListener for cancel edit button
            holder.btnCancelEdit.setOnClickListener(v -> {
                // Hide the EditText and show the TextView
                holder.tvContent.setVisibility(View.VISIBLE);
                holder.editCommentTextView.setVisibility(View.GONE);
                // Reset EditText to the original comment text
                holder.editCommentTextView.setText(current.getText());
                holder.btnCancelEdit.setVisibility(View.GONE);
                holder.btnSaveComment.setVisibility(View.GONE);
                current.setEditMode(false);
            });
            // Set the visibility of the TextView and EditText based on the edit mode
            if (current.isEditMode()) {
                holder.tvContent.setVisibility(View.GONE);
                holder.editCommentTextView.setVisibility(View.VISIBLE);
                holder.editCommentTextView.setText(current.getText());
                // Set OnClickListener for save button
                // Set OnClickListener for cancel button
            } else {
                holder.tvContent.setVisibility(View.VISIBLE);
                holder.editCommentTextView.setVisibility(View.GONE);
            }

        }
    }


    public void setComments(List<Comment> s, ClientUser user, Post currentPost, List<Post> postList){
        this.user = user;
        this.currentPost = currentPost;
        this.postList = postList;
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