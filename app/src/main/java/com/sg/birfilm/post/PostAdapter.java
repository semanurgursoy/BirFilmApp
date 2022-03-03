package com.sg.birfilm.post;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sg.birfilm.R;
import com.sg.birfilm.User;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    public Context mContext;
    public List<Post> mPosts;

    public FirebaseUser currentUser;

    public PostAdapter(Context mContext, List<Post> mPosts) {
        this.mContext = mContext;
        this.mPosts = mPosts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_component,parent,false);

        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        Post post = mPosts.get(position);

        Glide.with(mContext).load(post.getPoster()).into(holder.post_photo);

        if (post.getCriticism().equals(""))
            holder.txt_criticism.setVisibility(View.GONE);
        else{
            holder.txt_criticism.setVisibility(View.VISIBLE);
            holder.txt_criticism.setText(post.getCriticism());
        }
        if(post.getExtract().equals(""))
            holder.txt_extract.setVisibility(View.GONE);
        else{
            holder.txt_extract.setVisibility(View.VISIBLE);
            holder.txt_extract.setText(post.getExtract());
        }

        // metotlar çağrılır
        senderInfo(holder.profile_photo,holder.txt_username,holder.txt_sender,post.getSender());
        liked(post.getId(),holder.like_photo);
        likeCount(holder.txt_like,post.getId());
        getComments(post.getId(),holder.txt_comments);

        holder.like_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.like_photo.getTag().equals("patlat")){
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("likes")
                            .child(post.getId())
                            .child(currentUser.getUid())
                            .setValue(true);
                }else{
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("likes")
                            .child(post.getId())
                            .child(currentUser.getUid())
                            .removeValue();
                }
            }
        });

        holder.comment_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,CommentsActivity.class);
                intent.putExtra("postId",post.getId());
                intent.putExtra("senderId",post.getSender());
                mContext.startActivity(intent);
            }
        });

        holder.txt_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,CommentsActivity.class);
                intent.putExtra("postId",post.getId());
                intent.putExtra("senderId",post.getSender());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView profile_photo,post_photo,like_photo,comment_photo;
        TextView txt_username,txt_like,txt_sender,txt_criticism,txt_extract,txt_comments;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_photo = itemView.findViewById(R.id.pc_profile_photo);
            post_photo = itemView.findViewById(R.id.pc_post_photo);
            like_photo = itemView.findViewById(R.id.pc_like_photo);
            comment_photo = itemView.findViewById(R.id.pc_comment_photo);

            txt_username = itemView.findViewById(R.id.pc_txt_username);
            txt_like = itemView.findViewById(R.id.pc_txt_likes);
            txt_sender = itemView.findViewById(R.id.pc_txt_sender);
            txt_criticism= itemView.findViewById(R.id.pc_txt_criticism);
            txt_extract = itemView.findViewById(R.id.pc_txt_extract);
            txt_comments = itemView.findViewById(R.id.pc_txt_comments);

        }

    }

    private void getComments(String postId,TextView comments){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("comments")
                .child(postId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comments.setText(snapshot.getChildrenCount()+" yorumun hepsini gör..");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void liked(String postId,ImageView imageView){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference likeDbPath = FirebaseDatabase.getInstance()
                .getReference()
                .child("likes")
                .child(postId);

        likeDbPath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(currentUser.getUid()).exists()){
                    imageView.setImageResource(R.drawable.popcorn_boxx);
                    imageView.setTag("patlatıldı");
                }
                else{
                    imageView.setImageResource(R.drawable.popcorn_box);
                    imageView.setTag("patlat");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void likeCount(TextView likes,String postId){
        DatabaseReference likeDbPath = FirebaseDatabase.getInstance()
                .getReference()
                .child("likes")
                .child(postId);
        likeDbPath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                likes.setText(snapshot.getChildrenCount()+" mısır");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void senderInfo(ImageView profile_photo,TextView username,TextView sender,String userId){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(mContext).load(user.getPpUrl()).into(profile_photo);
                username.setText(user.getUserName());
                sender.setText(user.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
