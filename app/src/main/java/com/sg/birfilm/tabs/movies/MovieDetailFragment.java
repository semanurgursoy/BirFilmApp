package com.sg.birfilm.tabs.movies;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sg.birfilm.R;
import com.sg.birfilm.movie.models.Movie;
import com.sg.birfilm.movie.utils.Credentials;
import com.sg.birfilm.post.CreatePost;

public class MovieDetailFragment extends Fragment {

    private Movie movie;

    private ImageView poster;
    private TextView overview;
    private Button button;

    public MovieDetailFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        poster = view.findViewById(R.id.detail_poster);
        overview = view.findViewById(R.id.detail_overview);
        button = (Button) view.findViewById(R.id.add_to_post_btn);

        movie = getArguments().getParcelable("movie");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("movie",movie);
                Log.v("Tag","in detail put extras "+bundle.getParcelable("movie"));
                Intent intent = new Intent(getActivity(), CreatePost.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        GetDataFromIntent();

        return view;
    }

    private void GetDataFromIntent(){
        if(getArguments().containsKey("movie")){
            movie = getArguments().getParcelable("movie");
            overview.setText(movie.getOverview());
            Glide.with(getActivity())
                    .load(Credentials.BASE_URL_IMG+movie.getPoster_path())
                    .into(poster);
        }
    }

}