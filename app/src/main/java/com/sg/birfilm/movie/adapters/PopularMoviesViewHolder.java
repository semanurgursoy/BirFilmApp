package com.sg.birfilm.movie.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sg.birfilm.R;

public class PopularMoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    OnMovieListener onMovieListener;
    ImageView imageView22;
    RatingBar ratingBar22;
    TextView title22,release_date22,duration22;

    public PopularMoviesViewHolder(@NonNull View itemView, OnMovieListener onMovieListener) {
        super(itemView);

        this.onMovieListener = onMovieListener;
//        title22 = itemView.findViewById(R.id.title_popular);
//        release_date22 = itemView.findViewById(R.id.date_popular);
//        duration22 = itemView.findViewById(R.id.language_popular);
//        imageView22 = itemView.findViewById(R.id.movie_img_popular);
//        ratingBar22 = itemView.findViewById(R.id.rating_bar_pop);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
