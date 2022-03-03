package com.sg.birfilm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.sg.birfilm.databinding.ActivityHomeBinding;
import com.sg.birfilm.tabs.home.HomeFragment;
import com.sg.birfilm.tabs.notifications.NotificationsFragment;
import com.sg.birfilm.tabs.profile.ProfileFragment;
import com.sg.birfilm.tabs.movies.SearchMovieFragment;
import com.sg.birfilm.tabs.users.SearchUserFragment;

public class Home extends AppCompatActivity {

    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding .inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.search_user:
                    replaceFragment(new SearchUserFragment());
                    break;
                case R.id.search_movie:
                    replaceFragment(new SearchMovieFragment());
                    break;
                case R.id.notification:
                    replaceFragment(new NotificationsFragment());
                    break;
                case R.id.profile:
                    SharedPreferences.Editor editor = getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    editor.apply();
                    replaceFragment(new ProfileFragment());
                    break;
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

}
