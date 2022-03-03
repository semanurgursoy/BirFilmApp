package com.sg.birfilm.tabs.users;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
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
import com.sg.birfilm.tabs.profile.ProfileFragment;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;

    private FirebaseUser firebaseUser;

    public UserAdapter(Context mContext, List<User> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_component,parent,false);

        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final User user = mUsers.get(position);

        holder.ucFollowButton.setVisibility(View.VISIBLE);

        holder.ucUserName.setText(user.getUserName());
        holder.ucFullName.setText(user.getFullName());
        Glide.with(mContext).load(user.getPpUrl()).into(holder.ucPP);

        following(user.getId(),holder.ucFollowButton);
        if(user.getId().equals(firebaseUser.getUid())){
            holder.ucFollowButton.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid",user.getId());
                editor.apply();
                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new ProfileFragment()).commit();
            }
        });

        holder.ucFollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.ucFollowButton.getText().toString().equals("Takip Et")){
                    FirebaseDatabase.getInstance().getReference()
                            .child("Follow")
                            .child(firebaseUser.getUid())
                            .child("Followings")
                            .child(user.getId())
                            .setValue(true);
                    FirebaseDatabase.getInstance().getReference()
                            .child("Follow")
                            .child(user.getId())
                            .child("Followers")
                            .child(firebaseUser.getUid())
                            .setValue(true);
                }
                else{ // zaten takip ediliyor ise takipten çık
                    FirebaseDatabase.getInstance().getReference()
                            .child("Follow")
                            .child(firebaseUser.getUid())
                            .child("Followings")
                            .child(user.getId())
                            .removeValue();
                    FirebaseDatabase.getInstance().getReference()
                            .child("Follow")
                            .child(user.getId())
                            .child("Followers")
                            .child(firebaseUser.getUid())
                            .removeValue();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView ucUserName;
        public TextView ucFullName;
        public CircleImageView ucPP;
        public Button ucFollowButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ucUserName = itemView.findViewById(R.id.uc_user_name);
            ucFullName = itemView.findViewById(R.id.uc_fullname);
            ucPP = itemView.findViewById(R.id.uc_profile_photo);
            ucFollowButton = itemView.findViewById(R.id.uc_follow_btn);
        }
    }

    private void following(String userId,Button button){
        DatabaseReference path = FirebaseDatabase.getInstance().getReference().child("Follow")
                .child(firebaseUser.getUid()).child("Followings");
        path.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(userId).exists()){
                    button.setText("Takip Ediliyor");
                }
                else{
                    button.setText("Takip Et");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
