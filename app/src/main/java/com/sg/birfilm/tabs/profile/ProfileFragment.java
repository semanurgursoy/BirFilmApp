package com.sg.birfilm.tabs.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.sg.birfilm.post.Post;
import com.sg.birfilm.post.PostAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    ImageView optionsImg,profilePhoto;
    TextView txtPosts,txtFollowers,txtFollowings,txtUsername;
    Button setProfileButton;
    ImageButton postsImgBtn,collectionsImgBtn;

    FirebaseUser currentUser;
    String profileId,userId;

    private RecyclerView recyclerViewPosts,recyclerViewCollections;
    private PostAdapter postAdapter;
    private List<Post> postLists;

    public ProfileFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        recyclerViewPosts = view.findViewById(R.id.recyclerview_posts);
        recyclerViewPosts.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerViewPosts.setLayoutManager(linearLayoutManager);

        postLists = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(),postLists);
        recyclerViewPosts.setAdapter(postAdapter);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profileId = sharedPreferences.getString("profileid","none");

        optionsImg = view.findViewById(R.id.profile_options_img);
        profilePhoto = view.findViewById(R.id.profile_pp);

        txtPosts = view.findViewById(R.id.profile_posts);
        txtFollowers = view.findViewById(R.id.profile_followers);
        txtFollowings = view.findViewById(R.id.profile_followings);
        txtUsername = view.findViewById(R.id.profile_txt_username);

        setProfileButton = view.findViewById(R.id.profile_set_profile);

        postsImgBtn = view.findViewById(R.id.profile_imgbtn_posts);
        collectionsImgBtn = view.findViewById(R.id.profile_imgbtn_collections);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(profileId!="none"){
            userId=profileId;
            optionsImg.setVisibility(View.GONE);
        }

        getUserInfo();
        getFollowers();
        getPostCount();

        readPost();

        if(profileId.equals(currentUser.getUid())) {
            setProfileButton.setText("Profili Düzenle");
            optionsImg.setVisibility(View.VISIBLE);
        }
        else
            followControl();

        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SetPP.class);
                startActivity(intent);
            }
        });

        optionsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("user",currentUser);
                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new ProfileOptionsMenu()).commit();
            }
        });

        setProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn = setProfileButton.getText().toString();
                if(btn.equals("Profili Düzenle")){

                }else if(btn.equals("Takip Et")){
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(currentUser.getUid())
                            .child("Followings").child(profileId).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId)
                            .child("Followers").child(currentUser.getUid()).setValue(true);
                }else if(btn.equals("Following")){
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(currentUser.getUid())
                            .child("Followings").child(profileId).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId)
                            .child("Followers").child(currentUser.getUid()).removeValue();
                }
            }
        });


        return view;
    }

    private void readPost(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("posts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postLists.clear();
                for(DataSnapshot ss:snapshot.getChildren()){
                    Post post = ss.getValue(Post.class);
                    if(userId==null) {
                        if (post.getSender().equals(currentUser.getUid()))
                            postLists.add(post);
                    }else{
                        if (post.getSender().equals(userId))
                            postLists.add(post);
                    }
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUserInfo(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(profileId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(getContext()==null)
                    return;

                User user = snapshot.getValue(User.class);
                Glide.with(getContext()).load(user.getPpUrl()).into(profilePhoto);
                txtUsername.setText(user.getUserName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void followControl(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Follow")
                .child(currentUser.getUid()).child("Followings");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(profileId).exists())
                    setProfileButton.setText("Takip Ediliyor");
                else
                    setProfileButton.setText("Takip Et");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getFollowers(){
        DatabaseReference followerPath = FirebaseDatabase.getInstance().getReference().child("Follow")
                .child(profileId).child("Followers");
        followerPath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                txtFollowers.setText("Takipçiler\n    "+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference followingPath = FirebaseDatabase.getInstance().getReference().child("Follow")
                .child(profileId).child("Followings");
        followingPath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                txtFollowings.setText("Takipler\n    "+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getPostCount(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("posts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                for(DataSnapshot ss:snapshot.getChildren()){
                    Post post = ss.getValue(Post.class);

                    if(post.getSender().equals(profileId))
                        i++;
                }
                txtPosts.setText("Gönderiler\n    "+i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}