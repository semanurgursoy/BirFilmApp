package com.sg.birfilm.post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CommentAdapter commentAdapter;
    List<Comment> commentList;

    EditText edt_add_comment;
    ImageView pp;
    TextView txt_send;

    String postId;
    String senderId;

    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Toolbar toolbar = findViewById(R.id.toolbar_comments_act);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Yorumlar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerview_comments_act);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this,commentList);
        recyclerView.setAdapter(commentAdapter);

        edt_add_comment = findViewById(R.id.edt_addcomment_comments_act);
        pp = findViewById(R.id.pp_comments_act);
        txt_send = findViewById(R.id.txt_send_comments_act);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        postId=intent.getStringExtra("postId");
        senderId=intent.getStringExtra("senderId");

        txt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_add_comment.getText().toString().equals(""))
                    Toast.makeText(CommentsActivity.this,"Boş Yorum Gönderemezsiniz",Toast.LENGTH_LONG).show();
                else
                    addComment();
            }
        });

        getPhoto();
        readComments();

    }

    private void addComment(){
        DatabaseReference commentsPath = FirebaseDatabase.getInstance()
                .getReference("comments")
                .child(postId);

        String comment = edt_add_comment.getText().toString();
        String sender = currentUser.getUid();

        Comment newComment = new Comment(comment,sender);
        commentsPath.push().setValue(newComment);
        edt_add_comment.setText("");
    }

    private void getPhoto() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(currentUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(getApplicationContext()).load(user.getPpUrl()).into(pp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readComments(){
        DatabaseReference readCommentsPath = FirebaseDatabase.getInstance()
                .getReference("comments")
                .child(postId);
        readCommentsPath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for(DataSnapshot ss:snapshot.getChildren()){
                    Comment comment = ss.getValue(Comment.class);
                    commentList.add(comment);
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}