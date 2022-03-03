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
import com.sg.birfilm.Home;
import com.sg.birfilm.R;
import com.sg.birfilm.User;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{

    Context mContext;
    List<Comment> mCommentList;

    FirebaseUser currentUser;

    public CommentAdapter(Context mContext, List<Comment> mCommentList) {
        this.mContext = mContext;
        this.mCommentList = mCommentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_component,parent,false);

        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        final Comment comment = mCommentList.get(position);

        holder.txt_comment.setText(comment.getComment());

        getUserInfo(holder.pp,holder.txt_username,comment.getSender());

        holder.txt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Home.class);
                intent.putExtra("senderId",comment.getSender());
                mContext.startActivity(intent);
            }
        });

        holder.pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Home.class);
                intent.putExtra("senderId",comment.getSender());
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView pp;
        public TextView txt_username,txt_comment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pp = itemView.findViewById(R.id.pp_comment_component);
            txt_username = itemView.findViewById(R.id.txt_username_comment_component);
            txt_comment = itemView.findViewById(R.id.txt_comment_comment_component);
        }
    }

    private void getUserInfo(ImageView imageView,TextView username,String senderId){
        DatabaseReference senderIdPath = FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(senderId);
        senderIdPath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                Glide.with(mContext).load(user.getPpUrl()).into(imageView);
                username.setText(user.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
