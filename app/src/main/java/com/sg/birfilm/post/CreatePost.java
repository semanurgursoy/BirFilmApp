package com.sg.birfilm.post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.sg.birfilm.Home;
import com.sg.birfilm.R;
import com.sg.birfilm.movie.models.Movie;
import com.sg.birfilm.movie.utils.Credentials;

public class CreatePost extends AppCompatActivity {

    ImageView closeImg;
    TextView title,send;
    EditText criticism,extract,rating;

    FirebaseAuth mAuth;

    Movie movie;

    String postKey,criticismSt,extractSt;
    float ratingFloat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        Bundle bundle = getIntent().getExtras();
        movie = bundle.getParcelable("movie");
        Log.v("Tag","post get movie "+movie.toString());

        closeImg = (ImageView) findViewById(R.id.post_close_img);
        title = (TextView) findViewById(R.id.post_title);
        send = (TextView) findViewById(R.id.post_txt_send);
        criticism = (EditText) findViewById(R.id.post_criticism);
        extract = (EditText) findViewById(R.id.post_extract);
        rating = (EditText) findViewById(R.id.post_rating);

        title.setText(movie.getOriginal_title());
        mAuth = FirebaseAuth.getInstance();

        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreatePost.this, Home.class));
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetDataFromIntent();

                createPostUid();
                createPost();
                startActivity(new Intent(CreatePost.this, Home.class));
            }
        });
    }

    private void createPost(){
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        Post post = new Post(postKey,firebaseUser.getUid(),movie.getId(),criticismSt,extractSt,ratingFloat, Credentials.BASE_URL_IMG+movie.getPoster_path());

        FirebaseDatabase.getInstance()
                .getReference("posts")
                .child(postKey)
                .setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Toast.makeText(CreatePost.this, "Post Oluşturuldu", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(CreatePost.this, "Bir Hata Oluştu", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void createPostUid(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        postKey = database.getReference("posts").push().getKey();
    }

    private void GetDataFromIntent(){
        criticismSt = this.criticism.getText().toString();
        extractSt = this.extract.getText().toString();
        ratingFloat = Float.parseFloat(rating.getText().toString());
    }

}