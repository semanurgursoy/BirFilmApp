package com.sg.birfilm.movie.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sg.birfilm.R;
import com.sg.birfilm.movie.models.Movie;
import com.sg.birfilm.movie.utils.Credentials;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Movie> mMovies;
    private OnMovieListener onMovieListener;

    public MovieRecyclerView(OnMovieListener onMovieListener) {
        this.onMovieListener = onMovieListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_card,parent,false);
        return new MovieViewHolder(view,onMovieListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MovieViewHolder)holder).title.setText(mMovies.get(position).getOriginal_title());
        ((MovieViewHolder)holder).release_date.setText(mMovies.get(position).getRelease_date());
        ((MovieViewHolder)holder).duration.setText(mMovies.get(position).getOriginal_language());

        ((MovieViewHolder)holder).ratingBar.setRating((mMovies.get(position).getVote_average())/2);

        Picasso.get().load("https://image.tmdb.org/t/p/w500/"+mMovies.get(position).getPoster_path()).into(((MovieViewHolder) holder).imageView);
    }

    @Override
    public int getItemCount() {
        if(mMovies!=null)
            return mMovies.size();
        return 0;
    }

    public void setmMovies(List<Movie> mMovies) {
        this.mMovies = mMovies;
        notifyDataSetChanged();
    }

    // getting the id of the movie clicked
    public Movie getSelectedMovie(int position){
        if(mMovies!=null && mMovies.size()>0)
            return mMovies.get(position);
        return null;
    }

}
