package com.example.androidfacebook.adapters;

import static com.example.androidfacebook.login.Login.ServerIP;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.util.Base64;
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
import androidx.room.Room;

import com.example.androidfacebook.R;
import com.example.androidfacebook.api.AppDB;
import com.example.androidfacebook.api.CommentDao;
import com.example.androidfacebook.api.PostDao;
import com.example.androidfacebook.api.UserAPI;
import com.example.androidfacebook.comments.CommentPage;
import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.entities.Comment;
import com.example.androidfacebook.entities.DataHolder;
import com.example.androidfacebook.entities.Post;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    public byte[] convertBase64ToByteArray(String base64Image) {
        if (base64Image != null && base64Image.startsWith("data:image/jpeg;base64,")) {
            String base64EncodedImage = base64Image.substring("data:image/jpeg;base64,".length());
            return Base64.decode(base64EncodedImage, Base64.DEFAULT);
        }
        return null;
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
                        //current.setEditMode(true);
                    } else {
                        tvContent.setVisibility(View.VISIBLE);
                        editCommentTextView.setVisibility(View.GONE);
                        // Save edited comment here
                        //current.setEditMode(false);
                    }
                    return true;
                }

                if(id == R.id.action_delete_comment){

                    Context context = view.getContext();
                    String token = DataHolder.getInstance().getToken();
                    UserAPI userAPI = new UserAPI(ServerIP);
                    userAPI.deleteComment(token, current.getIdUserName(), current.getIdPost(), current.get_id(), new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.isSuccessful()) {
                                comments.remove(current);
                                Toast.makeText(context, "Comment deleted successfully", Toast.LENGTH_LONG).show();
                                notifyDataSetChanged();
                                new Thread(() -> {
                                    AppDB appDB = Room.databaseBuilder(context, AppDB.class, "facebookDB").build();
                                    CommentDao commentDao = appDB.commentDao();
                                    commentDao.delete(current);
                                }).start();
                                List<String> lc = currentPost.getComments();
                                lc.remove(current.get_id());
                                currentPost.setComments(lc);
                                DataHolder.getInstance().setCurrentPost(currentPost);

                            }
                            else{
                                Toast.makeText(context, "Can't delete this comment", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(context, "Invalid call from server", Toast.LENGTH_SHORT).show();
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
    private List<Comment> comments;
    private ClientUser userLoggedIn;
    private ClientUser postUser; //post writer
    private Post currentPost;

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
            byte[] iconBytes = convertBase64ToByteArray(current.getIcon());
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
                comments.set(comments.indexOf(current),current);
                Context context = v.getContext();
                Intent intent = new Intent(context, CommentPage.class);
                // Set the updated comments list to the DataHolder
                DataHolder.getInstance().setComments(comments);
                //DataHolder.getInstance().setPostList(postList);
                DataHolder.getInstance().setCurrentPost(currentPost);
                //intent.putExtra("USER", user);
                // Hide the EditText and show the TextView
                holder.btnCancelEdit.setVisibility(View.GONE);
                holder.btnSaveComment.setVisibility(View.GONE);
                //context.startActivity(intent);
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
                //current.setEditMode(false);
            });
            // Set the visibility of the TextView and EditText based on the edit mode


        }
    }


    public void setComments(List<Comment> s,Post currentPost, ClientUser postUser,ClientUser userLoggedIn){
        this.postUser = postUser;
        this.userLoggedIn=userLoggedIn;
        this.currentPost = currentPost;
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